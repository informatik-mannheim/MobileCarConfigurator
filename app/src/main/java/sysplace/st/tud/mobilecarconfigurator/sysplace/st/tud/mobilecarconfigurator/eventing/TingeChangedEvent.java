package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Tinge;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class TingeChangedEvent extends Event {

    public static final String Name = "TingeChanged";
    private Tinge tinge;

    public TingeChangedEvent(Object sender, Tinge tinge) {
        super(sender,Name);
        this.tinge = tinge;
    }

    public Tinge getTinge() {
        return tinge;
    }
}
