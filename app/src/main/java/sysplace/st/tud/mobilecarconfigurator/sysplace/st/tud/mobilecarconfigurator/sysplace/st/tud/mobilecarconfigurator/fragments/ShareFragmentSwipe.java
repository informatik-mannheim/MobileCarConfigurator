package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments;

import android.app.Fragment;
import android.content.ClipData;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.communication.ServerCommunicator;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.PeerDeviceManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.PeerDevice;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ExchangeReceivedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class ShareFragmentSwipe extends Fragment implements IEventReceiver, GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;
    private long flingTimestamp = 0;
    private int distanceX = 0;
    private long lastEventTimestamp = 0;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_swipe, container, false);
        updateImage(v);

        EventManager.getInstance().registerReceiver(ColorChangedEvent.class, this);
        EventManager.getInstance().registerReceiver(ExchangeReceivedEvent.class, this);

        image = (ImageView) v.findViewById(R.id.share_fragment_swipe_car_image);

        mDetector = new GestureDetectorCompat(getActivity(), this);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        ((Button) v.findViewById(R.id.share_fragment_swipe_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(ShareFragmentSwipe.this));
            }
        });

        ((Button) v.findViewById(R.id.share_fragment_swipe_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(ShareFragmentSwipe.this));
            }
        });

        switchToReceiveMode();

        return v;
    }

    private void switchToReceiveMode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerCommunicator c = new ServerCommunicator();
                while (true) {
                    final String newColor = c.getNewColor();

                    long timeDiff = System.currentTimeMillis() - flingTimestamp;

                    if (!newColor.equals("n") && (timeDiff > 2000)) {
                        c.clearExchange();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                select(newColor.equals("g") ? CarColor.Green : CarColor.Blue);
                                notifyReceived();
                            }
                        });
                    }

                    try {
                        Thread.sleep(newColor != "n" ? 1500 : 50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private void notifyReceived() {
        Animation animation = new TranslateAnimation(-1280, 0, 0, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        image.startAnimation(animation);
        updateImage(getView());
    }

    private void select(CarColor color) {
        CarManager.getInstance().getCar().setColor(color);
        updateImage(getView());
        new ServerCommunicator().store(color);
    }

    @Override
    public void handleEvent(Event e) {
        if (e instanceof ColorChangedEvent)
            updateImage(getView());

        if(e instanceof ExchangeReceivedEvent)
            notifyReceived();
    }

    private void updateImage(View v) {
        ImageView carImage = (ImageView) v.findViewById(R.id.share_fragment_swipe_car_image);
        carImage.setImageResource(CarManager.getInstance().getImageResource());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (System.currentTimeMillis() - lastEventTimestamp > 100)
            this.distanceX = 0;

        lastEventTimestamp = System.currentTimeMillis();
        int oldDistanceX = this.distanceX;
        this.distanceX -= distanceX;

        if (distanceX == 0) {
            int i = 0;
        }

        Animation a = new TranslateAnimation(oldDistanceX, distanceX, 0, 0);
        a.setDuration(1);
        a.setFillAfter(true);
        a.setFillEnabled(true);
        image.startAnimation(a);

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityX > 500) {
            flingTimestamp = System.currentTimeMillis();
            new ServerCommunicator().initiateExchange(CarManager.getInstance().getCar().getColor());
        }

        return true;
    }

}
