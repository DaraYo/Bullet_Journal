package com.example.bullet_journal.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;
import java.util.List;

public class GetHabitDayByHabitAsyncTask extends AsyncTask<Habit, Void, List<Long>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetHabitDayByHabitAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Long> doInBackground(Habit... habits) {
        List<Long> retVal= new ArrayList<>();
        try{
            Habit habit = habits[0];

            List<HabitDay> hb=  (List<HabitDay>) database.getHabitDayDao().getAllHabitsDayByHabit(habit.getId());

            for (HabitDay day: hb) {
                Day d= database.getDayDao().get(day.getDayId());

                retVal.add(d.getDate());
            }
            return retVal;

        } catch (Exception e){
            return retVal;
        }
    }



    @Override
    protected void onPostExecute(List<Long> habits) {

        this.delegate.taskFinished(habits);
    }
}
