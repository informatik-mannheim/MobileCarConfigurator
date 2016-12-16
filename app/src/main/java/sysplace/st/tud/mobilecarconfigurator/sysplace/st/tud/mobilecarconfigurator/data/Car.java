package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data;

import android.graphics.Color;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.CushionChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.NavigationSystemChanged;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.RimsChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.TingeChangedEvent;

/**
 * Created by cpiechnick on 25/07/16.
 */
public class Car {

    private CarColor color;
    private Tinge tinge;
    private Rims rims;
    private Cushion cushion;
    private NavigationSystem navigationSystem;

    public Car() {
        color = CarColor.Green;
        tinge = Tinge.NO_TINGE;
        rims = Rims.RIM_01;
        cushion = Cushion.WHITE;
        navigationSystem = NavigationSystem.SMALL;
    }

    public CarColor getColor() {
        return color;
    }

    public void setColor(CarColor color) {
        CarColor oldColor = this.color;
        this.color = color;

        if (oldColor != color)
            EventManager.getInstance().sendEvent(new ColorChangedEvent(this, color));
    }

    public Tinge getTinge() {
        return tinge;
    }

    public void setTinge(Tinge tinge) {
        this.tinge = tinge;

        EventManager.getInstance().sendEvent(new TingeChangedEvent(this, tinge));
    }

    public Rims getRims() {
        return rims;
    }

    public void setRims(Rims rims) {
        this.rims = rims;

        EventManager.getInstance().sendEvent(new RimsChangedEvent(this, rims));
    }

    public Cushion getCushion() {
        return cushion;
    }

    public void setCushion(Cushion cushion) {
        this.cushion = cushion;

        EventManager.getInstance().sendEvent(new CushionChangedEvent(this, cushion));
    }

    public NavigationSystem getNavigationSystem() {
        return navigationSystem;
    }

    public void setNavigationSystem(NavigationSystem navigationSystem) {
        this.navigationSystem = navigationSystem;

        EventManager.getInstance().sendEvent(new NavigationSystemChanged(this, navigationSystem));
    }
}

