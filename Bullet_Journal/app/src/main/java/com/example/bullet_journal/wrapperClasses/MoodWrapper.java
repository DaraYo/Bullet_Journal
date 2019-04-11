package com.example.bullet_journal.wrapperClasses;

import java.util.Date;

public class MoodWrapper {

    private double avgValue;

    private Date date;

    public MoodWrapper() {
    }

    public MoodWrapper(double avgValue, Date date) {
        this.avgValue = avgValue;
        this.date = date;
    }

    public double getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(double avgValue) {
        this.avgValue = avgValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
