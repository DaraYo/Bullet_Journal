package com.example.bullet_journal.model;

public class Mood {

    private String date;

    private int rating;

    public Mood() {
    }

    public Mood(String date, int rating) {
        this.date = date;
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
