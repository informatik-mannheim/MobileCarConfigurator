package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import sysplace.st.tud.mobilecarconfigurator.MainActivity;
import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.ServerData;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim.PersonalProfile;

/**
 * Created by cpiechnick on 17/09/16.
 */
public class SettingsFragment extends Fragment {

    private TextView firstnameTxt;
    private TextView lastnameTxt;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    private TextView ageTxt;
    private TextView ipTxt;
    private TextView portTxt;
    private TextView thresholdTxt;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        firstnameTxt = (TextView) v.findViewById(R.id.settings_fragment_firstname_txt);
        lastnameTxt = (TextView) v.findViewById(R.id.settings_fragment_lastname_txt);
        ageTxt = (TextView) v.findViewById(R.id.settings_fragment_age_txt);
        ipTxt = (TextView) v.findViewById(R.id.settings_fragment_ip_txt);
        portTxt = (TextView) v.findViewById(R.id.settings_fragment_port_txt);
        thresholdTxt = (TextView) v.findViewById(R.id.settings_fragment_threshold_txt);
        maleRadio = (RadioButton) v.findViewById(R.id.settings_fragment_male_check);
        femaleRadio = (RadioButton) v.findViewById(R.id.settings_fragment_female_check);
        mActivity = (MainActivity) getActivity();

        PersonalProfile p = PersonalProfile.getInstance();

        firstnameTxt.setText(p.getFirstName());
        lastnameTxt.setText(p.getLastName());
        ageTxt.setText(Integer.toString(p.getAge()));
        maleRadio.setChecked(p.getGender().toUpperCase().equals("M"));
        femaleRadio.setChecked(!p.getGender().toUpperCase().equals("M"));
        ipTxt.setText(ServerData.getInstance().getIp());
        portTxt.setText(Integer.toString(ServerData.getInstance().getPort()));
        thresholdTxt.setText(mActivity.getThreshold() + "");

        ((Button) v.findViewById(R.id.settings_fragment_previous_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.getInstance().sendEvent(new WizardPreviousRequestedEvent(SettingsFragment.this));
            }
        });

        ((Button) v.findViewById(R.id.settings_fragment_save_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstnameTxt.getText().toString();
                String lastname = lastnameTxt.getText().toString();
                String gender = maleRadio.isChecked() ? "m" : "f";
                int age = Integer.parseInt(ageTxt.getText().toString());

                PersonalProfile.getInstance().initialize(firstname, lastname, gender, age);
                PersonalProfile.getInstance().save(getActivity());

                String ip = ipTxt.getText().toString();
                int port = Integer.parseInt(portTxt.getText().toString());

                double threshold = Double.parseDouble(thresholdTxt.getText().toString());
                mActivity.setThreshold(threshold);

                ServerData.getInstance().initialize(ip, port);
                ServerData.getInstance().save(mActivity);

                Toast.makeText(getActivity(),"Daten gespeichert", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }
}
