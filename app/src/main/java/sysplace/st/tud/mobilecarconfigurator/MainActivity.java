package sysplace.st.tud.mobilecarconfigurator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.communication.ColorSyncher;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.communication.OrientationSensor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CasCarConfig;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ExchangeReceivedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.MessageRequiredEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.OrientationChanged;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.PersonalProfile;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.ProximityDetector;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.StringStore;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.KVEServer;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.SwipeDetector;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation.INavigationItem;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation.NavigationGroup;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation.NavigationProvider;

public class MainActivity extends AppCompatActivity implements NavigationProvider.IActiveItemChanged, ProximityDetector.ProximityListener, SwipeDetector.SwipeListener {

    private LinearLayout contentContainer;
    private Map<INavigationItem, TextView> views;

    private StringStore mStringStore;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 118;
    private KVEServer mKVEServer;
    private boolean mInsideCave;
    private ProximityDetector mProximityDetector;
    private static final String TAG = "[CaveActivity]";
    private String defaultIp = "37.61.204.167";
    private int defaultPort = 8080;
    private SwipeDetector swipeDetector;
    private final boolean DEBUG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        views = new HashMap<>();

        setContentView(R.layout.activity_main);
        contentContainer = (LinearLayout) findViewById(R.id.main_content_container);

        checkPermissions();
        checkGPSStatus();

        PersonalProfile.getInstance().loadFromPreferences(this);

        mStringStore = StringStore.getInstance();
        mKVEServer = new KVEServer();

