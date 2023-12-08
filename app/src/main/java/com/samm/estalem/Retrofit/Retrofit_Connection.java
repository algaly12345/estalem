package com.samm.estalem.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ADMIN on 10/25/2017.
 */

public class Retrofit_Connection {

    public Retrofit retrofit;
    public void con_GSON()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
                 retrofit = new Retrofit.Builder()
                         .baseUrl("https://astalem.com/api/")//http://zajelal-sadaa.com/api/
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
            /*
            this example for call function from backend
http://192.168.43.147:8080/

                    /*******************RetrofitConnection**************************************//*


            Retrofit_Connection retrofit_connection=new Retrofit_Connection();
                    retrofit_connection.con_GSON();
            final CREATE_GET_POST create_post_get=retrofit_connection.retrofit.create(CREATE_GET_POST.class);


            */
                    /******************End*Retrofit**************************************/
