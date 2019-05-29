package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.wrapperClasses.RatingCategoryWrapper;

import java.util.List;

public class RatingCategoryAdapter extends ArrayAdapter<RatingCategoryWrapper> {

    private Context context;

    public RatingCategoryAdapter(Context context, List<RatingCategoryWrapper> objects) {
        super(context, R.layout.rating_category_display_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rating_category_display_adapter, parent, false);
        RatingCategoryWrapper ratingObj = getItem(position);

        TextView categoryName = view.findViewById(R.id.rating_category_name);
        categoryName.setText(ratingObj.getCategoryNameIdx());

        ImageButton categoryIcon = view.findViewById(R.id.rating_category_icon);

        switch (ratingObj.getCategory()) {

            case ACTIVITY :
                categoryIcon.setImageResource(R.drawable.ic_bike_activity);
                break;

            case BOOK :
                categoryIcon.setImageResource(R.drawable.ic_diary_light);
                break;

            case MOVIE:
                categoryIcon.setImageResource(R.drawable.ic_movie);
                break;

            case MUSIC:
                categoryIcon.setImageResource(R.drawable.ic_music);
                break;
        }

        return view;
    }
}
