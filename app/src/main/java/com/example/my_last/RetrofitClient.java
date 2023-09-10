package com.example.my_last;


import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    
    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        Log.d("error",BASE_URL);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}



