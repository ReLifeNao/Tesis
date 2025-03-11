package com.example.opencv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.opencv.activity_3.games_3;
import com.example.opencv.activity_4.Progress_4;
import com.example.opencv.activity_5.Diccionario_5_search;

public class primary_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_primary1);

        // Configuración de Insets para pantalla Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencia al CardView en el layout (asegúrate de que tenga el id "cardViewAlfabeto" en activity_primary1.xml)
        CardView cardViewAlfabeto = findViewById(R.id.cardViewAlfabeto);
        CardView cardViewPrueba = findViewById(R.id.Prueba);
        CardView cardViewGames = findViewById(R.id.games);
        CardView cardViewAdvance = findViewById(R.id.advance);
        CardView cardViewDiccionario = findViewById(R.id.diccionario);


        // Al hacer clic en el CardView se lanza la actividad Alfabeto_1
        cardViewAlfabeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(primary_1.this, Alfabeto_1.class);
                startActivity(intent);
            }
        });

        cardViewPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(primary_1.this, activity_recognize2.class);
                startActivity(intent);
            }
        });

        cardViewGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(primary_1.this, games_3.class);
                startActivity(intent);
            }
        });

        cardViewAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(primary_1.this, Progress_4.class);
                startActivity(intent);
            }
        });

        cardViewDiccionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(primary_1.this, Diccionario_5_search.class);
                startActivity(intent);
            }
        });

    }
}
