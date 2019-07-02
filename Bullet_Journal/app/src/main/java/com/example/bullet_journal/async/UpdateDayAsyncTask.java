package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;

public class UpdateDayAsyncTask extends AsyncTask<Day, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public UpdateDayAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Day... days) {
        Day day= days[0];
        try{
            day.setSynced(false);
            database.getDayDao().update(day);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        delegate.taskFinished(result);
    }
}
