package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;

import java.util.List;

public class GetRemindersForHabitAsyncTask extends AsyncTask<Long, Void, List<Reminder>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetRemindersForHabitAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Reminder> doInBackground(Long... longs) {

        return database.getReminderDao().getAllRemindersForHabit(longs[0]);
    }

    @Override
    protected void onPostExecute(List<Reminder> reminders) {

        this.delegate.taskFinished(reminders);
    }
}