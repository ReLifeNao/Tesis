package com.example.opencv.activity_4;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opencv.R;

import java.util.ArrayList;
import java.util.List;

public class Progress_4 extends AppCompatActivity {

    private TextView userLevel, pointsText, streakText;
    private ProgressBar progressBar;
    private RecyclerView achievementsRecyclerView;
    private AchievementsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress4);

        // Inicializar vistas
        userLevel = findViewById(R.id.userLevel);
        pointsText = findViewById(R.id.pointsText);
        streakText = findViewById(R.id.streakText);
        progressBar = findViewById(R.id.progressBar);
        achievementsRecyclerView = findViewById(R.id.achievementsRecyclerView);

        // Datos ficticios para mostrar progreso
        userLevel.setText("Nivel: 5");
        pointsText.setText("Puntos: 750");
        streakText.setText("🔥 Racha: 15 días seguidos 🔥");
        progressBar.setProgress(70);

        // Configurar RecyclerView de insignias
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AchievementsAdapter(getAchievements());
        achievementsRecyclerView.setAdapter(adapter);
    }

    // Simulación de insignias obtenidas
    private List<AchievementItem> getAchievements() {
        List<AchievementItem> achievements = new ArrayList<>();
        achievements.add(new AchievementItem("Primer Día", R.drawable.badge_day1));
        achievements.add(new AchievementItem("5 Días Seguidos", R.drawable.badge_day2));
        achievements.add(new AchievementItem("10 Días Seguidos", R.drawable.badge_day3));
        achievements.add(new AchievementItem("Maestro de Señas", R.drawable.badge_master));
        return achievements;
    }
}
