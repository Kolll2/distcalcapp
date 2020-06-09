package com.kolll.model.entities;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "city")
@XmlType(propOrder = {"name", "latitude", "longitude"})
@XmlAccessorType(XmlAccessType.FIELD)
public class City {

    @XmlTransient
    Long id;

    private String name;
    private Float latitude;
    private Float longitude;

    public City() {
    }

    public City(String name, Float latitude, Float longitude) {
        this(-1L, name, latitude, longitude);
    }

    public City(Long id, String name, Float latitude, Float longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (id != null ? !id.equals(city.id) : city.id != null) return false;
        if (name != null ? !name.equals(city.name) : city.name != null) return false;
        if (latitude != null ? !latitude.equals(city.latitude) : city.latitude != null) return false;
        return longitude != null ? longitude.equals(city.longitude) : city.longitude == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
