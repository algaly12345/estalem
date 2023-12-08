package com.samm.estalem.Activities.Client;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Classes.Model.District;
import com.samm.estalem.Classes.Model.Client;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterClientActivity extends AppCompatActivity {
    Dialog dialog;
    String clientPhone;
    ArrayList<String> districtItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_regiseter_client);
        dialog = ShowDialog.progres(RegisterClientActivity.this);
        clientPhone = getIntent().getExtras().getString("clientPhone");

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        TextView txtCity = (TextView) findViewById(R.id.txtCity);
        TextView txtArea = (TextView) findViewById(R.id.txtArea);
        TextView txtFullName = (TextView) findViewById(R.id.txtFullName);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                addClient(new Client(clientPhone,txtFullName.getText().toString(), "image",txtArea.getText().toString() , txtCity.getText().toString()));
            }
        });
    }

    private void addClient(Client client) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.AddClient(client).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body().getMessage().equals(clientPhone)){
                    Toast.makeText(RegisterClientActivity.this, getResources().getString(R.string.register_done), Toast.LENGTH_SHORT).show();
                    SharedpreferencesData.setValuePreferences(RegisterClientActivity.this,"clientPhone",clientPhone);
                    startActivity(new Intent(RegisterClientActivity.this, SelectDistrict.class));
                }else {
                    Toast.makeText(RegisterClientActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), RegisterClientActivity.this);

            }
        });
    }

    private void getDistrict() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetDistrict().enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                List<District> district=response.body();
                for(int i=0;i<district.size();i++) {
                    districtItems.add(district.get(i).getDistrictName());
                }
                fillSplDistricts();
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {

            }
        });

    }

    private void fillSplDistricts() {
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, districtItems);
        //splDistricts.setAdapter(adapter3);

    }


}
