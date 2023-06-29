package com.example.my_last;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IComment {
    @FormUrlEncoded
    @POST("api/comments")
    Call<ResponseBody> saveComment(
            
            @Field("post_number") int postNumber,
            @Field("user_number") int userNumber,
            @Field("comment_body") String commentBody,
            @Field("before_comment") Integer beforeComment  // nullable
    );

    @DELETE("api/comments/{comment_number}")
    Call<ResponseBody> deleteComment(@Path("comment_number") int commentNumber);


}