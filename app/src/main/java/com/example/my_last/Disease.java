package com.example.my_last;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Disease extends Fragment {
    Button btn_camera;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_disease,null);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        String predictResult =data.getStringExtra("disease_key");
                        String base64Image = data.getStringExtra("image_key");
                        Bitmap eyesImage = ImageProcessing.base64ToBitmap(base64Image);

                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_predict_result);
                        ImageView imv = dialog.findViewById(R.id.imv_eyes);
                        imv.setImageBitmap(eyesImage);

                        startDialog(dialog, predictResult, base64Image);
                    }
                }
        );

        LinearLayout camera_linearLayout = (LinearLayout)view.findViewById(R.id.camera_linear);

        camera_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"asfsad",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), Eyes_detection.class);
//                startActivity(intent);
                launcher.launch(intent);

            }
        });

//        btn_camera = (Button) view.findViewById(R.id.btn_camera);
//        btn_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), Eyes_detection.class);
////                startActivity(intent);
//                launcher.launch(intent);
//            }
//        });
        return view;
    }

    private void startDialog(Dialog dialog, String predictResult,String base64Image){
        dialog.show();
        Button close_btn = dialog.findViewById(R.id.btn_result_close);
        Button write_post_btn= dialog.findViewById(R.id.btn_result_question);
        TextView textView = dialog.findViewById(R.id.resultTv);
        textView.setText(predictResult);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        write_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_create user_create = new user_create();
                dialog.dismiss();
                Bundle args = new Bundle();
                args.putString("image_key",base64Image);
                user_create.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.containers, user_create).commit();

            }
        });
    }

}
