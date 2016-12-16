package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import android.app.Fragment;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.OrientationChanged;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ViewRequestsRefresh;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.ShareFragment;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments.ShareFragmentSwipe;

/**
 * Created by cpiechnick on 19/09/16.
 */
public class ShareNavigationItem implements INavigationItem {

    private boolean orientationAvailable = false;

    public ShareNavigationItem() {
        EventManager.getInstance().registerReceiver(OrientationChanged.class, new IEventReceiver() {
            @Override
            public void handleEvent(Event e) {
                if (e instanceof OrientationChanged) {
                    OrientationChanged oc = (OrientationChanged) e;

                    boolean oldAvailable = orientationAvailable;
                    orientationAvailable = oc.getData().isAvailable();

                    if (oldAvailable != orientationAvailable)
                        EventManager.getInstance().sendEvent(new ViewRequestsRefresh(ShareNavigationItem.this));
                }
            }
        });
    }

    @Override
    public String getName() {
        return "Teilen";
    }

    @Override
    public Integer getIconResourceId() {
        return R.drawable.share;
    }

    @Override
    public Fragment getViewFragment() {
        return orientationAvailable ? new ShareFragment() : new ShareFragmentSwipe();
    }
}
