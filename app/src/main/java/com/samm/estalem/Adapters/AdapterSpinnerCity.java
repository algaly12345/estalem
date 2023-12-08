package com.samm.estalem.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Classes.City;
import com.samm.estalem.Classes.Model.District;
import com.samm.estalem.R;

import java.util.List;

public class AdapterSpinnerCity extends  RecyclerView.Adapter<AdapterSpinnerCity.MyViewHolder> implements SpinnerAdapter {

    private List<City> contacts;
    private Context context;

    public AdapterSpinnerCity(List<City> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public AdapterSpinnerCity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_spinner_state, parent, false);
        return new AdapterSpinnerCity.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSpinnerCity.MyViewHolder holder, int position) {
        final City contact =contacts.get(position);
//        holder.NameDistrict.setText(contacts.get(position).getDistrictName());
//        final int day_id=contacts.get(position).getId();
        holder.setItemclickListener(new ItemClicListener() {
            @Override
            public void onItemClick() {
                //    Toast.makeText(context, ""+day_id, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView NameDistrict;


        ItemClicListener itemclickListener;
        public MyViewHolder(View itemView) {
            super(itemView);
            NameDistrict = itemView.findViewById(R.id.idnamemarket);
            itemView.setOnClickListener(this);


        }
        public void setItemclickListener(ItemClicListener ic)

        {
            this.itemclickListener = ic;
        }
        @Override
        public void onClick(View view) {
            this.itemclickListener.onItemClick();
        }

    }
}
