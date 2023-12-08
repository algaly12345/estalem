package com.samm.estalem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.samm.estalem.Classes.Model.App;
import com.samm.estalem.R;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

public class About extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_about);
        TextView tvAbout=(TextView)findViewById(R.id.tvAbout);
        tvAbout.setText(SharedpreferencesData.getValuePreferences(this,"about",""));
    }
}
