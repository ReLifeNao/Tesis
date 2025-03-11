package com.example.opencv.activity_3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.opencv.R;

import java.util.ArrayList;
import java.util.Collections;

public class games_3 extends AppCompatActivity implements View.OnClickListener {

    private final int[] drawableCardIds = {R.drawable.sign_a, R.drawable.sign_b, R.drawable.sign_c,R.drawable.letter_2x,
            R.drawable.letter_3x,R.drawable.letter_4x,R.drawable.letra_c,R.drawable.letter_g,R.drawable.insignia};

    private CardInfo cardInfo1 = null;
    private int matchCount = 0;
    private boolean isWaiting = false;
    private TextView textViewGameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_games3);

        textViewGameStatus = findViewById(R.id.scoreTextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNewGame();
    }

    private void setupNewGame() {
        ArrayList<Integer> shuffledDrawableIds = new ArrayList<>();
        for (int drawableId : drawableCardIds) {
            shuffledDrawableIds.add(drawableId);
            shuffledDrawableIds.add(drawableId);
        }
        Collections.shuffle(shuffledDrawableIds);

        GridLayout cardGrid = findViewById(R.id.cardGrid);
        cardGrid.removeAllViews();
        cardGrid.setColumnCount(3);

        int cardSizeInPixels = (int) (100 * getResources().getDisplayMetrics().density);

        for (int i = 0; i < shuffledDrawableIds.size(); i++) {
            int drawableId = shuffledDrawableIds.get(i);

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.covered_tile);
            imageView.setOnClickListener(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cardSizeInPixels;
            params.height = cardSizeInPixels;
            params.setMargins(16, 16, 16, 16);
            imageView.setLayoutParams(params);

            CardInfo cardInfo = new CardInfo(drawableId, imageView); // Pasa el ImageView directamente
            imageView.setTag(cardInfo);

            cardGrid.addView(imageView);
        }

        cardInfo1 = null;
        matchCount = 0;
        isWaiting = false;
        textViewGameStatus.setText("Puntaje : " + matchCount);
    }

    @Override
    public void onClick(View v) {
        if (!isWaiting) {
            ImageView imageView = (ImageView) v;
            CardInfo cardInfo = (CardInfo) imageView.getTag();

            if (!cardInfo.isFlipped() && !cardInfo.isMatched()) {
                imageView.setImageResource(cardInfo.getDrawableId());
                cardInfo.setFlipped(true);

                if (cardInfo1 == null) {
                    cardInfo1 = cardInfo;
                } else {
                    if (cardInfo.getDrawableId() == cardInfo1.getDrawableId()) {
                        // Coincidencia
                        matchCount++;
                        cardInfo1.setMatched(true);
                        cardInfo.setMatched(true);

                        textViewGameStatus.setText("Puntaje : " + matchCount);

                        if (matchCount == drawableCardIds.length) {
                            textViewGameStatus.setText("¡Juego Completado!");
                            mostrarAlertaJuegoCompletado();
                        }
                        cardInfo1 = null;
                    } else {
                        // No coincidencia
                        isWaiting = true;
                        imageView.postDelayed(() -> {
                            isWaiting = false;
                            cardInfo.coverCard();
                            cardInfo1.coverCard();
                            cardInfo1 = null;
                        }, 2000);
                    }
                }
            }
        }
    }

    private void mostrarAlertaJuegoCompletado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}