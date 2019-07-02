package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Mood;

public class DeleteMoodAsyncTask extends AsyncTask<Mood, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public DeleteMoodAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Mood... moods) {

        try{
            Mood forDelete = moods[0];
            forDelete.setDeleted(true);
            database.getMoodDao().update(forDelete);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.delegate.taskFinished(aBoolean);
    }
}
