package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Habit;

import java.util.List;

public class GetHabitsForDayAsyncTask extends AsyncTask<Long, Void, List<Habit>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetHabitsForDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Habit> doInBackground(Long... longs) {

        return database.getHabitDao().getAll();
    }

    @Override
    protected void onPostExecute(List<Habit> habits) {

        this.delegate.taskFinished(habits);
    }
}
