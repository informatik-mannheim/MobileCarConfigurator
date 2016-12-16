package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.NavigationSystemFragment;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class NavigationSystemNavigationItem implements INavigationItem {
    @Override
    public String getName() {
        return "Navigationssystem";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.navi;
    }

    @Override
    public Fragment getViewFragment() {
        return new NavigationSystemFragment();
    }
}
