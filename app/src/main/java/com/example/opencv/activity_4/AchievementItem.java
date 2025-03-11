package com.example.opencv.activity_4;

public class AchievementItem {
    private String name;
    private int imageResId;

    public AchievementItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
