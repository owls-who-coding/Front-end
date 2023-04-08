package com.example.my_last;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface user_ceate_IF {
    @FormUrlEncoded
    @POST("create-post/")
    Call<Void> createPost(@Field("user_number") int userNumber,
                          @Field("disease_number") int diseaseNumber,
                          @Field("text") String text);

}