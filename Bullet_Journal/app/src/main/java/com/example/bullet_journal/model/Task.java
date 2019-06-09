package com.example.bullet_journal.model;

import com.example.bullet_journal.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private String title;

    private String text;

    private boolean status;

    private long date;

    private TaskType type;

    private List<Reminder> reminders;

    public Task(String title, String text, boolean status, long date, TaskType type) {
        this.title = title;
        this.text = text;
        this.status = status;
        this.date = date;
        this.type = type;
        this.reminders = new ArrayList<>();
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }
}
