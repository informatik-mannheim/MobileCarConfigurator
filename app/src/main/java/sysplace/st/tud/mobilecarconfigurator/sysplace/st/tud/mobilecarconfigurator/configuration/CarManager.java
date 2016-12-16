package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.configuration;

import android.content.res.Resources;

import sysplace.st.tud.mobilecarconfigurator.R;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.Car;

/**
 * Created by cpiechnick on 25/07/16.
 */
public class CarManager {

    private Car car;
    private static CarManager instance;

    private CarManager(){
        car = new Car();
    }

    public static CarManager getInstance(){
        if(instance == null)
            instance = new CarManager();

        return instance;
    }

    public Car getCar() {
        return car;
    }

    public int getImageResource(){
        switch (car.getColor()){
            case Black:
                return R.drawable.black_car;
            case Blue:
                return R.drawable.blue_car;
            case Gray:
                return R.drawable.gray_car;
            case Red:
                return R.drawable.red_car;
            case White:
                return R.drawable.white_car;
            case Green:
                return R.drawable.green_car;
        }

        return -1;
    }

    public int getInteriorImage() {
        switch(car.getCushion()){
            case BROWN:
                return R.drawable.brown_cushion;
            case WHITE:
                return R.drawable.white_cushion;
        }

        return -1;
    }
}
