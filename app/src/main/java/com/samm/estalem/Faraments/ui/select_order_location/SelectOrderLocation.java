package com.samm.estalem.Faraments.ui.select_order_location;


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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Adapters.AdapterArchiveSearch;
import com.samm.estalem.Adapters.PlaceAdapter;
import com.samm.estalem.Classes.ApiPlaceModel;
import com.samm.estalem.Database.ArchiveDataSource;
import com.samm.estalem.Database.ArchiveDatabase;
import com.samm.estalem.Database.Archiveitem;
import com.samm.estalem.Database.LocalArchiveDataSource;
import com.samm.estalem.Faraments.ui.client_order_details.ClientOrderDetailsFragment;
import com.samm.estalem.R;
import com.samm.estalem.Util.ApiPlaces;
import com.samm.estalem.Util.ShowDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOrderLocation extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    static GoogleMap mMap;
    double x = 0, y = 0;
    Activity activity;
    public static Dialog dialogs;
    static PlaceAdapter placeAdapter;
    static List<ApiPlaceModel> apiPlaceModels = new ArrayList<>();
    public static List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
    static Dialog dialog;
    TextView tvLocationDescription;
    private GoogleApiClient googleApiClient;
    double userLat;
    double userLog;
    LatLng orderLatLng =new LatLng(-9.419834, -11.375655);



    private RecyclerView.LayoutManager layoutManager1;
    RecyclerView recycler_archive;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private ArchiveDataSource mCartDataSource;
    private LayoutAnimationController mLayoutAnimationController;
    public static RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    public static PlacesClient placesClient;





    public SelectOrderLocation(Activity Activity) {
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
        View view= inflater.inflate(R.layout.fragment_select_order_location, container, false);

        ImageView button=(ImageView) view.findViewById(R.id.img_nav_menu);
        Button btnNext = (Button) view.findViewById(R.id.btnNext);
        tvLocationDescription = (TextView) view.findViewById(R.id.tvLocationDescription);
        mCartDataSource = new LocalArchiveDataSource(ArchiveDatabase.getInstance(getActivity()).cartDAO());
        recycler_archive =view.findViewById(R.id.recycler_archive);


        dialog = ShowDialog.progres(getActivity());

        userLat = getArguments().getDouble("Userlat");
        userLog = getArguments().getDouble("Userlog");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity)getActivity()).showNavBar();
            }
        });


        ImageView imgCurrentPlace = (ImageView) view.findViewById(R.id.imgCurrentPlace);
        imgCurrentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiPlaceModels.clear();
                Dialog dialog1 = showListPlace(getActivity());
                dialog1.show();

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderDetails(getActivity());
            }
        });

        mCartDataSource = new LocalArchiveDataSource(ArchiveDatabase.getInstance(getActivity()).cartDAO());
        recycler_archive.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_archive.setLayoutManager(layoutManager);
        recycler_archive.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        getAllItemInCart();

        initialLace(getActivity());


        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            String address = ApiPlaces.getAddress(getActivity(), orderLatLng.latitude, orderLatLng.longitude);
                            tvLocationDescription.setText(address);
                            setupPlaceAuto();
                        } catch (Exception e) {
                        }


                    }
                }.start();

            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //mMap.clear();
                orderLatLng = mMap.getCameraPosition().target;
             //   MarkerOptions currentUserLocation = new MarkerOptions();
               // currentUserLocation.position(orderLatLng);
                //mMap.addMarker(new MarkerOptions().position(orderLatLng).title(getActivity().getResources().getString(R.string.order_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.order_location_pin)));
//                for (int i = 0; i < apiPlaceModels.size(); i++) {
//                    mMap.addMarker(new MarkerOptions().position(apiPlaceModels.get(i).latLng).title(apiPlaceModels.get(i).placeName).icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_pin)));
//                }

            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
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
                orderLatLng =new LatLng(-9.419834, -11.375655);
                orderLatLng = currentUserLatLang;
                //     mMap.addMarker(new MarkerOptions().position(currentUserLatLang).title("موقعك الحلي").icon(BitmapDescriptorFactory.fromResource(R.drawable.abot_as)));

                if (ClientMainActivity.areaLat == 0 && ClientMainActivity.areaLog == 0) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(x, y), 15));
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ClientMainActivity.areaLat, ClientMainActivity.areaLog), 15));
                }

                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            String address = ApiPlaces.getAddress(getActivity(), orderLatLng.latitude, orderLatLng.longitude);
                            tvLocationDescription.setText(address);
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
    public void getLatLogToSearch(String placeId,String s) {
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

                            AddArchive(id,description,lat,log);


                        }//
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
    public static void getCameraLocation(LatLng latLng, String title) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(""+title+""));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
    public void setupPlaceAuto() {
        AutocompleteSupportFragment place_fragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.map_search);
        place_fragment.setPlaceFields(ApiPlaces.fieldList);
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(orderLatLng.latitude, orderLatLng.longitude),
                new LatLng(orderLatLng.latitude, orderLatLng.longitude));
        place_fragment.setLocationBias(bounds);



        place_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.clear();

                getLatLogToSearch(place.getId(),place.getName());

            }
            @Override
            public void onError(@NonNull Status status) {
               // Toast.makeText(getContext(), "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getOrderDetails(Activity activity) {
        dialog.show();
        try {
            new CountDownTimer(3000,1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("userLat", userLat);
                    bundle.putDouble("userLog", userLog);
                    bundle.putDouble("orderLat", orderLatLng.latitude);
                    bundle.putDouble("orderLog", orderLatLng.longitude);
                    ClientOrderDetailsFragment selectOrderLocation = new ClientOrderDetailsFragment();
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
    public static Dialog showListPlace(Activity context) {
        dialogs = new Dialog(context);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.list_place);

        recyclerView = dialogs.findViewById(R.id.lstPlace);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        getCurrentPlace(context);
        Window window = dialogs.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialogs;
    }
    public static void getCurrentPlace(Activity activity) {
        dialog.show();
        final FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(fieldList).build();
        @SuppressLint("MissingPermission") Task<FindCurrentPlaceResponse> placeResponseTask = placesClient.findCurrentPlace(request);
        placeResponseTask.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (int i = 0; i < response.getPlaceLikelihoods().size(); i++) {
                        PlaceLikelihood places = response.getPlaceLikelihoods().get(i);
                        //   apiPlaceModels.add(new ApiPlaceModel(places.getPlace().getName(), places.getPlace().getId(), places.getPlace().getLatLng()));
                        getLatLogToGetPlace(activity,places.getPlace().getId(),places.getPlace().getName());
                    }
                    dialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public static void getLatLogToGetPlace(Activity activity,String placeId,String Name) {
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG)).build();
        placesClient.fetchPlace(request)
                .addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                        if (task.isSuccessful()) {
                            Place places = task.getResult().getPlace();
                            apiPlaceModels.add(new ApiPlaceModel(Name, places.getId(), places.getLatLng()));
                        }
                        placeAdapter = new PlaceAdapter(activity,apiPlaceModels);
                        recyclerView.setAdapter(placeAdapter);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //   Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public void onDestroyView() {
        mCompositeDisposable.clear();
        super.onDestroyView();
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
