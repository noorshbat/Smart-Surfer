package com.movesense.showcaseapp.csv;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.movesense.showcaseapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GyroMultiCsvLogger {

    private final String TAG = GyroMultiCsvLogger.class.getSimpleName();
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 99;
    public static final int MAX_CAPACITY = 2147483647;
    public File file;
    public FileWriter fileWriter;
    private boolean isHeaderExists = false;

    public GyroMultiCsvLogger() {
        try {
            file = createLogFile();
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //NEW
    public String startTime;
    public String activity;


    public void appendHeader(String header) {
        if (!isHeaderExists) {
            try {
                fileWriter.write(header);
                fileWriter.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isHeaderExists = true;
    }

    public void appendLine(String line) {
        try {
            fileWriter.write(line);
            fileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
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
            //NEW
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            formatter.setTimeZone(cal.getTimeZone());
            String currentTimestamp = formatter.format(cal.getTime());

            String tmp = getStartTime();
            //tmp
            File externalDirectory = Environment.getExternalStorageDirectory();
            File appDirectory = new File(externalDirectory, "Movesense");
            File logFile = new File(appDirectory, tmp + "_" + file.getName() + "_" +getActivity() +"_"+ currentTimestamp + ".csv");
            file.renameTo(logFile);
            fileWriter.close();

            //MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createLogFile() {
        if (isExternalStorageWritable()) {
            File externalDirectory = Environment.getExternalStorageDirectory();
            File appDirectory = new File(externalDirectory, "Movesense");
            File logFile = new File(appDirectory, "GyroMultiSensorUsageActivity");

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
