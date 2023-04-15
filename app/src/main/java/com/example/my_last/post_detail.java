package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class post_detail extends Fragment {

    Button back;
    community community;
    TextView textview;
    ImageView imageView;
    TextView titleview;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_detail, container, false);


        String postBodyPath = getArguments().getString("post_body_path"); // 인텐트가 아닌 번들로부터 데이터를 가져옵니다.
        String postTitle = getArguments().getString("title");




       // back = (Button) view.findViewById(R.id.back_community);
        textview = (TextView) view.findViewById(R.id.post_detail_textview);
        imageView=(ImageView)view.findViewById(R.id.post_detail_image);
        titleview=(TextView) view.findViewById(R.id.post_detail_title);



        // Button 객체 초기화 예시
        Button back = view.findViewById(R.id.back_community);

        // 서버에 요청하여 txt 파일의 내용을 가져옵니다.
        //Retrofit retrofit = RetrofitClient.getClient("https://5e8c-125-133-41-82.jp.ngrok.io/");
        Retrofit retrofit = RetrofitClient.getClient();
        post_detail_IF apiService = retrofit.create(post_detail_IF.class);
        Call<ResponseBody> call = apiService.getPostAndImageContent(postBodyPath);

        Log.d("title", "title: " + postTitle);
        Log.d("DetailPostActivity", "postBodyPath: " + postBodyPath);



//이미지도 함께 불러오고자 새로 작성한 enqueue
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        String content = json.getString("content");
                        titleview.setText(postTitle);
                        textview.setText(content);

                        String image_base64 = json.getString("image_base64");

                        if (!image_base64.isEmpty()) {
                            byte[] decodedString = Base64.decode(image_base64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setVisibility(View.GONE); // 이미지가 없을 경우 ImageView를 숨깁니다.
                        }

                    } catch (JSONException | IOException e) {
                        Log.e("onResponse", "JSON parsing error", e);
                        textview.setText("파일 내용을 가져오는데 실패했습니다.");
                    }
                } else {
                    int statusCode = response.code();
                    Log.d("onResponse", "Response code: " + statusCode + ", message: " + response.message());
                    textview.setText("파일 내용을 가져오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", "Error message: " + t.getMessage());
                textview.setText("통신에 실패하였습니다.");
            }
        });
/*
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.containers, community).commit();
            }
        });
*/
        return view;
    }
}