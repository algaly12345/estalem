package com.samm.estalem.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Classes.ApiPlaceModel;
import com.samm.estalem.Faraments.ui.select_order_location.SelectOrderLocation;
import com.samm.estalem.R;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {

    private List<ApiPlaceModel> contacts;
    private Activity context;

    public PlaceAdapter(Activity context, List<ApiPlaceModel> apiPlaceModel) {
        this.contacts = apiPlaceModel;
        this.context = context;
    }

    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_place, parent, false);
        return new PlaceAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PlaceAdapter.MyViewHolder holder, int position) {
        final ApiPlaceModel contact = contacts.get(position);
        holder.tvPlaceName.setText("" + contact.placeName);
        holder.lnrGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                  //MainClientOrderFragment.getCameraLocation(contact.latLng,contact.placeName);
                    SelectOrderLocation.getCameraLocation(contact.latLng,contact.placeName);
                   SelectOrderLocation.dialogs.dismiss();


            }
        });

    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tvPlaceName;
        LinearLayout lnrGetLocation;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            lnrGetLocation = itemView.findViewById(R.id.lnrGetLocation);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
        }


    }
}

