package com.example.my_last;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class post_detail extends Fragment {

    Button back, sendCommentButton, fixButton;
    community community;
    TextView textview;
    ImageView imageView;
    TextView titleview, open_file;

    EditText Comment_EditText;

    int loggedInUserNumber;



    LinearLayout file;

    //이하 댓글 추가를 위한 부분
    private RecyclerView recyclerView;
    private CommentViewAdapter commentViewAdapter;
    private ArrayList<Comment> commentList;
    //이상 댓글 추가를 위한 부분
    private Retrofit retrofit = RetrofitClient.getClient();

    user_ceate_IF apiService = retrofit.create(user_ceate_IF.class);

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_detail, container, false);

        //로그인 user 번호 가져오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        community = new community();
        loggedInUserNumber = sharedPreferences.getInt("userNumber", -1);

        //번들로 받아온 게시글 작성자 번호
        int userNumber=getArguments().getInt("user_Number");

//        //둘이 일치하지 않으면 수정 버튼은 안보이도록
//        if (loggedInUserNumber != userNumber) {
//            fixButton.setVisibility(View.INVISIBLE);
//        } else {
//            fixButton.setVisibility(View.VISIBLE);
//        }


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
        fixButton=view.findViewById(R.id.fix);

        final int[] open_count = {0};

        titleview.setText(postTitle);
        textview.setText(postContent);
        //메뉴 목록 추가
        ImageButton setting = (ImageButton) view.findViewById(R.id.setting);

        String postImage = getArguments().getString("image");
        if (postImage != null && !postImage.isEmpty()) {
            byte[] decodedString = Base64.decode(postImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(bitmap);

        } else {
            imageView.setVisibility(View.GONE); // 이미지가 없을 경우 ImageView를 숨깁니다.
        }
        getArguments().clear();
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.post_detail_menu, popupMenu.getMenu());
                Toast.makeText(getActivity(),"게시글 작성자"+userNumber,Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"로그인 사용자"+loggedInUserNumber,Toast.LENGTH_SHORT).show();
                Log.d("이미지 파일 있나?", "이미지파일 확인 "+postImage);
                MenuItem fixMenuItem = popupMenu.getMenu().findItem(R.id.fix);
                MenuItem deleteItem=popupMenu.getMenu().findItem(R.id.delete);
                if (loggedInUserNumber != userNumber) {
                    fixMenuItem.setVisible(false);
                    deleteItem.setVisible(false);

                } else {
                    fixMenuItem.setVisible(true);
                    deleteItem.setVisible(true);
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.re:
                                Toast.makeText(getActivity(),"새로고침",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.fix:
                                // 수정 버튼 동작 정의

                                String content = textview.getText().toString(); // 게시글 글 전달

                                updatePost updatePostFragment = new updatePost();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", titleview.getText().toString());
                                bundle.putInt("post_number", postNumber); // postNumber 복사
                                bundle.putString("content", content);
                                bundle.putString("image", postImage);



                                updatePostFragment.setArguments(bundle);
                                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.containers, updatePostFragment).addToBackStack(null).commit(); // 변수명 변경 및 백스택 추가



                                Toast.makeText(getActivity(),"수정",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:

                                new AlertDialog.Builder(getContext())
                                        .setTitle("게시글 삭제")
                                        .setMessage("정말 게시글을 삭제하시겠습니까?")


                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {




                                                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postNumber));


                                                Call<Void> call = apiService.deletePost(requestBody);
                                                call.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if (response.isSuccessful()) {
                                                            Toast.makeText(getActivity(),"Post deleted successfully",Toast.LENGTH_SHORT).show();

                                                            getParentFragmentManager()
                                                                    .beginTransaction()
                                                                    .replace(R.id.containers, community)
                                                                    .commit();
                                                        } else {
                                                            Toast.makeText(getActivity(),"Failed to delete post",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        Toast.makeText(getActivity(),"An error occurred",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).create().show();


                                Toast.makeText(getActivity(),"삭제",Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        // 댓글을 불러오는 요청 수행
        GetCommentsTask getCommentsTask = new GetCommentsTask(postNumber);
        getCommentsTask.execute();

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
                int userId = loggedInUserNumber; // 실제 사용자 ID로 교체해야 함
                String content = Comment_EditText.getText().toString();
                if(content.trim().isEmpty()){
                    Toast.makeText(getContext(), "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    sendCommentToServer(postId, userId, content, 0);
                    Comment_EditText.setText("");
                }
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
    private void sendCommentToServer(int postId, int userId, String content, int before_comment) {
        comment_IF apiServiceForComment = retrofit.create(comment_IF.class);
        Call<ResponseBody> callForComment = apiServiceForComment.saveComment(postId, userId,content, before_comment);

        callForComment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "댓글이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    // 댓글 작성 후 댓글 목록을 다시 불러옴
                    loadComments(postId);
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
    private void loadComments(int postNumber) {
        GetCommentsTask getCommentsTask = new GetCommentsTask(postNumber);
        getCommentsTask.execute();
    }


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
                commentList.clear(); // 기존 댓글 목록을 비움
                commentList.addAll(comments); // 새로 받아온 댓글 목록으로 채움
                if(commentViewAdapter == null){
                    //commentViewAdapter = new CommentViewAdapter(commentList, getActivity());
                    commentViewAdapter = new CommentViewAdapter(commentList, getActivity(), new CommentViewHolder.OnCommentSendListener() {
                        @Override
                        public void onCommentSend(int commentNumber, String content) {
                            int postId = postNumber; // 실제 게시물 ID로 교체해야 함
                            int userId = loggedInUserNumber; // 실제 사용자 ID로 교체해야 함
                            if(content.trim().isEmpty()){
                                Toast.makeText(getContext(), "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                sendCommentToServer(postId, userId, content, commentNumber);
                                Comment_EditText.setText("");
                            }
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(commentViewAdapter);
                } else {
                    commentViewAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), "댓글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
