package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.NavigationSystem;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class NavigationSystemChanged extends Event {

    public static final String Name = "NavigationSystemChanged";
    private NavigationSystem navigationSystem;

    public NavigationSystemChanged(Object sender, NavigationSystem navigationSystem){
        super(sender, Name);

        this.navigationSystem = navigationSystem;
    }

    public NavigationSystem getNavigationSystem() {
        return navigationSystem;
    }
}
