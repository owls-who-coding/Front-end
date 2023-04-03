package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my_last.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class choice extends AppCompatActivity {


    community community;
    diary diary;
    disease disease;
    mypage mypage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        community = new community();
        diary = new diary();
        disease = new disease();
        mypage = new mypage();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers,disease).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.community:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers,community).commit();
                        return true;
                    case R.id.diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers,diary).commit();
                        return true;
                    case R.id.disease:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers,disease).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, mypage).commit();
                        return true;
                }
                return false;
            }
        });



    }


}