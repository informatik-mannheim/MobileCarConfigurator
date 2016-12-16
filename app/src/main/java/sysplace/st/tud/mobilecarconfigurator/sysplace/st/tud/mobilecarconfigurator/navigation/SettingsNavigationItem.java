package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.SettingsFragment;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.ShareFragmentSwipe;

/**
 * Created by cpiechnick on 17/09/16.
 */
public class SettingsNavigationItem implements INavigationItem {
    @Override
    public String getName() {
        return "Einstellungen";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.settings;
    }

    @Override
    public Fragment getViewFragment() {
        return new SettingsFragment();
    }
}
