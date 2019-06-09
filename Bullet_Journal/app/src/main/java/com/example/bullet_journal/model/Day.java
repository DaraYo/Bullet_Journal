package com.example.bullet_journal.model;

import java.util.List;

public class Day {

    private long date;

    private double avgMood;

    private String diaryInput;

    private List<Habit> habits;

    private List<Task> tasks;

    private Album album;


    public Day() { }

    public Day(long date, String diaryInput) {
        this.date = date;
        this.diaryInput = diaryInput;
        this.avgMood = 0.0;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDiaryInput() {
        return diaryInput;
    }

    public void setDiaryInput(String diaryInput) {
        this.diaryInput = diaryInput;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public double getAvgMood() {
        return avgMood;
    }

    public void setAvgMood(double avgMood) {
        this.avgMood = avgMood;
    }

}
