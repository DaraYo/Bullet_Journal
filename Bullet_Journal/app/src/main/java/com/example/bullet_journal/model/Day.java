package com.example.bullet_journal.model;

public class Day {

    private String date;

    private String diaryInput;

    public Day() {
    }

    public Day(String date, String diaryInput) {
        this.date = date;
        this.diaryInput = diaryInput;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiaryInput() {
        return diaryInput;
    }

    public void setDiaryInput(String diaryInput) {
        this.diaryInput = diaryInput;
    }
}
