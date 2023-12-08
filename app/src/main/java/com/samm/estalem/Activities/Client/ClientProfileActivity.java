package com.samm.estalem.Activities.Client;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samm.estalem.Classes.Model.Client;
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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientProfileActivity extends AppCompatActivity {
    EditText Name, PhoneNumber, Address, txtCity;
    private Bitmap bitmap;
    Uri uri;
    Dialog dialog;
    ImageView profileImage;
    Button updateProfile;
    ImageView imgProfile;
    FloatingActionButton fabChoosePic;
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_profile);
        dialog = ShowDialog.progres(this);
        Name = findViewById(R.id.txtFullName);
        PhoneNumber = findViewById(R.id.txtPhone);
        updateProfile = findViewById(R.id.btnUpdateProfile);
        txtCity = findViewById(R.id.txtCity);
        profileImage = findViewById(R.id.profileImage);
        Address = findViewById(R.id.txtArea);
        imgProfile = findViewById(R.id.imgProfile);
        fabChoosePic = findViewById(R.id.fabChoosePic);
        fabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Name.getText().toString().isEmpty()){
                    Name.setError(getResources().getString(R.string.empty));
                    return;
                }

                if(Address.getText().toString().isEmpty()){
                    Address.setError(getResources().getString(R.string.empty));
                    return;
                }

                if(txtCity.getText().toString().isEmpty()){
                    txtCity.setError(getResources().getString(R.string.empty));
                    return;
                }
                updateProfile();
            }
        });
        getProfile();
    }

    public void editName(View view) {
        Name.setEnabled(true);
        Name.requestFocus();
    }


    public void editAddress(View view) {
        Address.setEnabled(true);
        Address.requestFocus();
    }

    public void editCity(View view) {
        txtCity.setEnabled(true);
        txtCity.requestFocus();
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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


        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        MultipartBody.Part image = Upload.upLoadProfileImage(file);


        create_post_get.UploadImageClient(image, SharedpreferencesData.getValuePreferences(getApplicationContext(), "clientPhone", "0")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_changed), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ClientProfileActivity.this);
                Toast.makeText(ClientProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void getProfile() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.GetProfile(SharedpreferencesData.getValuePreferences(ClientProfileActivity.this, "clientPhone", "")).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                 client = response.body();
                Name.setText(client.fullName);
                PhoneNumber.setText(client.phone);
                Address.setText(client.district);
                txtCity.setText(client.city);
                PicassoClient.downloadImage(ClientProfileActivity.this, ApiClient.BASE_URL2 + client.image, profileImage);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ClientProfileActivity.this);
                dialog.dismiss();
            }
        });

    }

    private void updateProfile() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.UpdateClient(new Client(SharedpreferencesData.getValuePreferences(ClientProfileActivity.this, "clientPhone", "")
                ,Name.getText().toString(),client.image,Address.getText().toString(),txtCity.getText().toString(),client.lat,client.log,client.token,client.connectionId)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (response.body().getMessage().equals(SharedpreferencesData.getValuePreferences(ClientProfileActivity.this, "clientPhone", ""))){
                    Toast.makeText(ClientProfileActivity.this, ""+getResources().getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    finish();
                    getProfile();
                    }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ClientProfileActivity.this);
                dialog.dismiss();
            }
        });

    }
}
