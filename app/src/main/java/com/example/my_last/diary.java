package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class diary extends Fragment {

    Button check,re;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.activity_diary,null);


        check = (Button)view.findViewById(R.id.check);
        re = (Button)view.findViewById(R.id.re);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"asfsad",Toast.LENGTH_SHORT).show();
            }
        });

        //return inflater.inflate(R.layout.activity_community,container,false);
        return view;

//        return inflater.inflate(R.layout.activity_diary,container,false);


    }
}