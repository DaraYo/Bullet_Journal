package com.example.bullet_journal.model;

import com.example.bullet_journal.enums.RatingCategory;

public class Rating {

    private String id;

    private int rating;

    private long date;

    private String title;

    private String text;

    private RatingCategory category;

    public Rating() {
    }

    public Rating(String id, int rating, long date, String title, String text, RatingCategory category) {
        this.id = id;
        this.rating = rating;
        this.date = date;
        this.title = title;
        this.text = text;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RatingCategory getCategory() {
        return category;
    }

    public void setCategory(RatingCategory category) {
        this.category = category;
    }
}
