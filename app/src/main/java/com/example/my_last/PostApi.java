package com.example.my_last;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//문제 없으면 삭제해도 되는 인터페이스
public interface PostApi {
    //이미지 전송을 추가하기 전 코드
//    @FormUrlEncoded
//    @POST("create-post/")
//    Call<Void> createPost(@Field("user_number") int userNumber,
//                          @Field("disease_number") int diseaseNumber,
//                          @Field("text") String text,
//                          @Field("title") String title);
    
    @Multipart
    @POST("create-post/")
    Call<Void> createPost(@Part("user_number") RequestBody userNumber,
                          @Part("disease_number") RequestBody diseaseNumber,
                          @Part("text") RequestBody text,
                          @Part("title") RequestBody title,
                          @Part MultipartBody.Part image);


}