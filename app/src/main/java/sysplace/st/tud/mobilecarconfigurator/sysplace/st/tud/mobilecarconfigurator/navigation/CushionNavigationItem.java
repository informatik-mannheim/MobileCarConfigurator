package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.CushionFragment;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class CushionNavigationItem implements INavigationItem{
    @Override
    public String getName() {
        return "Polster";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.seat;
    }

    @Override
    public Fragment getViewFragment() {
        return new CushionFragment();
    }
}
