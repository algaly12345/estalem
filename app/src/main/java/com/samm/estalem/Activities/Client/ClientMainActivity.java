package com.samm.estalem.Activities.Client;

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
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.samm.estalem.Activities.About;
import com.samm.estalem.Activities.PermissionToAccess;
import com.samm.estalem.Activities.Policy;
import com.samm.estalem.Activities.SettingActivity;
import com.samm.estalem.Activities.Suggestion;
import com.samm.estalem.Classes.Model.Client;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.UpdateTokenViewModel;
import com.samm.estalem.Database.ArchiveDataSource;
import com.samm.estalem.Database.ArchiveDatabase;
import com.samm.estalem.Database.LocalArchiveDataSource;
import com.samm.estalem.Faraments.ui.client_orders.ClientOrders;
import com.samm.estalem.Faraments.ui.main_order_client_order.MainClientOrderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.ApiClient;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PicassoClient;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientMainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FloatingActionButton imageViewHeader;
    TextView Name,PhoneNumber;
    Dialog dialog;
    ImageView profileImage;
    View headerView;
    double amount=0;
    public static double areaLat = 0;
    public static double areaLog = 0;
    private ArchiveDataSource mCartDataSource;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
      //  startActivity(new Intent(this, SelectDistrict.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_main);
        dialog = ShowDialog.progres(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(getApplication(), R.style.actionBarTitleTextStyle);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        try {
            areaLat = getIntent().getExtras().getDouble("areaLat");
            areaLog = getIntent().getExtras().getDouble("areaLog");
        }catch (Exception e){}

        mCartDataSource = new LocalArchiveDataSource(ArchiveDatabase.getInstance(this).cartDAO());
        profileImage = (ImageView)headerView.findViewById(R.id.imgProfile);
        Name = (TextView)headerView.findViewById(R.id.txtFullName);
        PhoneNumber = (TextView)headerView.findViewById(R.id.txtPhone);
         drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        imageViewHeader=headerView.findViewById(R.id.edit);

        displaySelectedScreen(0);

        imageViewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientMainActivity.this, ClientProfileActivity.class));
            }
        });
        getProfile();
        getToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
                displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showNavBar() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.home_fragment:
                //  fragment =new MainClientOrderFragment(ClientMainActivity.this);
                startActivity(new Intent(this,SelectDistrict.class));
                break;

                case 0:
                  fragment =new MainClientOrderFragment(ClientMainActivity.this);
                    // startActivity(new Intent(this,SelectDistrict.class));
                break;

            case R.id.myOrder:
              fragment =new ClientOrders(this);
                break;


                case R.id.policy:
                    startActivity(new Intent(this, Policy.class));
                break;

            case R.id.my_count:
                final Dialog dialog = new Dialog(this);
                dialog.setCancelable(false);
                //  View view  = context.getLayoutInflater().inflate(R.layout.error_dialog, null);
                dialog.setContentView(R.layout.dilog_your_acount);
                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button ok = dialog.findViewById(R.id.btnMyAccount);
                ok.setText(getAccount()+getResources().getString(R.string.currency));
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            break;

            case R.id.about_as:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.tch_support:
                startActivity(new Intent(this, Suggestion.class));
               break;
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.n_exit:
               SharedpreferencesData.clearSharePreferences(ClientMainActivity.this,"clientPhone");
                clearArchive();
               finish();
                startActivity(new Intent(this, PermissionToAccess.class));

                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        create_post_get.UpdateClientToken(new UpdateTokenViewModel(SharedpreferencesData.getValuePreferences(ClientMainActivity.this, "clientPhone", ""), token)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getProfile() {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.GetProfile(SharedpreferencesData.getValuePreferences(ClientMainActivity.this, "clientPhone", "")).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Client client = response.body();
                Name.setText(client.fullName);
                PhoneNumber.setText(client.phone);
                PicassoClient.downloadImage(ClientMainActivity.this, ApiClient.BASE_URL2 + client.image, profileImage);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ClientMainActivity.this);
                dialog.dismiss();
            }
        });

    }

    private double getAccount(){
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ClientAccountAmount(SharedpreferencesData.getValuePreferences(ClientMainActivity.this, "clientPhone", "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                amount =Double.parseDouble(response.body().getMessage().toString());
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ClientMainActivity.this);
                dialog.dismiss();
            }
        });
        return amount;
    }


    public void clearArchive(){
        mCartDataSource.cleanCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ClientMainActivity.this, "[Archive CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
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