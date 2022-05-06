//
// Recorder.swift
// MovesenseShowcase
//
// Copyright (c) 2018 Suunto. All rights reserved.
//

import Foundation
import MovesenseApi

enum RecorderObserverEvent: ObserverEvent {

    case idle
    case recording
    case recordsUpdated
    case recorderConverting(_ target: String, progress: Int)
    case recorderError(_ error: Error)
}

public struct RecorderApi {

    static let instance: Recorder = RecorderConcrete.sharedInstance
}

protocol Recorder: Observable {

    func startRecording()
    func stopRecording()

    func addDeviceOperation(_ device: MovesenseDevice, _ operation: MovesenseOperation)
    func removeDeviceOperation(_ device: MovesenseDevice, _ operation: MovesenseOperation)

    func removeAllOperations()

    func getRecords() -> [RecorderFile]
    func removeRecord(_ record: RecorderFile)

    func tempCopyRecord(_ record: RecorderFile) -> URL?
    func tempClear()

    func convertToCsv(_ recordUrl: URL) -> URL?
}

protocol RecorderDelegate: class {

    func recorderError(_ record: RecorderFileJson, _ error: Error)
}

class RecorderConcrete: Recorder {

    private enum Constants {
        static let accHeader: String = "timestamp,x,y,z"
        static let ecgHeader: String = "timestamp,sample"
        static let gyroHeader: String = "timestamp,x,y,z"
        static let magnHeader: String = "timestamp,x,y,z"
        static let imuHeader: String = "timestamp,x,y,z,gx,gy,gz"
        static let hrHeader: String = "average,rrData"
        static let lfData: Data = Data([UInt8(0x0a)]) // Linefeed UTF8 code point 0x0a
        static let commaData: Data = Data([UInt8(0x2c)]) // Comma UTF8 code point 0x2c
        static let lfUInt8: UInt8 = 0x0a
        static let commaUInt8: UInt8 = 0x2c
        static let semicolonUInt8: UInt8 = 0x3b
    }

    fileprivate static let sharedInstance: Recorder = RecorderConcrete()

    internal var observations: [Observation] = [Observation]()
    private(set) var observationQueue: DispatchQueue = DispatchQueue.global()

    private let jsonDecoder: JSONDecoder = JSONDecoder()

    private var conversionWorkItem: DispatchWorkItem?
    private var operationFiles: [RecorderFileJson] = []
    private var recordingDate: Date = Date()

    init() {
        jsonDecoder.dateDecodingStrategy = .iso8601
    }

    func startRecording() {
        recordingDate = Date()

        DispatchQueue.global().async { [recordingDate, operationFiles] in
            operationFiles.forEach {
                $0.delegate = self
                $0.startRecording(recordingDate)
            }
        }

        notifyObservers(RecorderObserverEvent.recording)
    }

    func stopRecording() {
        DispatchQueue.global().async { [weak self, recordingDate, operationFiles] in
            operationFiles.forEach { $0.stopRecording(recordingDate) }
            self?.notifyObservers(RecorderObserverEvent.idle)
        }
    }

    func addDeviceOperation(_ device: MovesenseDevice, _ operation: MovesenseOperation) {
        let newFile = RecorderFileJson(device: device, operation: operation)
        guard (operationFiles.contains { $0 == newFile } == false) else { return }
        operationFiles.append(newFile)
    }

    func removeDeviceOperation(_ device: MovesenseDevice, _ operation: MovesenseOperation) {
        operationFiles.removeAll { $0 == RecorderFileJson(device: device, operation: operation) }
    }

    func removeAllOperations() {
        operationFiles.removeAll()
    }

    func getRecords() -> [RecorderFile] {
        guard let storageUrl: URL = FileManager.default.urls(for: .applicationSupportDirectory, in: .userDomainMask).first else {
            let error = AppError.operationError("Recorder::getRecords unable to get documents dir.")
            NSLog(error.localizedDescription)
            notifyObservers(RecorderObserverEvent.recorderError(error))
            return []
        }

        let recordPath: String = storageUrl.path + "/recordings/"
        guard let enumerator = FileManager.default.enumerator(atPath: recordPath) else {
            let error = AppError.operationError("Recorder::getRecords unable to read records.")
            NSLog(error.localizedDescription)
            notifyObservers(RecorderObserverEvent.recorderError(error))
            return []
        }

        let records: [RecorderFile] = enumerator.compactMap { $0 as? String }
            .filter { $0.hasSuffix(RecorderFileJson.Constants.headerSuffix) }
            .compactMap { FileManager.default.contents(atPath: recordPath + $0) }
            .compactMap { try? jsonDecoder.decode(RecorderFile.self, from: $0) }

        return records
    }

