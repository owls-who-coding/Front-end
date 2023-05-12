package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class post_detail extends Fragment {

    Button back, sendCommentButton;
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
    private Retrofit retrofit = RetrofitClient.getClient();

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_detail, container, false);



        // String postBodyPath = getArguments().getString("post_body_path"); // 인텐트가 아닌 번들로부터 데이터를 가져옵니다.
        String postTitle = getArguments().getString("title");
        int postNumber=getArguments().getInt("post_number");
        String postContent = getArguments().getString("content");

        // back = (Button) view.findViewById(R.id.back_community);
        textview = (TextView) view.findViewById(R.id.post_detail_textview);
        imageView = (ImageView) view.findViewById(R.id.post_detail_image);
        titleview = (TextView) view.findViewById(R.id.post_detail_title);
        open_file = (TextView) view.findViewById(R.id.open_file);
        file = (LinearLayout) view.findViewById(R.id.file);
        Comment_EditText = view.findViewById(R.id.write_comment);
        sendCommentButton = view.findViewById(R.id.save_comment);

        final int[] open_count = {0};

        titleview.setText(postTitle);
        textview.setText(postContent);

        // 댓글을 불러오는 요청 수행
        GetCommentsTask getCommentsTask = new GetCommentsTask(postNumber);
        getCommentsTask.execute();

        String postImage = getArguments().getString("image");
        if (postImage != null && !postImage.isEmpty()) {
            byte[] decodedString = Base64.decode(postImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.GONE); // 이미지가 없을 경우 ImageView를 숨깁니다.
        }


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
        // 댓글 작성 구현할 자리
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postId = postNumber; // 실제 게시물 ID로 교체해야 함
                String userId = "example_user_id"; // 실제 사용자 ID로 교체해야 함
                String content = Comment_EditText.getText().toString();
                sendCommentToServer(postId, userId, content);
            }
        });
        recyclerView = view.findViewById(R.id.post_detail_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        int verticalSpacing = 10; // 원하는 간격 크기를 설정하세요. (단위: dp)
        int spacingInPixels = (int) (getResources().getDisplayMetrics().density * verticalSpacing);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacingInPixels));

        commentList = new ArrayList<>();

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
//    private ArrayList<Comment> buildCommentTree(JSONArray commentJsonArray) throws JSONException {
//        ArrayList<Comment> commentList = new ArrayList<>();
//        for (int i = 0; i < commentJsonArray.length(); i++) {
//            JSONObject commentJson = commentJsonArray.getJSONObject(i);
//            Comment comment = jsonToComment(commentJson);
//            comment.setBeforeComment(commentJson.getInt("before_comment"));
//            commentList.add(comment);
//        }
//        return commentList;
//    }


    // 댓글 JSON을 파싱하여 Comment 객체로 변환하는 함수
    private Comment jsonToComment(JSONObject commentJson) throws JSONException {
        Comment comment = new Comment();
        comment.setCommentNumber(commentJson.getInt("comment_id"));
        comment.setContent(commentJson.getString("content"));
        comment.setBeforeComment(commentJson.getInt("before_comment"));

        User user = new User();
        user.setName(commentJson.getString("author_name"));
        comment.setUser(user);

        return comment;
    }
//여기까지 댓글 기능 구현중

    // 이 클래스를 추가하세요
    private class GetCommentsTask extends AsyncTask<Void, Void, List<Comment>> {
        private int postNumber;

        GetCommentsTask(int postNumber) {
            this.postNumber = postNumber;
        }

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            post_detail_IF apiService = retrofit.create(post_detail_IF.class);
            Call<ResponseBody> callForComments = apiService.getComments(postNumber);
            try {
                Response<ResponseBody> response = callForComments.execute();
                if (response.isSuccessful()) {
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray commentsJsonArray = json.getJSONArray("comments");
                    ArrayList<Comment> commentList = new ArrayList<>();

                    for (int i = 0; i < commentsJsonArray.length(); i++) {
                        JSONObject commentJson = commentsJsonArray.getJSONObject(i);
                        Comment comment = jsonToComment(commentJson);
                        commentList.add(comment);
                    }

                    return commentList;
                }
            } catch (IOException | JSONException e) {
                Log.e("GetCommentsTask", "JSON parsing error", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            if (comments != null) {
                commentList.addAll(comments);
                commentViewAdapter = new CommentViewAdapter(commentList, getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(commentViewAdapter);
            } else {
                Toast.makeText(getActivity(), "댓글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
