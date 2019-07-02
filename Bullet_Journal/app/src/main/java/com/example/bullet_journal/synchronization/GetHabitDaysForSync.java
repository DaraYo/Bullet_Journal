package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.model.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GetHabitDaysForSync extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference habitsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Habits");

    public GetHabitDaysForSync(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return deleteHabitDay() & pushHabitDay() & updateHabitDay();
    }

    private boolean pushHabitDay() {
        try {
            List<HabitDay> forUpdate = database.getHabitDayDao().getAllForInsert();
            Log.i("HABDAY FOR INSERT", "NUM: "+forUpdate.size());

            for (HabitDay habDay: forUpdate) {
                Habit habit = database.getHabitDao().get(habDay.getHabitId());

                CollectionReference habDayCollectionRef = habitsCollectionRef.document(habit.getFirestoreId()).collection("HabitDay");
                String firestoreId = habDayCollectionRef.document().getId();
                habDay.setFirestoreId(firestoreId);
                habDay.setSynced(true);
                Tasks.await(habDayCollectionRef.document(firestoreId).set(habDay));
                database.getHabitDayDao().update(habDay);
            }
            Log.i("HABDAY SYNC END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TASKS SYNC END", "FAILED");
            return false;
        }
    }

    private boolean updateHabitDay() {
        try {
            List<HabitDay> forUpdate = database.getHabitDayDao().getAllForUpdate();
            Log.i("HABDAY FOR UPDATE", "NUM: "+forUpdate.size());

            for (HabitDay habDay: forUpdate) {
                Habit habit = database.getHabitDao().get(habDay.getHabitId());

                CollectionReference habDayCollectionRef = habitsCollectionRef.document(habit.getFirestoreId()).collection("HabitDay");
                habDay.setSynced(true);
                Tasks.await(habDayCollectionRef.document(habDay.getFirestoreId()).set(habDay));
                database.getHabitDayDao().update(habDay);
            }
            Log.i("HABDAY UPDATE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("HABDAY UPDATE END", "FAILED");
            return false;
        }
    }

    private boolean deleteHabitDay() {
        try {
            List<HabitDay> forUpdate = database.getHabitDayDao().getAllForUpdate();
            Log.i("HABDAY FOR DELETE", "NUM: "+forUpdate.size());

            for (HabitDay habDay: forUpdate) {
                Habit habit = database.getHabitDao().get(habDay.getHabitId());

                CollectionReference habDayCollectionRef = habitsCollectionRef.document(habit.getFirestoreId()).collection("HabitDay");
                Tasks.await(habDayCollectionRef.document(habDay.getFirestoreId()).delete());
                database.getHabitDayDao().delete(habDay);
            }
            Log.i("HABDAY DELETE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("HABDAY DELETE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}