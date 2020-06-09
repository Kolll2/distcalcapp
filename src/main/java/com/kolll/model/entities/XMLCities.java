package com.kolll.model.entities;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="cities")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLCities {

    @XmlElement(name = "city")
    private List<City> cities = null;

    @XmlElement(name = "distance")
    private List<Distance> distances = null;

    public List<City> getCities() {
        return cities;
    }

    public List<Distance> getDistances() {
        return distances;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void setDistances(List<Distance> distances) {
        this.distances = distances;
    }
}
