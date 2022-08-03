package com.movesense.showcaseapp.section_02_multi_connection.sensor_usage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movesense.mds.Mds;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsSubscription;
import com.movesense.mds.internal.connectivity.BleManager;
import com.movesense.mds.internal.connectivity.MovesenseConnectedDevices;
import com.movesense.showcaseapp.BaseActivity;
import com.movesense.showcaseapp.R;
import com.movesense.showcaseapp.bluetooth.MdsRx;
import com.movesense.showcaseapp.bluetooth.MultiConnectionLostDialog;
import com.movesense.showcaseapp.model.AngularVelocity;
import com.movesense.showcaseapp.model.LinearAcceleration;
import com.movesense.showcaseapp.model.MdsConnectedDevice;
import com.movesense.showcaseapp.section_00_mainView.MainViewActivity;
import com.movesense.showcaseapp.utils.FormatHelper;
import com.movesense.showcaseapp.utils.ThrowableToastingAction;
import com.movesense.showcaseapp.csv.AccMultiCsvLogger;
import com.movesense.showcaseapp.csv.GyroMultiCsvLogger;
import com.polidea.rxandroidble2.RxBleDevice;


import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.reactivex.functions.Consumer;
import io.reactivex.disposables.CompositeDisposable;

public class MultiSensorUsageActivity extends BaseActivity implements MultiSensorUsageContract.View, BleManager.IBleConnectionMonitor {

    @BindView(R.id.selectedDeviceName_Tv_1) TextView mSelectedDeviceNameTv1;
    @BindView(R.id.selectedDeviceInfo_Ll_1) LinearLayout mSelectedDeviceInfoLl1;
    @BindView(R.id.selectedDeviceName_Tv_2) TextView mSelectedDeviceNameTv2;
    @BindView(R.id.selectedDeviceInfo_Ll_2) LinearLayout mSelectedDeviceInfoLl2;
    @BindView(R.id.selectedDeviceName_Tv_3) TextView mSelectedDeviceNameTv3;
    @BindView(R.id.selectedDeviceInfo_Ll_3) LinearLayout mSelectedDeviceInfoLl3;
    @BindView(R.id.multiSensorUsage_selectedDevice_movesense1Ll) LinearLayout mMultiSensorUsageSelectedDeviceMovesense1Ll;
    @BindView(R.id.multiSensorUsage_selectedDevice_movesense2Ll) LinearLayout mMultiSensorUsageSelectedDeviceMovesense2Ll;
    @BindView(R.id.multiSensorUsage_selectedDevice_movesense3Ll) LinearLayout mMultiSensorUsageSelectedDeviceMovesense3Ll;

