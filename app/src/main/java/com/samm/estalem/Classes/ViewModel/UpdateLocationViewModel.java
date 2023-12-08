package com.samm.estalem.Classes.ViewModel;

public class UpdateLocationViewModel {
    public String providerPhone ;
    public double lat ;
    public double log ;

    public UpdateLocationViewModel(String providerPhone, double lat, double log) {
        this.providerPhone = providerPhone;
        this.lat = lat;
        this.log = log;
    }
}
