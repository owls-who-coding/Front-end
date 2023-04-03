package com.example.my_last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    Call<data_model> call;
    TextView textView,tt;

    Button button_login,button_sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textvv);
        button_login = findViewById(R.id.btn_login);
        button_sign = findViewById(R.id.btn_sign);
        tt = findViewById(R.id.textView2);

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



//        call = retrofit_client.getApiService().test_api_get("5");
//        call.enqueue(new Callback<data_model>() {
//            @Override
//            public void onResponse(Call<data_model> call, Response<data_model> response) {
//                data_model result = response.body();
//                String str;
//                str= result.getUserId() +"\n"+
//                        result.getID()+"\n"+
//                        result.getTitle()+"\n"+
//                        result.getBody();
//                textView.setText(str);
//            }
//
//            @Override
//            public void onFailure(Call<data_model> call, Throwable t) {
//
//            }
//        });
    }
}