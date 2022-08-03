// Generated code from Butter Knife. Do not modify!
package com.movesense.showcaseapp.section_01_movesense.device_settings;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.movesense.showcaseapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeviceSettingsActivity_ViewBinding implements Unbinder {
  private DeviceSettingsActivity target;

  private View view7f070069;

  private View view7f070062;

  private View view7f070064;

  private View view7f070067;

  private View view7f070061;

  @UiThread
  public DeviceSettingsActivity_ViewBinding(DeviceSettingsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DeviceSettingsActivity_ViewBinding(final DeviceSettingsActivity target, View source) {
    this.target = target;

    View view;
    target.mDeviceSettingsUartStatusTv = Utils.findRequiredViewAsType(source, R.id.device_settings_uart_status_tv, "field 'mDeviceSettingsUartStatusTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.device_settings_uart_switch, "method 'onUartCheckedChange'");
    view7f070069 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onUartCheckedChange(p0, p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.device_settings_powerOffAfterReset_switch, "method 'onPowerOffCheckedChange'");
    view7f070062 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onPowerOffCheckedChange(p0, p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.device_settings_uart_get_btn, "method 'onViewClicked'");
    view7f070064 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.device_settings_uart_set_btn, "method 'onViewClicked'");
    view7f070067 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.device_settings_powerOffAfterReset_set_btn, "method 'onViewClicked'");
    view7f070061 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    DeviceSettingsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mDeviceSettingsUartStatusTv = null;

    ((CompoundButton) view7f070069).setOnCheckedChangeListener(null);
    view7f070069 = null;
    ((CompoundButton) view7f070062).setOnCheckedChangeListener(null);
    view7f070062 = null;
    view7f070064.setOnClickListener(null);
    view7f070064 = null;
    view7f070067.setOnClickListener(null);
    view7f070067 = null;
    view7f070061.setOnClickListener(null);
    view7f070061 = null;
  }
}
