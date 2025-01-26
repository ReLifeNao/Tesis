package com.example.opencv;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.DelegateFactory;
import org.tensorflow.lite.Delegate;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;

public class CameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "CameraActivity";

    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private objectDetectorClass objectDetectorClass;

    public CameraActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() started");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int MY_PERMISSIONS_REQUEST_CAMERA = 0;

        // Pedir permisos de cámara si no están concedidos
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            Log.d(TAG, "Camera permission denied, requesting permission.");
        }
        else {
            Log.d(TAG, "Camera permission granted.");
        }

        setContentView(R.layout.activity_camera);

        // Configuración de la vista de la cámara
        Log.d(TAG, "Setting up camera view.");
        mOpenCvCameraView = findViewById(R.id.frame_Surface);
        if (mOpenCvCameraView != null) {
            Log.d(TAG, "Camera view found and being configured.");
            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
        } else {
            Log.e(TAG, "Camera view is null!");
        }

       // mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        //mOpenCvCameraView.setCvCameraViewListener(this);

        try {
            // Cargar el modelo de detección
            objectDetectorClass = new objectDetectorClass( this,getAssets(), "hand_model.tflite", "label.txt", 320, "modelTest_03_01_all.tflite", 96);
            Log.d(TAG, "Model is successfully loaded");
        } catch (IOException e) {
            Log.d(TAG, "Error loading model");
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV initialized successfully");
            if (mOpenCvCameraView != null) {
                Log.d(TAG, "Enabling camera view.");
                mOpenCvCameraView.setCameraPermissionGranted();
                mOpenCvCameraView.enableView();  // Activa la cámara
                if (mOpenCvCameraView.isEnabled()) {
                    Log.d(TAG, "Camera view is enabled successfully.");
                } else {
                    Log.e(TAG, "Failed to enable the camera view.");

                }
            } else {
                Log.e(TAG, "mOpenCvCameraView is null in onResume!");
            }
        } else {
            Log.e(TAG, "OpenCV initialization failed");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called.");
        if (mOpenCvCameraView != null) {
            Log.d(TAG, "Disabling camera view in onPause.");
            mOpenCvCameraView.disableView();
        } else {
            Log.e(TAG, "mOpenCvCameraView is null in onPause!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }



    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "onCameraViewStarted() called with width: " + width + " and height: " + height);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
        Log.d(TAG, "Mats for mRgba and mGray initialized.");
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.d(TAG, "!on camera frame");
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        // Llamar a la función de detección de objetos
        Mat out = objectDetectorClass.recognizeImage(mRgba);
        Log.d(TAG, "Output Image Size: " + out.size());

        return out;
    }

    public void updateTextView(String detectedText) {
        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.Letra); // Asegúrate de que este sea el ID de tu TextView
            textView.setText(detectedText);
        });
    }
}