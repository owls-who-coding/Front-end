package com.example.my_last;


import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class eyes_detection extends AppCompatActivity {
    Preview preview;
    PreviewView previewView;
    private static final String[] PERMISSION = {Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
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
    }

    public void setupCameraX(){
        ProcessCameraProvider cameraProvider;
        try {
            cameraProvider = ProcessCameraProvider.getInstance(getApplicationContext()).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        previewView = (PreviewView) findViewById(R.id.preview_camera);
        System.out.println(previewView.getClass().getSimpleName());

        if(previewView == null){
            Log.d("NullExcetion", "너 널이니..?");
        }
        else{
            Log.d("NullExcetion", "널 아닌데용..?");
        }

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

        //cameraProvider.unbindAll();
    }
}