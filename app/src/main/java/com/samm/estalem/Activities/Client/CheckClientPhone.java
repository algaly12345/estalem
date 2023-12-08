package com.samm.estalem.Activities.Client;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckClientPhone extends AppCompatActivity {

    EditText PhoneNumber;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_check_client_phone);
        dialog = ShowDialog.progres(CheckClientPhone.this);

        PhoneNumber = findViewById(R.id.txtPhone);
        Button btnCheckPhone=(Button)findViewById(R.id.btnCheckPhone);
        btnCheckPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String phone = PhoneNumber.getText().toString();
                if (phone.isEmpty()) {
                    PhoneNumber.setError(getResources().getString(R.string.empty));
                    dialog.dismiss();
                    return;
                }
                if (!PhoneNumber.getText().toString().startsWith("05") || PhoneNumber.getText().toString().length() != 10 ) {
                    PhoneNumber.setError(getResources().getString(R.string.phone_not_correct));
                    dialog.dismiss();
                    return;
                }
                checkClientPhone(phone);
            }
        });
    }

    private void checkClientPhone(String phone) {
        Retrofit_Connection retrofit_connection=new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get=retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.CheckPhone(phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (responseBody.getMessage().equals("Used")) {
                    ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.used_as_provider), CheckClientPhone.this);
                    dialog.dismiss();
                    return;
                }
                Intent intent = new Intent(CheckClientPhone.this, VerifyClientCodeActivity.class);
                intent.putExtra("clientPhone","" + responseBody.getMessage());
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry),getResources().getString(R.string.no_internet),CheckClientPhone.this);
                dialog.dismiss();
            }
        });

    }
}
