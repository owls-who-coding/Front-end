package com.example.my_last;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
public class Mypage extends Fragment {

    private TextView txtLoginInfo,txtLogout, doctor, hospital, history, email;
    CustomDialogFragment dialog;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mypage, null);

        dialog = new CustomDialogFragment();

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

        doctor = view.findViewById(R.id.doctorCall);
        history = view.findViewById(R.id.myHistory);
        email = view.findViewById(R.id.devEmail);
        hospital = view.findViewById(R.id.searchHospital);

        View.OnClickListener not_dev = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.setText("준비중인 기능입니다.");
                dialog.show(getFragmentManager(), "custom");
            }
        };

        doctor.setOnClickListener(not_dev);
        hospital.setOnClickListener(not_dev);
        history.setOnClickListener(not_dev);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setText("팀장 : 신규철 - 98rbcjf@naver.com\n\n이준희 - joonhee305@naver.com\n\n윤상일 - yun090405@gmail.com");
                dialog.show(getFragmentManager(), "custom");
            }
        });

        return view;
    }


}