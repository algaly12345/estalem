package com.samm.estalem.Activities.Provider;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.samm.estalem.Activities.About;
import com.samm.estalem.Activities.PermissionToAccess;
import com.samm.estalem.Activities.Policy;
import com.samm.estalem.Activities.SettingActivity;
import com.samm.estalem.Activities.Suggestion;
import com.samm.estalem.Classes.Model.Provider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.UpdateTokenViewModel;
import com.samm.estalem.Faraments.ui.active_provider.ActiveProviderFragment;
import com.samm.estalem.Faraments.ui.main_provider_fragment.MainProviderFragment;
import com.samm.estalem.Faraments.ui.provider_orders.ProviderOrderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.ApiClient;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PicassoClient;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static   DrawerLayout drawer;
    ImageView imageViewHeader;
    View headerView;
    Dialog dialog;
    double amount = 0;
    ImageView profileImageProvide;
    TextView NameProvider;
    TextView PhoneNumberProvider;
    Button ok;
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_main2_provider);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(getApplication(), R.style.actionBarTitleTextStyle);
        dialog = ShowDialog.progres(this);

        setSupportActionBar(toolbar);

         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);

        displaySelectedScreen(R.id.customer_list);

        profileImageProvide = headerView.findViewById(R.id.imgProfileProvider);
        NameProvider = (TextView) headerView.findViewById(R.id.txtFullNameProviderheader);
        PhoneNumberProvider = (TextView) headerView.findViewById(R.id.txtPhoneProviderheader);
      FloatingActionButton floatingActionButton = headerView.findViewById(R.id.editProviderflo);
      floatingActionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(ProviderMainActivity.this,ProviderProfileActivity.class));
          }
      });

        imageViewHeader=headerView.findViewById(R.id.edit);

        profileImageProvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProviderMainActivity.this,ProviderProfileActivity.class));
            }
        });
        getProfile();
        getToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2_activity, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        Util.language(this);
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.customer_list:
                fragment =new MainProviderFragment(this);
                break;
            case R.id.my_counta_provider:
                final Dialog dialog1 = new Dialog(this);
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.dilog_your_acount_provider);
                Window window1 = dialog1.getWindow();
                window1.setBackgroundDrawableResource(android.R.color.transparent);
                window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                 ok = dialog1.findViewById(R.id.btnMyAccountProvider);
                ok.setText(getAccount()+getResources().getString(R.string.currency));

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();

                break;

            case R.id.myorderlist_list:
              fragment =new ProviderOrderFragment();
                break;


                case R.id.active:
              fragment =new ActiveProviderFragment();
                break;


            case R.id.tch_support_provider:
                startActivity(new Intent(this, Suggestion.class));
                break;


                case R.id.policy:
                startActivity(new Intent(this, Policy.class));
                break;

            case R.id.about_as_provider:
                startActivity(new Intent(this, About.class));
                break;


            case R.id.setting_provider:
                startActivity(new Intent(this, SettingActivity.class));
                break;

            case R.id.n_exit_provider:
                SharedpreferencesData.clearSharePreferences(ProviderMainActivity.this, "providerPhone");
                startActivity(new Intent(this, PermissionToAccess.class));
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    public static void showNavBar() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
        }
    }
    public void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            //   getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply();
            updateToken(newToken);
        });
    }
    public void updateToken(String token) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.UpdateProviderToken(new UpdateTokenViewModel(SharedpreferencesData.getValuePreferences(ProviderMainActivity.this, "providerPhone", ""), token)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private double getAccount() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.ProviderAccountAmount(SharedpreferencesData.getValuePreferences(ProviderMainActivity.this, "providerPhone", "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                amount =Double.parseDouble(response.body().getMessage().toString());
                ok.setText(getAccount()+getResources().getString(R.string.currency));
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderMainActivity.this);
                dialog.dismiss();
            }
        });
        return amount;
    }
    private void getProfile() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.GetProfileProvider(SharedpreferencesData.getValuePreferences(ProviderMainActivity.this, "providerPhone", "")).enqueue(new Callback<Provider>() {
            @Override
            public void onResponse(Call<Provider> call, Response<Provider> response) {
                Provider provider = response.body();


                NameProvider.setText(""+provider.getFullName());
                PhoneNumberProvider.setText(""+provider.getPhone());

                PicassoClient.downloadImage(ProviderMainActivity.this, ApiClient.BASE_URL2 + provider.getImage(), profileImageProvide);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Provider> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ProviderMainActivity.this);
                dialog.dismiss();
            }
        });

    }
    public void openInstgram(View view) {
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
    public void openTwitter(View view) {
        Uri uri = Uri.parse(SharedpreferencesData.getValuePreferences(getApplicationContext(),"twitter",""));
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.twitter.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(""+SharedpreferencesData.getValuePreferences(getApplicationContext(),"twitter",""))));
        }
    }
    public void openSamm(View view) {
        Uri uri = Uri.parse("http://samm.sa/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);


        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://samm.sa/")));
        }
    }
}
