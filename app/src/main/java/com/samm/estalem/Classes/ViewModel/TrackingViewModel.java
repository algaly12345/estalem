package com.samm.estalem.Classes.ViewModel;

public class TrackingViewModel {
    public String ProviderPhone ;
    public double Lat ;
    public double Log ;

    public TrackingViewModel(String providerPhone, double lat, double log) {
        ProviderPhone = providerPhone;
        Lat = lat;
        Log = log;
    }
}
