package com.example.bullet_journal.synchronization;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Rating;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GetRatingsForSyncTask extends AsyncTask<Void, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference ratingsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Ratings");

    public GetRatingsForSyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return deleteRatings() & pushRatings() & updateRatings();
    }

    private boolean pushRatings(){
        try{
            List<Rating> forUpdate = database.getRatingDao().getAllForInsert();
            Log.i("RATINGS FOR INSERT", "NUM: "+forUpdate.size());

            for(Rating rating : forUpdate){
                String firestoreId = ratingsCollectionRef.document().getId();
                rating.setFirestoreId(firestoreId);
                rating.setSynced(true);
                Tasks.await(ratingsCollectionRef.document(firestoreId).set(rating));
                database.getRatingDao().update(rating);
            }

            Log.i("RATINGS INSERT END", "SUCCESS");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("RATINGS INSERT END", "FAILED");
            return false;
        }
    }

    private boolean updateRatings(){
        try{
            List<Rating> forUpdate = database.getRatingDao().getAllForUpdate();
            Log.i("RATINGS FOR UPDATE", "NUM: "+forUpdate.size());

            for(Rating rating : forUpdate){
                rating.setSynced(true);
                Tasks.await(ratingsCollectionRef.document(rating.getFirestoreId()).set(rating));
                database.getRatingDao().update(rating);
            }

            Log.i("RATINGS UPDATE END", "SUCCESS");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("RATINGS UPDATE END", "FAILED");
            return false;
        }
    }

    private boolean deleteRatings(){
        try{
            List<Rating> forUpdate = database.getRatingDao().getAllForDelete();
            Log.i("RATINGS FOR DELETE", "NUM: "+forUpdate.size());

            for(Rating rating : forUpdate){

                if(rating.getFirestoreId() != null && !rating.getFirestoreId().isEmpty()){
                    Tasks.await(ratingsCollectionRef.document(rating.getFirestoreId()).delete());
                }

                database.getRatingDao().update(rating);
            }

            Log.i("RATINGS DELETE END", "SUCCESS");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("RATINGS DELETE END", "FAILED");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}