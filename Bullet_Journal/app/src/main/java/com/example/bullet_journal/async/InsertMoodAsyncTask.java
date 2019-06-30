package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Mood;

public class InsertMoodAsyncTask extends AsyncTask<Mood, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public InsertMoodAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Mood... moods) {
        try {
            database.getMoodDao().insert(moods[0]);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }
}
