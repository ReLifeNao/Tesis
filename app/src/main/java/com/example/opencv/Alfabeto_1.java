package com.example.opencv;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.opencv.Database.DatabaseHelper;
import com.example.opencv.Database.DatabaseManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Alfabeto_1 extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private DatabaseManager dbManager;
    private List<Lesson> lessons = new ArrayList<>();
    private int currentLessonIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alfabeto1);

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbHelper.openDataBase();
        dbHelper.printAllTables();

        dbManager = new DatabaseManager(this);
        dbManager.open();

        // Aplicar padding para acomodar las insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar todas las lecciones
        loadLessons();

        // Configurar el botón para cambiar de lección manualmente
        Button nextButton = findViewById(R.id.button);
        nextButton.setOnClickListener(v -> updateUIWithNextLesson());

        setupOptionButtons();

        // Mostrar la primera lección
        if (!lessons.isEmpty()) {
            updateUIWithLesson(lessons.get(currentLessonIndex));
        }
    }

    private void loadLessons() {
        Cursor cursor = dbManager.getAllLessons();
        if (cursor.moveToFirst()) {
            do {
                int typeIndex = cursor.getColumnIndex("type");
                int imageIndex = cursor.getColumnIndex("image");
                int descriptionIndex = cursor.getColumnIndex("description");
                int option1Index = cursor.getColumnIndex("option1");
                int option2Index = cursor.getColumnIndex("option2");
                int option3Index = cursor.getColumnIndex("option3");

                String type = (typeIndex != -1) ? cursor.getString(typeIndex) : null;
                String image = (imageIndex != -1) ? cursor.getString(imageIndex) : null;
                String description = (descriptionIndex != -1) ? cursor.getString(descriptionIndex) : null;
                String option1 = (option1Index != -1) ? cursor.getString(option1Index) : null;
                String option2 = (option2Index != -1) ? cursor.getString(option2Index) : null;
                String option3 = (option3Index != -1) ? cursor.getString(option3Index) : null;

                lessons.add(new Lesson(type, image, description, option1, option2, option3));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void updateUIWithNextLesson() {
        if (lessons.isEmpty()) return;

        currentLessonIndex = (currentLessonIndex + 1) % lessons.size();
        updateUIWithLesson(lessons.get(currentLessonIndex));
    }

    private void updateUIWithLesson(Lesson lesson) {
        LinearLayout linearLayoutOptions = findViewById(R.id.linearLayoutOptions);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        TextView option1View = findViewById(R.id.buttonOption1);
        TextView option2View = findViewById(R.id.buttonOption2);
        TextView option3View = findViewById(R.id.buttonOption3);
        //ImageView imageView = findViewById(R.id.imageViewMainSign);
        ImageView gifView = findViewById(R.id.imageViewMainSign);
        ImageView image1 = findViewById(R.id.imageViewAngle1);
        ImageView image2 = findViewById(R.id.imageViewAngle2);
        ImageView image3 = findViewById(R.id.imageViewAngle3);

        String baseImageName = lesson.image;
        String image1_str = baseImageName + "_img1";
        String image2_str = baseImageName + "_img2";
        String image3_str = baseImageName + "_img3";

        if ("interactive".equals(lesson.type)) {
            linearLayoutOptions.setVisibility(View.VISIBLE);
            textViewDescription.setVisibility(View.GONE);
            option1View.setText(lesson.option1);
            option2View.setText(lesson.option2);
            option3View.setText(lesson.option3);
        } else {
            linearLayoutOptions.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.VISIBLE);
            textViewDescription.setText(lesson.description);
        }

        int imgResId1 = getResources().getIdentifier(image1_str, "drawable", getPackageName());
        int imgResId2 = getResources().getIdentifier(image2_str, "drawable", getPackageName());
        int imgResId3 = getResources().getIdentifier(image3_str, "drawable", getPackageName());

        Glide.with(this)
                .load(imgResId1 != 0 ? imgResId1 : R.drawable.alfabeto)
                .into(image1);

        Glide.with(this)
                .load(imgResId2 != 0 ? imgResId2 : R.drawable.alfabeto)
                .into(image2);

        Glide.with(this)
                .load(imgResId3 != 0 ? imgResId3 : R.drawable.alfabeto)
                .into(image3);

        int gifResId = getResources().getIdentifier(lesson.image, "drawable", getPackageName());
        GlideApp.with(this).asGif().load(gifResId).into(gifView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }

    private static class Lesson {
        String type;
        String image;
        String description;
        String option1;
        String option2;
        String option3;

        Lesson(String type, String image, String description, String option1, String option2, String option3) {
            this.type = type;
            this.image = image;
            this.description = description;
            this.option1 = option1;
            this.option2 = option2;
            this.option3 = option3;
        }
    }

    private void setupOptionButtons() {
        Button option1Button = findViewById(R.id.buttonOption1);
        Button option2Button = findViewById(R.id.buttonOption2);
        Button option3Button = findViewById(R.id.buttonOption3);

        option1Button.setOnClickListener(v -> checkAnswer(option1Button));
        option2Button.setOnClickListener(v -> checkAnswer(option2Button));
        option3Button.setOnClickListener(v -> checkAnswer(option3Button));
    }

    private void checkAnswer(Button selectedButton) {
        // Aquí debes determinar si la opción seleccionada es la correcta
        boolean isCorrect = isCorrectAnswer(selectedButton);

        if (isCorrect) {
            showCorrectAnswerDialog();
        } else {
            // Manejar la lógica para respuestas incorrectas si es necesario
        }
    }

    private boolean isCorrectAnswer(Button selectedButton) {
        // Implementa la lógica para verificar si la opción seleccionada es la correcta
        // Esto es solo un ejemplo, debes adaptarlo a tu lógica específica
        String correctAnswer = lessons.get(currentLessonIndex).option1; // Cambia esto según tu lógica
        return selectedButton.getText().toString().equals(correctAnswer);
    }

    private void showCorrectAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_correct, null);
        builder.setView(dialogView);

        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(v -> {
            AlertDialog dialog = builder.create();
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
