package com.example.my_last;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface post_detail_IF {
// 기존의 이미와 텍스트를 불러와 게시글을 출력하는 인터페이스. 만약 대댓글 불러오기 실패하면 다시 이하 코드를 사용
    // 코드를 원상복귀 시 이하의 코드의 주석을 해제할 뿐 아니라, post_detail코드도 원상 복구. 서버의 기존의 view에 맞게 url도 변경.
//    @GET("api/posts/{post_body_path}/content_and_image")
//    Call<ResponseBody> getPostAndImageContent(@Path("post_body_path") String postBodyPath);
// 수정된 게시글 및 이미지, 댓글 및 대댓글 정보를 함께 반환하는 URL
      @GET("api/posts/{post_body_path}/{post_number}/content_and_image")
      Call<ResponseBody> getPostAndImageContentWithComments(@Path("post_body_path") String postBodyPath, @Path("post_number") int postNumber);
}
