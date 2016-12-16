package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing;

/**
 * Created by cpiechnick on 25/07/16.
 */
public class Event {

    private long timestamp;
    private Object sender;
    private String name;

    public Event(Object sender, String name){
        timestamp = System.currentTimeMillis();
        this.sender = sender;
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getSender() {
        return sender;
    }

    public String getName() {
        return name;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public void setName(String name) {
        this.name = name;
    }
}
