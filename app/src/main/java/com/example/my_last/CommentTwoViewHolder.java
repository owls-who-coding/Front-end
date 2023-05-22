package com.example.my_last;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentTwoViewHolder extends RecyclerView.ViewHolder {
    TextView replyUserNickname;
    TextView replyUserComment;

    TextView delete_comment;

    Comment comment;

    int userNumber;

    private CommentTwoViewHolder.OnCommentDeleteListener deleteListener;

    public interface OnCommentDeleteListener {
        void onCommentDelete(int commentNumber);
    }
    ImageView arrowImageView;

    public CommentTwoViewHolder(@NonNull View itemView, OnCommentDeleteListener deleteListener) {
        super(itemView);
        replyUserNickname = itemView.findViewById(R.id.reply_user_nickname);
        replyUserComment = itemView.findViewById(R.id.reply_user_comment);
        arrowImageView = itemView.findViewById(R.id.arrow_image_view);
        delete_comment=itemView.findViewById(R.id.delete_reply_comment);

        this.deleteListener = deleteListener;

        delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ViewHolder", "Delete button clicked2222"); // 로그 추가
                //child_linear.setVisibility(View.GONE);

                // Comment 객체에서 commentNumber를 가져옴
                int commentNumber = comment.getCommentNumber();

                // commentNumber를 프래그먼트로 전달
                if (deleteListener != null) {
                    deleteListener.onCommentDelete(commentNumber);
                }

                Toast.makeText(view.getContext(), "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bind(Comment comment) {
        this.comment = comment;
        replyUserNickname.setText(comment.getUser().getName());
        replyUserComment.setText(comment.getContent());
        arrowImageView = itemView.findViewById(R.id.arrow_image_view);

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