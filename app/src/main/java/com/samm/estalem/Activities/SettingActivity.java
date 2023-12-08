package com.samm.estalem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.samm.estalem.R;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.language(this);

        setContentView(R.layout.activity_setting);
        Button btnEnglish =(Button)findViewById(R.id.btnEnglish);
        Button btnArabic=(Button)findViewById(R.id.btnArabic);

        btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedpreferencesData.setValuePreferences(SettingActivity.this,"language","ar");
                startService(new Intent(SettingActivity.this,SplashScreen.class));

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
                SharedpreferencesData.setValuePreferences(SettingActivity.this,"language","en");

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
