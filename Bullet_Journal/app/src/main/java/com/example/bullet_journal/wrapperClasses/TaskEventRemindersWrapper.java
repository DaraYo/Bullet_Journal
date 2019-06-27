package com.example.bullet_journal.wrapperClasses;

import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskEventRemindersWrapper implements Serializable {

    private Task taskEvent;

    private ArrayList<Reminder> reminders;

    public TaskEventRemindersWrapper() {
    }

    public TaskEventRemindersWrapper(Task taskEvent, ArrayList<Reminder> reminders) {
        this.taskEvent = taskEvent;
        this.reminders = reminders;
    }

    public Task getTaskEvent() {
        return taskEvent;
    }

    public void setTaskEvent(Task taskEvent) {
        this.taskEvent = taskEvent;
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }
}
