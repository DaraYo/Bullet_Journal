package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Mood;
import com.example.bullet_journal.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class GetMoodsForDayAsyncTask extends AsyncTask<Long, Void, List<Mood>> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public GetMoodsForDayAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected List<Mood> doInBackground(Long... longs) {

        long timeMillis = longs[0];
        Day dayObj = database.getDayDao().getByDate(CalendarCalculationsUtils.trimTimeFromDateMillis(timeMillis));
        if( dayObj == null) {
            User currentUser = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid());
            long id = database.getDayDao().insert(new Day(null, null, currentUser.getId(), timeMillis, 0, null, null, null, null, false));
            dayObj = database.getDayDao().get(id);
        }

        return database.getMoodDao().getAllMoodsForDay(dayObj.getId());
    }

    @Override
    protected void onPostExecute(List<Mood> moods) {

        this.delegate.taskFinished(moods);
    }
}
