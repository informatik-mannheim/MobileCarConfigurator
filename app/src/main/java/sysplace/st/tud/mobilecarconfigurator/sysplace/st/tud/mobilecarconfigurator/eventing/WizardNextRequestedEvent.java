package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

/**
 * Created by cpiechnick on 25/07/16.
 */
public class WizardNextRequestedEvent extends Event {

    public static final String Name = "WizardNext";

    public WizardNextRequestedEvent(Object sender){
        super(sender, Name);
    }
}
