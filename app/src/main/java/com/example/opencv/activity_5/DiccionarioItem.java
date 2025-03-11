package com.example.opencv.activity_5;

public class DiccionarioItem {
    private String nombre;
    private int imagenResId;

    public DiccionarioItem(String nombre, int imagenResId) {
        this.nombre = nombre;
        this.imagenResId = imagenResId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagenResId() {
        return imagenResId;
    }
}
