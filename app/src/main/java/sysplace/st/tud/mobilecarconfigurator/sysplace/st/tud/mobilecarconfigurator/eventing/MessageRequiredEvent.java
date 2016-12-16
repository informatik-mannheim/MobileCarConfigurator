package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Cushion;

/**
 * Created by cpiechnick on 18/09/16.
 */
public class MessageRequiredEvent extends Event {

    public static final String Name = "MessageRequired";
    private String message;

    public MessageRequiredEvent(Object sender, String message){
        super(sender, Name);

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
