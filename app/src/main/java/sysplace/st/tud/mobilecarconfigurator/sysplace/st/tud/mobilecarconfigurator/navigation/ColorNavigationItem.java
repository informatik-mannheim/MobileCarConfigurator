package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.ColorFragment;

/**
 * Created by cpiechnick on 17/05/16.
 */
public class ColorNavigationItem implements INavigationItem {
    @Override
    public String getName() {
        return "Farbe";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.color;
    }

    @Override
    public Fragment getViewFragment() {
        return new ColorFragment();
    }
}
