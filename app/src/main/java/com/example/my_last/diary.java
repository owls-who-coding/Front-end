package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.text.LineBreakConfig;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;


public class diary extends Fragment implements CalendarView.OnDateChangeListener {

    CalendarView calendarView;
    Calendar calendar;
    Context context;
    SharedPreferences sharedPreferences;

    TextView y_kg,y_eat,y_count,y_out;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.activity_diary,null);

        context = getActivity();
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);

        calendar = calendar.getInstance();

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return view;

    }
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        showInputDialog(year,month+1,dayOfMonth);
    }

    @SuppressLint("MissingInflatedId")
    private void showInputDialog(int year, int month, int dayOfMonth) {
        LayoutInflater inflater = LayoutInflater.from(context);
        
        View view = inflater.inflate(R.layout.input_diary, null);

        final EditText Kg = view.findViewById(R.id.kg);
        final EditText Eat = view.findViewById(R.id.eat);
        final EditText Count = view.findViewById(R.id.count);
        final EditText Out = view.findViewById(R.id.out);
        final EditText Problem = view.findViewById(R.id.problem);

        String dataTime = getFormattedDateTime();

        String kg = sharedPreferences.getString(dataTime + "_kg","");
        String eat = sharedPreferences.getString(dataTime + "_eat","");
        String count = sharedPreferences.getString(dataTime + "_count","");
        String out = sharedPreferences.getString(dataTime + "_out","");
        String problem = sharedPreferences.getString(dataTime + "_problem","");
        Kg.setText(kg);
        Eat.setText(eat);
        Count.setText(count);
        Out.setText(out);
        Problem.setText(problem);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        TextView titleView = new TextView(context);
        titleView.setText(year+"/"+month+"/"+dayOfMonth);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(0,20,0,0);

        builder.setCustomTitle(titleView);

                builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String kg = Kg.getText().toString();
                        String eat = Eat.getText().toString();
                        String count = Count.getText().toString();
                        String out = Out.getText().toString();
                        String problem = Problem.getText().toString();
                        String dateTime = getFormattedDateTime();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(dateTime + "_kg", kg);
                        editor.putString(dateTime + "_eat", eat);
                        editor.putString(dateTime + "_count", count);
                        editor.putString(dateTime + "_out", out);
                        editor.putString(dateTime + "_problem", problem);
                        editor.apply();

                        Toast.makeText(context, "내용이 저장되었습니다. (" + dateTime + ")", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getFormattedDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}