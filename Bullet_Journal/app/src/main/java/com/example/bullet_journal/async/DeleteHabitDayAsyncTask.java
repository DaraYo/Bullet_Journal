package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.List;

public class DeleteHabitDayAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public DeleteHabitDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(HabitRemindersWrapper... habitRemindersWrappers) {

        try{
            Habit habit = habitRemindersWrappers[0].getHabitEvent();
            Day day= habitRemindersWrappers[0].getDay();

            List<HabitDay> hb= (List<HabitDay>) database.getHabitDayDao().getAllHabitsDayByHabit(habit.getId());

            for (HabitDay d:hb ) {
                if (d.getDayId()==day.getId()){
                    database.getHabitDayDao().delete(d);
                }
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