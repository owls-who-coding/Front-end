package com.example.my_last;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView userNickname;
    TextView commentText;

    LinearLayout child_linear;
    EditText child_comment_text;
    TextView upload_child_comment,  cancel_child_comment,delete_comment,create_comment ;

    Comment comment;

    int userNumber;

    private OnCommentSendListener listener;

    private OnCommentDeleteListener deleteListener;


    public interface OnCommentSendListener {
        void onCommentSend(int commentNumber, String content);
    }

    public interface OnCommentDeleteListener {
        void onCommentDelete(int commentNumber);
    }

    public CommentViewHolder(@NonNull View itemView,OnCommentSendListener listener, OnCommentDeleteListener deleteListener) {
        super(itemView);
        Log.d("ViewHolder", "Delete listener in constructor: " + deleteListener); // 로그 추가

        userNickname = itemView.findViewById(R.id.user_nickname);
        commentText = itemView.findViewById(R.id.comment_text);
        child_linear = itemView.findViewById(R.id.child_comment_linear);
        child_comment_text = itemView.findViewById(R.id.child_comment_text);
        upload_child_comment = itemView.findViewById(R.id.upload_child_comment);
        cancel_child_comment = itemView.findViewById(R.id.cancel_child_comment);
        create_comment=itemView.findViewById(R.id.child_comment);
        delete_comment=itemView.findViewById(R.id.delete_comment);



        this.listener = listener;
        this.deleteListener = deleteListener;

        Log.d("ViewHolder", "Delete listener: " + deleteListener);

        create_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (child_linear.getVisibility() == View.GONE) {
                    child_linear.setVisibility(View.VISIBLE);
                } else {
                    child_linear.setVisibility(View.GONE);
                }
            }
        });



        delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ViewHolder", "Delete button clicked"); // 로그 추가
                child_linear.setVisibility(View.GONE);

                // Comment 객체에서 commentNumber를 가져옴
                int commentNumber = comment.getCommentNumber();

                // commentNumber를 프래그먼트로 전달
                if (deleteListener != null) {
                    deleteListener.onCommentDelete(commentNumber);
                }

                Toast.makeText(view.getContext(), "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        upload_child_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                child_linear.setVisibility(View.GONE);

                // 대댓글 내용을 가져옴
                String content = child_comment_text.getText().toString();

                // Comment 객체에서 commentNumber를 가져옴
                int commentNumber = comment.getCommentNumber();

                // 대댓글 내용과 commentNumber를 프래그먼트로 전달
                if (listener != null) {
                    listener.onCommentSend(commentNumber, content);
                }

                Toast.makeText(view.getContext(), "대댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bind(Comment comment) {
        this.comment = comment;
        userNickname.setText(comment.getUser().getName());
        commentText.setText(comment.getContent());
        userNumber=comment.getUser().getUserNumber();

        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        int loggedInUserNumber = sharedPreferences.getInt("userNumber", -1);
        Log.d("로그인 정보 확인", "로그인 정보"+loggedInUserNumber); // 로그 추가
        Log.d("로그인 정보 확인", "댓글 주인장 정보"+userNumber); // 로그 추가
        Log.d("Bind Comment", "Comment: " + comment.toString());
        if (loggedInUserNumber == userNumber) {
            // 같다면 삭제 버튼을 보이게 합니다.
            delete_comment.setVisibility(View.VISIBLE);
        } else {
            // 다르다면 삭제 버튼을 숨깁니다.
            delete_comment.setVisibility(View.GONE);
        }
    }
}