package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;

public class GetRemindersCountForTaskEventAsyncTask extends AsyncTask<Long, Void, Integer> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public GetRemindersCountForTaskEventAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
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