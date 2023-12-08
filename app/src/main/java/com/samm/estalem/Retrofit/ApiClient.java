package com.samm.estalem.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//79.170.40.235 http://79.170.40.235
public class ApiClient {
   // public static final String BASE_URL = "http://sunrise-schools.app/schoolwebservice/";
   public static final String BASE_URL = "https://astalem.com/api/";

    public static final String BASE_URL2 ="https://astalem.com/Upload/";

    //public static final String BASE_URL = "http://primary.schools-gate.com/schoolwebservice/";
    public static Retrofit retrofit;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                .setLenient()
                .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();



        }
        return retrofit;
    }

}