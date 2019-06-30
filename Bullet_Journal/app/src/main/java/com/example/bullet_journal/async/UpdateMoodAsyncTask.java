package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Mood;

public class UpdateMoodAsyncTask  extends AsyncTask<Mood, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public UpdateMoodAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Mood... moods) {
        Mood mood = moods[0];
        try {
            mood.setSynced(false);
            database.getMoodDao().update(mood);
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
