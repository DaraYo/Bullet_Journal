package com.example.bullet_journal.model;

public class Mood {

    private String id;

    private long date;

    private int rating;

    private String description;

    public Mood() {
    }

    public Mood(String id, long date, int rating, String description) {
        this.id = id;
        this.date = date;
        this.rating = rating;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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
