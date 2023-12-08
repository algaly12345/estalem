package com.samm.estalem.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Activities.Provider.DirectionToClient;
import com.samm.estalem.Activities.OrderDetailsActivity;
import com.samm.estalem.Activities.Provider.InvoiceActivity;
import com.samm.estalem.Chat.ChatActivity;
import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Faraments.ui.provider_orders.ProviderOrderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Retrofit.Retrofit_Connection;
import com.samm.estalem.Util.ShowDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterProviderOrders extends RecyclerView.Adapter<AdapterProviderOrders.MyViewHolder> {

    private List<Order> contacts;
    public static Activity context;
    Dialog dialog;
    public AdapterProviderOrders(List<Order> contacts, Activity context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public AdapterProviderOrders.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_row_provider, parent, false);
        return new AdapterProviderOrders.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterProviderOrders.MyViewHolder holder, int position) {
        final Order contact = contacts.get(position);
        holder.tvOrderNumber.setText("#" + contact.id);
        dialog = ShowDialog.progres(context);


        if (contact.statues.equals("1")) {
            holder.setOrderEnded(holder.imgOrderAttend, holder.imgOrderEnded, holder.imgOrderInRoad,
                    holder.tvOrderAttend, holder.tvOrderEnded, holder.tvOrderInRoad);
        } else if (contact.statues.equals("4")) {
            holder.setOrderInRoad(holder.imgOrderAttend, holder.imgOrderEnded, holder.imgOrderInRoad,
                    holder.tvOrderAttend, holder.tvOrderEnded, holder.tvOrderInRoad);
        } else if (contact.statues.equals("5")) {
            holder.setOrderInAttend(holder.imgOrderAttend, holder.imgOrderEnded, holder.imgOrderInRoad,
                    holder.tvOrderAttend, holder.tvOrderEnded, holder.tvOrderInRoad);
        } else if (contact.statues.equals("3") || contact.statues.equals("2")) {
            holder.btnCancel.setText(context.getResources().getString(R.string.order_canceled));
        }

        holder.imgOrderEnded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.statues.equals("4")) {
                    holder.imgOrderEnded.setBackgroundResource(R.drawable.order_state_colorprovider);
                    holder.imgOrderInRoad.setBackgroundResource(R.drawable.order_state_no_color);
                    holder.imgOrderAttend.setBackgroundResource(R.drawable.order_state_no_color);
                    holder.imgOrderInRoad.setEnabled(false);
                    holder.imgOrderAttend.setEnabled(false);
                    ChangOrderEnded(contact.id);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.order_not_in_road), Toast.LENGTH_SHORT).show();

                }
            }
        });
        holder.gotoOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.statues.equals("4")) {
                    openDetailsActivity(contact.getId());
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.order_not_in_road), Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.statues.equals("5") || contact.statues.equals("6")|| contact.statues.equals("0") ) {
                    providerCancelOrder(contact.id);
                } else {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.cant_cancel_order), Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.imgOrderInRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.statues.equals("5")) {
                    holder.imgOrderInRoad.setBackgroundResource(R.drawable.order_state_colorprovider);
                    holder.imgOrderAttend.setBackgroundResource(R.drawable.order_state_no_color);
                    holder.imgOrderEnded. setBackgroundResource(R.drawable.order_state_no_color);
                    holder.tvOrderAttend.setEnabled(false);
                    holder.imgOrderEnded.setEnabled(false);
                    ChangOrderToInRoad(contact.getId());
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.order_not_attend), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contact.statues.equals("0")) {
                    Intent intent = new Intent(context, InvoiceActivity.class);
                    intent.putExtra("orderId", contact.getId());
                    context.startActivity(intent);
                }
            }
        });

        holder.imgOrderAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.statues.equals("6")) {
                        holder.imgOrderAttend.setBackgroundResource(R.drawable.order_state_colorprovider);
                        holder.imgOrderInRoad.setBackgroundResource(R.drawable.order_state_no_color);
                        holder.imgOrderEnded.setBackgroundResource(R.drawable.order_state_no_color);
                        holder.imgOrderInRoad.setEnabled(false);
                        holder.imgOrderEnded.setEnabled(false);
                        ChangeOrderToAttend(contact.id);
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.order_not_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId",contact.id);
                intent.putExtra("type","provider");
                context.startActivity(intent);
            }
        });


        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contact.statues.equals("6") || contact.statues.equals("5") || contact.statues.equals("4")) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("phone",contact.clientPhone);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.order_not_ok), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private void openDetailsActivity(int order_id) {
        Intent i = new Intent(context, DirectionToClient.class);
        i.putExtra("orderId", order_id);
        context.startActivity(i);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOrderNumber;
        TextView tvOrderAttend;
        TextView tvOrderEnded;
        TextView tvOrderInRoad;
        ImageView imgOrderAttend;
        ImageView imgOrderEnded;
        ImageView imgOrderInRoad;
        ImageView imgChat;
        Button gotoOrder;
        Button btnCancel;
        Button btnDetails;
        Button btnInvoice;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumberProvider);
            imgOrderAttend = itemView.findViewById(R.id.imgOrderAttendProvider);
            imgOrderEnded = itemView.findViewById(R.id.imgOrderEndedProvider);
            imgOrderInRoad = itemView.findViewById(R.id.imgOrderInRoadProvider);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            gotoOrder = itemView.findViewById(R.id.imgTrackingProvider);
            imgChat = itemView.findViewById(R.id.imgChat);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnInvoice = itemView.findViewById(R.id.btnInvoice);

            tvOrderAttend = itemView.findViewById(R.id.tvOrderAttendProvider);
            tvOrderEnded = itemView.findViewById(R.id.tvOrderEndedProvider);
            tvOrderInRoad = itemView.findViewById(R.id.tvOrderInRoadProvider);
          //  ButnConfir=itemView.findViewById(R.id.confirm_a);
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
            imgOrderInRood.setBackgroundResource(R.drawable.order_state_colorprovider);
            tvOrderAttend.setTextColor(R.color.gray);
            tvOrderEnded.setTextColor(R.color.gray);
            tvOrderInRoad.setTextColor(R.color.colorPrimaryDarkProvider);
        }

        @SuppressLint("ResourceAsColor")
        public void setOrderInAttend(ImageView imgOrderAttend, ImageView imgOrderEnded, ImageView imgOrderInRood,
                                     TextView tvOrderAttend, TextView tvOrderEnded, TextView tvOrderInRoad) {
            imgOrderAttend.setBackgroundResource(R.drawable .order_state_colorprovider);
            imgOrderEnded.setBackgroundResource(R.drawable.order_state_no_color);
            imgOrderInRood.setBackgroundResource(R.drawable.order_state_no_color);
            tvOrderAttend.setTextColor(R.color.colorPrimaryDarkProvider);
            tvOrderEnded.setTextColor(R.color.gray);
            tvOrderInRoad.setTextColor(R.color.gray);
        }

        @SuppressLint("ResourceAsColor")
        public void setOrderEnded(ImageView imgOrderAttend, ImageView imgOrderEnded, ImageView imgOrderInRood,
                                  TextView tvOrderAttend, TextView tvOrderEnded, TextView tvOrderInRoad) {
            imgOrderAttend.setBackgroundResource(R.drawable.order_state_no_color);
            imgOrderEnded.setBackgroundResource(R.drawable.order_state_colorprovider);
            imgOrderInRood.setBackgroundResource(R.drawable.order_state_no_color);
            tvOrderAttend.setTextColor(R.color.gray);
            tvOrderEnded.setTextColor(R.color.colorPrimaryDarkProvider);
            tvOrderInRoad.setTextColor(R.color.gray);
        }

    }

    private void providerCancelOrder(int orderId) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ProviderCancelOrder(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("1")) {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.order_canceled), Toast.LENGTH_SHORT).show();
                    ProviderOrderFragment.getMyOrder(context);
                    //dialog.dismiss();
                } else if(response.body().getMessage().equals("2")){
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

    public void ChangeOrderToAttend(int order) {
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ChangOrderToAttend(order).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                ProviderOrderFragment.getMyOrder(context);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(context.getResources().getString(R.string.sorry), context.getResources().getString(R.string.cant_cancel_order), context);
                dialog.dismiss();
            }
        });

    }

    public void ChangOrderEnded (int order){
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ChangOrderEnded(order).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProviderOrderFragment.getMyOrder(context);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(context.getResources().getString(R.string.sorry),t.getMessage(), context);
                Log.v("S","cccccccccccccccccccccccccccccccccccccccccccccccccccc"+t.getMessage());

                dialog.dismiss();
            }
        });

    }

    public void ChangOrderToInRoad (int order){
        dialog.show();
        Retrofit_Connection retrofit_connection = new Retrofit_Connection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.ChangOrderToInRoad(order).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProviderOrderFragment.getMyOrder(context);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(context.getResources().getString(R.string.sorry), context.getResources().getString(R.string.cant_cancel_order), context);
                dialog.dismiss();
            }
        });

    }
}

