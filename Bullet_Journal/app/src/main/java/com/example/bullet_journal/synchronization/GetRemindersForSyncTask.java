package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GetRemindersForSyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    final CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");
    private CollectionReference habitsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Habits");

    public GetRemindersForSyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return deleteReminders() & pushReminders() & updateReminders();
    }

    private boolean pushReminders() {
        try {
            List<Reminder> forUpdate = database.getReminderDao().getAllForInsert();
            Log.i("REMINDERS FOR INSERT", "NUM: "+forUpdate.size());

            for (Reminder reminder : forUpdate) {
                Task task = database.getTaskEventDao().get(reminder.getTaskId());

                if(task != null){
                    Day day = database.getDayDao().get(task.getDayId());

                    CollectionReference remindersCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks").document(task.getFirestoreId()).collection("Reminders");
                    String firestoreId = remindersCollectionRef.document().getId();
                    reminder.setFirestoreId(firestoreId);
                    reminder.setSynced(true);
                    Tasks.await(remindersCollectionRef.document(firestoreId).set(reminder));
                    database.getReminderDao().update(reminder);
                }else{
                    Habit habit = database.getHabitDao().get(reminder.getHabitId());

                    CollectionReference remindersCollectionRef = habitsCollectionRef.document(habit.getFirestoreId()).collection("Reminders");
                    String firestoreId = remindersCollectionRef.document().getId();
                    reminder.setFirestoreId(firestoreId);
                    reminder.setSynced(true);
                    Tasks.await(remindersCollectionRef.document(firestoreId).set(reminder));
                    database.getReminderDao().update(reminder);
                }
            }
            Log.i("REMINDERS INSERT END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("REMINDERS INSERT END", "FAILED");
            return false;
        }
    }

    private boolean updateReminders() {
        try {
            List<Reminder> forUpdate = database.getReminderDao().getAllForUpdate();
            Log.i("REMINDERS FOR UPDATE", "NUM: "+forUpdate.size());

            for (Reminder reminder : forUpdate) {
                Task task = database.getTaskEventDao().get(reminder.getTaskId());

                if(task != null){
                    Day day = database.getDayDao().get(task.getDayId());

                    CollectionReference remindersCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks").document(task.getFirestoreId()).collection("Reminders");
                    reminder.setSynced(true);
                    Tasks.await(remindersCollectionRef.document(task.getFirestoreId()).set(reminder));
                }else{
                    Habit habit = database.getHabitDao().get(reminder.getHabitId());

                    CollectionReference remindersCollectionRef = dayCollectionRef.document(habit.getFirestoreId()).collection("Reminders");
                    reminder.setSynced(true);
                    Tasks.await(remindersCollectionRef.document(task.getFirestoreId()).set(reminder));
                }
                database.getReminderDao().update(reminder);
            }
            Log.i("REMINDERS UPDATE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("REMINDERS UPDATE END", "FAILED");
            return false;
        }
    }

    private boolean deleteReminders() {
        try {
            List<Reminder> forUpdate = database.getReminderDao().getAllForDelete();
            Log.i("REMINDERS FOR DELETE", "NUM: "+forUpdate.size());

            for (Reminder reminder : forUpdate) {
                if(reminder.getFirestoreId() != null && !reminder.getFirestoreId().isEmpty()){
                    Task task = database.getTaskEventDao().get(reminder.getTaskId());

                    if(task != null){
                        Day day = database.getDayDao().get(task.getDayId());

                        CollectionReference remindersCollectionRef = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks").document(task.getFirestoreId()).collection("Reminders");
                        Tasks.await(remindersCollectionRef.document(task.getFirestoreId()).delete());
                    }else{
                        Habit habit = database.getHabitDao().get(reminder.getHabitId());

                        CollectionReference remindersCollectionRef = dayCollectionRef.document(habit.getFirestoreId()).collection("Reminders");
                        Tasks.await(remindersCollectionRef.document(task.getFirestoreId()).delete());
                    }
                }

                database.getReminderDao().delete(reminder);
            }
            Log.i("REMINDERS DELETE END", "SUCCESS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("REMINDERS DELETE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}