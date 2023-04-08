package com.example.my_last;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class user_create extends Fragment {
    ImageView imageView;

    Uri uri;
    Button finish;
    community community;
    private static final int REQUEST_CODE = 0;

    EditText editText;

    Retrofit retrofit = RetrofitClient.getClient("https://af22-125-133-41-82.jp.ngrok.io/");// 여기에 실제 API 주소를 입력하세요.

    PostApi apiService = retrofit.create(PostApi.class);

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.activity_user_create,null);

        finish = (Button) view.findViewById(R.id.finish);

        imageView = (ImageView)view.findViewById(R.id.image);
        community = new community();
        editText = (EditText)view.findViewById(R.id.editTextTextMultiLine);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4. 클릭 리스너 내에서 EditText의 텍스트 가져오기
                String text = editText.getText().toString();

                // userNumber와 diseaseNumber를 여기에 할당하세요. 예를 들어, 다음과 같이 할 수 있습니다.
                int userNumber = 1;
                int diseaseNumber = 1;

                // 5. 서버로 데이터 전송하기 위한 API 호출
                sendPostData(userNumber, diseaseNumber, text);
            }
        });



        return view;


    }



    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }
    private void sendPostData(int userNumber, int diseaseNumber, String text) {
        Call<Void> call = apiService.createPost(userNumber, diseaseNumber, text);//apiservice가 아닌 postApi였음
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(.this, "데이터가 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                  //  Toast.makeText(postcreat.this, "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(postcreat.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            imageView = getActivity().findViewById(R.id.image);

            // 이미지 설정
            imageView.setImageURI(selectedImageUri);
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // 선택한 이미지를 사용하여 원하는 작업을 수행합니다.
            // ...
        }
    }


}