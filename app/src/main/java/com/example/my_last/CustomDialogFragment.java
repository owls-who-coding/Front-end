package com.example.my_last;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.my_last.R;

public class CustomDialogFragment extends DialogFragment {
    Button cancle, ok;
    TextView textView;

    private String s;
    public void setText(String str){
        this.s = str;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 커스텀 레이아웃을 다이얼로그에 설정합니다.
        // 'custom_dialog'는 커스텀 레이아웃의 리소스 ID입니다.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dalog_custom_my, null);
        builder.setView(view);
        cancle = view.findViewById(R.id.btnCancel);
        ok=view.findViewById(R.id.btnOk);
        textView = view.findViewById(R.id.dialtxt);

        textView.setText(s);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }


}