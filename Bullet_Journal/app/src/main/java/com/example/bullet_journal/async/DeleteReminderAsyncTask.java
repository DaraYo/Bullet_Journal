package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;

public class DeleteReminderAsyncTask extends AsyncTask<Reminder, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public DeleteReminderAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Reminder... reminders) {

        try {
            Reminder forDelete = reminders[0];
            forDelete.setDeleted(true);
            database.getReminderDao().update(forDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.delegate.taskFinished(aBoolean);
    }
}