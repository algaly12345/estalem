package com.samm.estalem.Faraments.ui.main_provider_fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samm.estalem.Activities.Provider.ProviderMainActivity;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.GotoLiveMyLocation;
import com.samm.estalem.Util.LocationUpdateService;
import com.samm.estalem.Util.NetworkStateChangeReceiver;
import com.samm.estalem.Util.ReceiveOrderService;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.samm.estalem.R.id.map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainProviderFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Switch aSwitch;
    GoogleMap mMap;
    Activity activity;
    SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    double x = 0, y = 0;
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    public MainProviderFragment(Activity Activity) {
        this.activity = Activity;

        /*******new Maps*****************/
        if (googleApiClient == null) {

            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
//                    .enableAutoManage(activity, this)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

        }
        googleApiClient.connect();
//*********************end new maps*************/


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_provider_fragment, container, false);
        ImageView button=(ImageView) v.findViewById(R.id.img_nav_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProviderMainActivity)getActivity()).showNavBar();
            }
        });
        aSwitch = (Switch)v.findViewById(R.id.location_switch);
        isNetworkAvailable();//for check is internet available
        if (SharedpreferencesData.getValuePreferences(getActivity(), "switch", "").equals("1")) {

            if (!Util.isMyServiceRunning(getActivity(), ReceiveOrderService.class)) {
                getActivity().startService(new Intent(getActivity(), ReceiveOrderService.class));
            }

            if (!Util.isMyServiceRunning(getActivity(), GotoLiveMyLocation.class)) {
                getActivity().startService(new Intent(getActivity(), GotoLiveMyLocation.class));
            }

            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    if (isNetworkConnected()) {
                        SharedpreferencesData.setValuePreferences(getActivity(), "switch", "1");
                        if (!Util.isMyServiceRunning(getActivity(), ReceiveOrderService.class)) {
                           getActivity().startService(new Intent(getActivity(), ReceiveOrderService.class));
                        }

                        if (!Util.isMyServiceRunning(getActivity(), GotoLiveMyLocation.class)) {
                           getActivity().startService(new Intent(getActivity(), GotoLiveMyLocation.class));
                        }
                        setActive();
                    } else {
                        aSwitch.setChecked(false);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.openInternet), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //        Toast.makeText(MapsMainActivity.this, "closed", Toast.LENGTH_SHORT).show();

                    SharedpreferencesData.setValuePreferences(getActivity(), "switch", "0");
                    getActivity().stopService(new Intent(getActivity(), LocationUpdateService.class));
                    getActivity().stopService(new Intent(getActivity(), ReceiveOrderService.class));
                    getActivity().stopService(new Intent(getActivity(), GotoLiveMyLocation.class));
                    setNotActive();
                }
            }
        });

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
//
//
//        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        rlp.col
//        rlp.setMargins(0, 180, 180, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        /*******كود تحديد الموقع********/
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
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
        Toast.makeText(activity, "لا يوجد اتصال بالشبكة", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(activity, "لا توجد صلاحية للدخول الي المواقع", Toast.LENGTH_SHORT).show();
        }
    }


    boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void isNetworkAvailable() {
        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                // String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                if (!isNetworkAvailable) {
                    aSwitch.setChecked(false);
                }
            }
        }, intentFilter);
    }

    public void setActive() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.SetActive(SharedpreferencesData.getValuePreferences(getActivity(), "providerPhone", "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getActivity(), getResources().getString(R.string.active), Toast.LENGTH_SHORT).show();
                getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void setNotActive() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.SetNotActive(SharedpreferencesData.getValuePreferences(getActivity(), "providerPhone", "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getActivity(), getResources().getString(R.string.note_active), Toast.LENGTH_SHORT).show();
                rejectOrder();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void rejectOrder() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ProviderRejectOrder(SharedpreferencesData.getValuePreferences(getActivity(), "providerPhone", "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   Toast.makeText(MapsMainActivity.this, "" + response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}