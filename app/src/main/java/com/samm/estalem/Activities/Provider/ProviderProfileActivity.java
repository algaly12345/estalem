package com.samm.estalem.Activities.Provider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samm.estalem.Adapters.AdapterSpinnerDistrict;
import com.samm.estalem.Classes.Model.District;
import com.samm.estalem.Classes.Model.Provider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.ApiClient;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PathUtil;
import com.samm.estalem.Util.PicassoClient;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Upload;
import com.samm.estalem.Util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderProfileActivity extends AppCompatActivity {

    EditText Name, PhoneNumber, CarName, PlateNumber;
    private Bitmap bitmap;
    Uri uri;
    String city;
    Dialog dialog;
    ImageView profileImage;
    Button updateProfile;
    ImageView imgProfile;
    FloatingActionButton fabChoosePic;
    Provider provider;


    EditText bankName;
    EditText accountBankName;
    EditText iPanNumber;

    //dir
    private Spinner spinnerMarketPlace, splDistricts;
    private List<District> contatsDistrict;
    private List<District> gradelisDistrt;
    private AdapterSpinnerDistrict adapterDistrict;
    ArrayAdapter<String> adapter3;


    int id_district;

    private Spinner splCity;
    ArrayList<String> AllCityItems = new ArrayList<>();
    ArrayList<District> districtItem = new ArrayList<>();
    ArrayList<String> districtItems = new ArrayList<>();
    double log = 0, lat = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_provider_profile);

        dialog = ShowDialog.progres(this);
        Name = findViewById(R.id.txtFullNameProvider);
        PhoneNumber = findViewById(R.id.txtPhoneProviderPro);
        CarName = findViewById(R.id.txtCarName);
        PlateNumber = findViewById(R.id.txtPlateNumber);
        updateProfile = findViewById(R.id.btnUpdateProfileProvider);

        bankName = findViewById(R.id.txtBankName);
        accountBankName = findViewById(R.id.txtNameOnCard);
        iPanNumber = findViewById(R.id.txtIPanNumber);

        profileImage = findViewById(R.id.profileImageProviderProv);
        imgProfile = findViewById(R.id.imgProfileProvider);
        fabChoosePic = findViewById(R.id.fabChoosePicProvider);

        splCity = findViewById(R.id.splDistriCity);
        splDistricts = findViewById(R.id.splDistricprofile);

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


        splDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_district = districtItem.get(position).getId();
                //  Toast.makeText(ProviderProfileActivity.this, "" + id_district, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        fabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Name.getText().toString().isEmpty()) {
                    Name.setError(getResources().getString(R.string.empty));
                    return;
                }


                if (CarName.getText().toString().isEmpty()) {
                    CarName.setError(getResources().getString(R.string.empty));
                    return;
                }

                if (PlateNumber.getText().toString().isEmpty()) {
                    PlateNumber.setError(getResources().getString(R.string.empty));
                    return;
                }

                if (iPanNumber.getText().toString().isEmpty()) {
                    iPanNumber.setError(getResources().getString(R.string.empty));
                    return;
                }


                if (bankName.getText().toString().isEmpty()) {
                    bankName.setError(getResources().getString(R.string.empty));
                    return;
                }


                if (accountBankName.getText().toString().isEmpty()) {
                    accountBankName.setError(getResources().getString(R.string.empty));
                    return;
                }
                updateProfile();
            }
        });

        splCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                getDistrict(AllCityItems.get(position));
                city = AllCityItems.get(position);
                // Toast.makeText(SelectDistrict.this, "last" + AllCityItems.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getProfile();

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
                splCity.setSelection(getIndex(provider.getCity(),AllCityItems));
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderProfileActivity.this);
            }
        });

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
                try {
                    splDistricts.setSelection(getIndex(searchItem(provider.getDistrictId()).getDistrictName(),districtItems));
                }catch (Exception e){}

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderProfileActivity.this);
            }
        });

    }



    public void editName(View view) {
        Name.setEnabled(true);
        Name.requestFocus();
    }


    public void editCarName(View view) {
        CarName.setEnabled(true);
        CarName.requestFocus();
    }

    public void editPlateNumber(View view) {
        PlateNumber.setEnabled(true);
        PlateNumber.requestFocus();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();


            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 3000, 3000, false);
              uri =  getImageUri(this,bitmap);

                try {
                    imgProfile.setImageBitmap(bitmap);
                } catch (Exception e) {
                }
                UpdateImage();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    public void UpdateImage() {
        dialog.show();
        String realPath = null;
        File file = null;
        try {
            realPath = PathUtil.getPath(getApplicationContext(), uri);
            file = new File(realPath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            Retrofit_Connection retrofit_connection = new Retrofit_Connection();
            retrofit_connection.con_GSON();
            final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
            MultipartBody.Part image = Upload.upLoadProfileImage(file);
            create_post_get.UploadProviderProfileImage(image, SharedpreferencesData.getValuePreferences(getApplicationContext(), "providerPhone", "0")).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.v("x", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + response.body());

                    if (response.body().getMessage().equals("1")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_changed), Toast.LENGTH_SHORT).show();
                        getProfile();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderProfileActivity.this);
                    Toast.makeText(ProviderProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
        }

    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private void getProfile() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.GetProfileProvider(SharedpreferencesData.getValuePreferences(ProviderProfileActivity.this, "providerPhone", "")).enqueue(new Callback<Provider>() {
            @Override
            public void onResponse(Call<Provider> call, Response<Provider> response) {
                provider = response.body();
                Name.setText(provider.getFullName());
                PhoneNumber.setText(provider.getPhone());
                //  txtCity.setText(provider.getCity());
                PlateNumber.setText(provider.getPlateNumber());
                CarName.setText(provider.getCarClass());

                bankName.setText(provider.getBankName());
                accountBankName.setText(provider.getBankAccountName());
                iPanNumber.setText(provider.getiPanNumber());
                getCity();


                PicassoClient.downloadImage(ProviderProfileActivity.this, ApiClient.BASE_URL2 + provider.getImage(), profileImage);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Provider> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderProfileActivity.this);
                dialog.dismiss();
            }
        });

    }


    private void updateProfile() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        provider.setFullName(Name.getText().toString());
        provider.setDistrictId(id_district);
        provider.setPlateNumber("" + PlateNumber.getText().toString());
        provider.setCarClass("" + CarName.getText().toString());
        provider.setCity("" + city);

        provider.setBankAccountName("" + accountBankName.getText().toString());
        provider.setBankName("" + bankName.getText().toString());
        provider.setiPanNumber("" + iPanNumber.getText().toString());

        create_post_get.UpdateProvider(provider).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (responseBody.getMessage().equals(SharedpreferencesData.getValuePreferences(ProviderProfileActivity.this, "providerPhone", ""))) {
                    Toast.makeText(ProviderProfileActivity.this, "" + getResources().getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    getProfile();
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderProfileActivity.this);
                dialog.dismiss();
            }
        });

    }


    private void fillSplCity() {
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllCityItems);
        splCity.setAdapter(adapter3);
    }


    public int getIndex(String myString,ArrayList<String> list) {
        int aPosition = 0;
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).contains(myString))
                aPosition = i;

        return aPosition;
    }

    public District searchItem(int i){
        for (District district: districtItem) {
            if(district.getId() == i)
                return district;
        }
        return null;
           }
    private void fillSplDistricts() {
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, districtItems);
        splDistricts.setAdapter(adapter3);
    }


}
