package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class InsertRatingAsyncTask extends AsyncTask<Rating, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public InsertRatingAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Rating... ratings) {
        try{
            User currentUser = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid());

            Rating rating = ratings[0];
            rating.setUserId(currentUser.getId());
            database.getRatingDao().insert(rating);
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
