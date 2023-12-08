package com.samm.estalem.Classes.Model;

public class District {

    private int id;
    private String districtName;
    private String city;
    private double lat;
    private double log;
    private String providers;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public double getLat() {
        return lat;
    }

    public double getLog() {
        return log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
