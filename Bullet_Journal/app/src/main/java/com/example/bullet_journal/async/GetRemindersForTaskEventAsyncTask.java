package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;

import java.util.List;

public class GetRemindersForTaskEventAsyncTask extends AsyncTask<Long, Void, List<Reminder>> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public GetRemindersForTaskEventAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected List<Reminder> doInBackground(Long... longs) {

        return database.getReminderDao().getAllRemindersForTask(longs[0]);
    }

    @Override
    protected void onPostExecute(List<Reminder> reminders) {

        this.delegate.taskFinished(reminders);
    }
}