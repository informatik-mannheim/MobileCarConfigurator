package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cpiechnick on 18/09/16.
 */
public class ServerData {

    private static ServerData instance;
    private String ip;
    private Integer port;
    private static String DefaultIP = "37.61.204.167";

    public static ServerData getInstance(){
        if(instance == null)
            instance = new ServerData();

        return instance;
    }

    public void save(Activity context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("ip", ip);
        prefs.edit().putInt("port", port);
        boolean success = prefs.edit().commit();
        prefs.edit().apply();
    }

    public void initialize(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void loadFromPreferences(Activity context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            ip = sharedPrefs.getString("ip", null);
            port = Integer.parseInt(sharedPrefs.getString("port", "-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(ip == null)
            ip = DefaultIP;

        if(port <= 0)
            port = 8080;
    }

    private ServerData(){
        ip = DefaultIP;
        port = 8080;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
