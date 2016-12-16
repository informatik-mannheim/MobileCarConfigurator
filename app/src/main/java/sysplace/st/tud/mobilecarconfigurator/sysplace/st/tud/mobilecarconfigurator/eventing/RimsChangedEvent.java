package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Rims;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class RimsChangedEvent extends Event {

    public static final String Name = "RimsChanged";
    private Rims rims;

    public RimsChangedEvent(Object sender, Rims rims) {
        super(sender, Name);

        this.rims = rims;
    }

    public Rims getRims() {
        return rims;
    }
}
