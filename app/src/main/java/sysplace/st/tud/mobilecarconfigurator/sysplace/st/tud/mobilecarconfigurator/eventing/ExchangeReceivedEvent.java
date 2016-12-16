package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Rims;

/**
 * Created by cpiechnick on 17/09/16.
 */
public class ExchangeReceivedEvent extends Event {

    public static final String Name = "ExchangedReceived";

    public ExchangeReceivedEvent(Object sender) {
        super(sender, Name);
    }
}
