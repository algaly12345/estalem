package com.samm.estalem.Classes.Model;

public class Provider {

    private String phone;
    private String fullName;
    private String image;
    private String city;
    private String carClass;
    private String plateNumber;
    private String nationalCardImage;
    private String drivinglicenseImage;
    private String formImage;
    private String drivinglicenseAuthoImage;
    private String backCarImage;
    private String frontCarImage;
    private String statues;
    private String connectivity;
    private double log;
    private double lat;
    private String token;
    private String connectionId;
    private String expirationDate;
    private int districtId;
    private String iPanNumber;
    private String bankName;
    private String bankAccountName;

    public Provider(String phone, String fullName, String image, String city, String carClass, String plateNumber, String nationalCardImage, String drivinglicenseImage, String formImage, String drivinglicenseAuthoImage, String backCarImage, String frontCarImage, String statues, String connectivity, double log, double lat, String token, String connectionId, int districtId, String iPanNumber, String bankName, String bankAccountName) {
        this.phone = phone;
        this.fullName = fullName;
        this.image = image;
        this.city = city;
        this.carClass = carClass;
        this.plateNumber = plateNumber;
        this.nationalCardImage = nationalCardImage;
        this.drivinglicenseImage = drivinglicenseImage;
        this.formImage = formImage;
        this.drivinglicenseAuthoImage = drivinglicenseAuthoImage;
        this.backCarImage = backCarImage;
        this.frontCarImage = frontCarImage;
        this.statues = statues;
        this.connectivity = connectivity;
        this.log = log;
        this.lat = lat;
        this.token = token;
        this.connectionId = connectionId;
        this.districtId = districtId;
        this.iPanNumber = iPanNumber;
        this.bankName = bankName;
        this.bankAccountName = bankAccountName;
    }

//    public Provider(String phone, String fullName, String image, String district, String city, double log, double lat, String token) {
//        this.phone = phone;
//        this.fullName = fullName;
//        this.image = image;
//        this.city = city;
//        this.log = log;
//        this.lat = lat;
//        this.token = token;
//    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setiPanNumber(String iPanNumber) {
        this.iPanNumber = iPanNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getiPanNumber() {
        return iPanNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public int getDistrictId() {
        return districtId;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImage() {
        return image;
    }


    public String getCity() {
        return city;
    }

    public String getCarClass() {
        return carClass;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getNationalCardImage() {
        return nationalCardImage;
    }

    public String getDrivinglicenseImage() {
        return drivinglicenseImage;
    }

    public String getFormImage() {
        return formImage;
    }

    public String getDrivinglicenseAuthoImage() {
        return drivinglicenseAuthoImage;
    }

    public String getBackCarImage() {
        return backCarImage;
    }

    public String getFrontCarImage() {
        return frontCarImage;
    }

    public String getStatues() {
        return statues;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public double getLog() {
        return log;
    }

    public double getLat() {
        return lat;
    }

    public String getToken() {
        return token;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setNationalCardImage(String nationalCardImage) {
        this.nationalCardImage = nationalCardImage;
    }

    public void setDrivinglicenseImage(String drivinglicenseImage) {
        this.drivinglicenseImage = drivinglicenseImage;
    }

    public void setFormImage(String formImage) {
        this.formImage = formImage;
    }

    public void setDrivinglicenseAuthoImage(String drivinglicenseAuthoImage) {
        this.drivinglicenseAuthoImage = drivinglicenseAuthoImage;
    }

    public void setBackCarImage(String backCarImage) {
        this.backCarImage = backCarImage;
    }

    public void setFrontCarImage(String frontCarImage) {
        this.frontCarImage = frontCarImage;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }
}
