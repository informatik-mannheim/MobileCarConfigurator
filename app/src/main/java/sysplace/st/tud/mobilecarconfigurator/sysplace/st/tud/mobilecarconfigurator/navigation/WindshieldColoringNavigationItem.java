package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;
import android.view.View;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.WindshieldFragment;

/**
 * Created by cpiechnick on 23/05/16.
 */
public class WindshieldColoringNavigationItem implements INavigationItem {
    @Override
    public String getName() {
        return "Scheibentönung";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.windshield;
    }

    @Override
    public Fragment getViewFragment() {
        return new WindshieldFragment();
    }
}
