package com.samm.estalem.Faraments.ui.main_order_client_order;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Adapters.AdapterArchiveSearch;
import com.samm.estalem.Classes.Model.Provider;
import com.samm.estalem.Database.ArchiveDataSource;
import com.samm.estalem.Database.ArchiveDatabase;
import com.samm.estalem.Database.Archiveitem;
import com.samm.estalem.Database.LocalArchiveDataSource;
import com.samm.estalem.Faraments.ui.select_order_location.SelectOrderLocation;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ApiPlaces;
import com.samm.estalem.Util.ShowDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.samm.estalem.R.id.map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainClientOrderFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    static GoogleMap mMap;
    static GoogleMap mMap1;
    List<Provider> providers = new ArrayList<>();
    double x = 0, y = 0;
    Activity activity;
    LatLng userLatLng =new LatLng(-9.419834, -11.375655);
    public static PlacesClient placesClient;
    String address;
    Dialog dialog;
    TextView tvLocationDescription;
    public static RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    private GoogleApiClient googleApiClient;
    ImageView button;
    private RecyclerView.LayoutManager layoutManager1;

    RecyclerView recycler_archive;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private ArchiveDataSource mCartDataSource;

    private LayoutAnimationController mLayoutAnimationController;


    private android.app.AlertDialog mDialog;




    @Override
    public void onDestroyView() {
        mCompositeDisposable.clear();
        super.onDestroyView();
    }

    public MainClientOrderFragment(Activity Activity) {
        this.activity = Activity;

        /*******new Maps*****************/
        if (googleApiClient == null) {

            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
//                    .enableAutoManage(activity, this)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

//*********************end new maps*************/

        googleApiClient.connect();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main_client_order, container, false);

        Button btnNext=(Button)view.findViewById(R.id.btnNext);
        tvLocationDescription = (TextView) view.findViewById(R.id.tvLocationDescription);
        dialog = ShowDialog.progres(getActivity());
        button = (ImageView) view.findViewById(R.id.img_nav_menu);
        userLatLng = new LatLng(-9.419834, -11.375655);

        mCartDataSource = new LocalArchiveDataSource(ArchiveDatabase.getInstance(getActivity()).cartDAO());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity)getActivity()).showNavBar();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderLocation(getActivity());
            }
        });


        initialLace(getActivity());

        recycler_archive =view.findViewById(R.id.recycler_archive);
        mCartDataSource = new LocalArchiveDataSource(ArchiveDatabase.getInstance(getActivity()).cartDAO());
        recycler_archive.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_archive.setLayoutManager(layoutManager);
        recycler_archive.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        getAllItemInCart();
        return view;
    }

    private void getAllItemInCart() {
        mCompositeDisposable.add(mCartDataSource.getAllCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    AdapterArchiveSearch adapter = new AdapterArchiveSearch(getActivity(), cartItems);

                    layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recycler_archive.setLayoutManager(layoutManager1);
                    recycler_archive.setAdapter(adapter);
                    recycler_archive.setLayoutAnimation(mLayoutAnimationController);
                }, throwable -> {
                }));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                userLatLng = mMap.getCameraPosition().target;
                //MarkerOptions currentUserLocation = new MarkerOptions();
                //currentUserLocation.position(userLatLng);
               // mMap.addMarker(new MarkerOptions().position(userLatLng).title(getActivity().getResources().getString(R.string.current_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_locatio_pin)));
