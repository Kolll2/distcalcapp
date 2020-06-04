package com.kolll.model.entities;

public class Distance {

    private String fromCity;
    private String toCity;
    private int distance;

    public Distance() {
    }

    public Distance(String fromCity, String toCity, int distance) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public int getDistance() {
        return distance;
    }
}
