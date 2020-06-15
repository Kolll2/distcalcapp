package com.kolll.model.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cities")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLCitiesString {

    @XmlElement(name = "city")
    private List<City> cities = null;

    @XmlElement(name = "distance")
    private List<DistanceString> distances = null;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<DistanceString> getDistances() {
        return distances;
    }

    public void setDistances(List<DistanceString> distances) {
        this.distances = distances;
    }
}
