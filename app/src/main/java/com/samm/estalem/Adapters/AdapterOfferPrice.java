package com.samm.estalem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Activities.Client.CheckClientPhone;
import com.samm.estalem.Activities.Client.TrackingProvider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.ClientAcceptOfferPriceViewModel;
import com.samm.estalem.Classes.ViewModel.OfferPriceViewModel;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.ApiClient;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.PicassoClient;
import com.samm.estalem.Util.ShowDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterOfferPrice extends RecyclerView.Adapter<AdapterOfferPrice.MyViewHolder> {

    private List<OfferPriceViewModel> contacts;
    private Activity context;

    public AdapterOfferPrice(List<OfferPriceViewModel> contacts, Activity context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public AdapterOfferPrice.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_offers, parent, false);
        return new AdapterOfferPrice.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterOfferPrice.MyViewHolder holder, int position) {
        final OfferPriceViewModel contact = contacts.get(position);

        holder.btnOfferPrice.setText(""+contact.offerPrice);
        holder.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientAcceptOfferPrice(new ClientAcceptOfferPriceViewModel(contact.orderId,contact.providerPhone));
            }
        });
        PicassoClient.downloadImage(context, ApiClient.BASE_URL2+contact.providerImage,holder.imgProviderImage);
        holder.tvProviderCarName.setText(contact.providerCarPlateNumber+"-"+contact.providerCarName);
        holder.tvProviderName.setText(contact.providerName);
        holder.ratingBar.setRating((float)contact.providerValuation);

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button btnOfferPrice;
        Button btnOk;
        ImageView imgProviderImage;
        TextView tvProviderCarName;
        TextView tvProviderName;
        RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnOfferPrice = itemView.findViewById(R.id.btnOfferPrice);
            btnOk = itemView.findViewById(R.id.btnOk);


            tvProviderCarName = itemView.findViewById(R.id.tvProviderCarName);
            tvProviderName = itemView.findViewById(R.id.tvProviderName);
            imgProviderImage = itemView.findViewById(R.id.imgProviderImage);

            ratingBar = itemView.findViewById(R.id.rtProviderValuation);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
        }


    }

    private void clientAcceptOfferPrice(ClientAcceptOfferPriceViewModel clientAcceptOfferPriceViewModel) {
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ClientAcceptOfferPrice(clientAcceptOfferPriceViewModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body().getMessage().equals("1")) {
                    Intent intent = new Intent(context, TrackingProvider.class);
                    intent.putExtra("orderId", clientAcceptOfferPriceViewModel.orderId);
                    intent.putExtra("providerPhone", clientAcceptOfferPriceViewModel.providerPhone);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(context.getResources().getString(R.string.sorry),context.getResources().getString(R.string.no_internet), context);
            }
        });
    }
}

