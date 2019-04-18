package com.example.bullet_journal.model;

import java.util.Date;

public class Reminder {
    private Date date;

    private boolean status;

    public Reminder(Date date, boolean status) {
        this.date = date;
        this.status = status;
    }


    public Reminder() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
