package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration.CarManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.NavigationSystem;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Rims;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.NavigationSystemChanged;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.RimsChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class NavigationSystemFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation_system, container, false);

        v.findViewById(R.id.navi_fragment_navi_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNaviClicked(NavigationSystem.SMALL);
            }
        });

        v.findViewById(R.id.navi_fragment_navi_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNaviClicked(NavigationSystem.MEDIUM);
            }
        });

        v.findViewById(R.id.navi_fragment_navi_large).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNaviClicked(NavigationSystem.LARGE);
            }
        });

        ((Button) v.findViewById(R.id.navi_fragment_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(NavigationSystemFragment.this));
            }
        });

        ((Button) v.findViewById(R.id.navi_fragment_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(NavigationSystemFragment.this));
            }
        });

        EventManager.getInstance().registerReceiver(NavigationSystemChanged.class, this);
        updateSelected(v);

        return v;

    }

    private void handleNaviClicked(NavigationSystem system) {
        CarManager.getInstance().getCar().setNavigationSystem(system);
    }

    @Override
    public void handleEvent(Event e) {
        if(e instanceof NavigationSystemChanged){
            updateSelected(getView());
        }
    }

    private void updateSelected(View v){
        int id = -1;

        switch (CarManager.getInstance().getCar().getNavigationSystem()){
            case SMALL:
                id = R.id.navi_fragment_navi_small;
                break;
            case MEDIUM:
                id = R.id.navi_fragment_navi_medium;
                break;
            case LARGE:
                id = R.id.navi_fragment_navi_large;
                break;
        }

        if(id != -1){
            View selectedColor = v.findViewById(id);
            LinearLayout colorContainer = (LinearLayout)v.findViewById(R.id.navi_fragment_color_container);

            for(int i = 0; i < colorContainer.getChildCount(); i++){
                colorContainer.getChildAt(i).setPadding(0,0,0,0);
            }

            selectedColor.setPadding(2,2,2,2);

        }
    }
}
