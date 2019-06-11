package com.example.bullet_journal.model;

public class Reminder {

    private String name;

    private long date;

    private boolean status;

    public Reminder(String name, long date, boolean status) {
        this.name = name;
        this.date = date;
        this.status = status;
    }


    public Reminder() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
