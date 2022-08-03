// Generated code from Butter Knife. Do not modify!
package com.movesense.showcaseapp.section_00_mainView;

import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.movesense.showcaseapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainViewActivity_ViewBinding implements Unbinder {
  private MainViewActivity target;

  private View view7f0700a2;

  private View view7f0700a4;

  private View view7f0700a6;

  @UiThread
  public MainViewActivity_ViewBinding(MainViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainViewActivity_ViewBinding(final MainViewActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.mainView_movesense_Ll, "field 'mMainViewMovesenseLl' and method 'onViewClicked'");
    target.mMainViewMovesenseLl = Utils.castView(view, R.id.mainView_movesense_Ll, "field 'mMainViewMovesenseLl'", RelativeLayout.class);
    view7f0700a2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.mainView_multiConnection_Ll, "field 'mMainViewMultiConnectionLl' and method 'onViewClicked'");
    target.mMainViewMultiConnectionLl = Utils.castView(view, R.id.mainView_multiConnection_Ll, "field 'mMainViewMultiConnectionLl'", RelativeLayout.class);
    view7f0700a4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.mainView_savedData_Ll, "field 'mMainViewSavedDataLl' and method 'onViewClicked'");
    target.mMainViewSavedDataLl = Utils.castView(view, R.id.mainView_savedData_Ll, "field 'mMainViewSavedDataLl'", RelativeLayout.class);
    view7f0700a6 = view;
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
    MainViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mMainViewMovesenseLl = null;
    target.mMainViewMultiConnectionLl = null;
    target.mMainViewSavedDataLl = null;

    view7f0700a2.setOnClickListener(null);
    view7f0700a2 = null;
    view7f0700a4.setOnClickListener(null);
    view7f0700a4 = null;
    view7f0700a6.setOnClickListener(null);
    view7f0700a6 = null;
  }
}
