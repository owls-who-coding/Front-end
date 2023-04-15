package com.example.my_last;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("CommunityFragment", "위치 확인0 ");
                Post selectedItem = getItem(position);
                String postBodyPath = selectedItem.getPostBodyPath();
                String title=selectedItem.getTitle();
                //Log.d("어뎁터에서의 title", title);

                post_detail postDetailFragment = new post_detail();
                Bundle bundle = new Bundle();
                bundle.putString("post_body_path", postBodyPath);
                bundle.putString("title", title);
                postDetailFragment.setArguments(bundle);
               // Log.d("CommunityFragment", " 위치 확인 1");
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.containers, postDetailFragment).addToBackStack(null).commit(); // 변수명 변경 및 백스택 추가

               // Log.d("CommunityFragment", " 위치 확인 2");
            }
        });


        return convertView;
    }
}

