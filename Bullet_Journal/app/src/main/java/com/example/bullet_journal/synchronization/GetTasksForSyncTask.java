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

public class GetTasksForSyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    final CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");

    public GetTasksForSyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return deleteTasks() & pushTasks() & updateTasks();
    }

    private boolean pushTasks() {
        try {
            List<com.example.bullet_journal.model.Task> forUpdate = database.getTaskEventDao().getAllForInsert();
            Log.i("TASKS FOR INSERT", "NUM: "+forUpdate.size());

            for (com.example.bullet_journal.model.Task task : forUpdate) {
                Day day = database.getDayDao().get(task.getDayId());

                CollectionReference tasksCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks");
                String firestoreId = tasksCollectionRef.document().getId();
                task.setFirestoreId(firestoreId);
                task.setSynced(true);
                Tasks.await(tasksCollectionRef.document(firestoreId).set(task));
                database.getTaskEventDao().update(task);
            }
            Log.i("TASKS SYNC END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TASKS SYNC END", "FAILED");
            return false;
        }
    }

    private boolean updateTasks() {
        try {
            List<com.example.bullet_journal.model.Task> forUpdate = database.getTaskEventDao().getAllForUpdate();
            Log.i("TASKS FOR UPDATE", "NUM: "+forUpdate.size());

            for (com.example.bullet_journal.model.Task task : forUpdate) {
                Day day = database.getDayDao().get(task.getDayId());

                CollectionReference tasksCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks");
                task.setSynced(true);
                Tasks.await(tasksCollectionRef.document(task.getFirestoreId()).set(task));
                database.getTaskEventDao().update(task);
            }
            Log.i("TASKS UPDATE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TASKS UPDATE END", "FAILED");
            return false;
        }
    }

    private boolean deleteTasks() {
        try {
            List<com.example.bullet_journal.model.Task> forUpdate = database.getTaskEventDao().getAllForDelete();
            Log.i("TASKS FOR DELETE", "NUM: "+forUpdate.size());

            for (com.example.bullet_journal.model.Task task : forUpdate) {
                if(task.getFirestoreId() != null && !task.getFirestoreId().isEmpty()){
                    Day day = database.getDayDao().get(task.getDayId());
                    CollectionReference tasksCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks");
                    Tasks.await(tasksCollectionRef.document(task.getFirestoreId()).delete());
                }

                database.getTaskEventDao().delete(task);
            }
            Log.i("TASKS DELETE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TASKS DELETE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}