    @BindView(R.id.multiSensorUsage_linearAcc_textView) TextView mMultiSensorUsageLinearAccTextView;
    @BindView(R.id.multiSensorUsage_linearAcc_switch) SwitchCompat mMultiSensorUsageLinearAccSwitch;
    @BindView(R.id.multiSensorUsage_linearAcc_device1_x_tv) TextView mMultiSensorUsageLinearAccDevice1XTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device1_y_tv) TextView mMultiSensorUsageLinearAccDevice1YTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device1_z_tv) TextView mMultiSensorUsageLinearAccDevice1ZTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device2_x_tv) TextView mMultiSensorUsageLinearAccDevice2XTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device2_y_tv) TextView mMultiSensorUsageLinearAccDevice2YTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device2_z_tv) TextView mMultiSensorUsageLinearAccDevice2ZTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device3_x_tv) TextView mMultiSensorUsageLinearAccDevice3XTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device3_y_tv) TextView mMultiSensorUsageLinearAccDevice3YTv;
    @BindView(R.id.multiSensorUsage_linearAcc_device3_z_tv) TextView mMultiSensorUsageLinearAccDevice3ZTv;
    @BindView(R.id.multiSensorUsage_linearAcc_containerLl) LinearLayout mMultiSensorUsageLinearAccContainerLl;
    @BindView(R.id.multiSensorUsage_angularVelocity_textView) TextView mMultiSensorUsageAngularVelocityTextView;
    @BindView(R.id.multiSensorUsage_angularVelocity_switch) SwitchCompat mMultiSensorUsageAngularVelocitySwitch;
    @BindView(R.id.multiSensorUsage_angularVelocity_device1_x_tv) TextView mMultiSensorUsageAngularVelocityDevice1XTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device1_y_tv) TextView mMultiSensorUsageAngularVelocityDevice1YTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device1_z_tv) TextView mMultiSensorUsageAngularVelocityDevice1ZTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device2_x_tv) TextView mMultiSensorUsageAngularVelocityDevice2XTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device2_y_tv) TextView mMultiSensorUsageAngularVelocityDevice2YTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device2_z_tv) TextView mMultiSensorUsageAngularVelocityDevice2ZTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device3_x_tv) TextView mMultiSensorUsageAngularVelocityDevice3XTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device3_y_tv) TextView mMultiSensorUsageAngularVelocityDevice3YTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_device3_z_tv) TextView mMultiSensorUsageAngularVelocityDevice3ZTv;
    @BindView(R.id.multiSensorUsage_angularVelocity_containerLl) LinearLayout mMultiSensorUsageAngularVelocityContainerLl;


    private MultiSensorUsagePresenter mPresenter;
    private CompositeDisposable mCompositeSubscription;

    private final String TAG = MultiSensorUsageActivity.class.getSimpleName();

    private final String LINEAR_ACC_PATH = "Meas/Acc/52";
    private final String ANGULAR_VELOCITY_PATH = "Meas/Gyro/52";
    private final String TAP = "System/States/4";
    private MdsSubscription mMdsLinearAccSubscription1;
    private MdsSubscription mMdsLinearAccSubscription2;
    private MdsSubscription mMdsLinearAccSubscription3;

    private MdsSubscription mMdsAngularVelocitySubscription1;
    private MdsSubscription mMdsAngularVelocitySubscription2;
    private MdsSubscription mMdsAngularVelocitySubscription3;


    private AccMultiCsvLogger mCsvLoggerAcc;
    private GyroMultiCsvLogger mCsvLoggerGyro;
    private boolean isAccLogSaved = false;
    private boolean isGyroLogSaved = false;
    public boolean isAccSubscriped = false;
    public boolean isGyroSubscriped = false;
    int counterAcc1=0;
    long firstTimeAcc1=0;
    int counterAcc2=0;
    long firstTimeAcc2=0;
    int counterAcc3=0;
    long firstTimeAcc3=0;
    int counterGyro1=0;
    long firstTimeGyro1=0;
    int counterGyro2=0;
    long firstTimeGyro2=0;
    int counterGyro3=0;
    long firstTimeGyro3=0;


    private String millisecondsToTime(long milliseconds) {
        long hours =  ((milliseconds / 1000) / 3600) % 24;
        long minutes = ((milliseconds / 1000) / 60) % 60;
        long seconds = (milliseconds / 1000) % 60;
        return hours + ":" + minutes + ":" + seconds;
    }

    /* private String addtime(String time1, String time2) {
       int time1_secs=Integer.parseInt(time1.)*10+time1[7]);
    }*/

    private void AccUnSubscribe() {
        if (mMdsLinearAccSubscription1 != null) {
            mMdsLinearAccSubscription1.unsubscribe();
            mMdsLinearAccSubscription1 = null;
        }
        if (mMdsLinearAccSubscription2 != null) {
            mMdsLinearAccSubscription2.unsubscribe();
            mMdsLinearAccSubscription2 = null;
        }
        if (mMdsLinearAccSubscription3 != null) {
            mMdsLinearAccSubscription3.unsubscribe();
            mMdsLinearAccSubscription3 = null;
        }
        if(isAccSubscriped) {
            mCsvLoggerAcc.finishSavingLogs(this, TAG);
            isAccSubscriped = false;
            isAccLogSaved = true;
        }
    }

    private void GyroUnSubscribe() {
        if (mMdsAngularVelocitySubscription1 != null) {
            mMdsAngularVelocitySubscription1.unsubscribe();
            mMdsAngularVelocitySubscription1 = null;
        }
        if (mMdsAngularVelocitySubscription2 != null) {
            mMdsAngularVelocitySubscription2.unsubscribe();
            mMdsAngularVelocitySubscription2 = null;
        }
        if (mMdsAngularVelocitySubscription3 != null) {
            mMdsAngularVelocitySubscription3.unsubscribe();
            mMdsAngularVelocitySubscription3 = null;
        }
        if(isGyroSubscriped) {
            mCsvLoggerGyro.finishSavingLogs(this, TAG);
            isGyroSubscriped = false;
            isGyroLogSaved = true;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isAccSubscriped) {
            mCsvLoggerAcc.finishSavingLogs(this, TAG);
            isAccSubscriped = false;
            isAccLogSaved = true;
        }
        if(isGyroSubscriped) {
            mCsvLoggerGyro.finishSavingLogs(this, TAG);
            isGyroSubscriped = false;
            isGyroLogSaved = true;
        }

        AccUnSubscribe();
        GyroUnSubscribe();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sensor_usage);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Multi Sensor Usage");
        }
        mCsvLoggerAcc = new AccMultiCsvLogger();
        mCsvLoggerAcc.appendHeader("Device:,Serial:,Timestamp (h:m:s),X:,Y:,Z:");

        mCsvLoggerGyro = new GyroMultiCsvLogger();
        mCsvLoggerGyro.appendHeader("Device:,Serial:,Timestamp (h:m:s),X:,Y:,Z:");

        mPresenter = new MultiSensorUsagePresenter(this);
        mCompositeSubscription = new CompositeDisposable();

        mSelectedDeviceNameTv1.setText(MovesenseConnectedDevices.getConnectedDevice(0).getSerial() + " " +
                MovesenseConnectedDevices.getConnectedDevice(0).getMacAddress());

        mSelectedDeviceNameTv2.setText(MovesenseConnectedDevices.getConnectedDevice(1).getSerial() + " " +
                MovesenseConnectedDevices.getConnectedDevice(1).getMacAddress());

        mSelectedDeviceNameTv3.setText(MovesenseConnectedDevices.getConnectedDevice(2).getSerial() + " " +
                MovesenseConnectedDevices.getConnectedDevice(2).getMacAddress());

        mCompositeSubscription.add(MdsRx.Instance.connectedDeviceObservable()
                .subscribe(new Consumer<MdsConnectedDevice>() {
                    @Override
                    public void accept(MdsConnectedDevice mdsConnectedDevice) {
                        if (mdsConnectedDevice.getConnection() == null) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(MultiSensorUsageActivity.this, MainViewActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            }, 1000);
                        }
                    }
                }, new ThrowableToastingAction(this)));
    }

    /**
     * Linear Acceleration Switch
     *
     * @param buttonView
     * @param isChecked
     */
    @OnCheckedChanged(R.id.multiSensorUsage_linearAcc_switch)
    public void onLinearAccCheckedChange(final CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isAccSubscriped=true;
            isAccLogSaved = false;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            formatter.setTimeZone(cal.getTimeZone());
            String currentTimestamp = formatter.format(cal.getTime());
            mCsvLoggerAcc.setStartTime(currentTimestamp);
            mCsvLoggerAcc.setActivity("LinearAcceleration");
            Log.d(TAG, "=== Linear Acceleration Subscribe ===");
            mMdsLinearAccSubscription1 = Mds.builder().build(this).subscribe("suunto://MDS/EventListener",
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(0)
                            .getSerial(), LINEAR_ACC_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            LinearAcceleration linearAccelerationData = new Gson().fromJson(
                                    s, LinearAcceleration.class);
                            if(counterAcc1==0){
                                firstTimeAcc1 = linearAccelerationData.body.timestamp;
                                counterAcc1++;
                            }

                            if (linearAccelerationData != null) {
                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float)sampleRate;
                                Calendar cal = Calendar.getInstance();
                                LinearAcceleration.Array arrayData = null;

                                for (int i=0; i<linearAccelerationData.body.array.length; i++)
                                {
                                    arrayData = linearAccelerationData.body.array[i];
                                    String newTimestamp = millisecondsToTime(linearAccelerationData.body.timestamp+Math.round(sampleInterval * i)-firstTimeAcc1);
                                    mCsvLoggerAcc.appendLine(String.format(Locale.getDefault(),
                                            "Device 1,%s,%s,%.6f,%.6f,%.6f",
                                            MovesenseConnectedDevices.getConnectedDevice(0).getSerial(),newTimestamp,
                                            arrayData.x, arrayData.y, arrayData.z));
                                }
                                mMultiSensorUsageLinearAccDevice1XTv.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                mMultiSensorUsageLinearAccDevice1YTv.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                mMultiSensorUsageLinearAccDevice1ZTv.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));
                            }
                        }

                        @Override
                        public void onError(MdsException e) {
                            buttonView.setChecked(false);
                            AccUnSubscribe();
                            Toast.makeText(MultiSensorUsageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


            mMdsLinearAccSubscription2 = Mds.builder().build(this).subscribe("suunto://MDS/EventListener",
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(1)
                            .getSerial(), LINEAR_ACC_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            LinearAcceleration linearAccelerationData = new Gson().fromJson(
                                    s, LinearAcceleration.class);
                            if(counterAcc2==0){
                                firstTimeAcc2 = linearAccelerationData.body.timestamp;
                                counterAcc2++;
                            }
                            if (linearAccelerationData != null) {
                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float)sampleRate;

                                LinearAcceleration.Array arrayData = null;
                                for (int i=0; i<linearAccelerationData.body.array.length; i++)
                                {
                                    arrayData = linearAccelerationData.body.array[i];
                                    String currentTimestamp = millisecondsToTime(linearAccelerationData.body.timestamp+Math.round(sampleInterval * i)-firstTimeAcc2);
                                    mCsvLoggerAcc.appendLine(String.format(Locale.getDefault(),
                                            "Device 2,%s,%s,%.6f,%.6f,%.6f",
                                            MovesenseConnectedDevices.getConnectedDevice(1).getSerial(),currentTimestamp,
                                            arrayData.x, arrayData.y, arrayData.z));
                                }
                                mMultiSensorUsageLinearAccDevice2XTv.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                mMultiSensorUsageLinearAccDevice2YTv.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                mMultiSensorUsageLinearAccDevice2ZTv.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));
                            }
                        }

                        @Override
                        public void onError(MdsException e) {
                            buttonView.setChecked(false);
                            AccUnSubscribe();
                            Toast.makeText(MultiSensorUsageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

            mMdsLinearAccSubscription3 = Mds.builder().build(this).subscribe("suunto://MDS/EventListener",
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(2)
                            .getSerial(), LINEAR_ACC_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            LinearAcceleration linearAccelerationData = new Gson().fromJson(
                                    s, LinearAcceleration.class);
                            if(counterAcc3==0){
                                firstTimeAcc3 = linearAccelerationData.body.timestamp;
                                counterAcc3++;
                            }
                            if (linearAccelerationData != null) {
                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float)sampleRate;

                                LinearAcceleration.Array arrayData = null;
                                for (int i=0; i<linearAccelerationData.body.array.length; i++)
                                {
                                    arrayData = linearAccelerationData.body.array[i];
                                    String currentTimestamp = millisecondsToTime(linearAccelerationData.body.timestamp+Math.round(sampleInterval * i)-firstTimeAcc3);
                                    mCsvLoggerAcc.appendLine(String.format(Locale.getDefault(),
                                            "Device 3,%s,%s,%.6f,%.6f,%.6f",
                                            MovesenseConnectedDevices.getConnectedDevice(2).getSerial(),currentTimestamp,
                                            arrayData.x, arrayData.y, arrayData.z));
                                }
                                mMultiSensorUsageLinearAccDevice3XTv.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                mMultiSensorUsageLinearAccDevice3YTv.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                mMultiSensorUsageLinearAccDevice3ZTv.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));
                            }
                        }

                        @Override
                        public void onError(MdsException e) {
                            buttonView.setChecked(false);
                            AccUnSubscribe();
                            Toast.makeText(MultiSensorUsageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            mMdsLinearAccSubscription1.unsubscribe();
            mMdsLinearAccSubscription2.unsubscribe();
            mMdsLinearAccSubscription3.unsubscribe();

            Log.d(TAG, "=== Linear Acceleration Unubscribe ===");
        }

    }


    /**
     * Angular Velocity Switch
     *
     * @param buttonView
     * @param isChecked
     */
    @OnCheckedChanged(R.id.multiSensorUsage_angularVelocity_switch)
    public void onAngularVelocityCheckedChange(final CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isGyroSubscriped=true;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            formatter.setTimeZone(cal.getTimeZone());
            String currentTimestamp = formatter.format(cal.getTime());
            mCsvLoggerGyro.setStartTime(currentTimestamp);
            mCsvLoggerGyro.setActivity("AngularVelocity");
            isGyroLogSaved = false;
            Log.d(TAG, "=== Angular Velocity Subscribe ===");

            mMdsAngularVelocitySubscription1 = Mds.builder().build(this).subscribe("suunto://MDS/EventListener",
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(0)
                            .getSerial(), ANGULAR_VELOCITY_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            AngularVelocity angularVelocity = new Gson().fromJson(
                                    s, AngularVelocity.class);
                            if(counterGyro1==0){
                                firstTimeGyro1 = angularVelocity.body.timestamp;
                                counterGyro1++;
                            }
                            if (angularVelocity != null) {

                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float) sampleRate;

                                AngularVelocity.Array arrayData = null;
                                for (int i = 0; i < angularVelocity.body.array.length; i++) {
                                    arrayData = angularVelocity.body.array[i];
                                    String currentTimestamp = millisecondsToTime(angularVelocity.body.timestamp + Math.round(sampleInterval * i)-firstTimeGyro1);
                                    mCsvLoggerGyro.appendLine(String.format(Locale.getDefault(),
                                            "Device 1,%s,%s,%.6f,%.6f,%.6f",
                                            MovesenseConnectedDevices.getConnectedDevice(0).getSerial(), currentTimestamp,
                                            arrayData.x, arrayData.y, arrayData.z));
                                }
                                mMultiSensorUsageAngularVelocityDevice1XTv.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                mMultiSensorUsageAngularVelocityDevice1YTv.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                mMultiSensorUsageAngularVelocityDevice1ZTv.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));
                            }
                        }

                        @Override
                        public void onError(MdsException e) {
                            buttonView.setChecked(false);
                            GyroUnSubscribe();
                            Toast.makeText(MultiSensorUsageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


            mMdsAngularVelocitySubscription2 = Mds.builder().build(this).subscribe("suunto://MDS/EventListener",
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(1)
                            .getSerial(), ANGULAR_VELOCITY_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            AngularVelocity angularVelocity = new Gson().fromJson(
                                    s, AngularVelocity.class);
                            if(counterGyro2==0){
                                firstTimeGyro2 = angularVelocity.body.timestamp;
                                counterGyro2++;
                            }
                            if (angularVelocity != null) {

                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float) sampleRate;

                                AngularVelocity.Array arrayData = null;
                                for (int i = 0; i < angularVelocity.body.array.length; i++) {
                                    arrayData = angularVelocity.body.array[i];
                                    String currentTimestamp = millisecondsToTime(angularVelocity.body.timestamp + Math.round(sampleInterval * i)-firstTimeGyro2);
                                    mCsvLoggerGyro.appendLine(String.format(Locale.getDefault(),
                                            "Device 2,%s,%s,%.6f,%.6f,%.6f",
                                            MovesenseConnectedDevices.getConnectedDevice(1).getSerial(), currentTimestamp,
                                            arrayData.x, arrayData.y, arrayData.z));
                                }
                                mMultiSensorUsageAngularVelocityDevice2XTv.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                mMultiSensorUsageAngularVelocityDevice2YTv.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                mMultiSensorUsageAngularVelocityDevice2ZTv.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));
                            }
                        }

                        @Override
                        public void onError(MdsException e) {
                            buttonView.setChecked(false);
                            GyroUnSubscribe();
                            Toast.makeText(MultiSensorUsageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

            mMdsAngularVelocitySubscription3 = Mds.builder().build(this).subscribe("suunto://MDS/EventListener",
                    FormatHelper.formatContractToJson(MovesenseConnectedDevices.getConnectedDevice(2)
                            .getSerial(), ANGULAR_VELOCITY_PATH), new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            AngularVelocity angularVelocity = new Gson().fromJson(
                                    s, AngularVelocity.class);
                            if(counterGyro3==0){
                                firstTimeGyro3 = angularVelocity.body.timestamp;
                                counterGyro3++;
                            }
                            if (angularVelocity != null) {

                                final int sampleRate = Integer.parseInt("52");
                                final float sampleInterval = 1000.0f / (float) sampleRate;

                                AngularVelocity.Array arrayData = null;
                                for (int i = 0; i < angularVelocity.body.array.length; i++) {
                                    arrayData = angularVelocity.body.array[i];
                                    String currentTimestamp = millisecondsToTime(angularVelocity.body.timestamp + Math.round(sampleInterval * i)-firstTimeGyro3);
                                    mCsvLoggerGyro.appendLine(String.format(Locale.getDefault(),
                                            "Device 3,%s,%s,%.6f,%.6f,%.6f",
                                            MovesenseConnectedDevices.getConnectedDevice(2).getSerial(), currentTimestamp,
                                            arrayData.x, arrayData.y, arrayData.z));
                                }
                                mMultiSensorUsageAngularVelocityDevice3XTv.setText(String.format(Locale.getDefault(),
                                        "x: %.6f", arrayData.x));
                                mMultiSensorUsageAngularVelocityDevice3YTv.setText(String.format(Locale.getDefault(),
                                        "y: %.6f", arrayData.y));
                                mMultiSensorUsageAngularVelocityDevice3ZTv.setText(String.format(Locale.getDefault(),
                                        "z: %.6f", arrayData.z));
                            }
                        }

                        @Override
                        public void onError(MdsException e) {
                            buttonView.setChecked(false);
                            GyroUnSubscribe();
                            Toast.makeText(MultiSensorUsageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            mMdsAngularVelocitySubscription1.unsubscribe();
            mMdsAngularVelocitySubscription2.unsubscribe();
            mMdsAngularVelocitySubscription3.unsubscribe();

            Log.d(TAG, "=== Angular Velocity Unsubscribe ===");
        }
    }


    @Override
    public void setPresenter(MultiSensorUsageContract.Presenter presenter) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage(R.string.disconnect_dialog_text)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "TEST Disconnecting...");
                        BleManager.INSTANCE.disconnect(MovesenseConnectedDevices.getConnectedRxDevice(0));
                        BleManager.INSTANCE.disconnect(MovesenseConnectedDevices.getConnectedRxDevice(1));
                        BleManager.INSTANCE.disconnect(MovesenseConnectedDevices.getConnectedRxDevice(2));

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onDisconnect(String s) {
        Log.d(TAG, "onDisconnect: " + s);
        if (!isFinishing()) {
            runOnUiThread(() -> MultiConnectionLostDialog.INSTANCE.showDialog(MultiSensorUsageActivity.this));
        }
    }

    @Override
    public void onConnect(RxBleDevice rxBleDevice) {
        Log.d(TAG, "onConnect: " + rxBleDevice.getName() + " " + rxBleDevice.getMacAddress());
        MultiConnectionLostDialog.INSTANCE.dismissDialog();
    }

    @Override
    public void onConnectError(String s, Throwable throwable) {

    }
}
