package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.FirebaseUserDTO;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Mood;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PullFromFirestoreAsyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");
    private CollectionReference ratingsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Ratings");

    private static boolean isSuccessful = true;

    public PullFromFirestoreAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        database.clearAllTables();

        try{
            Task<DocumentSnapshot> getUserTask = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).get();
            FirebaseUserDTO currentUserData = Tasks.await(getUserTask).toObject(FirebaseUserDTO.class);

            User currentUser = new User();
            currentUser.setFirestoreId(currentUserData.getId());
            currentUser.setFirstName(currentUserData.getFirstName());
            currentUser.setLastName(currentUserData.getLastName());
            currentUser.setEmail(currentUser.getEmail());
            currentUser.setPassword(currentUser.getPassword());
            database.getUserDao().insert(currentUser);

            fetchAllData();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return isSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }

    private void fetchAllData(){

        fetchAllDays();
        fetchAllRatings();
    }

    private void fetchAllDays(){
        try {
            Task<QuerySnapshot> getDaysTask = dayCollectionRef.get();
            QuerySnapshot daysResult = Tasks.await(getDaysTask);

            for(QueryDocumentSnapshot snapshot : daysResult){
                Day day = snapshot.toObject(Day.class);
                database.getDayDao().insert(day);
                fetchAllMoodsForDay(day);
                fetchAllTasksForDay(day);
            }

            Log.i("DAYS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("DAYS FETCH", "FAILED");
        }
    }

    private void fetchAllRatings(){
        try {
            Task<QuerySnapshot> getRatingsTask = ratingsCollectionRef.get();
            QuerySnapshot ratingsResult = Tasks.await(getRatingsTask);

            for(QueryDocumentSnapshot snapshot : ratingsResult){
                Rating rating = snapshot.toObject(Rating.class);
                database.getRatingDao().insert(rating);
            }

            Log.i("RATINGS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("RATINGS FETCH", "FAILED");
        }
    }

    private void fetchAllMoodsForDay(Day day){
        try {
            Task<QuerySnapshot> getMoodsTask = dayCollectionRef.document(day.getFirestoreId()).collection("Moods").get();
            QuerySnapshot moodsResult = Tasks.await(getMoodsTask);

            for(QueryDocumentSnapshot snapshot : moodsResult){
                Mood mood = snapshot.toObject(Mood.class);
                mood.setDayId(day.getId());
                database.getMoodDao().insert(mood);
            }

            Log.i("MOODS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("MOODS FETCH", "FAILED");
        }
    }

    private void fetchAllTasksForDay(Day day){
        try {
            Task<QuerySnapshot> getTasksTask = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks").get();
            QuerySnapshot tasksResult = Tasks.await(getTasksTask);

            for(QueryDocumentSnapshot snapshot : tasksResult){
                com.example.bullet_journal.model.Task task = snapshot.toObject(com.example.bullet_journal.model.Task.class);
                task.setDayId(day.getId());
                database.getTaskEventDao().insert(task);
            }

            Log.i("TASKS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("TASKS FETCH", "FAILED");
        }
    }

    private boolean fetchAllRemindersForTasks(){

        return true;
    }


}
