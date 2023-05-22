package com.example.my_last;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PredictResult extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predict_result,null);
        String imageBase64 = savedInstanceState.getString("ImageBase64");
        String predictResult = savedInstanceState.getString("ResultJSON");
        
        return view;
    }
}