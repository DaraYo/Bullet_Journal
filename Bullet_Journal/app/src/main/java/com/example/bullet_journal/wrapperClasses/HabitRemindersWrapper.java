package com.example.bullet_journal.wrapperClasses;

import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;

import java.io.Serializable;
import java.util.ArrayList;

public class HabitRemindersWrapper implements Serializable {

    private Habit habitEvent;

    private ArrayList<Reminder> reminders;

    private Day day;

    public HabitRemindersWrapper() {
    }

    public HabitRemindersWrapper(Habit habitEvent, ArrayList<Reminder> reminders) {
        this.habitEvent = habitEvent;
        this.reminders = reminders;
    }

    public Habit getHabitEvent() {
        return habitEvent;
    }

    public void setHabitEvent(Habit habitEvent) {
        this.habitEvent = habitEvent;
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
