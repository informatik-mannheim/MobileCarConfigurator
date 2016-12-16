package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Cushion;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class CushionChangedEvent extends Event {

    public static final String Name = "Cushion";
    private Cushion cushion;

    public CushionChangedEvent(Object sender, Cushion cushion){
        super(sender, Name);

        this.cushion = cushion;
    }

    public Cushion getCushion() {
        return cushion;
    }
}
