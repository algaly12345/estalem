package com.samm.estalem.Activities.Provider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Activities.Client.SelectDistrict;
import com.samm.estalem.Adapters.AdapterSpinnerDistrict;
import com.samm.estalem.Classes.Model.District;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterProviderInfo extends AppCompatActivity {

    EditText NamePreson,City ;
    //LinearLayout ImagePerosn;
    private Bitmap bitmap;
    public MultipartBody.Part imagPerson ;
    Uri uri;
    int i;
    private Spinner splDistricts,spinnerCity;
    int id_district;
    Button Next;
    Dialog dialog;
    private Spinner splCity;
    private AdapterSpinnerDistrict adapterDistrict;
    private List<District> contatsDistrict;
    private List<District> gradelisDistrt;
    String providerPhone;
    ArrayList<String> districtItems = new ArrayList<>();
    ArrayList<District> districtItem = new ArrayList<>();
    ArrayList<String> AllCityItems = new ArrayList<>();
    public EditText txtBankName, txtIPanNumber,txtAccountBankName;

    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_provider_register);
        NamePreson=findViewById(R.id.txt_namePerson);

        providerPhone = getIntent().getExtras().getString("providerPhone");

        splDistricts = findViewById(R.id.splDistrictsProiver);
        spinnerCity = findViewById(R.id.splCityProvider);
        dialog = ShowDialog.progres(this);
        splCity = findViewById(R.id.splCityProvider);

        txtIPanNumber = findViewById(R.id.txtIPanNumber);
        txtBankName = findViewById(R.id.txtBankName);
        txtAccountBankName = findViewById(R.id.txtNameOnCard);


        ShowDialog.showErrorDialog(getResources().getString(R.string.dialog_details),getResources().getString(R.string.bank_account_alert),this);
        dialog.show();


        getCity();

        splDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_district = districtItem.get(position).getId();
               // Toast.makeText(RegisterProviderInfo.this, "" + id_district, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        splCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city =AllCityItems.get(position);
                getDistrict(city);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void Next(View view) {

        if (NamePreson.getText().toString().isEmpty()) {
            NamePreson.setError(getResources().getString(R.string.empty));
            return;
        }

        if (txtAccountBankName.getText().toString().isEmpty()) {
            txtAccountBankName.setError(getResources().getString(R.string.empty));
            return;
        }

        if (txtBankName.getText().toString().isEmpty()) {
            txtBankName.setError(getResources().getString(R.string.empty));
            return;
        }

        if (txtIPanNumber.getText().toString().isEmpty()) {
            txtIPanNumber.setError(getResources().getString(R.string.empty));
            return;
        }

        String namePerosn=NamePreson.getText().toString();
        Intent intent = new Intent(RegisterProviderInfo.this, RegisterProvider.class);
        intent.putExtra("fullName", namePerosn);
        intent.putExtra("districtId", ""+id_district);
        intent.putExtra("city", city);
        intent.putExtra("providerPhone", providerPhone);

        intent.putExtra("bankName", txtBankName.getText().toString());
        intent.putExtra("accountBankName", txtAccountBankName.getText().toString());
        intent.putExtra("iPanNumber", txtIPanNumber.getText().toString());
        startActivity(intent);
    }



    private void getDistrict(String values) {
        districtItems.clear();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.getSuCity(values).enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                List<District> district = response.body();
                for (int i = 0; i < district.size(); i++) {
                    districtItems.add(district.get(i).getDistrictName());
                    districtItem.add(district.get(i));
                }
                fillSplDistricts();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry),getResources().getString(R.string.no_internet), RegisterProviderInfo.this);
            }
        });

    }

    private void fillSplDistricts() {
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, districtItems);
        splDistricts.setAdapter(adapter3);
    }



    private void getCity() {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.getCiy().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> district = response.body();
                for (int i = 0; i < district.size(); i++) {
                    AllCityItems.add(district.get(i));
                }
                fillSplCity();


                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry),getResources().getString(R.string.no_internet),RegisterProviderInfo.this);
            }
        });

    }


    private void fillSplCity() {
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllCityItems);
        splCity.setAdapter(adapter3);
    }


//    private void showListinSpinnerDistrict() {
//        String[] items2 = new String[gradelisDistrt.size()];
//         for(int i = 0; i < gradelisDistrt.size(); i++) {
//            items2[i] = gradelisDistrt.get(i).getDistrictName();
//        }
//        ArrayAdapter<String> adapter3;
//        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items2);
//        spinnerMarketPlace.setAdapter(adapter3);
//        spinnerCity.setAdapter(adapter3);
//    }

}
