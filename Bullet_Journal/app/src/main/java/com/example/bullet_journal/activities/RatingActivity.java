package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.RatingDisplayAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetRatingsAsyncTask;
import com.example.bullet_journal.dialogs.AddEditRatingDialog;
import com.example.bullet_journal.enums.RatingCategory;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.wrapperClasses.RatingCategoryWrapper;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends RootActivity {

    final Context context = this;
    private long dateMillis;
    private List<Rating> ratings;

    RatingDisplayAdapter listAdapter;

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
                    }
                });

                dialog.show();
            }
        });

        fetchRatings();

    }

    private List<RatingCategoryWrapper> initCategories() {

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

    private void fetchRatings() {
        AsyncTask<Void, Void, List<Rating>> getRatingsAsyncTask = new GetRatingsAsyncTask(new AsyncResponse<List<Rating>>() {

            @Override
            public void taskFinished(List<Rating> retVal) {
                ratings.clear();
                ratings.addAll(retVal);
                listAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

}
