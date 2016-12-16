package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.communication;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.OrientationData;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.OrientationChanged;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.StringStore;

/**
 * Created by cpiechnick on 19/09/16.
 */
public class OrientationSensor {

    private static OrientationSensor instance;
    private Thread observerThread;
    private Boolean running = false;
    private OrientationData orientationData;

    private OrientationSensor(){

    }

    public static OrientationSensor getInstance(){
        if(instance == null)
            instance = new OrientationSensor();

        return instance;
    }

    public void start(){
        running = true;
        observerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                    String data = StringStore.getInstance().read("orientation");

                    if(data != null && data.length() > 0){
                        Gson gson = new Gson();
                        OrientationData newData = gson.fromJson(data, OrientationData.class);
                        updateOrientationData(newData);
                    }
                    else{
                        updateOrientationData(new OrientationData(false, ""));
                    }

                    StringStore.getInstance().write("orientation","");

                    try{
                        Thread.sleep(500);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        observerThread.start();
    }

    private void updateOrientationData(OrientationData newData){
        OrientationData oldData = orientationData;
        orientationData = newData;

        if(oldData == null || (oldData.isAvailable() != newData.isAvailable() || oldData.getOrientation() != newData.getOrientation()))
            EventManager.getInstance().sendEvent(new OrientationChanged(this, orientationData));
    }

    public void stop(){
        running = false;
        observerThread = null;
    }
}
