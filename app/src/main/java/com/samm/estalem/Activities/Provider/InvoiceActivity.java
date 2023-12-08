package com.samm.estalem.Activities.Provider;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.AddPriceToOrderViewModel;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity {
    double deliveryPrice = 0;
    Dialog dialog;
    EditText txtDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        dialog = ShowDialog.progres(this);
        EditText txtTotal = (EditText) findViewById(R.id.txtTotal);
        EditText txtOrderPrice = (EditText) findViewById(R.id.txtOrderPrice);
        txtDelivery = (EditText) findViewById(R.id.txtDelivery);
        Button btnAddOrderPrice = (Button) findViewById(R.id.btnAddOrderPrice);


        btnAddOrderPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtOrderPrice.getText().toString().isEmpty()) {
                    txtOrderPrice.setError(getResources().getString(R.string.empty));
                    dialog.dismiss();
                    return;
                }
                AddPriceToOrder(new AddPriceToOrderViewModel(getIntent().getIntExtra("orderId",0),Double.parseDouble(txtTotal.getText().toString())));
            }
        });

        getDeliveryPrice(getIntent().getIntExtra("orderId", 0));

        txtOrderPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    txtTotal.setText("" + (Double.parseDouble(txtOrderPrice.getText().toString()) + deliveryPrice));
                } catch (Exception e) {
                    txtTotal.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getDeliveryPrice(int orderId) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);

        create_post_get.GetDelivery(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    deliveryPrice = Double.parseDouble(response.body().getMessage() + "");
                    txtDelivery.setText(deliveryPrice + "");
                    dialog.dismiss();
                }catch (Exception e)
                {
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), InvoiceActivity.this);
                dialog.dismiss();
                finish();
            }
        });
    }

    public void AddPriceToOrder(AddPriceToOrderViewModel addPriceToOrderViewModel) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.AddPriceToOrder(addPriceToOrderViewModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if(response.body().getMessage().equals("1")){
                   Toast.makeText(InvoiceActivity.this, ""+getResources().getString(R.string.invoice_sended), Toast.LENGTH_SHORT).show();
                   dialog.dismiss();
                   finish();

               }else {
                   dialog.dismiss();
                   Toast.makeText(InvoiceActivity.this, ""+getResources().getString(R.string.invoice_no_sended), Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), InvoiceActivity.this);
                dialog.dismiss();
                finish();
            }
        });
    }
}