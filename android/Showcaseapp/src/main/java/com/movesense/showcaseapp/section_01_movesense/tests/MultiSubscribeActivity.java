package com.movesense.showcaseapp.section_01_movesense.tests;

import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.movesense.mds.Mds;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsSubscription;
import com.movesense.mds.internal.connectivity.BleManager;
import com.movesense.mds.internal.connectivity.MovesenseConnectedDevices;
import com.movesense.showcaseapp.BaseActivity;
import com.movesense.showcaseapp.R;
import com.movesense.showcaseapp.bluetooth.ConnectionLostDialog;
import com.movesense.showcaseapp.csv.CsvLogger;
import com.movesense.showcaseapp.model.AngularVelocity;
import com.movesense.showcaseapp.model.InfoResponse;
import com.movesense.showcaseapp.model.LinearAcceleration;
import com.movesense.showcaseapp.utils.FormatHelper;
import com.polidea.rxandroidble2.RxBleDevice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;


public class MultiSubscribeActivity extends BaseActivity implements BleManager.IBleConnectionMonitor {

    @BindView(R.id.switchSubscriptionLinearAcc) SwitchCompat switchSubscriptionLinearAcc;
    @BindView(R.id.x_axis_linearAcc_textView) TextView xAxisLinearAccTextView;
    @BindView(R.id.y_axis_linearAcc_textView) TextView yAxisLinearAccTextView;
    @BindView(R.id.z_axis_linearAcc_textView) TextView zAxisLinearAccTextView;
    @BindView(R.id.switchSubscriptionAngularVelocity) SwitchCompat switchSubscriptionAngularVelocity;
    @BindView(R.id.x_axis_angularVelocity_textView) TextView xAxisAngularVelocityTextView;
    @BindView(R.id.y_axis_angularVelocity_textView) TextView yAxisAngularVelocityTextView;
    @BindView(R.id.z_axis_angularVelocity_textView) TextView zAxisAngularVelocityTextView;

    private final String LOG_TAG = MultiSubscribeActivity.class.getSimpleName();
    private final String LINEAR_ACCELERATION_PATH = "Meas/Acc/52";
    private final String ANGULAR_VELOCITY_PATH = "Meas/Gyro/52";
    public static final String URI_EVENTLISTENER = "suunto://MDS/EventListener";
    @BindView(R.id.connected_device_name_textView) TextView mConnectedDeviceNameTextView;
    @BindView(R.id.connected_device_swVersion_textView) TextView mConnectedDeviceSwVersionTextView;
    private MdsSubscription mdsSubscriptionLinearAcc;
    private MdsSubscription mdsSubscriptionAngularVelocity;


    private CsvLogger mCsvLoggerAcc;
    private CsvLogger mCsvLoggerGyro;
    private boolean isAccLogSaved = false;
    private boolean isGyroLogSaved = false;
    public boolean isAccSubscriped = false;
    public boolean isGyroSubscriped = false;
    int counterAcc=0;
    int counterGyro=0;
    long firstTimeAcc=0;
    long firstTimeGyro=0;


    private String millisecondsToTime(long milliseconds) {
        long hours =  ((milliseconds / 1000) / 3600) % 24;
        long minutes = ((milliseconds / 1000) / 60) % 60;
        long seconds = (milliseconds / 1000) % 60;
        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_subscribe);
        ButterKnife.bind(this);

        mCsvLoggerAcc = new CsvLogger();
        mCsvLoggerAcc.appendHeader("Service:,Timestamp (h:m:s),X:,Y:,Z:");

