package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;
import android.view.View;

/**
 * Created by cpiechnick on 17/05/16.
 */
public interface INavigationItem {

    public String getName();
    public Integer getIconResourceId();
    public Fragment getViewFragment();

}
