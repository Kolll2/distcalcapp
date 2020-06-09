package com.kolll.model.entities;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="distance")
@XmlType(propOrder = {"fromCity", "toCity", "distance"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Distance {

    @XmlElement(name = "from")
    private Long fromCity;
    @XmlElement(name = "to")
    private Long toCity;
    @XmlElement(name="distance")
    private int distance;

    public Distance() {
    }

    public Distance(Long fromCity, Long toCity, int distance) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    public Long getFromCity() {
        return fromCity;
    }

    public Long getToCity() {
        return toCity;
    }

    public int getDistance() {
        return distance;
    }
}