    func tempCopyRecord(_ record: RecorderFile) -> URL? {
        guard let storageUrl: URL = FileManager.default.urls(for: .applicationSupportDirectory, in: .userDomainMask).first,
              let fileName = record.filePath.split(separator: "/").last else {
            return nil
        }

        let tempUrl = FileManager.default.temporaryDirectory
        let storageFilePath = storageUrl.path + record.filePath
        let fullFileName = record.startDate.iso8601 + "_" + record.serialNumber + "_" + fileName
        let tempFilePath = tempUrl.path + "/" + fullFileName

        // In case the file exists already
        try? FileManager.default.removeItem(atPath: tempFilePath)

        do {
            try FileManager.default.copyItem(atPath: storageFilePath, toPath: tempFilePath)
        } catch let error {
            notifyObservers(RecorderObserverEvent.recorderError(error))
            return nil
        }

        return URL(fileURLWithPath: tempFilePath)
    }

    func tempClear() {
        conversionWorkItem?.cancel()

        guard let filePaths = try? FileManager.default.contentsOfDirectory(at: FileManager.default.temporaryDirectory,
                                                                           includingPropertiesForKeys: nil,
                                                                           options: []) else {
            let error = AppError.operationError("Recorder::tempClear unable to get temporary dir contents.")
            notifyObservers(RecorderObserverEvent.recorderError(error))
            return
        }

        filePaths.forEach { filePath in try? FileManager.default.removeItem(at: filePath) }
    }

    func removeRecord(_ record: RecorderFile) {
        guard let storageUrl: URL = FileManager.default.urls(for: .applicationSupportDirectory, in: .userDomainMask).first else {
            return
        }

        let recordPath: String = record.filePath.split(separator: "/").dropLast().joined(separator: "/")
        let documentsFilePath = storageUrl.path + "/" + recordPath
        do {
            try FileManager.default.removeItem(atPath: documentsFilePath)
        } catch let error {
            notifyObservers(RecorderObserverEvent.recorderError(error))
            return
        }

        // TODO: Remove directory as well if empty

        notifyObservers(RecorderObserverEvent.recordsUpdated)
    }

    func convertToCsv(_ tempUrl: URL) -> URL? {
        let csvFileUrl = tempUrl.deletingPathExtension().appendingPathExtension("csv")

        guard FileManager.default.createFile(atPath: csvFileUrl.path, contents: nil),
              let jsonHandle = try? FileHandle(forReadingFrom: tempUrl),
              let csvHandle = try? FileHandle(forWritingTo: csvFileUrl) else {
            return nil
        }

        let endOffset = jsonHandle.seekToEndOfFile()
        jsonHandle.seek(toFileOffset: 0)

        var relativeOffset: Int = 0
        let csvEncoder = CsvEncoder()

        guard let _ = jsonHandle.readLine(delimiter: "\n"),
              var lineData = jsonHandle.readLine(delimiter: "\n") else {
            //TODO: Generate error
            return nil
        }

        var secondLineOffset: UInt64 = 0
        if #available(iOS 13.4, *) {
            guard let offset = try? jsonHandle.offset() else {
                return nil
            }
            secondLineOffset = offset
        } else {
            secondLineOffset = jsonHandle.offsetInFile
        }

        // If the last character is comma, remove before decoding
        if Constants.commaData == lineData[lineData.count-2..<lineData.count-1] {
            lineData = lineData.dropLast(2)
        }

        guard let decodedEvent = try? jsonDecoder.decode(MovesenseEvent.self, from: lineData),
              let csvHeaderData = getCsvHeaderData(event: decodedEvent)
              else {
            //TODO: Generate error
            return nil
        }

