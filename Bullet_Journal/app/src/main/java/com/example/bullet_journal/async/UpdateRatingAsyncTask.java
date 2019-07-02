package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Rating;

public class UpdateRatingAsyncTask extends AsyncTask<Rating, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public UpdateRatingAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Rating... ratings) {
        Rating rating = ratings[0];
        try{
            rating.setSynced(false);
            database.getRatingDao().update(rating);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        this.delegate.taskFinished(aBoolean);
    }
}