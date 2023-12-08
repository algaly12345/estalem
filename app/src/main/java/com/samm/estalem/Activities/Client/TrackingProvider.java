package com.samm.estalem.Activities.Client;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.samm.estalem.Chat.ChatActivity;
import com.samm.estalem.Classes.ViewModel.TrackingOrderDataViewModel;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.ApiClient;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PicassoClient;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingProvider extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static HubConnection hubConnection;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double x = 0, y = 0;
    int orderId;
    String providerPhone;
    Dialog dialog;
    TrackingOrderDataViewModel trackingOrderDataViewModel = new TrackingOrderDataViewModel();

    TextView tvProviderName;
    TextView tvProviderCarName;

    Button btnOfferPrice;
    Button btnChat;

    ImageView imgProviderImage;
    RatingBar MyRating;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hubConnection.stop();
        startActivity(new Intent(TrackingProvider.this, ClientMainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_tracking_provider);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tracking_map);
        mapFragment.getMapAsync(this);
        dialog = ShowDialog.progres(this);
        orderId=getIntent().getExtras().getInt("orderId");
        providerPhone = getIntent().getExtras().getString("providerPhone");
//        hubConnection.stop();
        try {
            hubConnection = HubConnectionBuilder.create("http://astalem.com:5000/orderHubs").build();//http://192.168.43.174:80
            hubConnection.start();
            if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
                getLocation();
            } else {
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
                            getLocation();
                        }
                    }
                }.start();
            }
        }catch (Exception e){}

        tvProviderName = (TextView) findViewById(R.id.tvProviderName);
        tvProviderCarName = (TextView) findViewById(R.id.tvProviderCarName);

        btnOfferPrice = (Button) findViewById(R.id.btnOfferPrice);
        btnChat = (Button) findViewById(R.id.btnChat);

        imgProviderImage = (ImageView) findViewById(R.id.imgProviderImage);
        MyRating = (RatingBar) findViewById(R.id.MyRating);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TrackingProvider.this, ChatActivity.class);
                intent.putExtra("phone",providerPhone);
                startActivity(intent);
            }
        });
        getOrderData();
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
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();

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

    private void getOrderData() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetTrackingOrderData(providerPhone, orderId).enqueue(new Callback<TrackingOrderDataViewModel>() {
            @Override
            public void onResponse(Call<TrackingOrderDataViewModel> call, Response<TrackingOrderDataViewModel> response) {
                trackingOrderDataViewModel = response.body();
                tvProviderName.setText("" + trackingOrderDataViewModel.providerName);
                tvProviderCarName.setText(trackingOrderDataViewModel.providerCarName + " - " + trackingOrderDataViewModel.providerCarPlateNumber);
                btnOfferPrice.setText("" + trackingOrderDataViewModel.offerPrice);
                MyRating.setRating((float) trackingOrderDataViewModel.providerValuation);
                PicassoClient.downloadImage(getApplication(), ApiClient.BASE_URL2+trackingOrderDataViewModel.providerImage,imgProviderImage);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TrackingOrderDataViewModel> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), TrackingProvider.this);
                dialog.dismiss();
            }
        });
    }

    public void getProviderLocation(double lat, double log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();
                LatLng currentUserLatLang = new LatLng(lat, log);
                mMap.addMarker(new MarkerOptions().position(currentUserLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.small_car)));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 15));
            }
        });


    }

    public void getLocation() {
        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED)
        hubConnection.on("ReceiveLocation", (Lat, Long, ProviderPhone) -> {
            if (providerPhone.equals(ProviderPhone)) {
                getProviderLocation(Lat, Long);
            }
        }, Double.class, Double.class, String.class);
    }
}
