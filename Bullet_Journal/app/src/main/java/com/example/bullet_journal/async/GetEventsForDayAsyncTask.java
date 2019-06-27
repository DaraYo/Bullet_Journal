package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Task;

import java.util.List;

public class GetEventsForDayAsyncTask extends AsyncTask<Long, Void, List<Task>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public GetEventsForDayAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Task> doInBackground(Long... longs) {

        long timeMillis = longs[0];
        Day dayObj = database.getDayDao().getByDate(CalendarCalculationsUtils.trimTimeFromDateMillis(timeMillis));
        if( dayObj == null) {
            long id = database.getDayDao().insert(new Day(null, null, null, timeMillis, 0, null, null, false));
            dayObj = database.getDayDao().get(id);
        }

        return database.getTaskEventDao().getAllEventsForDay(dayObj.getId());
    }

    @Override
    protected void onPostExecute(List<Task> events) {

        this.delegate.taskFinished(events);
    }
}
