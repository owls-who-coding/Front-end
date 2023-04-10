package com.example.my_last;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface user_ceate_IF {
    @Multipart
    @POST("create-post/")
    Call<Void> createPost(@Part("user_number") RequestBody userNumber,
                          @Part("disease_number") RequestBody diseaseNumber,
                          @Part("text") RequestBody text,
                          @Part("title") RequestBody title,
                          @Part MultipartBody.Part image);

}