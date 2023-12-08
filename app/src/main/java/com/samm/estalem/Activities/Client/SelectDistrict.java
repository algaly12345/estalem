package com.samm.estalem.Activities.Client;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Adapters.AdapterSpinnerDistrict;
import com.samm.estalem.Classes.Model.Category;
import com.samm.estalem.Classes.Model.District;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectDistrict extends AppCompatActivity {
    private List<Category> contacts2;
    private AdapterSpinnerDistrict adapter3;
    private List<District> contacts3;
    ProgressDialog progressDialog;
    private List<District> gradelis2;
    private Spinner splDistricts;
    private Spinner splCity;
    int id_state;
    Button Next;
    Button btnSkip;
    Dialog dialog;
    ArrayList<String> districtItems = new ArrayList<>();
    ArrayList<String> cityItems = new ArrayList<>();
    ArrayList<String> AllCityItems = new ArrayList<>();
    ArrayList<District> districtItem = new ArrayList<>();
    double log=0, lat=0;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_select_provider);
        splDistricts = findViewById(R.id.splDistricts);
        splCity = findViewById(R.id.splCity);
        Next = findViewById(R.id.next);
        btnSkip = findViewById(R.id.btnSkip);
        dialog = ShowDialog.progres(this);
        dialog.show();
        splDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lat = districtItem.get(i).getLat();
                log = districtItem.get(i).getLog();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        splCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                getDistrict( AllCityItems.get(position));
               // Toast.makeText(SelectDistrict.this, "last" + AllCityItems.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectDistrict.this, ClientMainActivity.class);
                i.putExtra("areaLat",lat);
                i.putExtra("areaLog",log);
                startActivity(i);
            }
        });



        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectDistrict.this, ClientMainActivity.class);
                i.putExtra("areaLat",0);
                i.putExtra("areaLog",0);
                startActivity(i);
            }
        });
        getCity();



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
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry),getResources().getString(R.string.no_internet),SelectDistrict.this);
            }
        });

    }



    private void fillSplCity() {
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllCityItems);
        splCity.setAdapter(adapter3);
    }

    private void getDistrict(String values) {
        districtItems.clear();
        districtItem.clear();
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
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry),getResources().getString(R.string.no_internet),SelectDistrict.this);
            }
        });

    }

    private void fillSplDistricts() {
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, districtItems);
        splDistricts.setAdapter(adapter3);
    }



}