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
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Cushion;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ColorChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.CushionChangedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 26/07/16.
 */
public class CushionFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cushion, container, false);

        v.findViewById(R.id.cushion_fragment_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCushionClicked(Cushion.WHITE);
            }
        });

        v.findViewById(R.id.cushion_fragment_brown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCushionClicked(Cushion.BROWN);
            }
        });

        ((Button) v.findViewById(R.id.cushion_fragment_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardNextRequestedEvent(CushionFragment.this));
            }
        });

        ((Button) v.findViewById(R.id.cushion_fragment_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(CushionFragment.this));
            }
        });

        EventManager.getInstance().registerReceiver(CushionChangedEvent.class, this);
        updateImage(v);
        updateSelected(v);

        return v;
    }

    private void handleCushionClicked(Cushion cushion) {
        CarManager.getInstance().getCar().setCushion(cushion);
    }

    @Override
    public void handleEvent(Event e) {
        if (e instanceof CushionChangedEvent) {
            updateImage(getView());
            updateSelected(getView());
        }
    }

    private void updateImage(View v) {
        ImageView carImage = (ImageView) v.findViewById(R.id.cushion_fragment_interior_image);
        carImage.setImageResource(CarManager.getInstance().getInteriorImage());
    }

    private void updateSelected(View v) {
        int id = -1;

        switch (CarManager.getInstance().getCar().getCushion()) {
            case WHITE:
                id = R.id.cushion_fragment_white;
                break;
            case BROWN:
                id = R.id.cushion_fragment_brown;
                break;
        }

        if (id != -1) {
            View selectedColor = v.findViewById(id);
            LinearLayout colorContainer = (LinearLayout) v.findViewById(R.id.cushion_fragment_color_container);

            for (int i = 0; i < colorContainer.getChildCount(); i++) {
                colorContainer.getChildAt(i).setPadding(0, 0, 0, 0);
            }

            selectedColor.setPadding(2, 2, 2, 2);

        }
    }
}