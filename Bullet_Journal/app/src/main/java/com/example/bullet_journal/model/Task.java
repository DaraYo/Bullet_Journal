package com.example.bullet_journal.model;

import com.example.bullet_journal.enums.TaskType;

public class Task {

    private String title;

    private String text;

    private boolean status;

    private String date;

    private TaskType type;

    public Task(String title, String text, boolean status, String date, TaskType type) {
        this.title = title;
        this.text = text;
        this.status = status;
        this.date = date;
        this.type = type;
    }

    public Task() {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
}
