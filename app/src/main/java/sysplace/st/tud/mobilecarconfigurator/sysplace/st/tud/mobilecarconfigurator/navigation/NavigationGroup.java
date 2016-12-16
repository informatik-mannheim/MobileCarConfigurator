package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpiechnick on 17/05/16.
 */
public class NavigationGroup {

    private List<INavigationItem> items;
    private String name;

    public NavigationGroup(String name){
        items = new ArrayList<>();
        this.name = name;
    }

    public NavigationGroup(List<INavigationItem> items, String name){
        this.items = items;
        this.name = name;
    }

    public List<INavigationItem> getItems(){
        return this.items;
    }

    public void setItems(List<INavigationItem> items){
        this.items = items;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
