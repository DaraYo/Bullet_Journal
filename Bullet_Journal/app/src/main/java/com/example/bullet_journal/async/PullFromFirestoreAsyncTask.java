package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.FirebaseUserDTO;
import com.example.bullet_journal.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PullFromFirestoreAsyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");

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

    private boolean fetchAllData(){

        return true;
    }

    private boolean fetchAllDays(){

        return true;
    }

    private boolean fetchAllRatings(){

        return true;
    }

    private boolean fetchAllMoodsForDay(){

        return true;
    }

    private boolean fetchAllTasksForDay(){

        return true;
    }

    private boolean fetchAllTasksRemindersForTasks(){

        return true;
    }


}
