package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.List;

public class GetHabitDayAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, HabitDay> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetHabitDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected HabitDay doInBackground(HabitRemindersWrapper... habitRemindersWrappers) {

        try{
            Habit habit = habitRemindersWrappers[0].getHabitEvent();
            Day day= habitRemindersWrappers[0].getDay();

            List<HabitDay> hb= (List<HabitDay>) database.getHabitDayDao().getAllHabitsDayByHabit(habit.getId());

            for (HabitDay d:hb ) {
                if (d.getDayId()==day.getId()){
                    return d;
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }



    @Override
    protected void onPostExecute(HabitDay habits) {

        this.delegate.taskFinished(habits);
    }
}
