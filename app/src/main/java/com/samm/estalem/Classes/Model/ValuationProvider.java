package com.samm.estalem.Classes.Model;

public class ValuationProvider {

    public int id;
    public double value ;
    public String note;
    public int orderId ;
    public String providerPhone;

    public ValuationProvider(double value, String note, int orderId,  String providerPhone) {
        this.value = value;
        this.note = note;
        this.orderId = orderId;
        this.providerPhone = providerPhone;
    }
}
