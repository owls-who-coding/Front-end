package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class post_detail extends Fragment {

    Button sendCommentButton;
    community community;
    TextView textview;
    ImageView imageView;
    TextView titleview, open_file;

    EditText Comment_EditText;

    LinearLayout file;

    //이하 댓글 추가를 위한 부분
    private RecyclerView recyclerView;
    private CommentViewAdapter commentViewAdapter;
    private ArrayList<Comment> commentList;
    //이상 댓글 추가를 위한 부분
    private Retrofit retrofit;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_detail, container, false);


        String postBodyPath = getArguments().getString("post_body_path"); // 인텐트가 아닌 번들로부터 데이터를 가져옵니다.
        String postTitle = getArguments().getString("title");
        int postNumber=getArguments().getInt("post_number");

        // back = (Button) view.findViewById(R.id.back_community);
        textview = (TextView) view.findViewById(R.id.post_detail_textview);
        imageView = (ImageView) view.findViewById(R.id.post_detail_image);
        titleview = (TextView) view.findViewById(R.id.post_detail_title);
        open_file = (TextView) view.findViewById(R.id.open_file);
        file = (LinearLayout) view.findViewById(R.id.file);
        Comment_EditText = view.findViewById(R.id.write_comment);
        sendCommentButton = view.findViewById(R.id.save_comment);

        final int[] open_count = {0};

        open_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open_count[0] == 0) {
                    file.setVisibility(View.VISIBLE);
                    open_count[0] = 1;
                } else {
                    file.setVisibility(View.GONE);
                    open_count[0] = 0;
                }

            }
        });
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postId = postNumber; // 실제 게시물 ID로 교체해야 함
                String userId = "example_user_id"; // 실제 사용자 ID로 교체해야 함
                String content = Comment_EditText.getText().toString();
                sendCommentToServer(postId, userId, content);
            }
        });

        // Button 객체 초기화 예시
        ImageButton back = (ImageButton) view.findViewById(R.id.back_community);

        community = new community();
        post_detail post_detail = new post_detail();


        ImageButton three = (ImageButton)view.findViewById(R.id.setting);

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.post_detail_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    //각 ID에 따른 결과 나오게 하면 될듯
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.re:
                                Toast.makeText(getActivity(),"새로고침 완료",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.fix:
                                Toast.makeText(getActivity(),"수정 완료",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:
                                Toast.makeText(getActivity(),"삭제 완료",Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.containers, community).commit();
            }
        });

        // 서버에 요청하여 txt 파일의 내용을 가져옵니다.
       // Retrofit retrofit = RetrofitClient.getClient();
        retrofit = RetrofitClient.getClient();// Retrofit 객체를 전역으로 뻄.
        post_detail_IF apiService = retrofit.create(post_detail_IF.class);
        // 이하 코드 한줄은 기존의 이미지와 게시글 본문만을 불러오는 인터페이스. 댓글 구현 실패시 다시 이하 코드 사용
       // Call<ResponseBody> call = apiService.getPostAndImageContent(postBodyPath);
        Log.d("postNumber", "postNumber: " + postNumber);
        Call<ResponseBody> call = apiService.getPostAndImageContentWithComments(postBodyPath,postNumber);



        // 추가: RecyclerView 초기화- 이하 댓글 구현을 위한 추가 코드
       // recyclerView = view.findViewById(R.id.post_detail_recycleView);
        recyclerView = view.findViewById(R.id.post_detail_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        int verticalSpacing = 10; // 원하는 간격 크기를 설정하세요. (단위: dp)
        int spacingInPixels = (int) (getResources().getDisplayMetrics().density * verticalSpacing);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacingInPixels));

        commentList = new ArrayList<>();

        //이상 댓글 구현을 위한 추가 코드

        Log.d("title", "title: " + postTitle);
        Log.d("DetailPostActivity", "postBodyPath: " + postBodyPath);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONObject postData = json.getJSONObject("post");
                        //JSONArray commentsData = json.getJSONArray("comments");
                        String content = postData.getString("content");


                        titleview.setText(postTitle);
                        textview.setText(content);

                        //String image_base64 = json.getString("image_base64");
                        String image_base64 = postData.optString("image_base64", "");


                        if (!image_base64.isEmpty()) {
                            byte[] decodedString = Base64.decode(image_base64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setVisibility(View.GONE); // 이미지가 없을 경우 ImageView를 숨깁니다.
                        }

                        JSONArray commentsJsonArray = json.getJSONArray("comments");
                        commentList = buildCommentTree(commentsJsonArray);
                        for (Comment comment : commentList) {
                            Log.d("CommentInfo", "Author: " + comment.getUser().getName() + ", Content: " + comment.getContent());
                        }


                                // 댓글 RecyclerView 업데이트
                        commentViewAdapter = new CommentViewAdapter(commentList, getActivity());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(commentViewAdapter);


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



        return view;
    }


    //이하 댓글 추가 기능 구현 중
    private void sendCommentToServer(int postId, String userId, String content) {
        comment_IF apiServiceForComment = retrofit.create(comment_IF.class);
        Call<ResponseBody> callForComment = apiServiceForComment.saveComment(postId, userId, content);

        callForComment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "댓글이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    // 댓글 목록을 새로고침하거나, 필요한 경우 다른 UI 업데이트를 수행합니다.
                } else {
                    Toast.makeText(getContext(), "댓글 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private ArrayList<Comment> buildCommentTree(JSONArray commentJsonArray) throws JSONException {
        ArrayList<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < commentJsonArray.length(); i++) {
            JSONObject commentJson = commentJsonArray.getJSONObject(i);
            Comment comment = jsonToComment(commentJson);
            comment.setBeforeComment(commentJson.getInt("before_comment"));
            commentList.add(comment);
        }
        return commentList;
    }

    private Comment jsonToComment(JSONObject commentJson) throws JSONException {
        Comment comment = new Comment();
        comment.setCommentNumber(commentJson.getInt("comment_id"));
        comment.setContent(commentJson.getString("content"));

        User user = new User();
        user.setName(commentJson.getString("author_name"));
        comment.setUser(user);

        JSONArray repliesJsonArray = commentJson.optJSONArray("replies");
        if (repliesJsonArray != null) {
            ArrayList<Comment> replies = buildCommentTree(repliesJsonArray);
            comment.setReplies(replies);
        } else {
            comment.setReplies(new ArrayList<>());
        }

        return comment;
    }
//여기까지 댓글 기능 구현중
}