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
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Tinge;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.TingeChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 31/05/16.
 */
public class WindshieldFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_windshield, container, false);

        v.findViewById(R.id.windshield_fragment_no_tinge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTingeClicked(Tinge.NO_TINGE);
            }
        });

        v.findViewById(R.id.windshield_fragment_tinge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTingeClicked(Tinge.TINGE);
            }
        });

        ((Button)v.findViewById(R.id.windshield_fragment_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(WindshieldFragment.this));
            }
        });

        ((Button)v.findViewById(R.id.windshield_fragment_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(WindshieldFragment.this));
            }
        });

        EventManager.getInstance().registerReceiver(TingeChangedEvent.class, this);
        updateImage(v);
        updateSelected(v);

        return v;

    }

    private void handleTingeClicked(Tinge tinge){
        CarManager.getInstance().getCar().setTinge(tinge);
    }

    @Override
    public void handleEvent(Event e) {
        if(e instanceof TingeChangedEvent){
            updateImage(getView());
            updateSelected(getView());
        }
    }

    private void updateImage(View v){
        ImageView carImage = (ImageView)v.findViewById(R.id.windshield_fragment_car_image);
        carImage.setImageResource(CarManager.getInstance().getImageResource());
    }

    private void updateSelected(View v){
        int id = -1;

        switch (CarManager.getInstance().getCar().getTinge()){
            case TINGE:
                id = R.id.windshield_fragment_tinge;
                break;
            case NO_TINGE:
                id = R.id.windshield_fragment_no_tinge;
                break;
        }

        if(id != -1){
            View selectedColor = v.findViewById(id);
            LinearLayout colorContainer = (LinearLayout)v.findViewById(R.id.windshield_fragment_color_container);

            for(int i = 0; i < colorContainer.getChildCount(); i++){
                colorContainer.getChildAt(i).setPadding(0,0,0,0);
            }

            selectedColor.setPadding(2,2,2,2);

        }
    }
}

