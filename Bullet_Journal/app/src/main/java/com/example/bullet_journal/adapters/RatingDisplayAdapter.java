package com.example.bullet_journal.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Rating;

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

        Rating ratingObj = getItem(position);

        TextView date = view.findViewById(R.id.rating_preview_date);
        date.setText(CalendarCalculationsUtils.dateMillisToString(ratingObj.getDate()));

        TextView rating = view.findViewById(R.id.rating_preview_score);
        rating.setText(""+ratingObj.getRating());

        TextView category = view.findViewById(R.id.rating_preview_category);

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

        return view;
    }
}
