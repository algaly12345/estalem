package com.samm.estalem.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    int orderId;
    Dialog dialog;
    String type;
    Order order;
    TextView AmontPaid , PaymentType,DateTime,NameCustomer,OrderDecrpation,LoctionDescription;
    Button WiteOrder, Ended , CanceledFromProvider,CanecedFromClient, OrderInRoad,AttedOrder,OkOrder ;
    int drawable = R.drawable.button_provider;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_order_details);
        orderId = getIntent().getExtras().getInt("orderId");
        type = getIntent().getExtras().getString("type");

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(getApplication(), R.style.actionBarTitleTextStyle);
        setSupportActionBar(toolbar);
        if (type.equals("client")) {
            drawable = R.drawable.primary_button;
            toolbar.setBackgroundColor(Color.parseColor("#78A2CC"));
        }
        dialog = ShowDialog.progres(this);
        getOrderDetails(orderId);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        AmontPaid=findViewById(R.id.txtAmountPaid);
        PaymentType=findViewById(R.id.txtPaymenttype);
        DateTime=findViewById(R.id.txtDatetimeNow);
        NameCustomer=findViewById(R.id.txtnamecustomer);
        OrderDecrpation=findViewById(R.id.txtorderDecreption);
        LoctionDescription=findViewById(R.id.txtORderLoction);
        //Order Status
        WiteOrder=findViewById(R.id.btnOrder_wite);
        Ended=findViewById(R.id.btn_ended);
        CanceledFromProvider=findViewById(R.id.btn_caneceledprovider);
        CanecedFromClient=findViewById(R.id.btn_cancel_from_client);
        OrderInRoad=findViewById(R.id.btn_order_in_road);
        AttedOrder=findViewById(R.id.btn_attendord);
        OkOrder = findViewById(R.id.btn_ok_order);
    }

    public void getOrderDetails(int orderId) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.GetOrderDetails(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                if(response.body()!=null){
                    order=response.body();
                    AmontPaid.setText(""+order.amountPayed);

                    DateTime.setText(""+order.dateTimeNow);
                    NameCustomer.setText(""+order.client.fullName);
                    OrderDecrpation.setText(""+order.orderDescription);
                    LoctionDescription.setText(""+order.locationDescription);


                    if (order.payType.equals("Cash")) {
                        PaymentType.setText(getResources().getString(R.string.cash));
                    } else {
                        PaymentType.setText(getResources().getString(R.string.electronic_payment));
                    }

                    if (order.statues.equals("0")) {
                        WiteOrder.setBackgroundResource(drawable);

                    }else if (order.statues.equals("1")){
                        Ended.setBackgroundResource(drawable);

                    }else if (order.statues.equals("2")){
                        CanceledFromProvider.setBackgroundResource(drawable);
                    }else if (order.statues.equals("3")){
                        CanecedFromClient.setBackgroundResource(drawable);
                    }else if (order.statues.equals("4")){
                        OrderInRoad.setBackgroundResource(drawable);
                    }else if (order.statues.equals("5")){
                        AttedOrder.setBackgroundResource(drawable);
                    }
                    else if (order.statues.equals("6")){
                        OkOrder.setBackgroundResource(drawable);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                dialog.dismiss();
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), OrderDetailsActivity.this);
            }
        });
    }
}
