package com.samm.estalem.Chat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.samm.estalem.R;

import java.io.IOException;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ImageView Image=(ImageView)findViewById(R.id.Image);
      String s=  getIntent().getExtras().getString("image");
        Uri myUri = Uri.parse(s);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image.setImageBitmap(bitmap);
    }
}
