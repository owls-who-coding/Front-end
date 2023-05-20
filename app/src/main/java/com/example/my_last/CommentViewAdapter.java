package com.example.my_last;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.user_comment);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView child_comment = (TextView) view.findViewById(R.id.child_comment);
            LinearLayout child_linear = (LinearLayout) view.findViewById(R.id.child_comment_linear);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView cancel_child_comment = (TextView) view.findViewById(R.id.cancel_child_comment);
            TextView upload_child_comment = (TextView) view.findViewById(R.id.upload_child_comment);

            final int[] open_count = {0};
            final int[] open_count_2 = {0};
            final int[] open_count_3 = {0};



            //대댓글 작성하기
            child_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (open_count[0] == 0) {
                        child_linear.setVisibility(View.VISIBLE);
                        open_count[0] = 1;
                    } else {
                        child_linear.setVisibility(View.GONE);
                        open_count[0] = 0;
                    }
                }
            });

            //대댓글 작성취소
            cancel_child_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     child_linear.setVisibility(View.GONE);
                }
            });


            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            EditText child_comment_text = (EditText)view.findViewById(R.id.child_comment_text);
            String str = child_comment_text.getText().toString();

            //대댓글 작성완료
            upload_child_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    child_linear.setVisibility(View.GONE);
                    Toast.makeText(view.getContext(), str,Toast.LENGTH_SHORT).show();
                }
            });

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