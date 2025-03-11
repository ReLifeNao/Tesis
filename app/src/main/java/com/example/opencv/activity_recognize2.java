package com.example.opencv;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
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

public class activity_recognize2 extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "activity_recognize2";

    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private objectDetectorClass_v2 objectDetectorClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recognize2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Pedir permisos de cámara si no están concedidos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            Log.d(TAG, "Se solicitó permiso para la cámara.");
        } else {
            Log.d(TAG, "Permiso de cámara concedido.");
        }

        // Inicializar la cámara
        mOpenCvCameraView = findViewById(R.id.frame_Surface);
        if (mOpenCvCameraView != null) {
            Log.d(TAG, "Configurando la vista de la cámara.");
            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
        } else {
            Log.e(TAG, "Error: la vista de la cámara es NULL.");
        }

        try {
            // Cargar modelo de detección de gestos con TensorFlow Lite
            objectDetectorClass = new objectDetectorClass_v2(this, getAssets(),
                    "hand_model.tflite", "label.txt", 320,
                    "modelTest_03_01_all.tflite", 96);
            Log.d(TAG, "Modelo cargado exitosamente.");
        } catch (IOException e) {
            Log.e(TAG, "Error al cargar el modelo.");
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV inicializado correctamente.");
            if (mOpenCvCameraView != null) {
                mOpenCvCameraView.setCameraPermissionGranted();
                mOpenCvCameraView.enableView();
                Log.d(TAG, "Cámara activada.");
            } else {
                Log.e(TAG, "Error: `mOpenCvCameraView` es NULL en onResume.");
            }
        } else {
            Log.e(TAG, "Error: OpenCV no pudo inicializarse.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            Log.d(TAG, "Desactivando la cámara en onPause.");
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            Log.d(TAG, "Cerrando cámara en onDestroy.");
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "Cámara iniciada con resolución: " + width + "x" + height);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        Log.d(TAG, "Cámara detenida.");
        mRgba.release();
        mGray.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        // Procesar imagen con modelo de detección de gestos
        Mat out = objectDetectorClass.recognizeImage(mRgba);
        return out;
    }

    public void updateTextView(String detectedText) {
        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.Letra);
            if (textView != null) {
                textView.setText(detectedText);
            } else {
                Log.e(TAG, "Error: TextView no encontrado.");
            }
        });
    }
}
