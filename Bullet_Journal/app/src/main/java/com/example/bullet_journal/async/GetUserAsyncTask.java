package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class GetUserAsyncTask extends AsyncTask<Void, Void, User> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public GetUserAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected User doInBackground(Void... voids) {
        try{
            User currentUser = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid());
            return currentUser;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        delegate.taskFinished(user);
    }
}