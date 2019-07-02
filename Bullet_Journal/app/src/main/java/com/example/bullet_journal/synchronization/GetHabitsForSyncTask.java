package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.model.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GetHabitsForSyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference habitsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Habits");

    public GetHabitsForSyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return deleteHabits() & pushHabits() & updateHabits();
    }

    private boolean pushHabits() {
        try {
            List<Habit> forUpdate = database.getHabitDao().getAllForInsert();
            Log.i("HABITS FOR INSERT", "NUM: "+forUpdate.size());

            for (Habit habit : forUpdate) {
                User user = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid());

                String firestoreId = habitsCollectionRef.document().getId();
                habit.setFirestoreId(firestoreId);
                habit.setSynced(true);
                Tasks.await(habitsCollectionRef.document(firestoreId).set(habit));
                database.getHabitDao().update(habit);
            }
            Log.i("TASKS SYNC END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TASKS SYNC END", "FAILED");
            return false;
        }
    }

    private boolean updateHabits() {
        try {
            List<Habit> forUpdate = database.getHabitDao().getAllForUpdate();
            Log.i("HABIT FOR UPDATE", "NUM: "+forUpdate.size());

            for (Habit habit : forUpdate) {
                habit.setSynced(true);
                Tasks.await(habitsCollectionRef.document(habit.getFirestoreId()).set(habit));
                database.getHabitDao().update(habit);
            }
            Log.i("HABIT SYNC END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("HABIT SYNC END", "FAILED");
            return false;
        }
    }

    private boolean deleteHabits() {
        try {
            List<Habit> forUpdate = database.getHabitDao().getAllForDelete();
            Log.i("HABITS FOR DELETE", "NUM: "+forUpdate.size());

            for (Habit habit : forUpdate) {
                if(habit.getFirestoreId() != null && !habit.getFirestoreId().isEmpty()){
                    Tasks.await(habitsCollectionRef.document(habit.getFirestoreId()).delete());
                }

                database.getHabitDao().delete(habit);
            }
            Log.i("HABITS DELETE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("HABITS DELETE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}