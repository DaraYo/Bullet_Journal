package com.example.bullet_journal.model;

public class Habit {
    private String title;

    private String text;

    private boolean status;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Habit(String title, String text, boolean status) {
        this.title = title;
        this.text = text;
        this.status = status;
    }

    public Habit() {
    }
}
