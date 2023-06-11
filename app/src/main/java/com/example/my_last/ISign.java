package com.example.my_last;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISign {
    @FormUrlEncoded
    @POST("sign_up/")
    Call<JsonObject> sign_up(@Field("id") String id,
                             @Field("password") String password,
                             @Field("name") String name,
                             @Field("age") int age,
                             @Field("dog_name") String dog_name);
}