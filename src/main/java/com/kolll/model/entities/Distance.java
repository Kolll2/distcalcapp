package com.kolll.model.entities;

public class Distance {

    private Long fromCity;
    private Long toCity;
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
