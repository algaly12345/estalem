package com.samm.estalem.Activities.Provider;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.samm.estalem.Classes.Model.OfferPrice;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.ProviderRecieveOrderDetilesViewModel;
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

public class ReceiveProviderOrderActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {
    int orderId;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double x = 0, y = 0;
    Dialog dialog;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    TextView tvLocationOrderDescription;
    ImageView imgClientImage;
    Button btnDistance;
    Button btnReject;
    Button btnSendPrice;
    Button btnProviderDistance;
    EditText txtPrice;
    ProviderRecieveOrderDetilesViewModel providerRecieveOrderDetilesViewModel = new ProviderRecieveOrderDetilesViewModel();
    MediaPlayer mMediaPlayer;
    @Override
    public void onBackPressed() {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_resive_provider_order);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map0);
        mapFragment.getMapAsync(this);
        openSound();
        stopSound();
        imgClientImage = (ImageView) findViewById(R.id.imgClientImage);
        tvLocationOrderDescription = (TextView) findViewById(R.id.tvLocationOrderDescription);
        btnDistance = (Button) findViewById(R.id.btnDistance);
        btnProviderDistance = (Button) findViewById(R.id.btnProviderDistance);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnSendPrice = (Button) findViewById(R.id.btnSendPrice);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        orderId = getIntent().getExtras().getInt("orderId");
        dialog = ShowDialog.progres(this);
        dialog.show();

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectOrder();
                mMediaPlayer.stop();
            }
        });

        btnSendPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPrice.getText().toString().isEmpty()) {
                    txtPrice.setError(getResources().getString(R.string.empty));
                    return;
                }
                double price =Double.parseDouble(SharedpreferencesData.getValuePreferences(ReceiveProviderOrderActivity.this,"counterPrice",""))+(Double.parseDouble(SharedpreferencesData.getValuePreferences(ReceiveProviderOrderActivity.this,"kmPrice",""))*providerRecieveOrderDetilesViewModel.distanceOrderLocationFromClientLocation);
                if (Double.parseDouble(txtPrice.getText().toString()) > price) {
                    txtPrice.setError(getResources().getString(R.string.highest_value) + ":" + Math.round(price)+getResources().getString(R.string.currency));
                    return;
                }
                mMediaPlayer.stop();
                addOfferPrice(new OfferPrice(Double.parseDouble(txtPrice.getText().toString()), orderId, SharedpreferencesData.getValuePreferences(ReceiveProviderOrderActivity.this, "providerPhone", "")));
            }
        });


        /*******new Maps*****************/
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
//*********************end new maps*************/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        // Add a marker in Sydney and move the camera

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
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
                orderDetails(orderId);


//                mMap.addMarker(new MarkerOptions().position(currentUserLatLang).title("موقعك الحلي").icon(BitmapDescriptorFactory.fromResource(R.drawable.order_location_pin)));
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
        Toast.makeText(ReceiveProviderOrderActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(ReceiveProviderOrderActivity.this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    public void rejectOrder() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ProviderRejectOrder(SharedpreferencesData.getValuePreferences(getApplicationContext(), "providerPhone", "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                finish();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ReceiveProviderOrderActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void orderDetails(int orderId) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ProviderGetOrderDetails(orderId).enqueue(new Callback<ProviderRecieveOrderDetilesViewModel>() {
            @Override
            public void onResponse(Call<ProviderRecieveOrderDetilesViewModel> call, Response<ProviderRecieveOrderDetilesViewModel> response) {
                response(response.body());
            }

            @Override
            public void onFailure(Call<ProviderRecieveOrderDetilesViewModel> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ReceiveProviderOrderActivity.this);
            }
        });
    }

    private void response(ProviderRecieveOrderDetilesViewModel response) {
        mMap.clear();
        providerRecieveOrderDetilesViewModel = response;
        place1 = new MarkerOptions().position(new LatLng(providerRecieveOrderDetilesViewModel.userLat, providerRecieveOrderDetilesViewModel.userLong)).title(getResources().getString(R.string.client_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_locatio_pin));
        place2 = new MarkerOptions().position(new LatLng(providerRecieveOrderDetilesViewModel.orderLat, providerRecieveOrderDetilesViewModel.orderLong)).title(getResources().getString(R.string.order_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.order_location_pin));
        new FetchURL(ReceiveProviderOrderActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(providerRecieveOrderDetilesViewModel.userLat, providerRecieveOrderDetilesViewModel.userLong), 15));
        PicassoClient.downloadImage(getApplicationContext(), ApiClient.BASE_URL2 + providerRecieveOrderDetilesViewModel.clientImage, imgClientImage);
        tvLocationOrderDescription.setText(providerRecieveOrderDetilesViewModel.descriptionLocation + "-" + providerRecieveOrderDetilesViewModel.descriptionOrder);
        btnProviderDistance.setText(getResources().getString(R.string.distance_from_order)+  " : " + getResources().getString(R.string.km) + Math.round(GetOrderDistance(x, y, providerRecieveOrderDetilesViewModel.orderLat, providerRecieveOrderDetilesViewModel.orderLong)));
        btnDistance.setText( getResources().getString(R.string.order_distance_from_order)+ " : " +getResources().getString(R.string.km) + Math.round(providerRecieveOrderDetilesViewModel.distanceOrderLocationFromClientLocation) );
        dialog.dismiss();
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

    private double GetOrderDistance(double currentLat, double currentLong, double otherLat, double otherLong) {
        try {
            double d = Math.acos(
                    Math.sin(Math.PI * currentLat / 180.0) *
                            Math.sin(Math.PI * otherLat / 180.0) +
                            Math.cos(Math.PI * currentLat / 180.0) *
                                    Math.cos(Math.PI * otherLat / 180.0) *
                                    Math.cos(Math.PI * otherLong / 180.0 -
                                            Math.PI * currentLong / 180.0)) * 6371;
            if (d < 0) {
                return 0;
            } else {
                return d;
            }

        } catch (Exception e) {
            return 0;
        }

    }

    private void addOfferPrice(OfferPrice offerPrice) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.AddOfferPrice(offerPrice).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body().getMessage().equals("1")) {
                    Toast.makeText(ReceiveProviderOrderActivity.this, getResources().getString(R.string.price_sent), Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ReceiveProviderOrderActivity.this);
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
    public void stopSound(){
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                try {
                    new CountDownTimer(60*1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            try {
                                mMediaPlayer.stop();
                                finish();
                            }catch (Exception e){}
                        }
                    }.start();
                }catch (Exception e){}
            }
        }.start();
    }
public void openSound(){
     mMediaPlayer = MediaPlayer.create(this, R.raw.you_know_its_morning_again);
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mMediaPlayer.setLooping(true);
    mMediaPlayer.start();
}
}
