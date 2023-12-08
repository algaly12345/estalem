package com.samm.estalem.Chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ADMIN on 10/25/2017.
 */

public class ChatConnection {
    public static final String BASE_URL2 ="http://astalem.com:8080/Upload/";
    public Retrofit retrofit;
    public void con_GSON()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://astalem.com:8080/api/")//http://zajelal-sadaa.com/api/
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
            /*
            this example for call function from backend


                    /*******************RetrofitConnection**************************************//*
            Retrofit_Connection retrofit_connection=new Retrofit_Connection();
                    retrofit_connection.con_GSON();
            final CREAT_GET_POST create_post_get=retrofit_connection.retrofit.create(CREAT_GET_POST.class);*/
/******************End*Retrofit**************************************/
