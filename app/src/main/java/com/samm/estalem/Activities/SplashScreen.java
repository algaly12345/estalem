package com.samm.estalem.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.samm.estalem.Activities.Client.SelectDistrict;
import com.samm.estalem.Activities.Provider.ProviderMainActivity;
import com.samm.estalem.Classes.Model.App;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    boolean testintrenet;
    int WaitInternetCount = 0;
    Thread timer;
    boolean isVersionShoed = true;

    //static String phoneNumber = "0963862878";
    boolean runJustOnce;
    String id = "";

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_splash_screen);

        configure_permittion();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE },
                1);

        if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)

            ActivityCompat.requestPermissions(SplashScreen.this, new String[] {Manifest.permission.CAMERA}, 1);

    }

    boolean isPermitted;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 10 :{
                //boolean openActivityOnce = true;
                boolean openDialogOnce = true;

                for (int i = 0; i < grantResults.length; i++) {
                    String permission = permissions[i];

                    isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // user rejected the permission
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SplashScreen.this, getResources().getString(R.string.location_permission), Toast.LENGTH_LONG).show();
                        } else {
                            if (openDialogOnce) {
                                alertView();
                            }
                        }
                    }
                }

                if (isPermitted) {
                    openGPS();
                    //GPSTracker GbsTracker = new GPSTracker(this);
                }

            }
            default:
                break;
        }
    }

    public void configure_permittion() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , 1);
                userOuthen();
            }
        } else {
            openGPS();
        }
    }

    void alertView() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this, R.style.AppTheme);

        dialog.setTitle(getString(R.string.permission_denied))
                //.setInverseBackgroundForced(true)
                .setIcon(R.drawable.exo_notification_small_icon)
                .setMessage((getString(R.string.giveLocation_permission)));

        dialog.setNegativeButton(getString(R.string.yes_denyLocation), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                dialoginterface.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.tryAgain), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                dialoginterface.dismiss();
                configure_permittion();
            }
        }).show();
    }

    void timerThreat() {

        if (isNetworkConnected()) {
            getApp();
            //userOuthen();
        } else
            alertDialog(getResources().getString(R.string.openInternet), "OK", "exit");

        Thread timerThreat = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThreat.start();
    }


    public void getApp() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.App().enqueue(new Callback<App>() {
            @Override
            public void onResponse(Call<App> call, Response<App> response) {
                App app = response.body();
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "facebook", app.facebook);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "instagram", app.instagram);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "twitter", app.twitter);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "version", app.version);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "email", app.email);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "phone", app.phone);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "termsAndPolicies", app.termsAndPolicies);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "about", app.about);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "counterPrice", app.counterPrice);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "kmPrice", app.kmPrice);
                SharedpreferencesData.setValuePreferences(SplashScreen.this, "amountToActiveProvider", ""+app.amountToActiveProvider);
                check_update();
            }

            @Override
            public void onFailure(Call<App> call, Throwable t) {
                finish();
                Toast.makeText(SplashScreen.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void check_update() {
        try {
            curVersion = getPackageManager().getPackageInfo("com.samm.estalem", 0).versionName;
            if (!curVersion.equals(SharedpreferencesData.getValuePreferences(SplashScreen.this, "version", ""))) {
                alertDialog("OK", "exit");
                return;
            }
            else {
                userOuthen();
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("MissingSuperCall")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {

                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(SplashScreen.this, "الرجاء فتح الGPS للأداء الأفضل  ", Toast.LENGTH_LONG).show();
                        break;
                    }

                }
                timerThreat();
        }
        //  timerThreat();


        //check if received result code
        //  is equal our requested code for draw permission
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        finish();
    }

    private void alertDialog(String lable, String goButton, String canceButton) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.openInternet));

        runJustOnce = false;
        timer = new Thread() {
            public void run() {

                do {

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ((Activity) SplashScreen.this).runOnUiThread(new Runnable() {
                        public void run() {
                            testintrenet = isNetworkConnected();
                            if (testintrenet == true) {

                                if (runJustOnce == false)
                                    runJustOnce = true;

                                timerThreat();
                                return;
                            } else if (WaitInternetCount % 19 == 0)
                                Message(getResources().getString(R.string.check_connection));


                            WaitInternetCount = WaitInternetCount + 1;
                        }
                    });
                } while (testintrenet == false);

            }
        };

        timer.start();
        //


        alertDialogBuilder.setPositiveButton(goButton,
                new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        alertDialogBuilder.setNegativeButton(canceButton, new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    context.finish();
            }
        });

        //TODO

    }

    boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    void Message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public String last_activity() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {

        }
        return taskList.get(0).topActivity.getClassName();
    }

    String curVersion, newVersion;

    private void alertDialog(String goButton, String canceButton) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        alertDialogBuilder.setMessage(getString(R.string.newVersion));
        alertDialogBuilder.setPositiveButton(goButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.samm.estalemaandroid=en"));
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton(canceButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        //TODO
//do it in UI htread
        ((Activity) this).runOnUiThread(new Runnable() {
            public void run() {

                AlertDialog alertDialog = alertDialogBuilder.create();
                if (!isFinishing()) {
                    //  if (isVersionShoed==false)
                    alertDialog.show();

                }
            }
        });
        isVersionShoed = true;
    }

    private void goRegistration(){


        if (SharedpreferencesData.getValuePreferences(SplashScreen.this, "clientPhone", "").equals("") &&
                SharedpreferencesData.getValuePreferences(SplashScreen.this, "providerPhone", "").equals("")) {
            Intent MainLogin = new Intent(SplashScreen.this, PermissionToAccess.class);
            startActivity(MainLogin);

        } else if (!SharedpreferencesData.getValuePreferences(SplashScreen.this, "clientPhone", "").equals("") &&
                SharedpreferencesData.getValuePreferences(SplashScreen.this, "providerPhone", "").equals("")) {
            startActivity(new Intent(SplashScreen.this, SelectDistrict.class));

        } else if (SharedpreferencesData.getValuePreferences(SplashScreen.this, "clientPhone", "").equals("") &&
                !SharedpreferencesData.getValuePreferences(SplashScreen.this, "providerPhone", "").equals("")) {
            startActivity(new Intent(SplashScreen.this, ProviderMainActivity.class));

        } else {
        }
        // if (!(last_activity().contains("Login") || (last_activity().contains("Orders"))))

        finish();
    }

    private void goOrders() {

//
//        Intent MainOrders = new Intent(SplashScreen.this, MapsActivity.class);
//        if (!(last_activity().contains("Login") || (last_activity().contains("MainActivity"))))
//            startActivity(MainOrders);
//        finish();
//

    }


    public void saveLocale(String key, String value) {

        SharedPreferences prefs = getSharedPreferences(key, SplashScreen.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();

    }



    public void openGPS() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(SplashScreen.this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        com.google.android.gms.location.LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        timerThreat();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        //  sharedInfo.LocationRequest = true;
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().

                            status.startResolutionForResult(SplashScreen.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            //    Log.i(sharedInfo.TAG, "PendingIntent unable to execute request.");
                            timerThreat();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Log.i(sharedInfo.TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        timerThreat();
                        break;
                }
            }
        });

    }

    public void userOuthen() {


      //  id = loadLocale("id");

        if (!(id.equals(""))) {
            //     Toast.makeText(Login.this, getResources().getString(R.string.loginWithNewDevice), Toast.LENGTH_LONG).show();
            saveLocale("id", id);

            goOrders();
        }
        goRegistration();
    }

}