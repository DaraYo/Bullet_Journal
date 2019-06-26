package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.RatingDisplayAdapter;
import com.example.bullet_journal.dialogs.AddEditRatingDialog;
import com.example.bullet_journal.enums.RatingCategory;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.wrapperClasses.RatingCategoryWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends RootActivity {

    final Context context  = this;
    private long dateMillis;
    private List<Rating> ratings;

    RatingDisplayAdapter listAdapter;

    private FirebaseFirestore firestore;
    private FirebaseAuth fAuth;
    private CollectionReference ratingsCollectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setTitle(R.string.title_activity_ratings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratings = new ArrayList<>();
        listAdapter = new RatingDisplayAdapter(this, ratings);
        ListView listView = findViewById(R.id.ratings_list_view);
        listView.setAdapter(listAdapter);

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        ratingsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Rating");

        Bundle bundle = getIntent().getExtras();
        dateMillis = bundle.getLong("date");

        ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_rating);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new AddEditRatingDialog(context, dateMillis, null, initCategories());

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        fetchRatings();
                        listAdapter.notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });

        fetchRatings();

    }

    private List<RatingCategoryWrapper> initCategories(){

        List<RatingCategoryWrapper> retVal = new ArrayList<>();

        RatingCategoryWrapper activity = new RatingCategoryWrapper(RatingCategory.ACTIVITY, R.string.rating_category_activity);
        RatingCategoryWrapper book = new RatingCategoryWrapper(RatingCategory.BOOK, R.string.rating_category_book);
        RatingCategoryWrapper music = new RatingCategoryWrapper(RatingCategory.MUSIC, R.string.rating_category_music);
        RatingCategoryWrapper movie = new RatingCategoryWrapper(RatingCategory.MOVIE, R.string.rating_category_movie);

        retVal.add(activity);
        retVal.add(book);
        retVal.add(music);
        retVal.add(movie);

        return retVal;
    }

    private void fetchRatings(){
        ratingsCollectionRef.whereEqualTo("date", dateMillis).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ratings.clear();
                    if(task.getResult().size() > 0){
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Rating tempRating = snapshot.toObject(Rating.class);
                            ratings.add(tempRating);
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
