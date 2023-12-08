package com.samm.estalem.Activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Classes.Model.Proposals;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Suggestion extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_suggestion);
        dialog = ShowDialog.progres(this);
        TextView tvTwitter = (TextView) findViewById(R.id.tvTwitter);
        TextView tvAstalemPhone = (TextView) findViewById(R.id.tvAstalemPhone);
        TextView tvInstgram = (TextView) findViewById(R.id.tvInstgram);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        TextView tvFacebook = (TextView) findViewById(R.id.tvFacebook);
        Button btnSuggestion = (Button) findViewById(R.id.btnSuggestion);
        tvTwitter.setText(SharedpreferencesData.getValuePreferences(this, "twitter", ""));
        tvAstalemPhone.setText(SharedpreferencesData.getValuePreferences(this, "phone", ""));
        tvInstgram.setText(SharedpreferencesData.getValuePreferences(this, "instagram", ""));
        tvFacebook.setText(SharedpreferencesData.getValuePreferences(this, "facebook", ""));
        tvEmail.setText(SharedpreferencesData.getValuePreferences(this, "email", ""));


        tvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacebook();
            }
        });
        tvInstgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstgram();
            }
        });
        tvTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTwitter();
            }
        });
        tvAstalemPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCall();
            }
        });
        EditText txtContainSuggestion = (EditText) findViewById(R.id.txtContainSuggestion);
        EditText txtTitleSuggestion = (EditText) findViewById(R.id.txtTitleSuggestion);
        EditText txtNameSuggestion = (EditText) findViewById(R.id.txtNameSuggestion);
        String phone = SharedpreferencesData.getValuePreferences(this, "clientPhone", ".") + "" + SharedpreferencesData.getValuePreferences(this, "providerId", ".");
        btnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtContainSuggestion.getText().toString().isEmpty()) {
                    txtContainSuggestion.setError(getResources().getString(R.string.empty));
                    return;
                }

                if (txtNameSuggestion.getText().toString().isEmpty()) {
                    txtNameSuggestion.setError(getResources().getString(R.string.empty));
                    return;
                }
                if (txtTitleSuggestion.getText().toString().isEmpty()) {
                    txtTitleSuggestion.setError(getResources().getString(R.string.empty));
                    return;
                }
                addSuggest(new Proposals(txtNameSuggestion.getText().toString(), phone, txtTitleSuggestion.getText().toString(), txtContainSuggestion.getText().toString()));
            }
        });
    }

    public void openInstgram() {
        Uri uri = Uri.parse(SharedpreferencesData.getValuePreferences(getApplicationContext(), "instagram", ""));
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(SharedpreferencesData.getValuePreferences(getApplicationContext(), "instagram", ""))));
        }

    }
    public void openFacebook() {
        Uri uri = Uri.parse(SharedpreferencesData.getValuePreferences(getApplicationContext(), "instagram", ""));
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.facebook.katana");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(SharedpreferencesData.getValuePreferences(getApplicationContext(), "facebook", ""))));
        }

    }

    public void getCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + SharedpreferencesData.getValuePreferences(Suggestion.this, "phone", "")));
        startActivity(intent);
    }

    public void openTwitter() {
        Uri uri = Uri.parse(SharedpreferencesData.getValuePreferences(getApplicationContext(), "twitter", ""));
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.twitter.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("" + SharedpreferencesData.getValuePreferences(getApplicationContext(), "twitter", ""))));
        }
    }


    public void addSuggest(Proposals proposals) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.AddSuggest(proposals).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(Suggestion.this, "" + getResources().getString(R.string.suggest_done), Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), Suggestion.this);
                dialog.dismiss();
            }
        });
    }
}
