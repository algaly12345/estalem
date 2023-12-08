package com.samm.estalem.Util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samm.estalem.Classes.ViewModel.TrackingViewModel;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GotoLiveMyLocation extends Service implements LocationListener {
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = INTERVAL / 2;
    public static double lat=0,log=0;
    boolean isRunning = false;
    LocationRequest mLocationRequest;

    public static GoogleMap googleMap;
    //  private ProviderService providerService = new ProviderService();
    public  GoogleApiClient mGoogleApiClient;



    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public  void stopLocationUpdates(){
        if(mGoogleApiClient !=null){
            if (mGoogleApiClient.isConnected()){
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
                Log.d("tag", "......................");
                //   Toast.makeText(this, " .", Toast.LENGTH_LONG).show();
                Log.d("tag", "Location update stopped......................");

            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates(){
        if (mGoogleApiClient!=null){
            //     Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            Log.d("tag", "api not equals null......................");
            if (mGoogleApiClient.isConnected()){
                //    startForeground(1111, showNotfication());
                //  Toast.makeText(this, " api connected  .", Toast.LENGTH_LONG).show();
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient, mLocationRequest, this);
                }catch (Exception e){
                    Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                }
                Log.d("tag", " Location update started .............. .: ");
                //      Toast.makeText(this, " ", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        //  Toast.makeText(this, "", Toast.LENGTH_LONG).show();

        Log.d("tag", "OnCommand......................");
        if (intent != null){
            //     Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            Log.d("tag", "Intent in on command not equals null......................");

            //   if (intent.getAction().equals(ServiceAction.STARTFOREGROUND_ACTION)) {
            if (isAppHasPermission()){
                //    Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                Log.d("tag", "Apps have permission......................");

                if (!isRunning) {
                    //  Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                    Log.d("tag", "Servicess start......................");

                    Log.d("serviceLog", "service started ");
                    buildGoogleApiClient();
                    if (mGoogleApiClient!=null){
                        //  Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                        Log.d("tag", "apiclint not equals null......................");

                        mGoogleApiClient.connect();
                    }
                    isRunning = true;
                } else {
                    Log.d("serviceLog", "");
                    //      Toast.makeText(this, " ", Toast.LENGTH_LONG).show();
                    Log.d("tag", "service is alreay started......................");

                }
            } else {
                StopServices();
                //   Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                Log.d("tag", "Services Stop Because No Permission......................");

            }

        /*    } else if (intent.getAction().equals(ServiceAction.STOPFOREGROUND_ACTION)) {
                Log.d("serviceLog", "service stoped ");
                stopLocationUpdates();
                StopServices();
            }*/
        }else {
            StopServices();
            //        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            Log.d("tag", "Services Stop Because Intend Equals Null......................");

        }
        return START_STICKY;
    }

    private void StopServices(){
        stopForeground(true);
        stopSelf();
        isRunning = false;
        if (mGoogleApiClient !=null){
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        //   Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        Log.d("tag", "on create......................");

    }



    protected synchronized void buildGoogleApiClient(){
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d("googleApi", "connected");
                        // Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                        Log.d("tag", "Google Api Connected......................");

                        startLocationUpdates();
                    }

                    @Override
                    public void onConnectionSuspended(int i){
                        //   Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                        Log.d("tag", "onConnectionSuspended......................");

                    }
                })
//                .addOnConnectionFailedListener(connectionResult -> {
//                    Log.d("googleApi", "not connected");
//                })
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LocServiceState ", "");
        //    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        Log.d("tag", "Location service stoped......................");

    }

    private boolean isGpsEnabled(){
        LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    private boolean isAppHasPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }



    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(Location location){
        if (isGpsEnabled()){
            try {
                ReceiveOrderService.SendLocation(new TrackingViewModel(SharedpreferencesData.getValuePreferences(GotoLiveMyLocation.this,"providerPhone",""),location.getLatitude(),location.getLongitude()));
            }catch (Exception e){}
        } else {
            Log.d("Add LocationState", "");
            Toast.makeText(this, "GPS or Network Is off", Toast.LENGTH_SHORT).show();
        }
    }

}
