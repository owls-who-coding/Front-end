package com.example.my_last;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PredictResult extends Fragment {
    ImageView captureImageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predict_result,null);


        captureImageView=view.findViewById(R.id.predict_imv_capture);
        if(getArguments() != null){
            String imageBase64 = getArguments().getString("image_key");
            String predictResult = getArguments().getString("result_key");
            Bitmap eyesImage = ImageProcessing.base64ToBitmap(imageBase64);
            captureImageView.setImageBitmap(eyesImage);
            captureImageView.setBackgroundResource(R.drawable.border);
            try {

            } finally {

            }
        }
        return view;
    }
}