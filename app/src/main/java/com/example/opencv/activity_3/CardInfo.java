package com.example.opencv.activity_3;

import android.widget.ImageView;

import com.example.opencv.R;

public class CardInfo {
    private int drawableId;
    private boolean isFlipped;
    private boolean isMatched;
    private ImageView imageView;

    public CardInfo(int drawableId, ImageView imageView) {
        this.drawableId = drawableId;
        this.imageView = imageView;
        this.isFlipped = false;
        this.isMatched = false;
    }

    // Getters y setters
    public ImageView getImageView() {
        return imageView;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    // Método para ocultar la carta
    public void coverCard() {
        if (imageView != null) {
            imageView.setImageResource(R.drawable.covered_tile);
            this.isFlipped = false;
        }
    }
}
