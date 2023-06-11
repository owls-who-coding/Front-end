package com.example.my_last;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Community extends Fragment{
   // Retrofit retrofit = RetrofitClient.getClient("https://af8a-222-117-126-33.jp.ngrok.io/");
    TextView text_1;
    ImageView write;
    ListView listView;

    Button button;
    CreatePost user_create;
    PostDetail PostDetail;
    List<Post> postList;

    //로딩화면
    ConstraintLayout layout_loading;
    ImageView imv_loading;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_community,null);

        user_create = new CreatePost();
        PostDetail =new PostDetail();

        text_1 = (TextView) view.findViewById(R.id.TEXT_1);//
        write = (ImageView) view.findViewById(R.id.user_write);//
        listView = (ListView) view.findViewById(R.id.lv_list);//

        layout_loading = view.findViewById(R.id.layout_community_parent);
        imv_loading = view.findViewById(R.id.imv_community_loading);
        setLoadingView();

       // Retrofit retrofit = RetrofitClient.getClient("https://5e8c-125-133-41-82.jp.ngrok.io/");
        Retrofit retrofit = RetrofitClient.getClient();

        IGetPostList apiService = retrofit.create(IGetPostList.class);
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
                            layout_loading.setVisibility(View.INVISIBLE);
                            postList = response.body(); // 멤버 변수에 할당
                            // 로그로 받은 데이터 출력
                            for (Post post : postList) {
                                Log.d("CommunityFragment",  " Title: " + post.getTitle());
                            }


                            // 여기에 ListViewAdapter와 관련된 코드를 추가합니다.
                            ListViewAdapter adapter = new ListViewAdapter(getContext(), postList, view);
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
    void setLoadingView(){
        layout_loading.setVisibility(View.VISIBLE);
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        // 애니메이션 시작
        imv_loading.startAnimation(rotateAnimation);

        // 필요할 때 애니메이션 멈춤
//        loadingEyes.clearAnimation();
//        Glide.with(this).load(R.drawable.).into(loadingEyes);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.TEXT_1:
                Toast.makeText(getContext(),"무슨 오류인지 위치 확인",Toast.LENGTH_LONG).show();
                break;
        }
    }

}