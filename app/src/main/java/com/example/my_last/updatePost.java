package com.example.my_last;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class updatePost extends Fragment {

    private static final int IMAGE_MAX_SIZE = 1024;

    ImageView imageView;

    Uri uri;
    Button finish;
    community community;
    private static final int REQUEST_CODE = 0;

    EditText editText;
    EditText editTexttitle;
    int postNumber;

    Retrofit retrofit = RetrofitClient.getClient();
    user_ceate_IF apiService = retrofit.create(user_ceate_IF.class);

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        // Shared Preferences에서 로그인 정보를 가져옵니다.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        int loggedInUserNumber = sharedPreferences.getInt("userNumber", 0);

        View view = inflater.inflate(R.layout.activity_update_post,null);

        finish = (Button) view.findViewById(R.id.updateFinish);

        imageView = (ImageView)view.findViewById(R.id.updateImage);
        community = new community();
        editText = (EditText)view.findViewById(R.id.updatePostMultiLine);
        editTexttitle=(EditText)view.findViewById(R.id.update_Title);

        if (getArguments() != null) {
            String title = getArguments().getString("title");
            postNumber = getArguments().getInt("post_number");
            String content = getArguments().getString("content");
            String base64Image = getArguments().getString("image");
            Log.d("받아온 건 있나?", "이미지 문자열 "+base64Image);
            getArguments().clear();

            editTexttitle.setText(title);
            editText.setText(content);

            if (base64Image != null && !base64Image.isEmpty()) {Bitmap postImage = ImageProcessing.base64ToBitmap(base64Image);
                // 이미지 크기를 조절합니다.
                Bitmap eyesImage = ImageProcessing.base64ToBitmap(base64Image);
                //Bitmap eyesImage= getScaledBitmap(base64Image);
                // 원하는 너비와 높이를 지정합니다
                int desiredWidth = 100;
                int desiredHeight = 100;

// 이미지를 리사이즈합니다
                Bitmap resizedImage = Bitmap.createScaledBitmap(eyesImage, desiredWidth, desiredHeight, true);

                imageView.setImageBitmap(resizedImage);

                // postImage 메모리 해제
                postImage.recycle();
                eyesImage.recycle();
                base64Image="";

                Log.d("base64초기화", "base64초기화 "+base64Image);

            }


        }



        imageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Log.d("트랜잭션 크기 측정 위치 확인", "트랜잭션 크기 측정 위치 확인1");
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
                // 5. 서버로 데이터 전송하기 위한 API 호출

                    sendPostData(userNumber, diseaseNumber, text, title,postNumber, uri);


            }

        });



        return view;


    }

    private void openGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, REQUEST_CODE);
        //imageView.setImageResource(0);
        Log.d("트랜잭션 크기 측정 위치 확인", "트랜잭션 크기 측정 위치 확인2");
        Intent intent = new Intent(Intent.ACTION_PICK);
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        Log.d("트랜잭션 크기 측정 위치 확인", "트랜잭션 크기 측정 위치 확인3");
        intent.setType("image/*");
        Log.d("트랜잭션 크기 측정 위치 확인", "트랜잭션 크기 측정 위치 확인4");
        startActivityForResult(intent, REQUEST_CODE);
        Log.d("트랜잭션 크기 측정 위치 확인", "트랜잭션 크기 측정 위치 확인5");
    }

    private void sendPostData(int userNumber, int diseaseNumber, String text, String title, int postNumber,Uri image) {
        Log.d("실행 클래스 확인", "uri 사용 . 사진 넣었을 때 이거" );
        RequestBody userNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userNumber));
        RequestBody diseaseNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(diseaseNumber));
        RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), text);
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody postNumberBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postNumber));


        MultipartBody.Part imagePart = null;
        if (image != null) {
            File file = new File(getRealPathFromURI(image));

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        Call<Void> call = apiService.updatePost(userNumberBody, diseaseNumberBody, textBody, titleBody, postNumberBody,imagePart);//apiservice가 아닌 postApi였음
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
            uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            imageView = getActivity().findViewById(R.id.updateImage);

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // 이미지 크기를 조절합니다.
            Bitmap scaledBitmap = getScaledBitmap(picturePath);

            // 이미지 설정
            imageView.setImageBitmap(scaledBitmap);

            // 이미지뷰에 URI를 저장하고, 이후 sendPostData 메서드에서 사용합니다.
            imageView.setTag(picturePath);
        }
    }
    //이미지 크기 조절하는 코드 시험중
    private Bitmap getScaledBitmap(String picturePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        final int maxWidth = 100;
        final int maxHeight = 100;

        int scaleFactor = 1;
        while (imageWidth / (scaleFactor * 2) >= maxWidth && imageHeight / (scaleFactor * 2) >= maxHeight) {
            scaleFactor *= 2;
        }

        options.inSampleSize = scaleFactor;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(picturePath, options);
    }
    //여기까지

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