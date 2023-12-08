package com.samm.estalem.Activities.Provider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.samm.estalem.Activities.Client.VerifyClientCodeActivity;
import com.samm.estalem.Classes.Model.ValuationProvider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.CheckCodeViewModel;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VarifyProviderCode extends AppCompatActivity {
    TextView otp;
    EditText otp_box_1,otp_box_2,otp_box_3,otp_box_4,otp_box_5;
    Button Verfy;
    String prviderPhone;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_varify_provider_code);

        dialog = ShowDialog.progres(this);

        prviderPhone = getIntent().getExtras().getString("providerPhone");

        Verfy=findViewById(R.id.btn_verify);
        otp = findViewById(R.id.otp);
        otp_box_1 = findViewById(R.id.otp_box_1);
        otp_box_2 = findViewById(R.id.otp_box_2);
        otp_box_3 = findViewById(R.id.otp_box_3);
        otp_box_4 = findViewById(R.id.otp_box_4);
        otp_box_5 = findViewById(R.id.otp_box_5);

        otp_box_1.requestFocus();




        otp_box_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_2.requestFocus();
                }
            }
        });
        otp_box_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_3.requestFocus();
                }
            }
        });
        otp_box_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_4.requestFocus();
                }
            }
        });
        otp_box_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_5.requestFocus();
                }
            }
        });

        Verfy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.show();
                if(otp_box_1.getText().toString().isEmpty()||otp_box_2.getText().toString().isEmpty()||otp_box_3.getText().toString().isEmpty()||otp_box_4.getText().toString().isEmpty()||otp_box_5.getText().toString().isEmpty())
                {
                    Toast.makeText(VarifyProviderCode.this, getResources().getString(R.string.enter_code), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                String codeString=otp_box_1.getText().toString()+""+otp_box_2.getText().toString()+""+otp_box_3.getText().toString()+""+otp_box_4.getText().toString()+""+otp_box_5.getText().toString();

                Retrofit_Connection retrofit_connection=new Retrofit_Connection();
                retrofit_connection.con_GSON();
                final CREATE_GET_POST create_post_get=retrofit_connection.retrofit.create(CREATE_GET_POST.class);
                long code=Long.parseLong(codeString);

                create_post_get.CheckProviderNumberPhone(new CheckCodeViewModel(code,prviderPhone)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        ResponseBody responseBody = response.body();

                        if (responseBody.getMessage().equals("0")) {
                            ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.code_not_correct), VarifyProviderCode.this);
                        } else if (responseBody.getMessage().equals("2")) {
                                //goto main       هنا يا الغالي بتمش الرئيسية لانه العميل مسجل قبل كدة
                            SharedpreferencesData.setValuePreferences(VarifyProviderCode.this,"providerPhone",prviderPhone);

                            startActivity(new Intent(VarifyProviderCode.this, ProviderMainActivity.class));
                        } else if (responseBody.getMessage().equals("1")) {
                                // goto Register   وهنا عشان اول مرة بتمش التسجيل

                            Intent intent = new Intent(VarifyProviderCode.this, RegisterProviderInfo.class);
                            intent.putExtra("providerPhone",prviderPhone);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), VarifyProviderCode.this);
                       dialog.dismiss();
                    }
                });
            }
        });


    }







}
