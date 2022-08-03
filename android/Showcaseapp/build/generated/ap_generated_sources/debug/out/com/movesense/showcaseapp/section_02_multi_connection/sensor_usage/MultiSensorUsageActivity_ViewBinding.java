// Generated code from Butter Knife. Do not modify!
package com.movesense.showcaseapp.section_02_multi_connection.sensor_usage;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.movesense.showcaseapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MultiSensorUsageActivity_ViewBinding implements Unbinder {
  private MultiSensorUsageActivity target;

  private View view7f0700d7;

  private View view7f0700cb;

  @UiThread
  public MultiSensorUsageActivity_ViewBinding(MultiSensorUsageActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MultiSensorUsageActivity_ViewBinding(final MultiSensorUsageActivity target, View source) {
    this.target = target;

    View view;
    target.mSelectedDeviceNameTv1 = Utils.findRequiredViewAsType(source, R.id.selectedDeviceName_Tv_1, "field 'mSelectedDeviceNameTv1'", TextView.class);
    target.mSelectedDeviceInfoLl1 = Utils.findRequiredViewAsType(source, R.id.selectedDeviceInfo_Ll_1, "field 'mSelectedDeviceInfoLl1'", LinearLayout.class);
    target.mSelectedDeviceNameTv2 = Utils.findRequiredViewAsType(source, R.id.selectedDeviceName_Tv_2, "field 'mSelectedDeviceNameTv2'", TextView.class);
    target.mSelectedDeviceInfoLl2 = Utils.findRequiredViewAsType(source, R.id.selectedDeviceInfo_Ll_2, "field 'mSelectedDeviceInfoLl2'", LinearLayout.class);
    target.mSelectedDeviceNameTv3 = Utils.findRequiredViewAsType(source, R.id.selectedDeviceName_Tv_3, "field 'mSelectedDeviceNameTv3'", TextView.class);
    target.mSelectedDeviceInfoLl3 = Utils.findRequiredViewAsType(source, R.id.selectedDeviceInfo_Ll_3, "field 'mSelectedDeviceInfoLl3'", LinearLayout.class);
    target.mMultiSensorUsageSelectedDeviceMovesense1Ll = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_selectedDevice_movesense1Ll, "field 'mMultiSensorUsageSelectedDeviceMovesense1Ll'", LinearLayout.class);
    target.mMultiSensorUsageSelectedDeviceMovesense2Ll = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_selectedDevice_movesense2Ll, "field 'mMultiSensorUsageSelectedDeviceMovesense2Ll'", LinearLayout.class);
    target.mMultiSensorUsageSelectedDeviceMovesense3Ll = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_selectedDevice_movesense3Ll, "field 'mMultiSensorUsageSelectedDeviceMovesense3Ll'", LinearLayout.class);
    target.mMultiSensorUsageLinearAccTextView = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_textView, "field 'mMultiSensorUsageLinearAccTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.multiSensorUsage_linearAcc_switch, "field 'mMultiSensorUsageLinearAccSwitch' and method 'onLinearAccCheckedChange'");
    target.mMultiSensorUsageLinearAccSwitch = Utils.castView(view, R.id.multiSensorUsage_linearAcc_switch, "field 'mMultiSensorUsageLinearAccSwitch'", SwitchCompat.class);
    view7f0700d7 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onLinearAccCheckedChange(p0, p1);
      }
    });
    target.mMultiSensorUsageLinearAccDevice1XTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device1_x_tv, "field 'mMultiSensorUsageLinearAccDevice1XTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice1YTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device1_y_tv, "field 'mMultiSensorUsageLinearAccDevice1YTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice1ZTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device1_z_tv, "field 'mMultiSensorUsageLinearAccDevice1ZTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice2XTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device2_x_tv, "field 'mMultiSensorUsageLinearAccDevice2XTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice2YTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device2_y_tv, "field 'mMultiSensorUsageLinearAccDevice2YTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice2ZTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device2_z_tv, "field 'mMultiSensorUsageLinearAccDevice2ZTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice3XTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device3_x_tv, "field 'mMultiSensorUsageLinearAccDevice3XTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice3YTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device3_y_tv, "field 'mMultiSensorUsageLinearAccDevice3YTv'", TextView.class);
    target.mMultiSensorUsageLinearAccDevice3ZTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_device3_z_tv, "field 'mMultiSensorUsageLinearAccDevice3ZTv'", TextView.class);
    target.mMultiSensorUsageLinearAccContainerLl = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_linearAcc_containerLl, "field 'mMultiSensorUsageLinearAccContainerLl'", LinearLayout.class);
    target.mMultiSensorUsageAngularVelocityTextView = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_textView, "field 'mMultiSensorUsageAngularVelocityTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.multiSensorUsage_angularVelocity_switch, "field 'mMultiSensorUsageAngularVelocitySwitch' and method 'onAngularVelocityCheckedChange'");
    target.mMultiSensorUsageAngularVelocitySwitch = Utils.castView(view, R.id.multiSensorUsage_angularVelocity_switch, "field 'mMultiSensorUsageAngularVelocitySwitch'", SwitchCompat.class);
    view7f0700cb = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onAngularVelocityCheckedChange(p0, p1);
      }
    });
    target.mMultiSensorUsageAngularVelocityDevice1XTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device1_x_tv, "field 'mMultiSensorUsageAngularVelocityDevice1XTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice1YTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device1_y_tv, "field 'mMultiSensorUsageAngularVelocityDevice1YTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice1ZTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device1_z_tv, "field 'mMultiSensorUsageAngularVelocityDevice1ZTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice2XTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device2_x_tv, "field 'mMultiSensorUsageAngularVelocityDevice2XTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice2YTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device2_y_tv, "field 'mMultiSensorUsageAngularVelocityDevice2YTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice2ZTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device2_z_tv, "field 'mMultiSensorUsageAngularVelocityDevice2ZTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice3XTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device3_x_tv, "field 'mMultiSensorUsageAngularVelocityDevice3XTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice3YTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device3_y_tv, "field 'mMultiSensorUsageAngularVelocityDevice3YTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityDevice3ZTv = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_device3_z_tv, "field 'mMultiSensorUsageAngularVelocityDevice3ZTv'", TextView.class);
    target.mMultiSensorUsageAngularVelocityContainerLl = Utils.findRequiredViewAsType(source, R.id.multiSensorUsage_angularVelocity_containerLl, "field 'mMultiSensorUsageAngularVelocityContainerLl'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MultiSensorUsageActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mSelectedDeviceNameTv1 = null;
    target.mSelectedDeviceInfoLl1 = null;
    target.mSelectedDeviceNameTv2 = null;
    target.mSelectedDeviceInfoLl2 = null;
    target.mSelectedDeviceNameTv3 = null;
    target.mSelectedDeviceInfoLl3 = null;
    target.mMultiSensorUsageSelectedDeviceMovesense1Ll = null;
    target.mMultiSensorUsageSelectedDeviceMovesense2Ll = null;
    target.mMultiSensorUsageSelectedDeviceMovesense3Ll = null;
    target.mMultiSensorUsageLinearAccTextView = null;
    target.mMultiSensorUsageLinearAccSwitch = null;
    target.mMultiSensorUsageLinearAccDevice1XTv = null;
    target.mMultiSensorUsageLinearAccDevice1YTv = null;
    target.mMultiSensorUsageLinearAccDevice1ZTv = null;
    target.mMultiSensorUsageLinearAccDevice2XTv = null;
    target.mMultiSensorUsageLinearAccDevice2YTv = null;
    target.mMultiSensorUsageLinearAccDevice2ZTv = null;
    target.mMultiSensorUsageLinearAccDevice3XTv = null;
    target.mMultiSensorUsageLinearAccDevice3YTv = null;
    target.mMultiSensorUsageLinearAccDevice3ZTv = null;
    target.mMultiSensorUsageLinearAccContainerLl = null;
    target.mMultiSensorUsageAngularVelocityTextView = null;
    target.mMultiSensorUsageAngularVelocitySwitch = null;
    target.mMultiSensorUsageAngularVelocityDevice1XTv = null;
    target.mMultiSensorUsageAngularVelocityDevice1YTv = null;
    target.mMultiSensorUsageAngularVelocityDevice1ZTv = null;
    target.mMultiSensorUsageAngularVelocityDevice2XTv = null;
    target.mMultiSensorUsageAngularVelocityDevice2YTv = null;
    target.mMultiSensorUsageAngularVelocityDevice2ZTv = null;
    target.mMultiSensorUsageAngularVelocityDevice3XTv = null;
    target.mMultiSensorUsageAngularVelocityDevice3YTv = null;
    target.mMultiSensorUsageAngularVelocityDevice3ZTv = null;
    target.mMultiSensorUsageAngularVelocityContainerLl = null;

    ((CompoundButton) view7f0700d7).setOnCheckedChangeListener(null);
    view7f0700d7 = null;
    ((CompoundButton) view7f0700cb).setOnCheckedChangeListener(null);
    view7f0700cb = null;
  }
}
