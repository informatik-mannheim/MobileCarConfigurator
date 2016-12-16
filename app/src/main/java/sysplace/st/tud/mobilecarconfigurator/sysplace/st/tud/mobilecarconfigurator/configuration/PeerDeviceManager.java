package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration;

import java.util.ArrayList;
import java.util.List;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.PeerDevice;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.PeerDeviceOrientation;

/**
 * Created by cpiechnick on 07/09/16.
 */
public class PeerDeviceManager {

    private static PeerDeviceManager instance;
    private List<PeerDevice> devices;
    private List<PeerDeviceObserver> observer;

    private PeerDeviceManager() {
        observer = new ArrayList<>();
        devices = new ArrayList<>();
        devices.add(new PeerDevice(PeerDeviceOrientation.West));
    }

    public static PeerDeviceManager getInstance() {
        if (instance == null)
            instance = new PeerDeviceManager();

        return instance;
    }

    public List<PeerDevice> getDevices() {
        return devices;
    }

    public void registerPeerDeviceObserver(PeerDeviceObserver observer) {
        if (!this.observer.contains(observer))
            this.observer.add(observer);
    }

    public void unregisterPeerDeviceObserver(PeerDeviceObserver observer) {
        this.observer.remove(observer);
    }

    public interface PeerDeviceObserver {
        void newDeviceDetected(PeerDevice device);

        void deviceLost(PeerDevice device);
    }

}
