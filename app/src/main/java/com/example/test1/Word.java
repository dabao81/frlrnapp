package com.example.test1;

public class Word {
    private String word;
    private String hint;
    private int imageResourceId;

    public Word(String word, String hint, int imageResourceId) {
        this.word = word;
        this.hint = hint;
        this.imageResourceId = imageResourceId;
    }

    public String getWord() {
        return word;
    }

    public String getHint() {
        return hint;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
} 