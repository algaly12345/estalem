package com.samm.estalem.Activities.Provider;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Activities.Client.CheckClientPhone;
import com.samm.estalem.Classes.Model.Provider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PathUtil;
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

public class RegisterProvider extends AppCompatActivity {
    private Bitmap bitmap;
    Uri uri;
    int i;
    Dialog dialog;
    public MultipartBody.Part nationalCardImage;
    public MultipartBody.Part drivinglicenseImage;
    public MultipartBody.Part formImage;
    public MultipartBody.Part drivinglicenseAuthoImage;
    public MultipartBody.Part backCarImage;
    public MultipartBody.Part frontCarImage;
    public String namePeron;
    public String id_Dir;
    public String city;

    public String bankName;
    public String accountBankName;
    public String iPanNumber;

    // public String city;
    public String providerPhone;
    public EditText CarClass, PlatNumber;

    Button btnAddprovider, btnNationalCardImage, btnDrivinglicenseImage, btnFormImage, btnDrivinglicenseAuthoImage, btnBackCarImage, FrontCarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_register_agents2);

        dialog = ShowDialog.progres(RegisterProvider.this);


        namePeron = getIntent().getExtras().getString("fullName");
        id_Dir = getIntent().getExtras().getString("districtId");
        // city=getIntent().getExtras().getString("city");
        providerPhone = getIntent().getExtras().getString("providerPhone");
        city = getIntent().getExtras().getString("city");

        bankName = getIntent().getExtras().getString("bankName");
        accountBankName = getIntent().getExtras().getString("accountBankName");
        iPanNumber = getIntent().getExtras().getString("iPanNumber");


        btnAddprovider = (Button) findViewById(R.id.btnAddProvider);
        btnNationalCardImage = (Button) findViewById(R.id.btnNationalCardImage);
        btnDrivinglicenseImage = (Button) findViewById(R.id.btnDrivinglicenseImage);
        btnFormImage = (Button) findViewById(R.id.btnFormImage);
        btnDrivinglicenseAuthoImage = (Button) findViewById(R.id.btnDrivinglicenseAuthoImage);
        FrontCarImage = (Button) findViewById(R.id.FrontCarImage);
        btnBackCarImage = (Button) findViewById(R.id.btnBackCarImage);
        CarClass = findViewById(R.id.txtCarClass);
        PlatNumber = findViewById(R.id.txtPlateNumber);



        btnNationalCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
                i = 1;
            }
        });
        btnDrivinglicenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();

                i = 2;

            }
        });
        btnFormImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();

                i = 3;
            }
        });
        btnDrivinglicenseAuthoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();

                i = 4;
            }
        });
        FrontCarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();

                i = 5;
            }
        });
        btnBackCarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
                i = 6;
            }
        });
        btnAddprovider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classCard = CarClass.getText().toString();
                String pate = PlatNumber.getText().toString();
                addProvider(new Provider(providerPhone, namePeron, "image", city, classCard, pate, "nationalCardImage", "drivinglicenseImage", "formImage", "drivinglicenseAuthoImage", "backCarImage", "frontCarImage", "statues", "connectivity", 16.5656323, 32.545121, "token", "connectionId", Integer.valueOf(id_Dir),iPanNumber,bankName,accountBankName));
            }
        });
    }

    private void addProvider(Provider provider) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        if (nationalCardImage == null || drivinglicenseImage == null || formImage == null  || backCarImage == null || frontCarImage == null) {
            dialog.dismiss();
            Toast.makeText(RegisterProvider.this, getResources().getString(R.string.select_all_image), Toast.LENGTH_SHORT).show();
            return;
        }
        if (CarClass.getText().toString().isEmpty()) {
            CarClass.setError(getResources().getString(R.string.empty));
            dialog.dismiss();

            return;
        }
        if (PlatNumber.getText().toString().isEmpty()) {
            PlatNumber.setError(getResources().getString(R.string.empty));
            dialog.dismiss();
            return;
        }

        create_post_get.AddProvider(provider).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.body().getMessage().equals(providerPhone)) {
                    ResponseBody responseBody = response.body();
                    UploadImage("" + responseBody.getMessage());
                } else {
                    Toast.makeText(RegisterProvider.this, getResources().getString(R.string.error) + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


                // dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), RegisterProvider.this);
                dialog.dismiss();
            }
        });

    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void UploadImage(String phone) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.UploadProviderImage(phone, nationalCardImage, drivinglicenseImage,
                formImage, drivinglicenseAuthoImage, backCarImage, frontCarImage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body().getMessage().equals(phone)) {
                    SharedpreferencesData.setValuePreferences(RegisterProvider.this, "providerPhone", providerPhone);
                    dialog.dismiss();
                    startActivity(new Intent(RegisterProvider.this, ProviderMainActivity.class));

                } else {
                    Toast.makeText(RegisterProvider.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), RegisterProvider.this);
                dialog.dismiss();
            }
        });
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

            } catch (IOException e) {
                e.printStackTrace();
            }





            if (i == 1) {
                try {
                    btnNationalCardImage.setBackgroundResource(R.drawable.done);
                    String realPath = PathUtil.getPath(RegisterProvider.this, uri);
                    File file = new File(realPath);
                    nationalCardImage = Upload.NationalCardImage(file);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (i == 2) {
                try {
                    //   Toast.makeText(this, "You Select 2", Toast.LENGTH_SHORT).show();

                    btnDrivinglicenseImage.setBackgroundResource(R.drawable.done);
                    String realPath = PathUtil.getPath(RegisterProvider.this, uri);
                    File file = new File(realPath);
                    drivinglicenseImage = Upload.DrivinglicenseImage(file);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else if (i == 3) {
                try {
                    btnFormImage.setBackgroundResource(R.drawable.done);
                    //Toast.makeText(this, "You Select 3", Toast.LENGTH_SHORT).show();
                    String realPath = PathUtil.getPath(RegisterProvider.this, uri);
                    File file = new File(realPath);
                    formImage = Upload.FormImage(file);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else if (i == 4) {
                try {
                    btnDrivinglicenseAuthoImage.setBackgroundResource(R.drawable.done);
                    String realPath = PathUtil.getPath(RegisterProvider.this, uri);
                    File file = new File(realPath);
                    drivinglicenseAuthoImage = Upload.DrivinglicenseAuthoImage(file);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else if (i == 5) {
                try {
                    FrontCarImage.setBackgroundResource(R.drawable.done);
                    String realPath = PathUtil.getPath(RegisterProvider.this, uri);
                    File file = new File(realPath);
                    frontCarImage = Upload.FrontCarImage(file);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else if (i == 6) {
                try {
                    btnBackCarImage.setBackgroundResource(R.drawable.done);
                    String realPath = PathUtil.getPath(RegisterProvider.this, uri);
                    File file = new File(realPath);
                    backCarImage = Upload.BackCarImage(file);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
