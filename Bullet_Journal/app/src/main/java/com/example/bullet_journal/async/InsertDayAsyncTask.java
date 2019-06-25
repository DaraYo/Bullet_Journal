package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;

public class InsertDayAsyncTask extends AsyncTask<Day, Void, Long> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Long doInBackground(Day... days) {

        return database.getDayDao().insert(days[0]);
    }

    @Override
    protected void onPostExecute(Long aLong) {

        delegate.taskFinished(database.getDayDao().get(aLong));
    }
}