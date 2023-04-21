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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    Button button;


    LineChart lineChart_kg,lineChart_eat,lineChart_count,lineChart_out;

    RadioGroup radioGroup;

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

        lineChart_kg = (LineChart) view.findViewById(R.id.kg_chart);
        lineChart_eat = (LineChart) view.findViewById(R.id.eat_chart);
        lineChart_count = (LineChart) view.findViewById(R.id.count_chart);
        lineChart_out = (LineChart) view.findViewById(R.id.out_chart);
        button = (Button) view.findViewById(R.id.button);

        setupLineChart();

        radioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.kg_graph:
                        updateKgLineChart();
                        lineChart_eat.setVisibility(View.GONE);
                        lineChart_count.setVisibility(View.GONE);
                        lineChart_out.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        lineChart_kg.setVisibility(View.VISIBLE);
                        break;
                    case R.id.eat_graph:
                        updateEatLineChart();
                        lineChart_kg.setVisibility(View.GONE);
                        lineChart_count.setVisibility(View.GONE);
                        lineChart_out.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        lineChart_eat.setVisibility(View.VISIBLE);
                        break;
                    case R.id.count_graph:
                        updateCountLineChart();
                        lineChart_kg.setVisibility(View.GONE);
                        lineChart_eat.setVisibility(View.GONE);
                        lineChart_out.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        lineChart_count.setVisibility(View.VISIBLE);
                        break;
                    case R.id.out_graph:
                        updateOutLineChart();
                        lineChart_kg.setVisibility(View.GONE);
                        lineChart_eat.setVisibility(View.GONE);
                        lineChart_count.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        lineChart_out.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Toast.makeText(context, "버튼을 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;

    }

    private void setupLineChart() {
        // 라인 차트의 설명 설정
        Description description = new Description();
        description.setText("지난 7일");
        description.setTextColor(Color.BLACK);
        lineChart_kg.setDescription(description);
        // 라인 차트의 레전드 설정
        lineChart_kg.getLegend().setEnabled(false);

        lineChart_eat.setDescription(description);
        // 라인 차트의 레전드 설정
        lineChart_eat.getLegend().setEnabled(false);

        lineChart_count.setDescription(description);
        // 라인 차트의 레전드 설정
        lineChart_count.getLegend().setEnabled(false);

        lineChart_out.setDescription(description);
        // 라인 차트의 레전드 설정
        lineChart_out.getLegend().setEnabled(false);

        // 라인 차트의 X축 설정
        XAxis xAxis_kg = lineChart_kg.getXAxis();
        xAxis_kg.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_kg.setTextColor(Color.BLACK);

        XAxis xAxis_eat = lineChart_eat.getXAxis();
        xAxis_eat.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_eat.setTextColor(Color.BLACK);

        XAxis xAxis_count = lineChart_count.getXAxis();
        xAxis_count.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_count.setTextColor(Color.BLACK);

        XAxis xAxis_out = lineChart_out.getXAxis();
        xAxis_out.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_out.setTextColor(Color.BLACK);

        lineChart_kg.getAxisRight().setEnabled(false);
        lineChart_kg.getAxisLeft().setTextColor(Color.BLACK);
        lineChart_kg.setBackgroundColor(Color.WHITE);
        lineChart_kg.animateXY(1000, 1000);
        lineChart_kg.getXAxis().setDrawGridLines(false);
        lineChart_kg.getAxisLeft().setDrawGridLines(false);
        lineChart_kg.getAxisRight().setDrawGridLines(false);

        lineChart_eat.getAxisRight().setEnabled(false);
        lineChart_eat.getAxisLeft().setTextColor(Color.BLACK);
        lineChart_eat.setBackgroundColor(Color.WHITE);
        lineChart_eat.animateXY(1000, 1000);
        lineChart_eat.getXAxis().setDrawGridLines(false);
        lineChart_eat.getAxisLeft().setDrawGridLines(false);
        lineChart_eat.getAxisRight().setDrawGridLines(false);

        lineChart_count.getAxisRight().setEnabled(false);
        lineChart_count.getAxisLeft().setTextColor(Color.BLACK);
        lineChart_count.setBackgroundColor(Color.WHITE);
        lineChart_count.animateXY(1000, 1000);
        lineChart_count.getXAxis().setDrawGridLines(false);
        lineChart_count.getAxisLeft().setDrawGridLines(false);
        lineChart_count.getAxisRight().setDrawGridLines(false);

        lineChart_out.getAxisRight().setEnabled(false);
        lineChart_out.getAxisLeft().setTextColor(Color.BLACK);
        lineChart_out.setBackgroundColor(Color.WHITE);
        lineChart_out.animateXY(1000, 1000);
        lineChart_out.getXAxis().setDrawGridLines(false);
        lineChart_out.getAxisLeft().setDrawGridLines(false);
        lineChart_out.getAxisRight().setDrawGridLines(false);
    }

    private void updateKgLineChart() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedYear,selectedMonth,selectedDay);
        startDate.add(Calendar.DAY_OF_MONTH,-7);

        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedYear,selectedMonth,selectedDay);

        // 라인 차트에 표시할 데이터 리스트 생성
        List<Entry> entries = new ArrayList<>();

        Calendar currentDate = (Calendar) startDate.clone();

        int sum = 0;

        while(currentDate.compareTo(endDate) <= 0){

            String str = Integer.toString(currentDate.get(Calendar.MONTH)) + "." + Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

            // SharedPreferences에서 해당 날짜의 Kg 값을 가져옴
            float weight = getWeightFromSharedPreferences(currentDate);

            Entry entry = new Entry(sum++,weight);

            // 그래프 데이터에 날짜와 Kg 값을 추가
            entries.add(entry);
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 라인 차트에 데이터 설정
        LineDataSet dataSet = new LineDataSet(entries, "KG");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        lineChart_kg.setData(lineData);
        lineChart_kg.invalidate(); // 차트 갱신
    }

    private void updateEatLineChart() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedYear,selectedMonth,selectedDay);
        startDate.add(Calendar.DAY_OF_MONTH,-7);

        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedYear,selectedMonth,selectedDay);

        // 라인 차트에 표시할 데이터 리스트 생성
        List<Entry> entries = new ArrayList<>();

        Calendar currentDate = (Calendar) startDate.clone();

        int sum = 0;

        while(currentDate.compareTo(endDate) <= 0){

            String str = Integer.toString(currentDate.get(Calendar.MONTH)) + "." + Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

            // SharedPreferences에서 해당 날짜의 Kg 값을 가져옴
            float eat = getEatFromSharedPreferences(currentDate);

            Entry entry = new Entry(sum++,eat);

            // 그래프 데이터에 날짜와 Kg 값을 추가
            entries.add(entry);
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 라인 차트에 데이터 설정
        LineDataSet dataSet = new LineDataSet(entries, "KG");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        lineChart_eat.setData(lineData);
        lineChart_eat.invalidate(); // 차트 갱신
    }


    private void updateCountLineChart() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedYear,selectedMonth,selectedDay);
        startDate.add(Calendar.DAY_OF_MONTH,-7);

        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedYear,selectedMonth,selectedDay);

        // 라인 차트에 표시할 데이터 리스트 생성
        List<Entry> entries = new ArrayList<>();

        Calendar currentDate = (Calendar) startDate.clone();

        int sum = 0;

        while(currentDate.compareTo(endDate) <= 0){

            String str = Integer.toString(currentDate.get(Calendar.MONTH)) + "." + Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

            // SharedPreferences에서 해당 날짜의 Kg 값을 가져옴
            float count = getCountFromSharedPreferences(currentDate);

            Entry entry = new Entry(sum++,count);

            // 그래프 데이터에 날짜와 Kg 값을 추가
            entries.add(entry);
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 라인 차트에 데이터 설정
        LineDataSet dataSet = new LineDataSet(entries, "KG");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        lineChart_count.setData(lineData);
        lineChart_count.invalidate(); // 차트 갱신
    }

    private void updateOutLineChart() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedYear,selectedMonth,selectedDay);
        startDate.add(Calendar.DAY_OF_MONTH,-7);

        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedYear,selectedMonth,selectedDay);

        // 라인 차트에 표시할 데이터 리스트 생성
        List<Entry> entries = new ArrayList<>();

        Calendar currentDate = (Calendar) startDate.clone();

        int sum = 0;

        while(currentDate.compareTo(endDate) <= 0){

            String str = Integer.toString(currentDate.get(Calendar.MONTH)) + "." + Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

            // SharedPreferences에서 해당 날짜의 Kg 값을 가져옴
            float out = getOutFromSharedPreferences(currentDate);

            Entry entry = new Entry(sum++,out);

            // 그래프 데이터에 날짜와 Kg 값을 추가
            entries.add(entry);
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 라인 차트에 데이터 설정
        LineDataSet dataSet = new LineDataSet(entries, "OUT");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        lineChart_out.setData(lineData);
        lineChart_out.invalidate(); // 차트 갱신
    }

    public float getWeightFromSharedPreferences(Calendar currentDate){

        String y = Integer.toString(currentDate.get(Calendar.YEAR));
        String m = Integer.toString(currentDate.get(Calendar.MONTH));
        String d = Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

        String dateTime = y+"/"+"0"+m+"/"+d;

        String kg = sharedPreferences.getString(dateTime + "_kg","");

        if(kg.isEmpty()){
            return 0;
        }else{
            return Float.parseFloat(kg);
        }
    }

    public float getEatFromSharedPreferences(Calendar currentDate){

        String y = Integer.toString(currentDate.get(Calendar.YEAR));
        String m = Integer.toString(currentDate.get(Calendar.MONTH));
        String d = Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

        String dateTime = y+"/"+"0"+m+"/"+d;

        String eat = sharedPreferences.getString(dateTime + "_eat","");

        if(eat.isEmpty()){
            return 0;
        }else{
            return Float.parseFloat(eat);
        }
    }

    public float getCountFromSharedPreferences(Calendar currentDate){

        String y = Integer.toString(currentDate.get(Calendar.YEAR));
        String m = Integer.toString(currentDate.get(Calendar.MONTH));
        String d = Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

        String dateTime = y+"/"+"0"+m+"/"+d;

        String count = sharedPreferences.getString(dateTime + "_count","");

        if(count.isEmpty()){
            return 0;
        }else{
            return Float.parseFloat(count);
        }
    }

    public float getOutFromSharedPreferences(Calendar currentDate){

        String y = Integer.toString(currentDate.get(Calendar.YEAR));
        String m = Integer.toString(currentDate.get(Calendar.MONTH));
        String d = Integer.toString(currentDate.get(Calendar.DAY_OF_MONTH));

        String dateTime = y+"/"+"0"+m+"/"+d;

        String out = sharedPreferences.getString(dateTime + "_out","");

        if(out.isEmpty()){
            return 0;
        }else{
            return Float.parseFloat(out);
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

                        Toast.makeText(context, "내용이 저장되었습니다. (" + dateTime + ")", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, kg+"!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, kg+"!!", Toast.LENGTH_SHORT).show();

                        updateKgLineChart();
                        updateEatLineChart();
                        updateCountLineChart();
                        updateOutLineChart();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        updateKgLineChart();
                        updateEatLineChart();
                        updateCountLineChart();
                        updateOutLineChart();

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