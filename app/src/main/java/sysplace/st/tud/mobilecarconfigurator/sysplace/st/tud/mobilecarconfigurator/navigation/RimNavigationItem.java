package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.RimsFragment;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class RimNavigationItem implements INavigationItem {
    @Override
    public String getName() {
        return "Felgen";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.wheel;
    }

    @Override
    public Fragment getViewFragment() {
        return new RimsFragment();
    }
}
