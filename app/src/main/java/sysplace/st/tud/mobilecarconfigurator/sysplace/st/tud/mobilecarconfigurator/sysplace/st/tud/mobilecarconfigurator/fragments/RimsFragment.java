package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Rims;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Tinge;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.RimsChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.TingeChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class RimsFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rim, container, false);

        v.findViewById(R.id.rims_fragment_rim_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRimClicked(Rims.RIM_01);
            }
        });

        v.findViewById(R.id.rims_fragment_rim_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRimClicked(Rims.RIM_02);
            }
        });

        v.findViewById(R.id.rims_fragment_rim_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRimClicked(Rims.RIM_03);
            }
        });

        v.findViewById(R.id.rims_fragment_rim_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRimClicked(Rims.RIM_04);
            }
        });

        ((Button) v.findViewById(R.id.rims_fragment_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(RimsFragment.this));
            }
        });

        ((Button) v.findViewById(R.id.rims_fragment_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(RimsFragment.this));
            }
        });

        EventManager.getInstance().registerReceiver(RimsChangedEvent.class, this);
        updateSelected(v);

        return v;

    }

    private void handleRimClicked(Rims rim) {
        CarManager.getInstance().getCar().setRims(rim);
    }

    @Override
    public void handleEvent(Event e) {
        if(e instanceof RimsChangedEvent){
            updateSelected(getView());
        }
    }

    private void updateSelected(View v){
        int id = -1;

        switch (CarManager.getInstance().getCar().getRims()){
            case RIM_01:
                id = R.id.rims_fragment_rim_01;
                break;
            case RIM_02:
                id = R.id.rims_fragment_rim_02;
                break;
            case RIM_03:
                id = R.id.rims_fragment_rim_03;
                break;
            case RIM_04:
                id = R.id.rims_fragment_rim_04;
                break;
        }

        if(id != -1){
            View selectedColor = v.findViewById(id);
            LinearLayout colorContainer = (LinearLayout)v.findViewById(R.id.rims_fragment_color_container);

            for(int i = 0; i < colorContainer.getChildCount(); i++){
                colorContainer.getChildAt(i).setPadding(0,0,0,0);
            }

            selectedColor.setPadding(2,2,2,2);

        }
    }
}