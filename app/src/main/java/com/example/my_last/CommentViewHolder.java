package com.example.my_last;

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
    ;


    private OnCommentSendListener listener;


    Comment comment;
    public interface OnCommentSendListener {
        void onCommentSend(int commentNumber, String content);
    }

    public CommentViewHolder(@NonNull View itemView,OnCommentSendListener listener) {
        super(itemView);

        userNickname = itemView.findViewById(R.id.user_nickname);
        commentText = itemView.findViewById(R.id.comment_text);
        child_linear = itemView.findViewById(R.id.child_comment_linear);
        child_comment_text = itemView.findViewById(R.id.child_comment_text);
        upload_child_comment = itemView.findViewById(R.id.upload_child_comment);
        cancel_child_comment = itemView.findViewById(R.id.cancel_child_comment);
        create_comment=itemView.findViewById(R.id.child_comment);
        delete_comment=itemView.findViewById(R.id.delete_comment);



        this.listener = listener;

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
                child_linear.setVisibility(View.GONE);
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
    }
}