//                for (int i = 0; i < providers.size(); i++) {
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(providers.get(i).getLat(), providers.get(i).getLog())).title(providers.get(i).getFullName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_pin)));
//                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
        googleApiClient.disconnect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        /*******كود تحديد الموقع********/
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (userCurrentLocation != null) {
                MarkerOptions currentUserLocation = new MarkerOptions();
                LatLng currentUserLatLang = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                x = userCurrentLocation.getLatitude();
                y = userCurrentLocation.getLongitude();
                currentUserLocation.position(currentUserLatLang);
                setupPlaceAuto();
                userLatLng = currentUserLatLang;
                gatAllProviderNear(x,y);
                //     mMap.addMarker(new MarkerOptions().position(currentUserLatLang).title("موقعك الحلي").icon(BitmapDescriptorFactory.fromResource(R.drawable.abot_as)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 15));



                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        try {
                            address = getResources().getString(R.string.my_location) + ApiPlaces.getAddress(getActivity(), userLatLng.latitude, userLatLng.longitude);
                            tvLocationDescription.setText(address);
                            button.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                        }


                    }
                }.start();


            }
        }
        /*****end**كود تحديد الموقع********/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(activity, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    public void setupPlaceAuto() {
        AutocompleteSupportFragment place_fragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.map_search);
        place_fragment.setPlaceFields(ApiPlaces.fieldList);

//        View view = place_fragment.getView().findViewById(R.id.map_search);
//        view.setX(5);
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(x, y),
                new LatLng(x, y));
        place_fragment.setLocationBias(bounds);


        place_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.clear();
                getLatLog(place.getId(),place.getName());

            }
            @Override
            public void onError(@NonNull Status status) {
             //   Toast.makeText(getContext(), "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void showListPlace(Activity context) {
        Dialog  dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.list_place);

        ApiPlaces apiPlaces = new ApiPlaces(context);

        recyclerView = dialog.findViewById(R.id.lstPlace);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        apiPlaces.getCurrentPlace();
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         dialog.show();
    }


    public static void getCameraLocation(LatLng latLng, String title) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(""+title+""));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    //Arch

    public void getLatLog(String placeId,String s) {
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG)).build();
        placesClient.fetchPlace(request)
                .addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                        if (task.isSuccessful()) {
                            Place place = task.getResult().getPlace();
                            getCameraLocation(place.getLatLng(), s);
                            double lat = place.getLatLng().latitude;
                            double log = place.getLatLng().longitude;
                            String id = placeId;
                            String description = s;
                            ///
                            AddArchive(id,description,lat,log);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static void initialLace(Activity context) {
        com.google.android.libraries.places.api.Places.initialize(context, context.getResources().getString(R.string.google_maps_key));
        placesClient = com.google.android.libraries.places.api.Places.createClient(context);
    }

    public void gatAllProviderNear(double lat,double log) {
        Retrofit_Connection retrofit_connection=new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get=retrofit_connection.retrofit.create(CREATE_GET_POST.class);
           create_post_get.GetNearProvider(lat,log).enqueue(new Callback<List<Provider>>() {
               @Override
               public void onResponse(Call<List<Provider>> call, Response<List<Provider>> response) {
                   providers = response.body();
                   mMap.clear();
                for (int i = 0; i < providers.size(); i++) {
                       mMap.addMarker(new MarkerOptions().position(new LatLng(providers.get(i).getLat(), providers.get(i).getLog())).title(providers.get(i).getFullName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_pin)));
                   }
               }

               @Override
               public void onFailure(Call<List<Provider>> call, Throwable t) {

               }
           });
    }

    public void getOrderLocation(Activity activity) {
        dialog.show();
        try {
            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Userlat", userLatLng.latitude);
                    bundle.putDouble("Userlog", userLatLng.longitude);
                    SelectOrderLocation selectOrderLocation = new SelectOrderLocation(activity);
                    selectOrderLocation.setArguments(bundle);
                    FragmentTransaction ft = ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, selectOrderLocation);
                    ft.commit();
                    dialog.dismiss();
                }
            }.start();
        } catch (Exception e) {
        }

    }

   public void AddArchive(String id,String description ,double lat, double log){
       Archiveitem cartItem = new Archiveitem();
       cartItem.setId(String.valueOf(id));
       cartItem.setDescription(description);
       cartItem.setLatitue(lat);
       cartItem.setLongitude(log);

       mCompositeDisposable.add(mCartDataSource.insertOrReplaceAll(cartItem)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(() -> {
               }, throwable -> {
               }));
   }

}
