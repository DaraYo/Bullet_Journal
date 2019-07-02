package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class UserSignUpAsyncTask extends AsyncTask<User, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public UserSignUpAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(User... users) {

        User user = users[0];
        try{
            database.getUserDao().insert(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {
        delegate.taskFinished(retVal);
    }
}