package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments;

import android.app.Fragment;
import android.content.ClipData;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.PeerDeviceManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.PeerDevice;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.PeerDeviceOrientation;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.OrientationChanged;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class ShareFragment extends Fragment implements IEventReceiver, PeerDeviceManager.PeerDeviceObserver, PeerDevice.OrientationChangedObserver {

    private List<PeerDevice> trackedDevices;
    private View profileImageContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share, container, false);
        updateImage(v);

        EventManager.getInstance().registerReceiver(ColorChangedEvent.class, this);

        ((Button) v.findViewById(R.id.share_fragment_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(ShareFragment.this));
            }
        });

        ((Button) v.findViewById(R.id.share_fragment_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(ShareFragment.this));
            }
        });

        ImageView iv = (ImageView) v.findViewById(R.id.share_fragment_car_image);
        iv.setOnTouchListener(new MyTouchListener());

        final ImageView circleIv = (ImageView) v.findViewById(R.id.share_fragment_profile_active_indicator);
        final ImageView completedIv = (ImageView) v.findViewById(R.id.share_fragment_profile_image_completed);

        ImageView piv = (ImageView) v.findViewById(R.id.share_fragment_profile_image);

        piv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        circleIv.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        circleIv.setVisibility(View.GONE);
                        break;
                    case DragEvent.ACTION_DROP:
                        circleIv.setVisibility(View.GONE);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        completedIv.setVisibility(View.VISIBLE);
                                    }
                                });

                                try {
                                    Thread.sleep(1500);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        completedIv.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }).start();

                        break;
                }

                return true;
            }
        });

        profileImageContainer = v.findViewById(R.id.share_fragment_profile_image_container);

        trackedDevices = new ArrayList<>();
        for (PeerDevice pd : PeerDeviceManager.getInstance().getDevices())
            addDevice(pd);

        return v;
    }

    private void addDevice(PeerDevice pd) {
        this.trackedDevices.add(pd);
        pd.registerOrientationObserver(this);
        updateOrientation(pd);
    }

    @Override
    public void onResume() {
        super.onResume();

        PeerDeviceManager.getInstance().registerPeerDeviceObserver(this);
        EventManager.getInstance().registerReceiver(OrientationChanged.class, this);
    }

    @Override
    public void onPause() {
        super.onPause();

        PeerDeviceManager.getInstance().unregisterPeerDeviceObserver(this);
        EventManager.getInstance().unregisterReceiver(OrientationChanged.class, this);
    }

    @Override
    public void handleEvent(Event e) {
        if (e instanceof ColorChangedEvent)
            updateImage(getView());

        if(e instanceof OrientationChanged){
            final OrientationChanged oc = (OrientationChanged)e;
            if(oc.getData().isAvailable() && oc.getData().getOrientation().length() > 0){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profileImageContainer.setVisibility(View.VISIBLE);
                        PeerDevice pd = new PeerDevice(decode(oc.getData().getOrientation()));
                        updateOrientation(pd);
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profileImageContainer.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    private PeerDeviceOrientation decode(String orientation){
        switch (orientation){
            case "N":
                return PeerDeviceOrientation.North;
            case "NE":
                return PeerDeviceOrientation.NorthEast;
            case "E":
                return PeerDeviceOrientation.East;
            case "SE":
                return PeerDeviceOrientation.SouthEast;
            case "S":
                return PeerDeviceOrientation.South;
            case "SW":
                return PeerDeviceOrientation.Southwest;
            case "W":
                return PeerDeviceOrientation.West;
            case "NW":
                return PeerDeviceOrientation.Northwest;
            default:
                return PeerDeviceOrientation.North;
        }
    }

    private void updateImage(View v) {
        ImageView carImage = (ImageView) v.findViewById(R.id.share_fragment_car_image);
        carImage.setImageResource(CarManager.getInstance().getImageResource());
    }

    @Override
    public void newDeviceDetected(PeerDevice device) {
        addDevice(device);
    }

    @Override
    public void deviceLost(PeerDevice device) {
        device.unregisterOrientationObserver(this);
    }

    @Override
    public void orientationChanged(PeerDevice sender) {
        updateOrientation(sender);
    }

    private void updateOrientation(PeerDevice sender) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) profileImageContainer.getLayoutParams();
        for(int i = 0; i < params.getRules().length; i++)
            params.removeRule(i);

        switch (sender.getOrientation()) {
            case North:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case Northwest:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case West:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
            case Southwest:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case South:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case SouthEast:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case East:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
            case NorthEast:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
        }

        profileImageContainer.setLayoutParams(params);
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

}
