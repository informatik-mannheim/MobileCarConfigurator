package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data;

/**
 * Created by cpiechnick on 19/09/16.
 */
public class OrientationData {

    private boolean available;
    private String orientation;

    public OrientationData(boolean available, String orientation) {
        this.available = available;
        this.orientation = orientation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
