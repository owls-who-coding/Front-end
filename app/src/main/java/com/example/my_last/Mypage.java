package com.example.my_last;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class Mypage extends Fragment {

    private TextView txtLoginInfo,txtLogout;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mypage, null);

        txtLogout = view.findViewById(R.id.txtLogout); // 로그아웃 버튼을 레이아웃에서 찾습니다.
        txtLoginInfo = view.findViewById(R.id.viewcheckLoginInfo); // 로그인 정보를 출력할 텍스트 뷰를 레이아웃에서 찾습니다.

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 처리: Shared Preferences의 로그인 정보를 삭제합니다.
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // 로그인 화면으로 이동
                Login login = new Login();
                getParentFragmentManager().beginTransaction().replace(R.id.containers, login).commit();
            }
        });


                // Shared Preferences에서 로그인 정보를 가져옵니다.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        int userNumber = sharedPreferences.getInt("userNumber", 0);
        String userName = sharedPreferences.getString("user_name", "");

        // 로그인 정보를 텍스트 뷰에 출력합니다.
        String loginInfo =  "안녕하세요 " + userName + "님!";
        txtLoginInfo.setText(loginInfo);


        return view;
    }
}