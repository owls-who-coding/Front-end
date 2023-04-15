package com.example.my_last;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Fragment {

//    Call<data_model> call;
    TextView textView,text_password;
    mypage mypage;
    sign_in sign_in;

    Button button_login,button_sign;
    EditText edit_id;
    EditText edit_password;

    List<Post> postList;

    private List<String> titles = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        int userNumber = sharedPreferences.getInt("userNumber", -1);

        if (userNumber != -1) {
            // 이미 로그인되어 있는 경우, 바로 마이페이지로 이동
            mypage mypage = new mypage(); // mypage 객체 초기화
            getParentFragmentManager().beginTransaction().replace(R.id.containers, mypage).commit();
        }

        View view = inflater.inflate(R.layout.activity_main,null);

        button_login = (Button) view.findViewById(R.id.btn_login);
        button_sign = (Button) view.findViewById(R.id.btn_sign);
        text_password = (TextView) view.findViewById(R.id.PWtext);
        edit_id=(EditText) view.findViewById(R.id.IDedit);
        edit_password=(EditText) view.findViewById(R.id.PWedit);



        //Retrofit retrofit = RetrofitClient.getClient("https://5e8c-125-133-41-82.jp.ngrok.io/");
        //Retrofit retrofit = RetrofitClient.getClient();


        mypage = new mypage();
        sign_in = new sign_in();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 입력한 아이디와 비밀번호 가져오기
                String id = edit_id.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                Log.d(TAG, "Sending login request with id: " + id + ", password: " + password);


                // 로그인 요청을 위한 서비스 생성, 여기서 retrofit 연결.
                MainActivityIF apiService = RetrofitClient.getClient().create(MainActivityIF.class);

                // 서버에 로그인 요청
                Call<JsonObject> call = apiService.login(id, password);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject responseBody = response.body();
                            if (responseBody.get("success").getAsBoolean()) {
                                // 로그인 성공
                                int userNumber = 0;
                                if (responseBody.has("user_number") && responseBody.get("user_number").isJsonPrimitive()) {
                                    userNumber = responseBody.get("user_number").getAsInt();
                                    Log.d("usernumber 있음", String.valueOf(userNumber));
                                }else {
                                    Log.d("usernumber 없음", String.valueOf(userNumber));
                                }
                                String userName = responseBody.get("user_name").getAsString();


                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("userNumber", userNumber);
                                editor.putString("user_name", userName);
                                editor.apply();
                                Log.d("SharedPreferences userNumber 저장 값", String.valueOf(userNumber));
                                Log.d("SharedPreferences user_name 저장 값", String.valueOf(userName));
                                getParentFragmentManager().beginTransaction().replace(R.id.containers, mypage).commit();
                            } else {
                                // 로그인 실패
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

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.containers, sign_in).commit();
            }
        });

        text_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_password.setText("DFafdf");
                text_password.setTextColor(Color.parseColor("#e65d5d"));
            }
        });


        return view;
    }
}