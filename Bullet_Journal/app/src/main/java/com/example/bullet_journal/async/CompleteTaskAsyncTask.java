package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Task;

public class CompleteTaskAsyncTask extends AsyncTask<Task, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public CompleteTaskAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Task... tasks) {

        try{
            Task toComplete = tasks[0];
            toComplete.setStatus(!toComplete.isStatus());
            toComplete.setSynced(false);
            database.getTaskEventDao().update(toComplete);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }
}
