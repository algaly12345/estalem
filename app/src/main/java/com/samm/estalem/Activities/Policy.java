package com.samm.estalem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Classes.Model.App;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Policy extends AppCompatActivity {
    TextView tvAbout;
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_policy);
        CheckBox chkOkPolicy = findViewById(R.id.chkOkPolicy);
        Button btnOk = findViewById(R.id.btnOkPolicy);
        btnOk.setEnabled(false);

        getApp();
        chkOkPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkOkPolicy.isChecked()){
                    btnOk.setEnabled(true);
                }else {
                    btnOk.setEnabled(false);
                }
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvAbout = (TextView) findViewById(R.id.tvPolicy);
        tvAbout.setText(SharedpreferencesData.getValuePreferences(this, "termsAndPolicies", ""));
    }


    public void getApp() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.App().enqueue(new Callback<App>() {
            @Override
            public void onResponse(Call<App> call, Response<App> response) {
                App app = response.body();
                tvAbout.setText(app.termsAndPolicies);

            }

            @Override
            public void onFailure(Call<App> call, Throwable t) {

            }
        });
    }
}