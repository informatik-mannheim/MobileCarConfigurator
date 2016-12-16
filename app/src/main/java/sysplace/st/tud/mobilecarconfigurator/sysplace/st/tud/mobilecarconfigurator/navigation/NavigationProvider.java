package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.navigation;

import java.util.ArrayList;
import java.util.List;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.Event;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.EventManager;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.IEventReceiver;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.ViewRequestsRefresh;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardNextRequestedEvent;
import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.eventing.WizardPreviousRequestedEvent;

/**
 * Created by cpiechnick on 17/05/16.
 */
public class NavigationProvider implements IEventReceiver {

    private static NavigationProvider instance;
    private List<NavigationGroup> groups;
    private List<IActiveItemChanged> itemObserver;
    private INavigationItem currentItem;

    private NavigationProvider() {
        initialize();
    }

    public static NavigationProvider getInstance() {
        if (instance == null)
            instance = new NavigationProvider();

        return instance;
    }

    public void initialize() {
        groups = new ArrayList<>();
        itemObserver = new ArrayList<>();

        NavigationGroup extGroup = new NavigationGroup("Exterior");
        extGroup.getItems().add(new ColorNavigationItem());
        extGroup.getItems().add(new WindshieldColoringNavigationItem());
        extGroup.getItems().add(new RimNavigationItem());

        NavigationGroup intGroup = new NavigationGroup("Interior");
        intGroup.getItems().add(new CushionNavigationItem());
        intGroup.getItems().add(new NavigationSystemNavigationItem());

        NavigationGroup summaryGroup = new NavigationGroup("Weiteres");
        summaryGroup.getItems().add(new ShareNavigationItem());
        summaryGroup.getItems().add(new SettingsNavigationItem());

        groups.add(extGroup);
        groups.add(intGroup);
        groups.add(summaryGroup);

        EventManager.getInstance().registerReceiver(WizardNextRequestedEvent.class, this);
        EventManager.getInstance().registerReceiver(WizardPreviousRequestedEvent.class, this);
        EventManager.getInstance().registerReceiver(ViewRequestsRefresh.class, this);
    }

    public void addIActiveItemChangedObserver(IActiveItemChanged observer) {
        if (!itemObserver.contains(observer))
            itemObserver.add(observer);
    }

    public void removeIActiveItemChangedObserver(IActiveItemChanged observer) {
        if (itemObserver.contains(observer))
            itemObserver.remove(observer);
    }

    public List<NavigationGroup> getGroups() {
        return groups;
    }

    private void notifyCurrentChanged() {
        for (IActiveItemChanged observer : itemObserver)
            observer.notifyActiveItemChanged(currentItem);
    }

    public void initializeActiveItem() {
        currentItem = groups.get(0).getItems().get(0);
        notifyCurrentChanged();

    }

    @Override
    public void handleEvent(Event e) {
        if (e instanceof WizardNextRequestedEvent) {
            for (NavigationGroup g : groups) {
                for (INavigationItem item : g.getItems()) {
                    if (item == currentItem) {
                        int itemIndex = g.getItems().indexOf(item);
                        if (itemIndex < g.getItems().size() - 1) {
                            currentItem = g.getItems().get(itemIndex + 1);
                        } else {
                            int groupIndex = groups.indexOf(g);
                            if(groupIndex < groups.size() - 1)
                                currentItem = groups.get(groupIndex + 1).getItems().get(0);
                        }

                        notifyCurrentChanged();

                        return;
                    }
                }
            }
        } else if(e instanceof WizardPreviousRequestedEvent){
            for (NavigationGroup g : groups) {
                for (INavigationItem item : g.getItems()) {
                    if (item == currentItem) {
                        int itemIndex = g.getItems().indexOf(item);
                        if (itemIndex > 0) {
                            currentItem = g.getItems().get(itemIndex - 1);
                        } else {
                            int groupIndex = groups.indexOf(g);
                            if(groupIndex > 0) {
                                NavigationGroup newGroup = groups.get(groupIndex - 1);
                                currentItem = newGroup.getItems().get(newGroup.getItems().size() - 1);
                            }
                        }

                        notifyCurrentChanged();

                        return;
                    }
                }
            }
        } else if(e instanceof ViewRequestsRefresh){
            if(e.getSender() == currentItem){
                    notifyCurrentChanged();
            }
        }
    }

    public interface IActiveItemChanged {
        void notifyActiveItemChanged(INavigationItem item);
    }
}
