package com.example.my_last;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.WorkerThread;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
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
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

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
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class eyes_detection extends BaseModuleActivity {
    Preview preview;
    PreviewView previewView;
    private Module mModule = null;
    private ResultView mResultView;

    private Long mLastAnalysisResultTime;
    private static final String[] PERMISSION = {Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;


    //캡쳐 관련
    private Button btn_capture;
    private ImageCapture imageCapture = null;
    private ExecutorService cameraExecutor;
    private File outputDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eyes_detection);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSION,
                    REQUEST_CODE_CAMERA_PERMISSION
            );
        }
        else{
            setupCameraX();
        }
        outputDirectory = getOutputDirectory();
        btn_capture = findViewById(R.id.btn_capture);
        btn_capture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN :
                        btn_capture.setBackgroundResource(R.drawable.circlebuttondown);
                        takePicture();
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_capture.setBackgroundResource(R.drawable.circlebuttonup);
                        break;
                }
                return false;
            }
        });
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    static class AnalysisResult{
        private final ArrayList<Result> mResults;

        public AnalysisResult(ArrayList<Result> results) {mResults = results;}
    }

//    protected TextureView getCameraPreviewTextureView(){
////        mResultView = findViewById(R.id.resultView);
//        return ((ViewStub) findViewById(R.id.object_detection_texture_view_stub))
//                .inflate()
//                .findViewById(R.id.object_detection_texture_view);
//    }
    protected PreviewView getPreviewView(){
        mResultView = findViewById(R.id.resultView);
        return (PreviewView) findViewById(R.id.preview_camera);
    }
    protected void applyToUiAnalyzeImageResult(AnalysisResult result){
        mResultView.setResults(result.mResults);
        mResultView.invalidate();
    }

    private Bitmap imgToBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
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
    public AnalysisResult  analyzeImage(ImageProxy image, int rotationDegrees) {
        try {
            if (mModule == null) {
                mModule = LiteModuleLoader.load(assetFilePath(getApplicationContext(), "best.torchscript.ptl"));
            }
        } catch (IOException e) {
            Log.e("Object Detection", "Error reading assets", e);
            return null;
        }
        Bitmap bitmap = imgToBitmap(image.getImage());
        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);

        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
        final Tensor outputTensor = outputTuple[0].toTensor();
        final float[] outputs = outputTensor.getDataAsFloatArray();

        float imgScaleX = (float)bitmap.getWidth() / PrePostProcessor.mInputWidth;
        float imgScaleY = (float)bitmap.getHeight() / PrePostProcessor.mInputHeight;
        float ivScaleX = (float)mResultView.getWidth() / bitmap.getWidth();
        float ivScaleY = (float)mResultView.getHeight() / bitmap.getHeight();

        final ArrayList<Result> results = PrePostProcessor.outputsToNMSPredictions(outputs, imgScaleX, imgScaleY, ivScaleX, ivScaleY, 0, 0);
        return new AnalysisResult(results);
    }


    public void setupCameraX(){
        mLastAnalysisResultTime = SystemClock.elapsedRealtime();
        previewView = getPreviewView();
        preview = new Preview.Builder()
                //.setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),imageProxy ->{
            if(SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 500){
                imageProxy.close();
                return;
            }

            final AnalysisResult result = analyzeImage(imageProxy, imageProxy.getImageInfo().getRotationDegrees());
            if(result != null){
                mLastAnalysisResultTime = SystemClock.elapsedRealtime();
                runOnUiThread(() -> applyToUiAnalyzeImageResult(result));
                Log.d("Detected","Check 분석중");
            }
            imageProxy.close();
        });


        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(()->{
            try{
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                imageCapture = new ImageCapture.Builder().build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this,cameraSelector, preview, imageAnalysis, imageCapture);

            } catch(ExecutionException | InterruptedException e){
                Log.e("Object Detection", "Error setting up CameraX", e);
            }
        }, ContextCompat.getMainExecutor(this));

        //cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    private void takePicture() {
        if (imageCapture == null) {
            Log.d("Capture", "아직 NULL ㅜㅜ!");
            return;
        }

        File photoFile = new File(
                outputDirectory,
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis()) + ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // 이미지가 저장된 후에 작업을 수행하려면 이 메서드를 사용하십시오.
                Toast.makeText(getApplicationContext(), "캡쳐!",Toast.LENGTH_SHORT);
                Log.d("Capture", "캡쳐!");
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // 이미지 저장에 실패한 경우 여기서 오류를 처리합니다.
                Log.d("Capture", "캡쳐안됐음 ㅜㅜ!");
            }
        });
    }

    private File getOutputDirectory(){
        File[] mediaDirs = getExternalMediaDirs();
        if(mediaDirs.length >0){
            File mediaDir = new File(mediaDirs[0],"My_last");
            mediaDir.mkdir();
            return mediaDir;
        }
        return getFilesDir();
    }
}