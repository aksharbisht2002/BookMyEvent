package com.example.evento.Models;

public class catagoryModel {
    private int Image;
    private String text;

    public catagoryModel(int image, String text) {
        Image = image;
        this.text = text;
    }

    public int getImage() {
        return Image;
    }

    public String getText() {
        return text;
    }
}
