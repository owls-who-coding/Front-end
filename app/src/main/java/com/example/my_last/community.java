package com.example.my_last;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class community extends Fragment{

    TextView text_1;
    ImageView write;
    ListView listView;
    ScrollView scrollView;
    Button button;
    user_create user_create;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_community,null);

        user_create = new user_create();

        text_1 = view.findViewById(R.id.TEXT_1);
        write = view.findViewById(R.id.user_write);
        listView = view.findViewById(R.id.lv_list);
        scrollView = view.findViewById(R.id.scroll);
//        button = view.findViewById(R.id.following);

//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int scrollViewHeight = scrollView.getHeight();
//                int scrollY = scrollView.getScrollY();
//                int buttonHeight = button.getHeight();
//
//                int bottom = scrollViewHeight + scrollY - buttonHeight;
//
//                 button.setY(bottom);
//            }
//        });


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

        //return inflater.inflate(R.layout.activity_community,container,false);
        return view;


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.TEXT_1:
                Toast.makeText(getContext(),"응우앵",Toast.LENGTH_LONG).show();
                break;
        }
    }
}