package com.example.my_last;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class diary extends Fragment implements CalendarView.OnDateChangeListener {

    CalendarView calendarView;
    Calendar calendar;
    Context context;
    SharedPreferences sharedPreferences;

    TextView y_kg,y_eat,y_count,y_out;

    LineChart lineChart;

    public int selectedYear;
    public int selectedMonth;
    public int selectedDay;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.activity_diary,null);

        context = getActivity();
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);

        calendar = calendar.getInstance();

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        lineChart = (LineChart) view.findViewById(R.id.chart);

        setupLineChart();



        return view;

    }

    private void setupLineChart() {
        // 라인 차트의 설명 설정
        Description description = new Description();
        description.setText("지난 7일");
        description.setTextColor(Color.BLACK);
        lineChart.setDescription(description);

        // 라인 차트의 레전드 설정
        lineChart.getLegend().setEnabled(false);

        // 라인 차트의 X축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);

        // 라인 차트의 Y축 설정
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setTextColor(Color.BLACK);

        // 라인 차트의 배경색 설정
        lineChart.setBackgroundColor(Color.WHITE);

        // 라인 차트의 애니메이션 설정
        lineChart.animateXY(1000, 1000);
    }

    private void updateLineChart() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedYear,selectedMonth,selectedDay);
        startDate.add(Calendar.DAY_OF_MONTH,-7);

//        String s = Integer.toString(startDate.get(Calendar.YEAR)) + Integer.toString(startDate.get(Calendar.MONTH)) + Integer.toString(startDate.get(Calendar.DAY_OF_MONTH)) ;
//        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedYear,selectedMonth,selectedDay);

        // 라인 차트에 표시할 데이터 리스트 생성
        List<Entry> entries = new ArrayList<>();

        Calendar currentDate = (Calendar) startDate.clone();

        int count = 0;

        while(currentDate.compareTo(endDate) <= 0){

            String str = Integer.toString(currentDate.get(Calendar.MONTH)) + "." + Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));


            // SharedPreferences에서 해당 날짜의 Kg 값을 가져옴
            float weight = getWeightFromSharedPreferences(currentDate);


            Entry entry = new Entry(count++,weight);

            // 그래프 데이터에 날짜와 Kg 값을 추가
            entries.add(entry);

            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 라인 차트에 데이터 설정
        LineDataSet dataSet = new LineDataSet(entries, "KG");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // 차트 갱신
    }

    public float getWeightFromSharedPreferences(Calendar currentDate){

//        String key = year + "/" +"0"+ month + "/" + day;
//        Float weight = sharedPreferences.getFloat(key, 0.0f);

        String y = Integer.toString(currentDate.get(Calendar.YEAR));
        String m = Integer.toString(currentDate.get(Calendar.MONTH));
        String d = Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

        String dateTime = y+"/"+"0"+m+"/"+d;

        String kg = sharedPreferences.getString(dateTime + "_kg","");

//        Toast.makeText(context, "("+kg+"!!!)", Toast.LENGTH_SHORT).show();

//        float Kg = Float.parseFloat(kg);

        if(kg == ""){
            return 0;
        }else{
            return Float.parseFloat(kg);
        }
    }


    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        selectedYear = year;
        selectedMonth = month+1;
        selectedDay = dayOfMonth;

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

        String dataTime = year+"/"+"0"+month+"/"+dayOfMonth;

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
                        String dateTime = year+"/"+"0"+month+"/"+dayOfMonth;

//                        Toast.makeText(context, dateTime, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(dateTime + "_kg", kg);
                        editor.putString(dateTime + "_eat", eat);
                        editor.putString(dateTime + "_count", count);
                        editor.putString(dateTime + "_out", out);
                        editor.putString(dateTime + "_problem", problem);
                        editor.apply();

//                        Toast.makeText(context, "내용이 저장되었습니다. (" + dateTime + ")", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, kg+"!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, kg+"!!", Toast.LENGTH_SHORT).show();
                        updateLineChart();

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