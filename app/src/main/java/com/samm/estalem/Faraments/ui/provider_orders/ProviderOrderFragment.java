package com.samm.estalem.Faraments.ui.provider_orders;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Activities.Provider.ProviderMainActivity;
import com.samm.estalem.Adapters.AdapterProviderOrders;
import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.Faraments.ui.main_provider_fragment.MainProviderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProviderOrderFragment extends Fragment {

    static Dialog dialog;
    static  AdapterProviderOrders adapterProviderOrders;
    private  static     RecyclerView.LayoutManager layoutManager;
    static RecyclerView recyclerView;


    public ProviderOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_order, container, false);
        dialog = ShowDialog.progres(getActivity());
        dialog.show();

        if (SharedpreferencesData.getValuePreferences(getActivity(), "info", "").equals("")) {
            ShowDialog.showErrorDialog(getActivity().getResources().getString(R.string.dialog_details), getActivity().getResources().getString(R.string.info), getActivity());
        }
        SharedpreferencesData.setValuePreferences(getActivity(), "info", "00");
        ImageView button=(ImageView) view.findViewById(R.id.img_nav_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProviderMainActivity)getActivity()).showNavBar();
            }
        });

        ImageView imgBack=(ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainProviderFragment mainProviderFragment=new MainProviderFragment(getActivity());
                FragmentTransaction ft =((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, mainProviderFragment);
                ft.commit();
            }
        });

        recyclerView = view.findViewById(R.id.rycMyOrderProvider);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getMyOrder(getActivity());
        return  view;
    }

    public static void getMyOrder(Activity activity) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetProviderOrders(SharedpreferencesData.getValuePreferences(activity,"providerPhone","")).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                List<Order> orders = response.body();
                adapterProviderOrders = new AdapterProviderOrders(orders, activity);
                recyclerView.setAdapter(adapterProviderOrders);
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
