package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class GetDayAsyncTask extends AsyncTask<Long, Void, Day> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public GetDayAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Day doInBackground(Long... longs) {
        long timeMillis = longs[0];
        try{
            Day retVal = database.getDayDao().getByDate(CalendarCalculationsUtils.trimTimeFromDateMillis(timeMillis));
            if( retVal == null){
                User currentUser = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid());
                long id = database.getDayDao().insert(new Day(null, null, null, timeMillis, 0, null, null, null, null, false));
                retVal = database.getDayDao().get(id);
            }
            return retVal;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Day day) {
        delegate.taskFinished(day);
    }
}
