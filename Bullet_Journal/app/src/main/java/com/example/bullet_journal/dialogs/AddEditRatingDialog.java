package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.adapters.RatingCategoryAdapter;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.wrapperClasses.RatingCategoryWrapper;

import java.util.List;

public class AddEditRatingDialog extends Dialog {

    private Context context;
    private String selectedDate;
    private Rating ratingObj;

    private int rating;

    private ImageButton ratingBtn1;
    private ImageButton ratingBtn2;
    private ImageButton ratingBtn3;
    private ImageButton ratingBtn4;
    private ImageButton ratingBtn5;

    private List<RatingCategoryWrapper> categories;
    private RatingCategoryAdapter categoryAdapter;

    public AddEditRatingDialog(Context context, String selectedDate, Rating ratingObj, List<RatingCategoryWrapper> categories) {
        super(context);
        this.context = context;
        this.selectedDate = selectedDate;
        this.ratingObj = ratingObj;
        this.rating = -1;
        this.categories = categories;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_rating_dialog);

        TextView dateStr = findViewById(R.id.rating_dialog_date_str);
        dateStr.setText(selectedDate);

        ratingBtn1 = findViewById(R.id.add_rating_1);
        ratingBtn1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(1);
            }
        });

        ratingBtn2 = findViewById(R.id.add_rating_2);
        ratingBtn2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(2);
            }
        });

        ratingBtn3 = findViewById(R.id.add_rating_3);
        ratingBtn3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(3);
            }
        });

        ratingBtn4 = findViewById(R.id.add_rating_4);
        ratingBtn4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(4);
            }
        });

        ratingBtn5 = findViewById(R.id.add_rating_5);
        ratingBtn5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(5);
            }
        });

        Button dialogOkBtn = findViewById(R.id.rating_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button dialogCancelBtn = findViewById(R.id.rating_dialog_btn_cancel);
        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Spinner categorySpinner = findViewById(R.id.rating_category_spinner);
        categoryAdapter = new RatingCategoryAdapter(context, categories);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RatingCategoryWrapper categoryObj = (RatingCategoryWrapper) parent.getItemAtPosition(position);
                Toast.makeText(context, categoryObj.getCategory().toString(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void resolveSelection(int newRating){

        this.rating = newRating;

        switch (this.rating) {
            case 1 :
                this.ratingBtn1.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn2.setImageResource(R.drawable.ic_star_border_light);
                this.ratingBtn3.setImageResource(R.drawable.ic_star_border_light);
                this.ratingBtn4.setImageResource(R.drawable.ic_star_border_light);
                this.ratingBtn5.setImageResource(R.drawable.ic_star_border_light);
                break;
            case 2 :
                this.ratingBtn1.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn2.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn3.setImageResource(R.drawable.ic_star_border_light);
                this.ratingBtn4.setImageResource(R.drawable.ic_star_border_light);
                this.ratingBtn5.setImageResource(R.drawable.ic_star_border_light);
                break;
            case 3 :
                this.ratingBtn1.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn2.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn3.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn4.setImageResource(R.drawable.ic_star_border_light);
                this.ratingBtn5.setImageResource(R.drawable.ic_star_border_light);
                break;
            case 4 :
                this.ratingBtn1.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn2.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn3.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn4.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn5.setImageResource(R.drawable.ic_star_border_light);
                break;
            case 5 :
                this.ratingBtn1.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn2.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn3.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn4.setImageResource(R.drawable.ic_star_filled_light);
                this.ratingBtn5.setImageResource(R.drawable.ic_star_filled_light);
                break;
        }

    }
}
