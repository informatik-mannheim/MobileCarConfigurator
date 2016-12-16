package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

import android.support.v4.os.IResultReceiver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cpiechnick on 25/07/16.
 */
public class EventManager {

    private HashMap<Class, Set<IEventReceiver>> receiver;
    private static EventManager instance;

    private EventManager(){
        this.receiver = new HashMap<>();
    }

    public static EventManager getInstance(){
        if(instance == null)
            instance = new EventManager();

        return instance;
    }

    public void registerReceiver(Class eventType, IEventReceiver receiver){
        if(this.receiver.containsKey(eventType))
            this.receiver.get(eventType).add(receiver);
        else{
            Set<IEventReceiver> newSet = new HashSet<IEventReceiver>();
            newSet.add(receiver);
            this.receiver.put(eventType, newSet);
        }
    }

    public void unregisterReceiver(Class eventType, IEventReceiver receiver){
        if(this.receiver.containsKey(eventType))
            this.receiver.get(eventType).remove(receiver);
    }

    public void unregisterReceiver(IEventReceiver receiver){
        for(Class eventType : this.receiver.keySet())
            this.receiver.get(eventType).remove(receiver);
    }

    public void sendEvent(Event e){
        for(Class eventType : this.receiver.keySet()){
            if(e.getClass().isAssignableFrom(eventType)){
                for(IEventReceiver receiver : this.receiver.get(eventType))
                    receiver.handleEvent(e);
            }
        }
    }
}
