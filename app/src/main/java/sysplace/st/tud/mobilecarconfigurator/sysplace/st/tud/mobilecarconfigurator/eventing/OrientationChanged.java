package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.OrientationData;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Rims;

/**
 * Created by cpiechnick on 19/09/16.
 */
public class OrientationChanged extends Event {

    public static final String Name = "RimsChanged";
    private OrientationData data;

    public OrientationChanged(Object sender, OrientationData data) {
        super(sender, Name);

        this.data = data;
    }

    public OrientationData getData() {
        return data;
    }
}
