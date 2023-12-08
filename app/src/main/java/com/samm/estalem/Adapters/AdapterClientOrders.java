package com.samm.estalem.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Activities.Client.TrackingProvider;
import com.samm.estalem.Activities.OrderDetailsActivity;
import com.samm.estalem.Chat.ChatActivity;
import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.Classes.Model.ValuationProvider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Faraments.ui.client_orders.ClientOrders;
import com.samm.estalem.Faraments.ui.client_payment_fragment.ClientPaymentFragment;
import com.samm.estalem.Faraments.ui.offer_price.OfferPriceFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterClientOrders extends RecyclerView.Adapter<AdapterClientOrders.MyViewHolder> {

    private List<Order> contacts;
    public static Activity context;
    Dialog dialog;
    public AdapterClientOrders(List<Order> contacts, Activity context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public AdapterClientOrders.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_row, parent, false);
        return new AdapterClientOrders.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterClientOrders.MyViewHolder holder, int position) {
        final Order contact = contacts.get(position);
        dialog = ShowDialog.progres(context);
        holder.tvOrderNumber.setText("#" + contact.id);

        holder.valuation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.statues.equals("1")) {
                    holder.showValuation(contact.providerPhone, contact.getId());
                } else {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.must_be_end_order), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (contact.statues.equals("1")) {
            holder.setOrderEnded(holder.imgOrderAttend, holder.imgOrderEnded, holder.imgOrderInRoad,
                    holder.tvOrderAttend, holder.tvOrderEnded, holder.tvOrderInRoad);
        } else if (contact.statues.equals("4")) {
            holder.setOrderInRoad(holder.imgOrderAttend, holder.imgOrderEnded, holder.imgOrderInRoad,
                    holder.tvOrderAttend, holder.tvOrderEnded, holder.tvOrderInRoad);
        }

        else if (contact.statues.equals("5")) {
            holder.setOrderInAttend(holder.imgOrderAttend, holder.imgOrderEnded, holder.imgOrderInRoad,
                    holder.tvOrderAttend, holder.tvOrderEnded, holder.tvOrderInRoad);
        } else if (contact.statues.equals("3") || contact.statues.equals("2")) {
            holder.btnCancel.setText(context.getResources().getString(R.string.order_canceled));
        }

        holder.imgOfferPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.statues.equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderId", contact.id);
                    OfferPriceFragment offerPriceFragment = new OfferPriceFragment();
                    offerPriceFragment.setArguments(bundle);
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, offerPriceFragment);
                    ft.commit();
                } else {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.order_ok_done), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.statues.equals("5")||contact.statues.equals("0")||contact.statues.equals("6")) {
                    clientCancelOrder(contact.id);
                }else {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.cant_cancel_order), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnGotoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.getPayType().equals("Cash")) {
                    Toast.makeText(context, context.getResources().getString(R.string.payment_is_not_available), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contact.statues.equals("4") || contact.statues.equals("5") || contact.statues.equals("6") || contact.statues.equals("1")) {
                    enterAmount(context, contact.id);
                } else {
                    ClientOrders.getMyOrder(context);
                    Toast.makeText(context, context.getResources().getString(R.string.payment_is_not_available), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgTrackingProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.statues.equals("4"))
                {
                    Intent intent=new Intent(context, TrackingProvider.class);
                    intent.putExtra("orderId",contact.id);
                    intent.putExtra("providerPhone",contact.providerPhone);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.order_not_in_road), Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId",contact.id);
                intent.putExtra("type","client");
                context.startActivity(intent);
            }
        });

        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.statues.equals("4")||contact.statues.equals("5")||contact.statues.equals("6"))
                {
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("phone",contact.providerPhone);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.chat_blocked), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private void clientCancelOrder(int orderId) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ClientCancelOrder(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if(response.body().getMessage().toString().equals("1")) {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.order_canceled), Toast.LENGTH_SHORT).show();
                    ClientOrders.getMyOrder(context);
                }else if(response.body().getMessage().equals("2")){
                ShowDialog.showErrorDialog(context.getResources().getString(R.string.sorry), context.getResources().getString(R.string.cant_cancel_order), context);
                //dialog.dismiss();
            }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(context.getResources().getString(R.string.sorry), context.getResources().getString(R.string.no_internet), context);
                dialog.dismiss();
            }
        });
    }

    public void enterAmount(Activity context, int orderId) {

                ClientPaymentFragment clientPaymentFragment = new ClientPaymentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", orderId);
                clientPaymentFragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, clientPaymentFragment);
                ft.commit();
                dialog.dismiss();
            }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOrderNumber;
        TextView tvOrderAttend;
        TextView tvOrderEnded;
        TextView tvOrderInRoad;
        ImageView imgOrderAttend;
        ImageView imgOrderEnded;
        ImageView imgOrderInRoad;
        Button imgTrackingProvider;
        ImageView imgChat;
        Button imgOfferPrice;
        Button valuation;
        Button btnCancel;
        Button btnDetail;
        Button btnGotoPayment;
        ItemClicListener itemclickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            imgOrderAttend = itemView.findViewById(R.id.imgOrderAttend);
            imgOrderEnded = itemView.findViewById(R.id.imgOrderEnded);
            imgOrderInRoad = itemView.findViewById(R.id.imgOrderInRoad);
            imgOfferPrice = itemView.findViewById(R.id.imgOfferPrice);
            valuation = itemView.findViewById(R.id.btnValuation);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            imgTrackingProvider = itemView.findViewById(R.id.imgTrackingProvider);
            imgChat = itemView.findViewById(R.id.imgChat);
            btnGotoPayment = itemView.findViewById(R.id.btnGotoPayment);
            btnDetail = itemView.findViewById(R.id.btnDetails);

            tvOrderAttend = itemView.findViewById(R.id.tvOrderAttend);
            tvOrderEnded = itemView.findViewById(R.id.tvOrderEnded);
            tvOrderInRoad = itemView.findViewById(R.id.tvOrderInRoad);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
        }

        @SuppressLint("ResourceAsColor")
        public void setOrderInRoad(ImageView imgOrderAttend, ImageView imgOrderEnded, ImageView imgOrderInRood,
                                   TextView tvOrderAttend, TextView tvOrderEnded, TextView tvOrderInRoad) {
            imgOrderAttend.setBackgroundResource(R.drawable.order_state_no_color);
            imgOrderEnded.setBackgroundResource(R.drawable.order_state_no_color);
            imgOrderInRood.setBackgroundResource(R.drawable.order_state_color);
            tvOrderAttend.setTextColor(R.color.gray);
            tvOrderEnded.setTextColor(R.color.gray);
            tvOrderInRoad.setTextColor(R.color.colorPrimaryDark);
        }

        @SuppressLint("ResourceAsColor")
        public void setOrderInAttend(ImageView imgOrderAttend, ImageView imgOrderEnded, ImageView imgOrderInRood,
                                     TextView tvOrderAttend, TextView tvOrderEnded, TextView tvOrderInRoad) {
            imgOrderAttend.setBackgroundResource(R.drawable .order_state_color);
            imgOrderEnded.setBackgroundResource(R.drawable.order_state_no_color);
            imgOrderInRood.setBackgroundResource(R.drawable.order_state_no_color);
            tvOrderAttend.setTextColor(R.color.colorPrimaryDark);
            tvOrderEnded.setTextColor(R.color.gray);
            tvOrderInRoad.setTextColor(R.color.gray);
        }

        @SuppressLint("ResourceAsColor")
        public void setOrderEnded(ImageView imgOrderAttend, ImageView imgOrderEnded, ImageView imgOrderInRood,
                                  TextView tvOrderAttend, TextView tvOrderEnded, TextView tvOrderInRoad) {
            imgOrderAttend.setBackgroundResource(R.drawable.order_state_no_color);
            imgOrderEnded.setBackgroundResource(R.drawable.order_state_color);
            imgOrderInRood.setBackgroundResource(R.drawable.order_state_no_color);
            tvOrderAttend.setTextColor(R.color.gray);
            tvOrderEnded.setTextColor(R.color.colorPrimaryDark);
            tvOrderInRoad.setTextColor(R.color.gray);
        }


        public void showValuation(String providerPhone, int orderId) {
            final Dialog dialog = new Dialog(AdapterClientOrders.context);
            dialog.setCancelable(false);

            //  View view  = context.getLayoutInflater().inflate(R.layout.error_dialog, null);
            dialog.setContentView(R.layout.dialog_valuation_provider);
            Window window = dialog.getWindow();
            assert window != null;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final RatingBar ratingRatingBar = dialog.findViewById(R.id.rating_rating_bar);

            Button ok = dialog.findViewById(R.id.submit_button);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valuation(Double.parseDouble(String.valueOf(ratingRatingBar.getRating())), providerPhone, orderId);

                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        public void valuation(double value, String providerPhone, int orderId) {

            Retrofit_Connection retrofit_connection = new Retrofit_Connection();
            retrofit_connection.con_GSON();
            final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
            create_post_get.ValuationProvider(new ValuationProvider(value, "not", orderId, providerPhone)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    Toast.makeText(AdapterClientOrders.context, AdapterClientOrders.context.getResources().getString(R.string.valuation_done), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ShowDialog.showErrorDialog(AdapterClientOrders.context.getResources().getString(R.string.sorry), AdapterClientOrders.context.getResources().getString(R.string.no_internet), (Activity) AdapterClientOrders.context);
                }
            });

        }
    }
}

