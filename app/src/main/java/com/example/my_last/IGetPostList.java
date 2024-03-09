package com.example.my_last;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetPostList {
    @GET("post/")
    Call<List<Post>> getPosts();
}




