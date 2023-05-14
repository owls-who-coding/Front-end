package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sign_in extends Fragment {

    MainActivity mainActivity;

    Button back,create;

    EditText edit_id, edit_password, edit_name, edit_age, edit_dog_name;
// ...



    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_sign_in,null);

        create = (Button) view.findViewById(R.id.create);
        edit_id = (EditText) view.findViewById(R.id.ed_sign_id);
        edit_password = (EditText) view.findViewById(R.id.ed_sign_password);
        edit_name = (EditText) view.findViewById(R.id.ed_sign_name);
        edit_age = (EditText) view.findViewById(R.id.ed_sign_age);
        edit_dog_name = (EditText) view.findViewById(R.id.sign_dog_name);


        mainActivity = new MainActivity();
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getParentFragmentManager().beginTransaction().replace(R.id.containers, mainActivity).commit();
//            }
//        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SIGN_UP", "Sign up button clicked"); // 로그 추가
                // 회원가입 정보 가져오기
                String id = edit_id.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                String name = edit_name.getText().toString().trim();
                int age = Integer.parseInt(edit_age.getText().toString().trim());
                String dog_name = edit_dog_name.getText().toString().trim();

                // 회원가입 요청을 위한 서비스 생성
                sign_inIF apiService = RetrofitClient.getClient().create(sign_inIF.class);

                // 서버에 회원가입 요청
                Call<JsonObject> call = apiService.sign_up(id, password, name, age, dog_name);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject responseBody = response.body();
                            if (responseBody.get("success").getAsBoolean()) {
                                // 회원가입 성공
                                Toast.makeText(getContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().beginTransaction().replace(R.id.containers, mainActivity).commit();
                            } else {
                                // 회원가입 실패
                                String errorMessage = responseBody.get("message").getAsString();
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;


    }
}