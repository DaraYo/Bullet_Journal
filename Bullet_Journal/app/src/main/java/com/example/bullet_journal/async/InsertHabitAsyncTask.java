package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;

public class InsertHabitAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertHabitAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(HabitRemindersWrapper... habitRemindersWrappers) {

        try{
            Habit habitEvent = habitRemindersWrappers[0].getHabitEvent();
            ArrayList<Reminder> reminders = habitRemindersWrappers[0].getReminders();
            Day day= habitRemindersWrappers[0].getDay();

            long id = database.getHabitDao().insert(habitEvent);

            for(Reminder reminder : reminders){
                reminder.setHabitId(id);
                database.getReminderDao().insert(reminder);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }
}