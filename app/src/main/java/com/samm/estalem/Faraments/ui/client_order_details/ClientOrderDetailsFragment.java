package com.samm.estalem.Faraments.ui.client_order_details;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Activities.Client.OrderCompletedActivity;
import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ApiPlaces;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClientOrderDetailsFragment extends Fragment {
    Dialog dialog;
    double userLat;
    double userLog;
    double orderLat;
    double orderLog;
    RadioButton rdCash,rdElectronic;
    String payTyp="OnLine";
    public ClientOrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Util.language(getActivity());

        View view = inflater.inflate(R.layout.fragment_order_detalis, container, false);

        EditText txtDescriptionLocation = (EditText) view.findViewById(R.id.txtDescriptionLocation);
        EditText txtDescriptionOrder = (EditText) view.findViewById(R.id.txtDescriptionOrder);

        rdCash=(RadioButton)view.findViewById(R.id.rdCash);
        rdElectronic=(RadioButton)view.findViewById(R.id.rdElectronic);
        changePayType();
        dialog = ShowDialog.progres(getActivity());
        userLat = getArguments().getDouble("userLat");
        userLog = getArguments().getDouble("userLog");
        orderLat = getArguments().getDouble("orderLat");
        orderLog = getArguments().getDouble("orderLog");
        try {
            txtDescriptionLocation.setText(""+ ApiPlaces.getAddress(getActivity(),orderLat,orderLog).replace(":"," "));
        }catch (Exception e){}


        Button btnGotoPayment=(Button)view.findViewById(R.id.btnGotoPayment);
        Button cancel = (Button) view.findViewById(R.id.btnCancel);
        btnGotoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtDescriptionLocation.getText().toString().isEmpty()) {
                    txtDescriptionLocation.setError(getResources().getString(R.string.empty));
                    return;
                }

                if (txtDescriptionOrder.getText().toString().isEmpty()) {
                    txtDescriptionOrder.setError(getResources().getString(R.string.empty));
                    return;
                }

                addOrder(new Order("8/8/2000", 0, "No Note", payTyp, 0, "0", "0",
                        0, userLat, userLog, orderLat, orderLog,
                        SharedpreferencesData.getValuePreferences(getActivity(), "clientPhone", ""),
                        txtDescriptionLocation.getText().toString(), txtDescriptionOrder.getText().toString()));


                // getOrderLocation(getActivity(), txtDescriptionLocation.getText().toString(), txtDescriptionOrder.getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ClientMainActivity.class));
            }
        });

        ImageView button = (ImageView) view.findViewById(R.id.img_nav_menu);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity) getActivity()).showNavBar();
            }
        });
        return view;
    }

    private void changePayType(){
        rdElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payTyp="OnLine";
            }
        });


        rdCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payTyp="Cash";
            }
        });
    }

    private void addOrder(Order order) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.AddOrder(order).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                startActivity(new Intent(getContext(), OrderCompletedActivity.class));
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), getActivity());
                dialog.dismiss();
            }
        });


    }



}
