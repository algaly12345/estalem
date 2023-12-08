package com.samm.estalem.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.samm.estalem.Adapters.PlaceAdapter;
import com.samm.estalem.Classes.ApiPlaceModel;
import com.samm.estalem.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class ApiPlaces extends Fragment {
    public static PlacesClient placesClient;
    public static Activity context;
    public static List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    static LatLng latLng = new LatLng(0, 0);
    static PlaceAdapter placeAdapter;
    public ApiPlaces(Activity Context) {
        this.context = Context;
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    public List<ApiPlaceModel> getCurrentPlace() {

        List<ApiPlaceModel> apiPlaceModels = new ArrayList<>();
        final FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(fieldList).build();
        @SuppressLint("MissingPermission") Task<FindCurrentPlaceResponse> placeResponseTask = placesClient.findCurrentPlace(request);
        placeResponseTask.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (int i = 0; i < response.getPlaceLikelihoods().size(); i++) {
                        PlaceLikelihood places = response.getPlaceLikelihoods().get(i);
                        apiPlaceModels.add(new ApiPlaceModel(places.getPlace().getName(), places.getPlace().getId(), getLatLog(places.getPlace().getId())));
                    }
                    placeAdapter = new PlaceAdapter(context,apiPlaceModels);
              //     recyclerView.setAdapter(placeAdapter);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return apiPlaceModels;
    }

    public LatLng getLatLog(String placeId) {
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG)).build();
        placesClient.fetchPlace(request)
                .addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                        if (task.isSuccessful()) {
                            Place place = task.getResult().getPlace();
                            latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        }
                        //    return latLng;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return latLng;

    }

    public static void initialLace(Activity context) {
        Places.initialize(context, context.getResources().getString(R.string.google_maps_key));
        placesClient = Places.createClient(context);
    }


    public static void hideSoftKeyboard() {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static String getAddress(Context context, double lat, double lng) {
        try {
            Geocoder geocoder = null;
            try {
                geocoder = new Geocoder(context, Locale.getDefault());
            } catch (Exception e
            ) {
            }


            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj ;
            obj = addresses.get(0);


            try {

            }catch (Exception e)
            {}

            String add = " : ";// obj.getAddressLine(0);
            //  add = add + obj.getCountryName()+"-";
            // add = add + "\n" + obj.getCountryCode();
            //add = add + obj.getAdminArea()+"-";
            // add = add + "\n" + obj.getPostalCode();
            add = add + obj.getSubAdminArea() + "-";
            add = add + obj.getSubLocality() + "-";
            add = add + obj.getThoroughfare() + "";

            //add = add + obj.getLocality()+"-";
            // add = add + obj.getFeatureName()+"-";
            // add = add + obj.getSubThoroughfare();
            String address = add.replace("null", "");
            return "" + address;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }


}