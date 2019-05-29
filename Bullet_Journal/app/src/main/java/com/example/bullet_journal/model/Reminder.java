package com.example.bullet_journal.model;

import java.util.Date;

public class Reminder {

    private String name;

    private Date date;

    private boolean status;

    public Reminder(String name, Date date, boolean status) {
        this.name=name;
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
