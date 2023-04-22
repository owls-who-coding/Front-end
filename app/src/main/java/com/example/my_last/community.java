package com.example.my_last;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class community extends Fragment{
   // Retrofit retrofit = RetrofitClient.getClient("https://af8a-222-117-126-33.jp.ngrok.io/");
    TextView text_1;
    ImageView write;
    ListView listView;

    Button button;
    user_create user_create;
    post_detail post_detail;
    List<Post> postList;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_community,null);

        user_create = new user_create();
        post_detail=new post_detail();

        text_1 = (TextView) view.findViewById(R.id.TEXT_1);
        write = (ImageView) view.findViewById(R.id.user_write);
        listView = (ListView) view.findViewById(R.id.lv_list);


       // Retrofit retrofit = RetrofitClient.getClient("https://5e8c-125-133-41-82.jp.ngrok.io/");
        Retrofit retrofit = RetrofitClient.getClient();

        getListIF apiService = retrofit.create(getListIF.class);
       // Log.d("CommunityFragment", "apiService: " + apiService.toString());
        Call<List<Post>> call = apiService.getPosts();
       // Log.d("CommunityFragment", "위치 확인000 " );

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.containers,user_create).commit();
            }
        });
        text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"asfsad",Toast.LENGTH_SHORT).show();
            }
        });




        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        //Toast.makeText(getActivity(), "onRespnse 작동.", Toast.LENGTH_SHORT).show();

                        if (response.isSuccessful()) {
                            postList = response.body(); // 멤버 변수에 할당
                            // 로그로 받은 데이터 출력
                            for (Post post : postList) {
                                Log.d("CommunityFragment",  " Title: " + post.getTitle());
                            }


                            // 여기에 ListViewAdapter와 관련된 코드를 추가합니다.
                            ListViewAdapter adapter = new ListViewAdapter(getContext(), postList);
                            listView.setAdapter(adapter);
                            Toast.makeText(getActivity(), "데이터를 가져왔습니다.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            // 여기에 로그 추가
                            Log.d("CommunityFragment", "onResponse: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        Toast.makeText(getActivity(), "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        // 여기에 로그 추가
                        Log.d("CommunityFragment", "onFailure: " + t.getMessage());
                    }
                });

            }
        });
        thread.start();


        return view;
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.TEXT_1:
                Toast.makeText(getContext(),"무슨 오류인지 위치 확인",Toast.LENGTH_LONG).show();
                break;
        }
    }

}