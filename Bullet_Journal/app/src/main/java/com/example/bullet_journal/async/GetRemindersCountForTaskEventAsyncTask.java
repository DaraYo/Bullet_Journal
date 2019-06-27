package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;

public class GetRemindersCountForTaskEventAsyncTask extends AsyncTask<Long, Void, Integer> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetRemindersCountForTaskEventAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Integer doInBackground(Long... longs) {

        return database.getReminderDao().getRemindersCountForTask(longs[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {

        this.delegate.taskFinished(integer);
    }
}