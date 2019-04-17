package com.example.bullet_journal.model;

import com.example.bullet_journal.enums.RatingCategory;

public class Rating {

    private int rating;

    private String date;

    private String title;

    private String text;

    private RatingCategory category;

    public Rating() {
    }

    public Rating(int rating, String date, String title, String text, RatingCategory category) {
        this.rating = rating;
        this.date = date;
        this.title = title;
        this.text = text;
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
