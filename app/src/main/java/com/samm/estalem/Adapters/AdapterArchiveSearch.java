package com.samm.estalem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.samm.estalem.Database.ArchiveDataSource;
import com.samm.estalem.Database.ArchiveDatabase;
import com.samm.estalem.Database.Archiveitem;
import com.samm.estalem.Database.LocalArchiveDataSource;
import com.samm.estalem.Faraments.ui.main_order_client_order.MainClientOrderFragment;
import com.samm.estalem.Faraments.ui.select_order_location.SelectOrderLocation;
import com.samm.estalem.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AdapterArchiveSearch extends RecyclerView.Adapter<AdapterArchiveSearch.MyViewHolder>{

    public static Context mContext;
    public static List<Archiveitem> mCartItemList;
    private ArchiveDataSource mCartDataSource;

    public AdapterArchiveSearch( Context context,List<Archiveitem> contacts) {
        this.mCartItemList = contacts;
        this.mContext = context;
        mCartDataSource = new LocalArchiveDataSource(ArchiveDatabase.getInstance(context).cartDAO());
    }

    @Override
    public AdapterArchiveSearch.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_archive, parent, false);
        return new AdapterArchiveSearch.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterArchiveSearch.MyViewHolder holder, int position) {
        holder.nameSearch.setText(mCartItemList.get(position).getDescription());
        holder.deletArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartDataSource.deleteCart(mCartItemList.get(position))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                notifyItemRemoved(position);
                               // EventBus.getDefault().postSticky(new CalculatePriceEvent());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "[DELETE CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartItemList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameSearch ,deletArchive;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameSearch = itemView.findViewById(R.id.tvItem);
            deletArchive=itemView.findViewById(R.id.edit_Delate_Archive);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            try {
                MainClientOrderFragment.getCameraLocation(new LatLng(mCartItemList.get(getPosition()).getLatitue(), mCartItemList.get(getPosition()).getLongitude()), mCartItemList.get(getPosition()).getDescription());
                SelectOrderLocation.getCameraLocation(new LatLng(mCartItemList.get(getPosition()).getLatitue(), mCartItemList.get(getPosition()).getLongitude()), mCartItemList.get(getPosition()).getDescription());
            } catch (Exception e) {
            }
        }
    }


}
