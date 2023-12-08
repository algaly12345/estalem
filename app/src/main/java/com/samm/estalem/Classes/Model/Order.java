package com.samm.estalem.Classes.Model;

public class Order {

    public int id;
    public String dateTimeNow;
    public double amount;
    public String note;
    public String payType;
    public double amountPayed;
    public String statues;
    public String providerPhone;
    public double cobonValue;
    public double userLat;
    public double userLong;
    public double orderLat;
    public double orderLong;
    public String clientPhone;
    public String ProviderPhone;
    public String locationDescription ;
    public String orderDescription ;
    public Client client;
    public Order(String dateTimeNow, double amount, String note, String payType, double amountPayed, String statues, String providerPhone, double cobonValue, double userLat, double userLong, double orderLat, double orderLong, String clientPhone, String locationDescription, String orderDescription) {
        this.dateTimeNow = dateTimeNow;
        this.amount = amount;
        this.note = note;
        this.payType = payType;
        this.amountPayed = amountPayed;
        this.statues = statues;
        this.providerPhone = providerPhone;
        this.cobonValue = cobonValue;
        this.userLat = userLat;
        this.userLong = userLong;
        this.orderLat = orderLat;
        this.orderLong = orderLong;
        this.clientPhone = clientPhone;
        this.locationDescription = locationDescription;
        this.orderDescription = orderDescription;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public int getId() {
        return id;
    }

    public String getDateTimeNow() {
        return dateTimeNow;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getPayType() {
        return payType;
    }

    public double getAmountPayed() {
        return amountPayed;
    }

    public String getStatues() {
        return statues;
    }

    public String getProviderId() {
        return providerPhone;
    }

    public double getCobonValue() {
        return cobonValue;
    }

    public double getUserLat() {
        return userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public double getOrderLat() {
        return orderLat;
    }

    public double getOrderLong() {
        return orderLong;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getProviderPhone() {
        return ProviderPhone;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDateTimeNow(String dateTimeNow) {
        this.dateTimeNow = dateTimeNow;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setAmountPayed(double amountPayed) {
        this.amountPayed = amountPayed;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public void setProviderId(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public void setCobonValue(double cobonValue) {
        this.cobonValue = cobonValue;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public void setOrderLat(double orderLat) {
        this.orderLat = orderLat;
    }

    public void setOrderLong(double orderLong) {
        this.orderLong = orderLong;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setProviderPhone(String providerPhone) {
        ProviderPhone = providerPhone;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }
}
