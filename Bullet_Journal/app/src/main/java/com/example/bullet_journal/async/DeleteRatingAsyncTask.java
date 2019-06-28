package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Rating;

public class DeleteRatingAsyncTask extends AsyncTask<Rating, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public DeleteRatingAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Rating... ratings) {

        try{
            database.getRatingDao().delete(ratings[0]);
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