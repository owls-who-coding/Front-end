package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class choice extends AppCompatActivity {


    community community;
    diary diary;
    Disease disease;
    mypage mypage;
    MainActivity mainActivity;
    post_detail post_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        community = new community();
        diary = new diary();
        disease = new Disease();
        mypage = new mypage();
        mainActivity = new MainActivity();
        post_detail = new post_detail();

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, mainActivity).commit();
                        return true;
                }
                return false;
            }
        });



    }


}