        EventManager.getInstance().registerReceiver(ColorChangedEvent.class, new IEventReceiver() {
            @Override
            public void handleEvent(Event e) {
                if (e instanceof ColorChangedEvent) {
                    final ColorChangedEvent che = (ColorChangedEvent) e;

                    final String config = "{\"product\":{\"attributeGroups\":[{\"name\":\"Exterior\",\"attributes\":[{\"name\":\"Farbe\",\"selectedValues\":[\"" + (che.getColor() == CarColor.Green ? "Grün" : "Blau") + "\"]},{\"name\":\"Scheibentönung\",\"selectedValues\":[]},{\"name\":\"Felgen\",\"selectedValues\":[]}]},{\"name\":\"Interior\",\"attributes\":[{\"name\":\"Polster\",\"selectedValues\":[]},{\"name\":\"Navigation\",\"selectedValues\":[]}]}]},\"timestamp\":146278164764.9251}";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mStringStore.write("cas-config", config);
                            mStringStore.write("config-cas", config);

                            if (mInsideCave){
                                mStringStore.write("personal_profile", PersonalProfile.getInstance().toJSON(getColor()));
                                mKVEServer.send(true, getColor());
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,
                                            String.format("Profil gepeichert \n\n(%s).\n\nCave betreten.",
                                                    che.getColor()),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        swipeDetector = SwipeDetector.getInstance();
        swipeDetector.initialize(mStringStore);
        swipeDetector.registerObserver(this);

        float threshold = Float.parseFloat(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("proximity_threshold", "1.5"));

        Log.d(TAG, String.format(Locale.GERMANY, "Threshold is %.2f", threshold));

        mProximityDetector = new ProximityDetector(this, threshold);
        mProximityDetector.registerObserver(this);

        EventManager.getInstance().registerReceiver(MessageRequiredEvent.class, new IEventReceiver() {
            @Override
            public void handleEvent(Event e) {
                if (e instanceof MessageRequiredEvent)
                    Toast.makeText(MainActivity.this, ((MessageRequiredEvent) e).getMessage(), Toast.LENGTH_LONG);
            }
        });

        EventManager.getInstance().registerReceiver(OrientationChanged.class, new IEventReceiver() {
            @Override
            public void handleEvent(Event e) {
                if (e instanceof OrientationChanged) {
                    OrientationChanged oc = (OrientationChanged) e;
                }
            }
        });


        generateLayout();

        /**
         * needed for the KVE protocol
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    mKVEServer.send(mInsideCave, getColor());
                    try{
                        Thread.sleep(500);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        mInsideCave = false;
    }

    private void checkGPSStatus() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.d("e", "Exception GPS");
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.d("e", "Exception Network");
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("GPS not enabled");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void generateLayout() {
        generateNavigation();
    }

    private void generateNavigation() {
        LinearLayout navigationContainer = (LinearLayout) findViewById(R.id.main_navigation_container);
        navigationContainer.removeAllViews();

        List<NavigationGroup> groups = NavigationProvider.getInstance().getGroups();
        for (NavigationGroup group : groups)
            navigationContainer.addView(createViewForNavigationGroup(group));

        NavigationProvider.getInstance().addIActiveItemChangedObserver(this);
        NavigationProvider.getInstance().initializeActiveItem();
    }

    private View createViewForNavigationGroup(NavigationGroup group) {
        LinearLayout groupLayout = new LinearLayout(this);
        groupLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        groupLayout.setPadding(20, 20, 20, 20);
        groupLayout.setLayoutParams(params);
        groupLayout.setElevation(5f);
        groupLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 0, 0, 10);
        tv.setLayoutParams(textParams);
        tv.setTextSize(16f);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(ContextCompat.getColor(this, R.color.textColor));
        tv.setText(group.getName());

        groupLayout.addView(tv);

        for (INavigationItem item : group.getItems())
            groupLayout.addView(createViewForNavigationItem(item));

        return groupLayout;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mProximityDetector.startScanning();
        swipeDetector.start();
        OrientationSensor.getInstance().start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mProximityDetector.stopScanning();
        swipeDetector.stop();
        OrientationSensor.getInstance().stop();
    }

    @Override
    public void onEntry() {
        if (!mInsideCave) {
            mInsideCave = true;
            notifyCaveServerEntry();
            Toast.makeText(this, "Cave betreten", Toast.LENGTH_SHORT).show();
            ColorSyncher.getInstance().start();
            sendProfileToStringStore();
        }
    }

    @Override
    public void onExit() {
        if (mInsideCave) {
            mInsideCave = false;
            notifyCaveServerExit();
            Toast.makeText(this, "Cave verlassen", Toast.LENGTH_SHORT).show();

            ColorSyncher.getInstance().stop();

            removeProfileFromStringStore();
        }
    }

    private View createViewForNavigationItem(INavigationItem item) {
        LinearLayout itemsContainer = new LinearLayout(this);
        itemsContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        itemsContainer.setLayoutParams(params);

        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(40, 40);
        ivParams.setMargins(0, 0, 20, 0);
        iv.setLayoutParams(ivParams);
        iv.setImageResource(item.getIconResourceId());

        itemsContainer.addView(iv);

        TextView tv = new TextView(this);
        tv.setText(item.getName());
        tv.setTextSize(14f);
        tv.setTextColor(ContextCompat.getColor(this, R.color.textColor));
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.gravity = Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(tvParams);

        itemsContainer.addView(tv);

        views.put(item, tv);

        return itemsContainer;
    }

    private String getColor() {
        return CarManager.getInstance().getCar().getColor() == CarColor.Blue ? "blue" : "green";
    }

    private void notifyCaveServerEntry() {
        mKVEServer.send(true, getColor());
    }

    private void notifyCaveServerExit() {
        mKVEServer.send(false, getColor());
    }

    private void sendProfileToStringStore() {
        final PersonalProfile profile = PersonalProfile.getInstance();

        Toast.makeText(this,
                String.format("Profil gepeichert \n\n(%s).\n\n",
                        profile.toJSON(getColor())),
                Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mStringStore.write("personal_profile", profile.toJSON(getColor()));
            }
        }).start();
    }

    private void removeProfileFromStringStore() {
        Toast.makeText(this, "Profil entfernt. \n\n", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mStringStore.write("personal_profile", "[]");
            }
        }).start();
    }

    @Override
    public void notifyActiveItemChanged(final INavigationItem item) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    contentContainer.removeAllViews();

                    Fragment fragment = item.getViewFragment();

                    // Update the layout
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    // The id specified here identifies which ViewGroup to
                    // append the Fragment to.
                    ft.add(R.id.main_content_container, fragment);
                    ft.commit();

                    for (INavigationItem navItem : views.keySet()) {
                        TextView tv = views.get(navItem);
                        if (item == navItem) {
                            tv.setTypeface(null, Typeface.BOLD);
                            tv.setTextColor(getResources().getColor(R.color.colorAccent, null));
                        } else {
                            tv.setTypeface(null, Typeface.NORMAL);
                            tv.setTextColor(Color.BLACK);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSwipeDetected(String result) {

        Gson gson = new Gson();
        try {
            CasCarConfig config = gson.fromJson(result, CasCarConfig.class);

            CarColor newColor = config.getCarColor();

            if (newColor != CarColor.Unknown) {
                CarManager.getInstance().getCar().setColor(newColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventManager.getInstance().sendEvent(new ExchangeReceivedEvent(this));


    }
}
