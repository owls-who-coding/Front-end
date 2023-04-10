package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Fragment {

//    Call<data_model> call;
    TextView textView,tt;
    mypage mypage;
    sign_in sign_in;

    Button button_login,button_sign;

    List<Post> postList;

    private List<String> titles = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_main,null);

        button_login = (Button) view.findViewById(R.id.btn_login);
        button_sign = (Button) view.findViewById(R.id.btn_sign);
        tt = (TextView) view.findViewById(R.id.textView2);


        Retrofit retrofit = RetrofitClient.getClient("https://cfa5-125-133-41-82.jp.ngrok.io/");



        mypage = new mypage();
        sign_in = new sign_in();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.containers, mypage).commit();
            }
        });

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.containers, sign_in).commit();
            }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tt.setText("DFafdf");
                tt.setTextColor(Color.parseColor("#e65d5d"));
            }
        });


        return view;
    }
}