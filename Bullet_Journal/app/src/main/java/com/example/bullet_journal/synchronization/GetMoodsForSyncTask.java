package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Mood;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GetMoodsForSyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    final CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");

    public GetMoodsForSyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return deleteMoods() & pushMoods() & updateMoods();
    }

    private boolean pushMoods() {
        try {
            List<Mood> forUpdate = database.getMoodDao().getAllForInsert();
            Log.i("MOODS FOR INSERT", "NUM: "+forUpdate.size());

            for (Mood mood : forUpdate) {
                Day day = database.getDayDao().get(mood.getDayId());

                CollectionReference moodCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Moods");
                String firestoreId = moodCollectionRef.document().getId();
                mood.setFirestoreId(firestoreId);
                mood.setSynced(true);
                Tasks.await(moodCollectionRef.document(firestoreId).set(mood));
                database.getMoodDao().update(mood);
            }
            Log.i("MOODS INSERT END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MOODS INSERT END", "FAILED");
            return false;
        }
    }

    private boolean updateMoods() {
        try {
            List<Mood> forUpdate = database.getMoodDao().getAllForUpdate();
            Log.i("MOODS FOR UPDATE", "NUM: "+forUpdate.size());

            for (Mood mood : forUpdate) {
                Day day = database.getDayDao().get(mood.getDayId());

                CollectionReference moodCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Moods");
                mood.setSynced(true);
                Tasks.await(moodCollectionRef.document(mood.getFirestoreId()).set(mood));
                database.getMoodDao().update(mood);
            }
            Log.i("MOODS UPDATE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MOODS UPDATE END", "FAILED");
            return false;
        }
    }

    private boolean deleteMoods() {
        try {
            List<Mood> forUpdate = database.getMoodDao().getAllForDelete();
            Log.i("MOODS FOR DELETE", "NUM: "+forUpdate.size());

            for (Mood mood : forUpdate) {

                if(mood.getFirestoreId() != null && !mood.getFirestoreId().isEmpty()){
                    Day day = database.getDayDao().get(mood.getDayId());
                    CollectionReference moodCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Moods");
                    Tasks.await(moodCollectionRef.document(mood.getFirestoreId()).delete());
                }

                database.getMoodDao().update(mood);
            }
            Log.i("MOODS DELETE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MOODS DELETE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}