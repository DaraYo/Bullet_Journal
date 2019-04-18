package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.RatingDisplayAdapter;
import com.example.bullet_journal.dialogs.AddEditRatingDialog;
import com.example.bullet_journal.enums.RatingCategory;
import com.example.bullet_journal.model.Rating;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends RootActivity {

    final Context context  = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setTitle(R.string.title_activity_ratings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RatingDisplayAdapter listAdapter = new RatingDisplayAdapter(this, buildRatings());
        ListView listView = findViewById(R.id.ratings_list_view);
        listView.setAdapter(listAdapter);

        ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_rating);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new AddEditRatingDialog(context, "18/04/2019", null);
                dialog.show();
            }
        });
    }

    private List<Rating> buildRatings(){

        List<Rating> retVal = new ArrayList<>();

        Rating rating1 = new Rating(5, "18/04/2019", "Finally finished!", "Best book i ever read!", RatingCategory.BOOK);
        Rating rating2 = new Rating(2, "04/02/2019", "Concert day", "What a mess, sound was bad, band was probably drunk...", RatingCategory.MUSIC);
        Rating rating3 = new Rating(3, "10/01/2019", "Movie was ok", "Could have been better...", RatingCategory.MOVIE);
        Rating rating4 = new Rating(5, "22/10/2018", "Marathon", "Awesome weather, very tired, feeling rewarded.", RatingCategory.ACTIVITY);
        Rating rating5 = new Rating(5, "07/09/2018", "This movie is goood", "Pure perfection!", RatingCategory.MOVIE);
        Rating rating6 = new Rating(4, "20/18/2018", "Game over :(", "Pretty good game, except one flaw.", RatingCategory.ACTIVITY);

        retVal.add(rating1);
        retVal.add(rating2);
        retVal.add(rating3);
        retVal.add(rating4);
        retVal.add(rating5);
        retVal.add(rating6);

        return retVal;
    }
}
