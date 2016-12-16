package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class WizardPreviousRequestedEvent extends Event {

    private static final String Name = "PreviousWizardStep";

    public WizardPreviousRequestedEvent(Object sender){
        super(sender, Name);
    }
}
