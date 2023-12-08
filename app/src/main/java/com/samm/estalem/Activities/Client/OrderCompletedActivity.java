package com.samm.estalem.Activities.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.samm.estalem.R;
import com.samm.estalem.Util.Util;

public class OrderCompletedActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.language(this);
        setContentView(R.layout.activity_order_completed);


        Button btnOk=(Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderCompletedActivity.this,SelectDistrict.class));
            }
        });
    }
}
