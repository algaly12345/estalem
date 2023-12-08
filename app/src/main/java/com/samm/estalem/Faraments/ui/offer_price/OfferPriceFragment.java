package com.samm.estalem.Faraments.ui.offer_price;


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
import com.samm.estalem.Adapters.AdapterOfferPrice;
import com.samm.estalem.Classes.ViewModel.OfferPriceViewModel;
import com.samm.estalem.Faraments.ui.client_orders.ClientOrders;
import com.samm.estalem.Faraments.ui.main_order_client_order.MainClientOrderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfferPriceFragment extends Fragment {
    Dialog dialog;
    AdapterOfferPrice adapterOfferPrice;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    int orderId;

    public OfferPriceFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_price, container, false);
        dialog = ShowDialog.progres(getActivity());
        dialog.show();
        ImageView button = (ImageView) view.findViewById(R.id.img_nav_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity) getActivity()).showNavBar();
            }
        });

        ImageView imgBack=(ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientOrders clientOrders=new ClientOrders(getActivity());
                FragmentTransaction ft = ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, clientOrders);
                ft.commit();
            }
        });

        recyclerView = view.findViewById(R.id.rycMyOrder);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        orderId =getArguments().getInt("orderId");

        getOffer(orderId);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getOffer(int orderId) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetOfferPrice(orderId).enqueue(new Callback<List<OfferPriceViewModel>>() {
            @Override
            public void onResponse(Call<List<OfferPriceViewModel>> call, Response<List<OfferPriceViewModel>> response) {
                List<OfferPriceViewModel> offerPriceViewModels = response.body();
                adapterOfferPrice = new AdapterOfferPrice(offerPriceViewModels, getActivity());
                recyclerView.setAdapter(adapterOfferPrice);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<OfferPriceViewModel>> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), getActivity());
                dialog.dismiss();
            }
        });
    }
}
