package com.example.my_last;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    TextView userNickname;
    TextView commentText;

    public CommentViewHolder(@NonNull View itemView) {

        super(itemView);
        userNickname = itemView.findViewById(R.id.user_nickname);
        commentText = itemView.findViewById(R.id.comment_text);
    }

    public void bind(Comment comment) {
        userNickname.setText(comment.getUser().getName());
        commentText.setText(comment.getContent());
    }



}