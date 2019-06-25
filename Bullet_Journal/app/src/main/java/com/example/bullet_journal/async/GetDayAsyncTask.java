package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;

public class GetDayAsyncTask extends AsyncTask<Long, Void, Day> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Day doInBackground(Long... longs) {
        long timeMillis = longs[0];
        Day retVal = database.getDayDao().getByDate(timeMillis);
        if( retVal == null){
            long id = database.getDayDao().insert(new Day(null, null, null, timeMillis, 0, null, null, false));
            retVal = database.getDayDao().get(id);
        }
        return retVal;
    }

    @Override
    protected void onPostExecute(Day day) {
        delegate.taskFinished(day);
    }
}
