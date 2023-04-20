package com.example.my_last;


import static com.example.my_last.ImageProcessing.imageProxyToBitmap;
import static com.example.my_last.ImageProcessing.imgToBitmap;
import static com.example.my_last.ImageProcessing.rotateBitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.WorkerThread;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonObject;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Eyes_detection extends BaseModuleActivity {
    Preview preview;
    PreviewView previewView;
    private Module mModule = null;
    private ResultView mResultView;

    private Long mLastAnalysisResultTime;
    private static final String[] PERMISSION = {Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;


    //캡쳐 관련
    private Button btn_capture;
    private Button btn_reset;
    private Button btn_gallery;
    private ImageCapture imageCapture = null;
    private ExecutorService cameraExecutor;

    private Bitmap detectedBitmap;
    ImageView imageView;

    View.OnClickListener detectedListener;
    View.OnClickListener cameraResetListener;
    View.OnClickListener captureListener;

    PredictAPI predictAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eyes_detection);

        setButtonListener();
        predictAPI = RetrofitClient.getClient().create(PredictAPI.class);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSION,
                    REQUEST_CODE_CAMERA_PERMISSION
            );
        } else {
            setupCameraX();
        }
        btn_capture = findViewById(R.id.btn_capture);

        btn_capture.setOnClickListener(captureListener);
        btn_reset = findViewById(R.id.btn_camera_reset);
        btn_reset.setOnClickListener(cameraResetListener);
    }
    static class AnalysisResult {
        private final ArrayList<Result> mResults;

        public AnalysisResult(ArrayList<Result> results) {
            mResults = results;
        }
    }

    void setButtonListener() {
        detectedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectedBitmap = Bitmap.createScaledBitmap(detectedBitmap, 224,224,true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                detectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodeImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                Call<JsonObject> predictCall = predictAPI.predict_eyes(encodeImage);
                predictCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject jsonVal = response.body();
                            Toast.makeText(getApplicationContext(), jsonVal.toString(),Toast.LENGTH_SHORT).show();

                        }
                        else{
                            int statusCode=response.code();
                            Log.i("APIError", "Status Code : " + statusCode + "Don't Response");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
                Toast.makeText(getApplicationContext(), "detected", Toast.LENGTH_SHORT).show();
            }
        };
        captureListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_capture.setBackgroundResource(R.drawable.circlebuttondown);
                takePicture();
                btn_capture.setOnClickListener(detectedListener);
            }
        };
        cameraResetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCamera();
            }
        };
    }
    protected PreviewView getPreviewView() {
        mResultView = findViewById(R.id.resultView);
        return (PreviewView) findViewById(R.id.preview_camera);
    }

    protected void applyToUiAnalyzeImageResult(AnalysisResult result) {
        mResultView.setResults(result.mResults);
        mResultView.invalidate();
    }

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    @Nullable
    @WorkerThread
    @OptIn(markerClass = ExperimentalGetImage.class)
    public AnalysisResult analyzeImage(ImageProxy image, int rotationDegrees) {
        try {
            if (mModule == null) {
                mModule = LiteModuleLoader.load(assetFilePath(getApplicationContext(), "best.torchscript.ptl"));
            }
        } catch (IOException e) {
            Log.e("Object Detection", "Error reading assets", e);
            return null;
        }
        Bitmap bitmap = imgToBitmap(image.getImage());
        bitmap = rotateBitmap(bitmap, 90.0f);
        return getDetectResult(bitmap);
    }

    public void setupCameraX() {

        imageView = (ImageView) findViewById(R.id.img_view_capture);
        mLastAnalysisResultTime = SystemClock.elapsedRealtime();
        previewView = getPreviewView();
        preview = new Preview.Builder()
                //.setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(480,720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
            if (SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 300) {
                imageProxy.close();
                return;
            }

            final AnalysisResult result = analyzeImage(imageProxy, imageProxy.getImageInfo().getRotationDegrees());
            if (result != null) {
                mLastAnalysisResultTime = SystemClock.elapsedRealtime();
                runOnUiThread(() -> applyToUiAnalyzeImageResult(result));
                Log.d("Detected", "Check 분석중");
            }
            imageProxy.close();
            return;
        });


        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                imageCapture = new ImageCapture.Builder().build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("Object Detection", "Error setting up CameraX", e);
            }
        }, ContextCompat.getMainExecutor(this));

        //cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    private void takePicture() {
        btn_capture.setBackgroundResource(R.drawable.detected_icon);

        if (imageCapture == null) {
            Log.d("Capture", "아직 NULL ㅜㅜ!");
            return;
        }
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            @OptIn(markerClass = ExperimentalGetImage.class)
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);
                Integer str = image.getImage().getFormat();
                Log.d("ImageType2", str.toString());

                Bitmap captureImageBitmap = imageProxyToBitmap(image);
                captureImageBitmap = Bitmap.createScaledBitmap(captureImageBitmap, mResultView.getWidth(),mResultView.getHeight(), true);
                AnalysisResult analysisResult = getDetectResult(captureImageBitmap);

//                Log.d("caputreImageBitmapSize ", width.toString() + " , "+ height.toString());
                if(analysisResult.mResults.size() == 0){
                    Toast.makeText(getApplicationContext(), "눈을 확인할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Rect rect = analysisResult.mResults.get(0).rect;
                    captureImageBitmap = ImageProcessing.cropBitmap(captureImageBitmap, rect);

                    captureImageBitmap = Bitmap.createScaledBitmap(captureImageBitmap, 480,  480, true);
                }
                captureImageBitmap = rotateBitmap(captureImageBitmap, 90.0f);
               // captureImageBitmap = Bitmap.createScaledBitmap(captureImageBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
                imageView.setImageBitmap(captureImageBitmap);
                imageView.setVisibility(View.VISIBLE);
                mResultView.setVisibility(View.INVISIBLE);
                previewView.setVisibility(View.INVISIBLE);
                detectedBitmap = captureImageBitmap;
                // 작업이 끝난 후 반드시 ImageProxy를 닫아야 합니다.
                image.close();
            }
        });
    }

    private void resetCamera() {
        imageView.setVisibility(View.INVISIBLE);
        mResultView.setVisibility(View.VISIBLE);
        previewView.setVisibility(View.VISIBLE);
        btn_capture.setOnClickListener(captureListener);
        btn_capture.setBackgroundResource(R.drawable.circlebuttonup);
    }

    private AnalysisResult getDetectResult(Bitmap mBitmap){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();

        float mImgScaleX = (float) mBitmap.getWidth() / PrePostProcessor.mInputWidth;
        float mImgScaleY = (float) mBitmap.getHeight() / PrePostProcessor.mInputHeight;
        float mIvScaleX = (float) mResultView.getWidth() / mBitmap.getWidth();
        float mIvScaleY = (float) mResultView.getHeight() / mBitmap.getHeight();


        final Tensor outputTensor = outputTuple[0].toTensor();
        final float[] outputs = outputTensor.getDataAsFloatArray();
        final ArrayList<Result> results =  PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, 0, 0);

        return new AnalysisResult(results);
    }
}
