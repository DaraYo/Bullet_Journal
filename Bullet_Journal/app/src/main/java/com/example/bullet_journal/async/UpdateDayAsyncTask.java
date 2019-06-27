package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;

public class UpdateDayAsyncTask extends AsyncTask<Day, Void, Boolean> {
    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public UpdateDayAsyncTask(AsyncResponse delegate) { this.delegate= delegate; }


    @Override
    protected Boolean doInBackground(Day... days) {
        Day day= days[0];
        try{
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
