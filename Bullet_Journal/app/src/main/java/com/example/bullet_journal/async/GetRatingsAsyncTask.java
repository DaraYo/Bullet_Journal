package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Rating;

import java.util.List;

public class GetRatingsAsyncTask extends AsyncTask<Void, Void, List<Rating>> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public GetRatingsAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected List<Rating> doInBackground(Void... voids) {
        return database.getRatingDao().getAll();
    }

    @Override
    protected void onPostExecute(List<Rating> ratings) {
        this.delegate.taskFinished(ratings);
    }
}
