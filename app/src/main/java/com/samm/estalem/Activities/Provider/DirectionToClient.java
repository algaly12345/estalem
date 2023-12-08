package com.samm.estalem.Activities.Provider;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.samm.estalem.Chat.ChatActivity;
import com.samm.estalem.Classes.ViewModel.TrackingOrderDataViewModel;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.ApiClient;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PicassoClient;
import com.samm.estalem.Util.ReceiveOrderService;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;
import com.samm.estalem.Util.directionhelpers.FetchURL;
import com.samm.estalem.Util.directionhelpers.TaskLoadedCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionToClient extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double x = 0, y = 0;
    int orderId;
    Dialog dialog;
    TrackingOrderDataViewModel trackingOrderDataViewModel = new TrackingOrderDataViewModel();

    TextView tvClientName;
    TextView tvClientAddress;
    Button btnOfferPrice;
    Button btnChat;

    ImageView imgClientImage;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_direction_to_client);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tracking_map);
        mapFragment.getMapAsync(this);
        dialog = ShowDialog.progres(this);
        orderId = getIntent().getExtras().getInt("orderId");

        tvClientName=(TextView)findViewById(R.id.tvClientName);
        tvClientAddress=(TextView)findViewById(R.id.tvClientAddress);

        btnOfferPrice = (Button) findViewById(R.id.btnOfferPrice);
        btnChat = (Button) findViewById(R.id.btnChat);

        imgClientImage = (ImageView) findViewById(R.id.imgClientImage);


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DirectionToClient.this, ChatActivity.class);
                intent.putExtra("phone",trackingOrderDataViewModel.clientPhone);
                startActivity(intent);
            }
        });
//        stopService(new Intent(this, ReceiveOrderService.class));
//        startService(new Intent(this, ReceiveOrderService.class));
//
//        stopService(new Intent(this, GotoLiveMyLocation.class));
//        startService(new Intent(this, GotoLiveMyLocation.class));
        getOrderData(SharedpreferencesData.getValuePreferences(DirectionToClient.this,"providerPhone",""),orderId);
        /*******new Maps*****************/
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    //.enableAutoManage(this, this)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        googleApiClient.connect();
//*********************end new maps*************/

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
//        stopService(new Intent(this, ReceiveOrderService.class));
//        stopService(new Intent(this, GotoLiveMyLocation.class));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        /*******كود تحديد الموقع********/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (userCurrentLocation != null) {
                MarkerOptions currentUserLocation = new MarkerOptions();
                LatLng currentUserLatLang = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                x = userCurrentLocation.getLatitude();
                y = userCurrentLocation.getLongitude();
                currentUserLocation.position(currentUserLatLang);
                //     mMap.addMarker(new MarkerOptions().position(currentUserLatLang).title("موقعك الحلي").icon(BitmapDescriptorFactory.fromResource(R.drawable.abot_as)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 15));
            }
        }
        /*****end**كود تحديد الموقع********/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private void getOrderData(String providerPhone,int orderId) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetDataDirectionToClient(providerPhone, orderId).enqueue(new Callback<TrackingOrderDataViewModel>() {
            @Override
            public void onResponse(Call<TrackingOrderDataViewModel> call, Response<TrackingOrderDataViewModel> response) {
                mMap.clear();
                trackingOrderDataViewModel = response.body();
                tvClientName.setText("" + trackingOrderDataViewModel.clientName);
                tvClientAddress.setText(""+trackingOrderDataViewModel.clientAddress);
                btnOfferPrice.setText("" + trackingOrderDataViewModel.offerPrice);
                PicassoClient.downloadImage(getApplication(), ApiClient.BASE_URL2 + trackingOrderDataViewModel.clientImage, imgClientImage);


                place1 = new MarkerOptions().position(new LatLng(trackingOrderDataViewModel.userLat, trackingOrderDataViewModel.userLong)).title(getResources().getString(R.string.client_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_locatio_pin));
                place2 = new MarkerOptions().position(new LatLng(trackingOrderDataViewModel.orderLat, trackingOrderDataViewModel.orderLong)).title(getResources().getString(R.string.order_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.order_location_pin));
                new FetchURL(DirectionToClient.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                mMap.addMarker(place1);
                mMap.addMarker(place2);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trackingOrderDataViewModel.userLat, trackingOrderDataViewModel.userLong), 15));




                dialog.dismiss();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TrackingOrderDataViewModel> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), DirectionToClient.this);
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();


        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setColor(Color.parseColor("#ffb148"));

    }

}
