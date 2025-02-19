package com.example.opencv;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Alfabeto_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alfabeto1);

        // Aplicamos padding para acomodar las insets del sistema (barra de estado, navegación, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Variable que determina si la lección es interactiva o no
        boolean isInteractiveLesson = false; // Cambia a false para lecciones informativas

        // Obtenemos referencias a las vistas que queremos mostrar u ocultar
        // Asegúrate de que estos IDs existan en tu layout (por ejemplo, "textViewDescription")
        View linearLayoutOptions = findViewById(R.id.linearLayoutOptions);
        View textViewDescription = findViewById(R.id.textViewDescription);

        if (isInteractiveLesson) {
            // Lección interactiva: se muestran las opciones y se oculta la descripción
            linearLayoutOptions.setVisibility(View.VISIBLE);
            if (textViewDescription != null) {
                textViewDescription.setVisibility(View.GONE);
            }
        } else {
            // Lección informativa: se ocultan las opciones y se muestra la descripción
            linearLayoutOptions.setVisibility(View.GONE);
            if (textViewDescription != null) {
                textViewDescription.setVisibility(View.VISIBLE);
            }
        }
    }
}
