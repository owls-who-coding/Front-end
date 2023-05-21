package com.example.my_last;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CommentViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment> commentList;
    public static int count = 0;

    private CommentViewHolder.OnCommentSendListener listener;

    // 어댑터 생성자에 리스너를 추가하고, ViewHolder 생성시 리스너를 전달합니다.
    public CommentViewAdapter(List<Comment> commentList, FragmentActivity activity, CommentViewHolder.OnCommentSendListener listener) {
        this.commentList = commentList;
        this.listener = listener;
    }

    public CommentViewAdapter(List<Comment> commentList, FragmentActivity activity) {
        this.commentList = commentList;
    }

    @Override
    public int getItemViewType(int position) {
        return commentList.get(position).getBeforeComment() == 0 ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view, listener);
        } else {
            View view = inflater.inflate(R.layout.comment_two_item, parent, false);
            return new CommentTwoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).bind(comment);
        } else if (holder instanceof CommentTwoViewHolder) {
            ((CommentTwoViewHolder) holder).bind(comment);
        }
    }

    private List<Comment> flattenCommentList(List<Comment> comments) {
        List<Comment> flattenedList = new ArrayList<>();
        for (Comment comment : comments) {
            flattenedList.add(comment);
            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                flattenedList.addAll(flattenCommentList(comment.getReplies()));
            }
        }
        return flattenedList;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
