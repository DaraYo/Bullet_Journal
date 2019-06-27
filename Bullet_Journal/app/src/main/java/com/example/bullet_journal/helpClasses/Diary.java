package com.example.bullet_journal.helpClasses;

import java.util.Date;
import java.util.List;

public class Diary {
    private Date diaryDate;
    private String title;
    private String thoughts;
    private List<AlbumItem> albumItems;

    public Diary(){}

    public Diary(Date diaryDate, String title, String thoughts, List<AlbumItem> albumItems) {
        this.diaryDate = diaryDate;
        this.title = title;
        this.thoughts = thoughts;
        this.albumItems = albumItems;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(Date diaryDate) {
        this.diaryDate = diaryDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public List<AlbumItem> getAlbumItems() {
        return albumItems;
    }

    public void setAlbumItems(List<AlbumItem> albumItems) {
        this.albumItems = albumItems;
    }
}
