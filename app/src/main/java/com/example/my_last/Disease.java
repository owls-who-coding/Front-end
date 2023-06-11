package com.example.my_last;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Disease extends Fragment {
    Button btn_camera;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_disease,null);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        String res = data.getStringExtra("disease_key");
                        String base64Image = data.getStringExtra("image_key");
                        Log.d("-------------------------------------------------------------------res",res);
                        Log.d("--------------------------------------------------------------------image",base64Image);
//                        Bitmap eyesImage = ImageProcessing.base64ToBitmap(base64Image);
//
//                        Dialog dialog = new Dialog(getContext());
//                        dialog.setContentView(R.layout.dialog_predict_result);
//                        ImageView imv = dialog.findViewById(R.id.imv_eyes);
//                        imv.setImageBitmap(eyesImage);
//
                        PredictResult predictResult = new PredictResult();
                        Bundle args = new Bundle();
                        args.putString("result_key",res);
                        args.putString("image_key", base64Image);
                        predictResult.setArguments(args);
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.containers, predictResult).addToBackStack(null).commit(); // 변수명 변경 및 백스택 추가
                    }
                }
        );

        LinearLayout camera_linearLayout = (LinearLayout)view.findViewById(R.id.camera_linear);

        camera_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EyesDetection.class);
//                startActivity(intent);
                launcher.launch(intent);

            }
        });

        return view;
    }

}
