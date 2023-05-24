package com.example.my_last;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListViewAdapter extends ArrayAdapter<Post> {
    private Retrofit retrofit;
    private String content;
    private String image;

    View parent_view;
    public ListViewAdapter(Context context, List<Post> posts, View view) {
        super(context, 0, posts);
        parent_view = view;
    }

    ConstraintLayout layout_loading;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_item, parent, false);
        }

        Post post = getItem(position);
        Log.d("ListViewAdapter", "Post: " + post.toString());
       // User user = post.getUser(); // 수정된 부분

        layout_loading = parent_view.findViewById(R.id.layout_community_parent);

        // TextView postBodyPathLabel = convertView.findViewById(R.id.path_invisible);// 보이지 않는 텍스트뷰 하나 더 만들어야함
        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView tv_tag = (TextView) convertView.findViewById(R.id.tag);
        TextView tv_nickname = (TextView) convertView.findViewById(R.id.nickname);
        TextView tv_upload_date = (TextView)convertView.findViewById(R.id.upload_date);


        //TextView tv_comment = (TextView)convertView.findViewById(R.id.comment);
        TextView tv_title = (TextView)convertView.findViewById(R.id.title);
        Button tv_button=(Button) convertView.findViewById(R.id.comment);

        tv_tag.setText(String.valueOf(post.getDiseaseNumber()));

        tv_nickname.setText(post.getName());
        tv_upload_date.setText(post.getUpdatedAt());
        String buttonText = String.valueOf(post.getCommentCount());
        tv_button.setText(buttonText);

        int userNumber=post.getUserNumber();// 게시글 작성자 번호를 보내기 위한 코드
        tv_title.setText(post.getTitle());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Post selectedItem = getItem(position);
                String postBodyPath = selectedItem.getPostBodyPath();
                String title=selectedItem.getTitle();
                int postNumber = selectedItem.getPostNumber();

                layout_loading.setVisibility(View.VISIBLE);


                retrofit = RetrofitClient.getClient();// Retrofit 객체를 전역으로 뻄.
                post_detail_IF apiService = retrofit.create(post_detail_IF.class);

                Call<ResponseBody> call = apiService.getPostAndImageContent(postBodyPath);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // 게시글 내용과 이미지를 처리하는 코드
                        if (response.isSuccessful()) {
                            try {
                                String jsonString = response.body().string();
                                Log.d("onResponseForPost", "JSON response: " + jsonString);
                                JSONObject json = new JSONObject(jsonString);

                                content = json.getString("content");
                                image = json.optString("image_base64", "");

                                post_detail postDetailFragment = new post_detail();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", title);
                                bundle.putInt("post_number", postNumber);
                                bundle.putString("content", content);
                                bundle.putString("image", image);
                                bundle.putInt("user_Number", userNumber);// userNumber를 전달시도
                                Log.d("usernumber", "JSON response: " + userNumber);
                                postDetailFragment.setArguments(bundle);

                                layout_loading.setVisibility(View.INVISIBLE);

                                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.containers, postDetailFragment).addToBackStack(null).commit(); // 변수명 변경 및 백스택 추가

                            } catch (JSONException | IOException e) {
                                Log.e("onResponseForPost", "JSON parsing error", e);

                            }
                        } else {
                            int statusCode = response.code();
                            Log.d("onResponseForPost", "Response code: " + statusCode + ", message: " + response.message());

                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("onFailureForPost", "Error message: " + t.getMessage());

                    }
                });
                // Log.d("CommunityFragment", " 위치 확인 2");
            }
        });


        return convertView;
    }

}