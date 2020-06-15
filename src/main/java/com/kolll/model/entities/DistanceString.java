package com.kolll.model.entities;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "distance")
@XmlType(propOrder = {"fromCity", "toCity", "distance"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DistanceString {

    @XmlElement(name = "from")
    private String fromCity;
    @XmlElement(name = "to")
    private String toCity;
    @XmlElement(name = "distance")
    private int distance;

    public DistanceString() {
    }

    public DistanceString(String fromCity, String toCity, int distance) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "DistanceString{" +
                "fromCity='" + fromCity + '\'' +
                ", toCity='" + toCity + '\'' +
                ", distance=" + distance +
                '}';
    }
}
