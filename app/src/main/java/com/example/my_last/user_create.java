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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import kotlin.OverloadResolutionByLambdaReturnType;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    EditText editTexttitle;

    //Retrofit retrofit = RetrofitClient.getClient("https://5e8c-125-133-41-82.jp.ngrok.io/");// 여기에 실제 API 주소를 입력하세요.
    Retrofit retrofit = RetrofitClient.getClient();
    user_ceate_IF apiService = retrofit.create(user_ceate_IF.class);//PostApi에 해당 기능의 인터페이스를 만들었지만, user_create_IF를 새로 생성해서 수정하려했음. 근데 수정하니 오류가 생겨서 일단 보류

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        // Shared Preferences에서 로그인 정보를 가져옵니다.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        int loggedInUserNumber = sharedPreferences.getInt("userNumber", 0);

        View view = inflater.inflate(R.layout.activity_user_create,null);

        finish = (Button) view.findViewById(R.id.finish);

        imageView = (ImageView)view.findViewById(R.id.image);
        community = new community();
        editText = (EditText)view.findViewById(R.id.editTextTextMultiLine);
        editTexttitle=(EditText)view.findViewById(R.id.editTextTitle);

        if(getArguments() != null){
            String base64Image = getArguments().getString("image_key");
            Bitmap eyesImage = ImageProcessing.base64ToBitmap(base64Image);
            imageView.setImageBitmap(eyesImage);
            try {

            } finally {

            }
        }
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
                String title = editTexttitle.getText().toString();

                // userNumber와 diseaseNumber를 여기에 할당하세요. 예를 들어, 다음과 같이 할 수 있습니다.
                int userNumber =loggedInUserNumber;;
                int diseaseNumber = 1;

                // 5. 서버로 데이터 전송하기 위한 API 호출
                if(getArguments() == null)
                    sendPostData(userNumber, diseaseNumber, text, title, uri);
                else
                    sendPostData(userNumber, diseaseNumber, text, title);
            }
        });



        return view;


    }




    private void openGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, REQUEST_CODE);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void sendPostData(int userNumber, int diseaseNumber, String text, String title, Uri image) {
        RequestBody userNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userNumber));
        RequestBody diseaseNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(diseaseNumber));
        RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), text);
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);

        MultipartBody.Part imagePart = null;
        if (image != null) {
            File file = new File(getRealPathFromURI(image));

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        Call<Void> call = apiService.createPost(userNumberBody, diseaseNumberBody, textBody, titleBody, imagePart);//apiservice가 아닌 postApi였음
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "게시글 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();

                    // 게시글 리스트로 이동
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.containers, community)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(postcreat.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendPostData(int userNumber, int diseaseNumber, String text, String title) {
        RequestBody userNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userNumber));
        RequestBody diseaseNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(diseaseNumber));
        RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), text);
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);

        MultipartBody.Part imagePart = null;

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Bitmap을 바이트 배열로 변환
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);

        Call<Void> call = apiService.createPost(userNumberBody, diseaseNumberBody, textBody, titleBody, imagePart);//apiservice가 아닌 postApi였음
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "게시글 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();

                    // 게시글 리스트로 이동
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.containers, community)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
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
                uri = data.getData(); // uri 변수 초기화
                String[] filePathColumn = { MediaStore.Images.Media.DATA };



                Cursor cursor = getActivity().getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                imageView = getActivity().findViewById(R.id.image);

                // 이미지 설정
                imageView.setImageURI(uri);
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

            // 선택한 이미지를 사용하여 원하는 작업을 수행합니다.
            // ...
        }
    }
    //시험삼아 추가하는 메서드
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
    //여기까지


}