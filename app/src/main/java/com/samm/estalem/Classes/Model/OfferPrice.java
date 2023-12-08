package com.samm.estalem.Classes.Model;

public class OfferPrice {
    public double offer;
    public int orderId ;
    public String  providerPhone ;
    public OfferPrice(double offer, int orderId, String providerPhone) {
        this.offer = offer;
        this.orderId = orderId;
        this.providerPhone = providerPhone;
    }
}
