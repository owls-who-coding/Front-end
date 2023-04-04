package com.example.my_last;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Post> {
    public ListViewAdapter(Context context, List<Post> posts) {
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_item, parent, false);
        }

        Post post = getItem(position);


       // TextView postBodyPathLabel = convertView.findViewById(R.id.path_invisible);// 보이지 않는 텍스트뷰 하나 더 만들어야함
        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView tv_tag = (TextView) convertView.findViewById(R.id.tag);
        TextView tv_nickname = (TextView) convertView.findViewById(R.id.nickname);
        TextView tv_upload_date = (TextView)convertView.findViewById(R.id.upload_date);

        TextView tv_comment = (TextView)convertView.findViewById(R.id.comment);
        TextView tv_title = (TextView)convertView.findViewById(R.id.title);

        tv_tag.setText(String.valueOf(post.getDiseaseNumber()));
        tv_nickname.setText(String.valueOf(post.getUserNumber()));
        tv_upload_date.setText(post.getUpdatedAt());

        tv_comment.setText(String.valueOf(post.getCommentCount()));

        tv_title.setText(post.getTitle());
        //postBodyPathLabel.setText(post.getPostBodyPath());


        return convertView;
    }
}

