package com.samm.estalem.Classes.Model;

public class Client {

    public String  phone;
    public String  fullName;
    public String  image ;
    public String  district;
    public String  city;
    public double log;
    public double lat;
    public String token;
    public String connectionId;

    public Client(String phone, String fullName, String image, String district, String city) {
        this.phone = phone;
        this.fullName = fullName;
        this.image = image;
        this.district = district;
        this.city = city;
    }

    public Client(String phone, String fullName, String image, String district, String city, double log, double lat, String token, String connectionId) {
        this.phone = phone;
        this.fullName = fullName;
        this.image = image;
        this.district = district;
        this.city = city;
        this.log = log;
        this.lat = lat;
        this.token = token;
        this.connectionId = connectionId;
    }

    public Client(String phone, String fullName , String district, String city) {
        this.phone = phone;
        this.fullName = fullName;
        this.district = district;
        this.city = city;
    }
}
