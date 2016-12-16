package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data;

import android.view.OrientationEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpiechnick on 07/09/16.
 */
public class PeerDevice {

    private PeerDeviceOrientation orientation;
    private List<OrientationChangedObserver> observer;

    public PeerDevice(PeerDeviceOrientation orientation) {
        this.orientation = orientation;
        observer = new ArrayList<>();
    }

    public PeerDeviceOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(PeerDeviceOrientation orientation) {
        PeerDeviceOrientation oldOrientation = this.orientation;
        this.orientation = orientation;

        if(oldOrientation != this.orientation)
            for(OrientationChangedObserver observer : this.observer)
                observer.orientationChanged(this);
    }

    public void registerOrientationObserver(OrientationChangedObserver observer) {
        if (!this.observer.contains(observer))
            this.observer.add(observer);
    }

    public void unregisterOrientationObserver(OrientationChangedObserver observer){
        this.observer.remove(observer);
    }

    public interface OrientationChangedObserver {
        void orientationChanged(PeerDevice sender);
    }
}
