package com.example.my_last;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MainActivityIF {

    @FormUrlEncoded
    @POST("login/")
    Call<JsonObject> login(@Field("id") String id, @Field("password") String password);

}
