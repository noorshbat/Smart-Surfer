package com.movesense.showcaseapp.csv;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.movesense.mds.internal.connectivity.MovesenseConnectedDevices;
import com.movesense.showcaseapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CsvLogger {

    private final String TAG = CsvLogger.class.getSimpleName();

    private final StringBuilder mStringBuilder1;
    private final StringBuilder mStringBuilder2;
    private final StringBuilder mStringBuilder3;

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 99;
    public static final int MAX_CAPACITY = 2147483647;

    private boolean isHeaderExists = false;

    public CsvLogger() {
        mStringBuilder1 = new StringBuilder();
        mStringBuilder2 = new StringBuilder();
        mStringBuilder3 = new StringBuilder();

    }

    //NEW
    public String startTime;
    public String activity;


    public void appendHeader(String header) {
        if (!isHeaderExists) {
            mStringBuilder1.append(header);
            mStringBuilder1.append("\n");
        }
        isHeaderExists = true;
    }

    public void appendLine(String line) {
        if (mStringBuilder1.length() >= MAX_CAPACITY-150){
            if (mStringBuilder2.length() >= MAX_CAPACITY-150){
                mStringBuilder3.append(line);
                mStringBuilder3.append("\n");
            } else {
                mStringBuilder2.append(line);
                mStringBuilder2.append("\n");
            }
        } else {
            mStringBuilder1.append(line);
            mStringBuilder1.append("\n");
        }
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
         this.startTime=startTime;
    }
    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity=activity;
    }

    public void finishSavingLogs(Context context, String sensorName) {
        try {
            File file = createLogFile(sensorName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(mStringBuilder1.toString());
           // if(mStringBuilder2.length()!=0)
                fileWriter.write(mStringBuilder2.toString());
            //if(mStringBuilder3.length()!=0)
                fileWriter.write(mStringBuilder3.toString());

            //NEW
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            formatter.setTimeZone(cal.getTimeZone());
            String currentTimestamp = formatter.format(cal.getTime());

            String tmp = getStartTime();
            //tmp
            File externalDirectory = Environment.getExternalStorageDirectory();
            File appDirectory = new File(externalDirectory, "Movesense");
            File logFile;
            if(file.getName().contains("MultiSensorUsageActivity"))
                logFile = new File(appDirectory, tmp + "_" + file.getName() + "_" +getActivity() +"_"+ currentTimestamp + ".csv");
            else
                logFile = new File(appDirectory, tmp + "_" + file.getName() +"_"+ currentTimestamp + ".csv");
            file.renameTo(logFile);
            fileWriter.close();

            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createLogFile(String sensorName) {
        if (isExternalStorageWritable()) {
            File externalDirectory = Environment.getExternalStorageDirectory();
            File appDirectory = new File(externalDirectory, "Movesense");
            File logFile = new File(appDirectory, createFileName(sensorName));

            // create app folder
            if (!appDirectory.exists()) {
                boolean status = appDirectory.mkdirs();
                Log.e(TAG, "appDirectory created: " + status);
            }

            // create log file
            if (!logFile.exists()) {
                boolean status = false;
                try {
                    status = logFile.createNewFile();
                    return logFile;
                } catch (IOException e) {
                    Log.e(TAG, "logFile.createNewFile(): ", e);
                    e.printStackTrace();
                }
                Log.e(TAG, "logFile.createNewFile() created: " + status);
            } else {
                return logFile;
            }
        } else {
            Log.e(TAG, "createFile isExternalStorageWritable Error");
        }
        return null;
    }

    private String createFileName(String tag) {
        // timestamp + device serial + data type,
        StringBuilder sb = new StringBuilder();
        // Get connected device serial
        String deviceName = "Unknown";
        if (MovesenseConnectedDevices.getConnectedDevices().size() > 0) {
            deviceName = MovesenseConnectedDevices.getConnectedDevice(0).getSerial();
        }

        sb.append(deviceName).append("_")
                .append(tag);

        return sb.toString();
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean checkRuntimeWriteExternalStoragePermission(Context context, final Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.write_external_storage_permission_title)
                        .setMessage(R.string.write_external_storage_permission_text)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                requestForWriteExternalStoragePermission(activity);
                            }
                        })
                        .create()
                        .show();

            } else {
                requestForWriteExternalStoragePermission(activity);
            }
            Log.e(TAG, "checkRuntimeWriteExternalStoragePermission() FALSE");
            return false;
        } else {
            Log.e(TAG, "checkRuntimeWriteExternalStoragePermission() TRUE");
            return true;
        }
    }

    private void requestForWriteExternalStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }
}
