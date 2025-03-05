package com.example.opencv;

import static android.content.ContentValues.TAG;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.EdgeToEdge;
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
        ImageView imageView = findViewById(R.id.imageViewMainSign);

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

        int imageResId = getResources().getIdentifier(lesson.image, "drawable", getPackageName());
        imageView.setImageResource(imageResId);
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
}
