package com.samm.estalem.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.R;


public class showWindow {


    public static void showNotActive(Context context){

        Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);

        //  View view  = context.getLayoutInflater().inflate(R.layout.error_dialog, null);
        dialog.setContentView(R.layout.exo_player_view);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    public int  enterAmount(Activity context){

        Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);

        //  View view  = context.getLayoutInflater().inflate(R.layout.error_dialog, null);
        dialog.setContentView(R.layout.set_amount);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        dialog.show();

return 1;



    }

}
