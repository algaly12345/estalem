package com.samm.estalem.Faraments.ui.client_orders;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Adapters.AdapterClientOrders;
import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.Faraments.ui.main_order_client_order.MainClientOrderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientOrders extends Fragment {
    Activity activity;
    static   Dialog dialog;
    static AdapterClientOrders adapterClientOrders;
    static   private RecyclerView.LayoutManager layoutManager;
    static    RecyclerView recyclerView;

    public ClientOrders(Activity Activity) {
        this.activity = Activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_orders, container, false);
        dialog = ShowDialog.progres(getActivity());
        dialog.show();
        ImageView button=(ImageView) view.findViewById(R.id.img_nav_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity)getActivity()).showNavBar();
            }
        });

        ImageView imgBack=(ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainClientOrderFragment offerPriceFragment=new MainClientOrderFragment(activity);
                FragmentTransaction ft =((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, offerPriceFragment);
                ft.commit();
            }
        });

        recyclerView = view.findViewById(R.id.rycMyOrder);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getMyOrder(getActivity());
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static void getMyOrder(Activity activity) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetClientOrders(SharedpreferencesData.getValuePreferences(activity, "clientPhone", "")).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                List<Order> orders = response.body();
                adapterClientOrders = new AdapterClientOrders(orders, activity);
                recyclerView.setAdapter(adapterClientOrders);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                ShowDialog.showErrorDialog(activity.getResources().getString(R.string.sorry),activity.getResources().getString(R.string.no_internet), activity);
                dialog.dismiss();
            }
        });

    }
}
