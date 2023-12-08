package com.samm.estalem.Classes;

import com.google.android.gms.maps.model.LatLng;

public class ApiPlaceModel {
    public String placeName;
    public String placeId;
    public LatLng latLng;

    public ApiPlaceModel(String placeName, String placeId, LatLng latLng) {
        this.placeName = placeName;
        this.placeId = placeId;
        this.latLng = latLng;
    }

}
