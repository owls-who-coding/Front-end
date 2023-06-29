package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChoiceMenu extends AppCompatActivity {


    Community community;
    Diary diary;
    Disease disease;
    Mypage mypage;
    Login login;
    PostDetail PostDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        community = new Community();
        diary = new Diary();
        disease = new Disease();
        mypage = new Mypage();
        login = new Login();
        PostDetail = new PostDetail();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers,disease).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.community:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers,community).addToBackStack(null).commit();
                        return true;
                    case R.id.diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers,diary).commit();
                        return true;
                    case R.id.disease:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers,disease).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, login).commit();
                        return true;
                }
                return false;
            }
        });



    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView titleView = new TextView(this);
        titleView.setText("애플리케이션을 종료 시키겠습니까?");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(0,60,0,10);
        titleView.setTextColor(Color.rgb(0, 0, 0));
        builder.setCustomTitle(titleView);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);

                    }
                });

        builder.show();


    }




}