        // Remove the first line even if converting from old files which are not proper json.
        jsonHandle.seek(toFileOffset: secondLineOffset)
        csvHandle.write(csvHeaderData.header + Constants.lfData)

        conversionWorkItem = DispatchWorkItem { [weak self] in

            // Calculate timestamps for array items
            var prevTimeStamp: UInt32 = 0

            // Convert JSON data line by line
            while var lineData = jsonHandle.readLine(delimiter: "\n"), self?.conversionWorkItem?.isCancelled == false {

                if Constants.commaData == lineData[lineData.count-2..<lineData.count-1] {
                    lineData = lineData.dropLast(2)
                }

                guard let decoded = ((try? self?.jsonDecoder.decode(MovesenseEvent.self, from: lineData)) as MovesenseEvent??),
                      let encoded = try? csvEncoder.encode(decoded) else {
                    //TODO: Generate error
                    break
                }

                let newRelativeOffset = Int(100 * Double(jsonHandle.offsetInFile) / Double(endOffset))
                if relativeOffset < newRelativeOffset {
                    relativeOffset = newRelativeOffset
                    self?.notifyObservers(RecorderObserverEvent.recorderConverting(csvFileUrl.lastPathComponent,
                                                                                   progress: relativeOffset))
                }

                // Split semicolon separated chunks in order to calculate timestamps for array items
                let byteData = Data(_ :encoded).split(separator: Constants.semicolonUInt8)

                // Heartrate measurements are without timestamp
                if (csvHeaderData.numOfTimedMeas == 0) {
                    let semicolonRemoved = byteData.reduce(Data(), +)
                    csvHandle.write(semicolonRemoved + Constants.lfData)
                    continue
                }

                // Decode. TODO: Simplify conversion
                guard let firstByte = byteData.first?.dropLast(),
                      let strTimestamp = String(data: firstByte, encoding: .utf8),
                      let ts = UInt32(strTimestamp) else {break}

                if prevTimeStamp > 0 {
                    // Check if we have two measurement arrays for one timestamp
                    let sampleCount = csvHeaderData.numOfTimedMeas > 1 ? (byteData.count - 1)/2 : byteData.count - 1
                    let deltaTs = Double(ts - prevTimeStamp)/Double(sampleCount)
                    var timestamp = Double(ts)

                    for i in 0..<sampleCount {
                        var newLine = String(format: "%.1f", timestamp).data(using: .utf8)! + Constants.commaData
                        if csvHeaderData.numOfTimedMeas > 1 {
                            newLine += byteData[1 + i]
                            if i != sampleCount - 1 {
                                newLine += Constants.commaData
                            }
                            newLine += byteData[sampleCount + i + 1]
                        } else {
                            newLine += byteData[i + 1]
                        }
                        newLine += Constants.lfData
                        csvHandle.write(newLine)
                        timestamp += deltaTs
                    }
                }
                prevTimeStamp = ts
            }
        }

        conversionWorkItem?.perform()

        if conversionWorkItem?.isCancelled == true {
            return nil
        }

        return csvFileUrl
    }

    private func getCsvHeaderData(event: MovesenseEvent) -> (header: Data, numOfTimedMeas: UInt8)? {
        let csvHeaderString: String
        let numOfTimestamped: UInt8
        switch event {
        case .acc: csvHeaderString = Constants.accHeader; numOfTimestamped = 1
        case .ecg: csvHeaderString = Constants.ecgHeader; numOfTimestamped = 1
        case .gyroscope: csvHeaderString = Constants.gyroHeader; numOfTimestamped = 1
        case .magn: csvHeaderString = Constants.magnHeader; numOfTimestamped = 1
        case .imu: csvHeaderString = Constants.imuHeader; numOfTimestamped = 2
        case .heartRate: csvHeaderString = Constants.hrHeader; numOfTimestamped = 0
        }

        if let data = csvHeaderString.data(using: .utf8) {
            return (data, numOfTimestamped)
        } else {
            return nil
        }
    }
}

extension RecorderConcrete: RecorderDelegate {

    func recorderError(_ record: RecorderFileJson, _ error: Error) {
        stopRecording()
        notifyObservers(RecorderObserverEvent.recorderError(error))
    }
}
