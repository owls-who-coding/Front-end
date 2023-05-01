package com.example.my_last;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentTwoViewHolder extends RecyclerView.ViewHolder {
    TextView replyUserNickname;
    TextView replyUserComment;

    ImageView arrowImageView;

    public CommentTwoViewHolder(@NonNull View itemView) {
        super(itemView);
        replyUserNickname = itemView.findViewById(R.id.reply_user_nickname);
        replyUserComment = itemView.findViewById(R.id.reply_user_comment);
        arrowImageView = itemView.findViewById(R.id.arrow_image_view);

    }

    public void bind(Comment comment) {
        replyUserNickname.setText(comment.getUser().getName());
        replyUserComment.setText(comment.getContent());
        arrowImageView = itemView.findViewById(R.id.arrow_image_view);

    }
}