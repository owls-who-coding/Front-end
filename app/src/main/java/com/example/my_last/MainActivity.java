package com.example.my_last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

//    Call<data_model> call;
    TextView textView,tt;

    Button button_login,button_sign;

    List<Post> postList;

    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textvv);
        button_login = findViewById(R.id.btn_login);
        button_sign = findViewById(R.id.btn_sign);
        tt = findViewById(R.id.textView2);

        Toast.makeText(MainActivity.this, "위치 확인1", Toast.LENGTH_SHORT).show();

        Retrofit retrofit = RetrofitClient.getClient("https://150c-222-117-126-33.jp.ngrok.io/");



        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),choice.class);
                startActivity(i);
            }
        });

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),sign_in.class);
                startActivity(i);
            }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tt.setText("DFafdf");
                tt.setTextColor(Color.parseColor("#e65d5d"));
            }
        });




    }
}