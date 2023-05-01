package com.example.my_last;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface comment_IF {
    @FormUrlEncoded
    @POST("api/comments")
    Call<ResponseBody> saveComment(
            @Field("post_id") int postId,
            @Field("user_id") String userId,
            @Field("content") String content
    );
}