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


    public CommentViewAdapter(List<Comment> commentList, FragmentActivity activity) {
        this.commentList = commentList; // 수정된 부분
    }

    @Override
    public int getItemViewType(int position) {
        // 부모 댓글과 대댓글을 구별하기 위해 comment_id와 before_comment가 같은지 확인
        return commentList.get(position).getBeforeComment() == commentList.get(position).getCommentNumber() ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.comment_two_item, parent, false);
            return new CommentTwoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        if (holder instanceof CommentViewHolder) {
            // 댓글 내용을 설정 (공란 채우기)
            ((CommentViewHolder) holder).bind(comment);
        } else if (holder instanceof CommentTwoViewHolder) {
            // 대댓글 내용을 설정 (공란 채우기)
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