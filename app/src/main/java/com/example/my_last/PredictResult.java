package com.example.my_last;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Set;

public class PredictResult extends Fragment {

    HashMap<String, Float> diseasePercent;
    HashMap<String, TextView> diseaseChart;
    HashMap<String, LinearLayout> diseaseInfo;

    //종료 뷰
    ImageView predictClose;
    //상태 레이아웃
    TextView statusTitle, statusComment;
    ImageView statusImage;

    //이미지 레이아웃
    ImageView captureImageView;
    TextView topPercent, topComment;

    //차트 레이아웃
    TextView chartCon, chartLek, chartBle, chartEup;

    //질병 파트 레이아웃
    TextView administerTitle, administerComment;

    //질병 정보 레이아웃
    LinearLayout infoCon, infoLek, infoBle, infoEup, infoCommunity;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predict_result,null);
        initViews(view);
        initDiseasePercent(view);
        predictClose = view.findViewById(R.id.predict_close);
        predictClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        if(getArguments() != null){
            String imageBase64 = getArguments().getString("image_key");
            String predictResult = getArguments().getString("result_key");
            Bitmap eyesImage = ImageProcessing.base64ToBitmap(imageBase64);
            eyesImage = getRoundedBitmap(eyesImage, 15);
//            GradientDrawable gd = new GradientDrawable();
//            gd.setCornerRadius(25);
            captureImageView.setImageBitmap(eyesImage);
//            captureImageView.setBackgroundResource(R.drawable.border);

            Gson gson = new Gson();
            JsonObject result = gson.fromJson(predictResult, JsonObject.class);
            Set<String> keys = result.keySet();

            if(keys.contains("증상 없음")){
                setGoodResult(view);
            }
            else{
                for(String k : keys){
                    diseasePercent.put(k, result.get(k).getAsFloat());
                }

                setBadResult(view);
            }

        }
        return view;
    }

    public Bitmap getRoundedBitmap(Bitmap bitmap, int dp) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final int roundPx = convertDpToPixel(dp);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    int convertDpToPixel(int dp){
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }
    void initDiseasePercent(View view){
        chartCon = view.findViewById(R.id.conjunctivitis_chart);
        chartLek = view.findViewById(R.id.leukaemia_chart);
        chartBle = view.findViewById(R.id.blepharitis_chart);
        chartEup = view.findViewById(R.id.euphorbia_chart);

        infoCon = view.findViewById(R.id.predict_administer_conjunctivitis);
        infoLek = view.findViewById(R.id.predict_administer_leukaemia);
        infoBle = view.findViewById(R.id.predict_administer_blepharitis);
        infoEup = view.findViewById(R.id.predict_administer_euphorbia);
        infoCommunity = view.findViewById(R.id.predict_administer_community);


        chartCon.setHeight(10);
        diseaseChart = new HashMap<>();
        diseasePercent = new HashMap<>();
        diseaseInfo = new HashMap<>();


        diseaseChart.put("결막염", chartCon);
        diseaseChart.put("백내장", chartLek);
        diseaseChart.put("안검염", chartBle);
        diseaseChart.put("유루증", chartEup);

        diseaseInfo.put("결막염", infoCon);
        diseaseInfo.put("백내장", infoLek);
        diseaseInfo.put("안검염", infoBle);
        diseaseInfo.put("유루증", infoEup);


        for(String keys : diseaseChart.keySet()){
            setChartHeight(20,diseaseChart.get(keys));
            setNormalChartBar(diseaseChart.get(keys));

            diseaseInfo.get(keys).setVisibility(View.GONE);
        }
        return;
    }
    GradientDrawable getChartTopDrawable(int color){
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color); // Change this to the color you want
        gd.setCornerRadius(25);
        return gd;
    }

    void setChartColor(int color, TextView chartBar){
        chartBar.setBackground(getChartTopDrawable(color));
    }

    void setChartHeight(int dp, TextView chartBar){
        ViewGroup.LayoutParams params = chartBar.getLayoutParams();
        params.height = convertDpToPixel(dp);
        chartBar.setLayoutParams(params);
    }

    void setNormalChartBar(TextView chartBar){
        int color = Color.rgb(0xAA,0xAA,0xAA);
        setChartColor(color, chartBar);
    }
    void setTopChartBar(TextView chartBar){
        int color = Color.argb(0xA9,0xE4,0x62,0x1D);
        setChartColor(color, chartBar);
    }
    void initViews(View view){
        statusTitle = view.findViewById(R.id.predict_tv_title);
        statusComment = view.findViewById(R.id.predict_tv_subtitle);
        statusImage = view.findViewById(R.id.predict_ic_status);

        captureImageView = view.findViewById(R.id.predict_imv_capture);
        topPercent = view.findViewById(R.id.predict_tv_percent);
        topComment = view.findViewById(R.id.predict_tv_percent_comment);


    }
    void setGoodResult(View view){
        statusTitle.setText("상태가 좋아요");
        statusImage.setBackgroundResource(R.drawable.ic_smile);
        statusComment.setText("지금 상태를 계속 유지하세요");

        topPercent.setText("GOOD");
        topComment.setText("검출된 질병이 없어요");
    }
    void setBadResult(View view){
        statusTitle.setText("주의가 필요해요");
        statusImage.setBackgroundResource(R.drawable.ic_sad);
        statusComment.setText("눈 건강에 관리가 필요해요");

        Integer top = 0;
        String topKey = new String();
        for(String key : diseasePercent.keySet()){
            Integer value = diseasePercent.get(key).intValue();
            if(value > top){
                topKey = key;
                top = value;
            }
            setChartHeight(value * 2, diseaseChart.get(key));
            diseaseInfo.get(key).setVisibility(View.VISIBLE);
        }

        setTopChartBar(diseaseChart.get(topKey));

        topPercent.setText(top.toString()+"%");
        topComment.setText(topKey+"의 확률이 제일 높아요");
    }
}