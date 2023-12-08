package com.samm.estalem.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.samm.estalem.Activities.Client.CheckClientPhone;
import com.samm.estalem.Activities.Provider.CheckProviderPhone;
import com.samm.estalem.Activities.Provider.DirectionToClient;
import com.samm.estalem.R;
import com.samm.estalem.Util.ReceiveOrderService;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

public class PermissionToAccess extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_permission_to_access);

        startActivity(new Intent(PermissionToAccess.this, Policy.class));

            if (ActivityCompat.checkSelfPermission(PermissionToAccess.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                            , 1);
                }
        }

        Button btnRegisterAsClient=(Button)findViewById(R.id.btnRegisterAsClient);
        Button btnRegisterAsProvider=(Button)findViewById(R.id.btnRegisterAsProvider);
        btnRegisterAsClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(PermissionToAccess.this, CheckClientPhone.class));
            }
        });
        btnRegisterAsProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(PermissionToAccess.this, CheckProviderPhone.class));
            }
        });




        Button btnEnglish =(Button)findViewById(R.id.btnEnglish);
        Button btnArabic=(Button)findViewById(R.id.btnArabic);

        btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedpreferencesData.setValuePreferences(PermissionToAccess.this,"language","ar");
                startService(new Intent(PermissionToAccess.this,SplashScreen.class));

                SharedpreferencesData.setValuePreferences(getBaseContext(), "switch", "0");

                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                startActivity(mainIntent);
                Runtime.getRuntime().exit(0);

            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedpreferencesData.setValuePreferences(PermissionToAccess.this,"language","en");

                SharedpreferencesData.setValuePreferences(getBaseContext(), "switch", "0");


                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                startActivity(mainIntent);
                Runtime.getRuntime().exit(0);

            }
        });


    }


}