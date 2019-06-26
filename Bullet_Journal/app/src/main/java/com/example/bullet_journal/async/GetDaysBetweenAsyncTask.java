package com.example.bullet_journal.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;

import java.util.List;

public class GetDaysBetweenAsyncTask  extends AsyncTask<Long, Void, List<Day>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetDaysBetweenAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Day> doInBackground(Long... longs) {

        Log.i("JEDAN", "Primljeno: "+longs[0]);
        Log.i("DVAAA", "Primljeno: "+longs[1]);

        return database.getDayDao().getDaysBetween(longs[0], longs[1]);
    }

    @Override
    protected void onPostExecute(List<Day> days) {

        this.delegate.taskFinished(days);
    }
}