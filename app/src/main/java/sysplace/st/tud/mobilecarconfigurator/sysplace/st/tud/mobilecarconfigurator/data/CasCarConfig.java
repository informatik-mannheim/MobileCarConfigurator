package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data;

import java.util.List;

/**
 * Created by cpiechnick on 17/09/16.
 */
public class CasCarConfig {

    private CasProduct product;

    public CarColor getCarColor(){
        for(CasAttributeGroup ag : product.getAttributeGroups()){
            if(ag.getName().toUpperCase().equals("EXTERIOR")){
                for(CasAttribute at : ag.getAttributes()){
                    if(at.getName().toUpperCase().equals("FARBE") && at.getSelectedValues().size() > 0){
                        switch (at.getSelectedValues().get(0).toUpperCase()){
                            case "GRÜN":
                                return CarColor.Green;
                            default:
                                return CarColor.Blue;
                        }
                    }
                }
            }
        }

        return CarColor.Unknown;
    }

    public class CasProduct {
        private List<CasAttributeGroup> attributeGroups;


        public List<CasAttributeGroup> getAttributeGroups() {
            return attributeGroups;
        }

        public void setAttributeGroups(List<CasAttributeGroup> attributeGroups) {
            this.attributeGroups = attributeGroups;
        }
    }

    public class CasAttributeGroup {
        private String name;
        private List<CasAttribute> attributes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CasAttribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<CasAttribute> attributes) {
            this.attributes = attributes;
        }
    }

    public class CasAttribute {
        private String name;
        private List<String> selectedValues;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getSelectedValues() {
            return selectedValues;
        }

        public void setSelectedValues(List<String> selectedValues) {
            this.selectedValues = selectedValues;
        }
    }

}
