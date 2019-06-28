package com.example.bullet_journal.adapters;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.DeleteRatingAsyncTask;
import com.example.bullet_journal.dialogs.AddEditRatingDialog;
import com.example.bullet_journal.enums.RatingCategory;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.wrapperClasses.RatingCategoryWrapper;

import java.util.ArrayList;
import java.util.List;

public class RatingDisplayAdapter extends ArrayAdapter<Rating> {

    private Context context;

    public RatingDisplayAdapter(Context context, List<Rating> objects) {
        super(context, R.layout.rating_preview_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.rating_preview_adapter, parent, false);

        final Rating ratingObj = getItem(position);

        TextView date = view.findViewById(R.id.rating_preview_date);
        date.setText(CalendarCalculationsUtils.dateMillisToString(ratingObj.getDate()));

        TextView rating = view.findViewById(R.id.rating_preview_score);
        rating.setText(""+ratingObj.getRating());

        final TextView category = view.findViewById(R.id.rating_preview_category);

        switch (ratingObj.getCategory()) {
            case ACTIVITY:
                category.setText(R.string.rating_category_activity);
                break;
            case BOOK :
                category.setText(R.string.rating_category_book);
                break;
            case MOVIE:
                category.setText(R.string.rating_category_movie);
                break;
            case MUSIC:
                category.setText(R.string.rating_category_music);
                break;
        }

        TextView title = view.findViewById(R.id.rating_preview_title);
        title.setText(ratingObj.getTitle());

        TextView text = view.findViewById(R.id.rating_preview_text);
        text.setText(ratingObj.getText());

        ImageButton editBtn = view.findViewById(R.id.rating_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new AddEditRatingDialog(context, ratingObj.getDate(), ratingObj, initCategories());

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        notifyDataSetChanged();
                    }
                });

                dialog.show();

            }
        });

        ImageButton deleteBtn = view.findViewById(R.id.rating_delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Rating, Void, Boolean> deleteRatingAsyncTask = new DeleteRatingAsyncTask(new AsyncResponse<Boolean>(){
                    @Override
                    public void taskFinished(Boolean retVal) {
                        if(retVal){
                            remove(ratingObj);
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute(ratingObj);
            }
        });

        return view;
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
}
