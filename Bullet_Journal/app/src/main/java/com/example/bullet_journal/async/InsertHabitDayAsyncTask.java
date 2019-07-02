package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

public class InsertHabitDayAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertHabitDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(HabitRemindersWrapper... habitRemindersWrappers) {

        try{
            Habit habit = habitRemindersWrappers[0].getHabitEvent();
            Day day= habitRemindersWrappers[0].getDay();
            HabitDay hb = new HabitDay(null, null, day.getId(), habit.getId(), false);

            database.getHabitDayDao().insert(hb);

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