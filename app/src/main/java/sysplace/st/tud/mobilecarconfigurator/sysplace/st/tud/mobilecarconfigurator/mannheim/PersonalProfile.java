package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.mannheim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

public class PersonalProfile {
    private String mFirstName;
    private String mLastName;
    private String mGender;
    private int mAge;

    private static PersonalProfile instance;

    public static PersonalProfile getInstance() {
        if (instance == null)
            instance = new PersonalProfile();

        return instance;
    }

    public void initialize(String firstname, String lastname, String gender, int age) {
        this.mFirstName = firstname;
        this.mLastName = lastname;
        this.mGender = gender;
        this.mAge = age;
    }

    public void save(Activity context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("first_name", mFirstName);
        prefs.edit().putString("last_name", mLastName);
        prefs.edit().putString("gender", mGender);
        prefs.edit().putInt("age", mAge);
        boolean success = prefs.edit().commit();
        prefs.edit().apply();
    }

    public void loadFromPreferences(Activity context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            mFirstName = sharedPrefs.getString("first_name", null);
            mLastName = sharedPrefs.getString("last_name", null);
            mGender = sharedPrefs.getString("gender", "?");
            mAge = Integer.parseInt(sharedPrefs.getString("age", "-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(mFirstName == null)
            mFirstName = "Max";

        if(mLastName == null)
            mLastName = "Mustermann";

        if(mGender == null || mGender.equals("?"))
            mGender = "m";

        if(mAge <= 0)
            mAge = 30;
    }

    private PersonalProfile() {
        mFirstName = "Max";
        mLastName = "Mustermann";
        mGender = "m";
        mAge = 30;
    }

    /**
     * Sends profile data alongside the current configuration (green = 1, blue = 2)
     *
     * @param color
     * @return
     */
    public String toJSON(String color) {
        return String.format(Locale.GERMANY,
                "[{\"first_name\":\"%s\",\"last_name\":\"%s\",\"gender\":\"%s\",\"age\":\"%d\",\"config\":\"%d\"}]",
                getFirstName(), getLastName(), getGender(), getAge(), color.equals("green") ? 1 : 2);
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int mAge) {
        this.mAge = mAge;
    }
}
