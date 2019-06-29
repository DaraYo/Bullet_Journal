package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GetDaysForSyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    final CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");

    public GetDaysForSyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return pushDays() & updateDays();
    }

    private boolean pushDays() {
        try {
            List<Day> forUpdate = database.getDayDao().getAllForInsert();
            Log.i("DAYS FOR INSERT", "NUM: "+forUpdate.size());

            for (Day day : forUpdate) {
                day.setFirestoreId("" + day.getDate());
                day.setSynced(true);
                Tasks.await(dayCollectionRef.document("" + day.getDate()).set(day));
                database.getDayDao().update(day);
            }
            Log.i("DAY SYNC END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DAY SYNC END", "FAILED");
            return false;
        }
    }

    private boolean updateDays() {
        try {
            List<Day> forUpdate = database.getDayDao().getAllForUpdate();

            Log.i("DAYS FOR UPDATE", "NUM: "+forUpdate.size());

            for (Day day : forUpdate) {
                day.setSynced(true);
                Tasks.await(dayCollectionRef.document("" + day.getDate()).set(day));
                database.getDayDao().update(day);
            }

            Log.i("DAY UPDATE SYNC END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DAY UPDATE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}