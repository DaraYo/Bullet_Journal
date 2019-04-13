package com.example.bullet_journal.model;

public class Mood {

    private String date;

    private int rating;

    private String description;

    public Mood() {
    }

    public Mood(String date, int rating, String description) {
        this.date = date;
        this.rating = rating;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
