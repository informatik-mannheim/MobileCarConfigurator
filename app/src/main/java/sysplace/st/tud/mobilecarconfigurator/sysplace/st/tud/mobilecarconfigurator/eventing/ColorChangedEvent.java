package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;

/**
 * Created by cpiechnick on 25/07/16.
 */
public class ColorChangedEvent extends Event {

    private CarColor color;
    public static final String ColorChangedEventName = "ColorChanged";

    public ColorChangedEvent(Object sender, CarColor color){
        super(sender, ColorChangedEventName);

        this.color = color;
    }

    public CarColor getColor() {
        return color;
    }
}
