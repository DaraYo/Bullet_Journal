package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;

public class InsertReminderAsyncTask extends AsyncTask<Reminder, Void, Long> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertReminderAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Long doInBackground(Reminder... reminder) {
        try{
            long id= database.getReminderDao().insert(reminder[0]);
            return id;
        }catch(Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long along) {

        this.delegate.taskFinished(along);
    }
}
