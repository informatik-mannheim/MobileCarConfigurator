package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.communication;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.Normalizer;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.MessageRequiredEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.StringStore;

/**
 * Created by cpiechnick on 18/09/16.
 */
public class ColorSyncher implements IEventReceiver {

    private boolean running;

    private static ColorSyncher instance;

    private ColorSyncher() {
        EventManager.getInstance().registerReceiver(ColorChangedEvent.class, this);
    }

    public static ColorSyncher getInstance() {
        if (instance == null)
            instance = new ColorSyncher();

        return instance;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }


    @Override
    public void handleEvent(Event e) {
        if (running && e instanceof ColorChangedEvent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringStore.getInstance().write("cas-config", getConfigJson());

                }
            }).start();
        }

        EventManager.getInstance().sendEvent(new MessageRequiredEvent(this, "Configuration Update send to Cave"));
    }

    private String getConfigJson() {
        try {
            return "{\"product\":{\"attributeGroups\":[{\"name\":\"Exterior\",\"attributes\":[{\"name\":\"Farbe\",\"selectedValues\":[\"" + (CarManager.getInstance().getCar().getColor() == CarColor.Green ? URLEncoder.encode("Gr\u00fcn", "UTF-8") : "Blau") + "\"]},{\"name\":\"Scheibent\u00f6nung\",\"selectedValues\":[]},{\"name\":\"Felgen\",\"selectedValues\":[]}]},{\"name\":\"Interior\",\"attributes\":[{\"name\":\"Polster\",\"selectedValues\":[]},{\"name\":\"Navigation\",\"selectedValues\":[]}]}]},\"timestamp\":146278164764.9251}";
        } catch (Exception e) {
            return null;
        }
    }
}
