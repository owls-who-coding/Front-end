package com.example.my_last;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface post_detail_IF {
//    @GET("api/posts/{post_body_path}/content")
//    Call<String> getTextFileContent(@Path("post_body_path") String postBodyPath);

//기존의 텍스트만 불러오는 코드.
//    @GET("api/posts/{post_body_path}/content")
//    Call<ResponseBody> getTextFileContent(@Path("post_body_path") String postBodyPath);
    @GET("api/posts/{post_body_path}/content_and_image")
    Call<ResponseBody> getPostAndImageContent(@Path("post_body_path") String postBodyPath);
}
