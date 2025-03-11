package com.example.opencv.activity_5;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opencv.R;
import com.example.opencv.activity_5.Diccionario_5_find;

import java.util.ArrayList;
import java.util.List;

public class Diccionario_5_search extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView searchResults;
    private DiccionarioAdapter adapter;
    private List<DiccionarioItem> listaCompleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diccionario5_search);

        // Ajuste de márgenes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        searchBar = findViewById(R.id.searchBar);
        searchResults = findViewById(R.id.searchResults);
        Button diccionarioFindButton = findViewById(R.id.button2);

        // Configurar RecyclerView
        searchResults.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos en la lista
        listaCompleta = obtenerListaSeñas();

        // Configurar el adaptador con contexto
        adapter = new DiccionarioAdapter(listaCompleta, this);
        searchResults.setAdapter(adapter);

        // Búsqueda en tiempo real
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botón para otra actividad
        diccionarioFindButton.setOnClickListener(v -> {
            Intent intent = new Intent(Diccionario_5_search.this, Diccionario_5_find.class);
            startActivity(intent);
        });
    }

    // Método para filtrar la lista en tiempo real
    private void filtrar(String texto) {
        List<DiccionarioItem> listaFiltrada = new ArrayList<>();
        for (DiccionarioItem item : listaCompleta) {
            if (item.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(item);
            }
        }
        adapter.actualizarLista(listaFiltrada);
    }

    // Método para obtener datos de ejemplo
    private List<DiccionarioItem> obtenerListaSeñas() {
        List<DiccionarioItem> lista = new ArrayList<>();
        lista.add(new DiccionarioItem("A", R.drawable.sign_a));
        lista.add(new DiccionarioItem("B", R.drawable.sign_b));
        lista.add(new DiccionarioItem("C", R.drawable.sign_c));
        lista.add(new DiccionarioItem("Tomori", R.drawable.letter_2x));
        lista.add(new DiccionarioItem("Carta", R.drawable.covered_tile));
        return lista;
    }
}
