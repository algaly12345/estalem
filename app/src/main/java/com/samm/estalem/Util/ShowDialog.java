package com.samm.estalem.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.R;


public  class ShowDialog extends AppCompatActivity {
    Dialog dialog;
    Context Context;


    public static void showErrorDialog(String title,String message,Activity context)
    {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);

        //  View view  = context.getLayoutInflater().inflate(R.layout.error_dialog, null);
        dialog.setContentView(R.layout.error_dialog);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        dialog_title.setText(title);
        TextView errorMessage = dialog.findViewById(R.id.TV_errorMessage);
        errorMessage.setText(message);

        Button ok = dialog.findViewById(R.id.Btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Dialog progres(Activity context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }


    public static void showNoInternetDialog(Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);

        //  View view  = context.getLayoutInflater().inflate(R.layout.error_dialog, null);
        dialog.setContentView(R.layout.error_inter_connect);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Button ok = dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