        mCsvLoggerGyro = new CsvLogger();
        mCsvLoggerGyro.appendHeader("Service:,Timestamp (h:m:s),X:,Y:,Z:");



        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Multi Subscribe");
        }

        mConnectedDeviceNameTextView.setText("Serial: " + MovesenseConnectedDevices.getConnectedDevice(0)
                .getSerial());

        mConnectedDeviceSwVersionTextView.setText("Sw version: " + MovesenseConnectedDevices.getConnectedDevice(0)
                .getSwVersion());

        BleManager.INSTANCE.addBleConnectionMonitorListener(this);

        mCsvLoggerAcc.checkRuntimeWriteExternalStoragePermission(this, this);
        mCsvLoggerGyro.checkRuntimeWriteExternalStoragePermission(this, this);

    }
    private void AccUnSubscribe() {
        if (mdsSubscriptionLinearAcc != null) {
            mdsSubscriptionLinearAcc.unsubscribe();
            mdsSubscriptionLinearAcc = null;
        }
        if(isAccSubscriped) {
            mCsvLoggerAcc.finishSavingLogs(this, LOG_TAG);
            isAccSubscriped = false;
            isAccLogSaved = true;
        }
    }

    private void GyroUnSubscribe() {
        if (mdsSubscriptionAngularVelocity != null) {
            mdsSubscriptionAngularVelocity.unsubscribe();
            mdsSubscriptionAngularVelocity = null;
        }
        if(isGyroSubscriped) {
            mCsvLoggerGyro.finishSavingLogs(this, LOG_TAG);
            isGyroSubscriped = false;
            isGyroLogSaved = true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        BleManager.INSTANCE.removeBleConnectionMonitorListener(this);
        if(isAccSubscriped) {
            mCsvLoggerAcc.finishSavingLogs(this, LOG_TAG);
            isAccSubscriped = false;
            isAccLogSaved = true;
        }
        if(isGyroSubscriped) {
            mCsvLoggerGyro.finishSavingLogs(this, LOG_TAG);
            isGyroSubscriped = false;
            isGyroLogSaved = true;
        }

        AccUnSubscribe();
        GyroUnSubscribe();
    }

    private void unsubscribe() {
        if (mdsSubscriptionLinearAcc != null) {
            mdsSubscriptionLinearAcc.unsubscribe();
            mdsSubscriptionLinearAcc = null;
        }

        if (mdsSubscriptionAngularVelocity != null) {
            mdsSubscriptionAngularVelocity.unsubscribe();
            mdsSubscriptionAngularVelocity = null;
        }
    }

    @OnCheckedChanged(R.id.switchSubscriptionLinearAcc)
    public void onCheckedChangedLinear(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isAccSubscriped=true;
            isAccLogSaved = false;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            formatter.setTimeZone(cal.getTimeZone());
            String currentTimestamp = formatter.format(cal.getTime());
            mCsvLoggerAcc.setStartTime(currentTimestamp);
            mCsvLoggerAcc.setActivity("LinearAcceleration");
            Log.d(LOG_TAG, "+++ Subscribe LinearAcc");
            mdsSubscriptionLinearAcc = Mds.builder().build(this).subscribe(URI_EVENTLISTENER,
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(0)
                            .getSerial(), LINEAR_ACCELERATION_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String data) {
                            Log.d(LOG_TAG, "onSuccess(): " + data);

                            LinearAcceleration linearAccelerationData = new Gson().fromJson(
                                    data, LinearAcceleration.class);
                            if(counterAcc==0){
                                firstTimeAcc = linearAccelerationData.body.timestamp;
                                counterAcc++;
                            }
                            if (linearAccelerationData != null) {
                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float)sampleRate;
                                LinearAcceleration.Array arrayData = null;
                                for (int i=0; i<linearAccelerationData.body.array.length; i++)
                                {
                                    arrayData = linearAccelerationData.body.array[i];
                                    String newTimestamp = millisecondsToTime(linearAccelerationData.body.timestamp+Math.round(sampleInterval * i)-firstTimeAcc);
                                    mCsvLoggerAcc.appendLine(String.format(Locale.getDefault(),
                                            "LinearAcc,%s,%.6f,%.6f,%.6f", newTimestamp,arrayData.x, arrayData.y, arrayData.z));
                                }

                                xAxisLinearAccTextView.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                yAxisLinearAccTextView.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                zAxisLinearAccTextView.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));

                            }
                        }

                        @Override
                        public void onError(MdsException error) {
                            Log.e(LOG_TAG, "onError(): ", error);
                        }
                    });
        } else {
            Log.d(LOG_TAG, "--- Unsubscribe LinearAcc");
            mdsSubscriptionLinearAcc.unsubscribe();
        }
    }
    @OnCheckedChanged(R.id.switchSubscriptionAngularVelocity)
    public void onCheckedChangedAngularVielocity(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isGyroSubscriped=true;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            formatter.setTimeZone(cal.getTimeZone());
            String currentTimestamp = formatter.format(cal.getTime());
            mCsvLoggerGyro.setStartTime(currentTimestamp);
            mCsvLoggerGyro.setActivity("AngularVelocity");
            isGyroLogSaved = false;
            Log.d(LOG_TAG, "+++ Subscribe AngularVelocity");
            mdsSubscriptionAngularVelocity = Mds.builder().build(this).subscribe(URI_EVENTLISTENER,
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(0)
                            .getSerial(), ANGULAR_VELOCITY_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String data) {
                            Log.d(LOG_TAG, "onSuccess(): " + data);

                            AngularVelocity angularVelocity = new Gson().fromJson(
                                    data, AngularVelocity.class);
                            if(counterGyro==0){
                                firstTimeGyro = angularVelocity.body.timestamp;
                                counterGyro++;
                            }

                            if (angularVelocity != null) {
                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float) sampleRate;

                                AngularVelocity.Array arrayData = null;
                                for (int i = 0; i < angularVelocity.body.array.length; i++) {
                                    arrayData = angularVelocity.body.array[i];
                                    String currentTimestamp = millisecondsToTime(angularVelocity.body.timestamp + Math.round(sampleInterval * i)-firstTimeGyro);
                                    mCsvLoggerGyro.appendLine(String.format(Locale.getDefault(),
                                            "AngularVelocity,%s, %.6f,%.6f,%.6f",currentTimestamp, arrayData.x, arrayData.y, arrayData.z));
                                }

                                xAxisAngularVelocityTextView.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                yAxisAngularVelocityTextView.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                zAxisAngularVelocityTextView.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));

                            }
                        }

                        @Override
                        public void onError(MdsException error) {
                            Log.e(LOG_TAG, "onError(): ", error);
                        }
                    });
        } else {
            Log.d(LOG_TAG, "--- Unsubscribe AngularVelocity");
            mdsSubscriptionAngularVelocity.unsubscribe();
        }
    }

    @Override
    public void onDisconnect(String s) {
        Log.d(LOG_TAG, "onDisconnect: " + s);
        if (!isFinishing()) {
            runOnUiThread(() -> ConnectionLostDialog.INSTANCE.showDialog(MultiSubscribeActivity.this));
        }
    }

    @Override
    public void onConnect(RxBleDevice rxBleDevice) {
        Log.d(LOG_TAG, "onConnect: " + rxBleDevice.getName() + " " + rxBleDevice.getMacAddress());
        ConnectionLostDialog.INSTANCE.dismissDialog();
    }

    @Override
    public void onConnectError(String s, Throwable throwable) {

    }
}
