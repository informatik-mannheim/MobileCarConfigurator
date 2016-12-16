package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;

/**
 * Created by cpiechnick on 17/05/16.
 */
public class ColorFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_color, container, false);

        /*v.findViewById(R.id.color_fragment_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClicked(CarColor.Black);
            }
        });*/

        v.findViewById(R.id.color_fragment_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClicked(CarColor.Blue);
            }
        });

        v.findViewById(R.id.color_fragment_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClicked(CarColor.Green);
            }
        });

        /*v.findViewById(R.id.color_fragment_gray).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClicked(CarColor.Gray);
            }
        });

        v.findViewById(R.id.color_fragment_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClicked(CarColor.Red);
            }
        });

        v.findViewById(R.id.color_fragment_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClicked(CarColor.White);
            }
        });*/

        ((Button)v.findViewById(R.id.color_fragment_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(ColorFragment.this));
            }
        });

        EventManager.getInstance().registerReceiver(ColorChangedEvent.class, this);
        updateImage(v);
        updateSelected(v);

        return v;
    }

    private void handleColorClicked(CarColor color){
        CarManager.getInstance().getCar().setColor(color);
    }

    @Override
    public void handleEvent(Event e) {
        if(e instanceof ColorChangedEvent){
            updateImage(getView());
            updateSelected(getView());
        }
    }

    private void updateImage(View v){
        ImageView carImage = (ImageView)v.findViewById(R.id.color_fragment_car_image);
        carImage.setImageResource(CarManager.getInstance().getImageResource());
    }

    private void updateSelected(View v){
        int id = -1;

        switch (CarManager.getInstance().getCar().getColor()){
            /*case Black:
                id = R.id.color_fragment_black;
                break;
            */case Blue:
                id = R.id.color_fragment_blue;
                break;
            case Green:
                id = R.id.color_fragment_green;
                break;
           /*case Gray:
                id = R.id.color_fragment_gray;
                break;
            case Red:
                id = R.id.color_fragment_red;
                break;
            case White:
                id = R.id.color_fragment_white;
                break;*/
        }

        if(id != -1){
            View selectedColor = v.findViewById(id);
            LinearLayout colorContainer = (LinearLayout)v.findViewById(R.id.color_fragment_color_container);

            for(int i = 0; i < colorContainer.getChildCount(); i++){
                colorContainer.getChildAt(i).setPadding(0,0,0,0);
            }

            selectedColor.setPadding(2,2,2,2);

        }
    }
}
