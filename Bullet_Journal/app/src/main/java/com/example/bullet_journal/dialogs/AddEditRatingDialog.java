package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.model.Rating;

public class AddEditRatingDialog extends Dialog {

    private Context context;
    private String selectedDate;
    private Rating ratingObj;

    public AddEditRatingDialog(Context context, String selectedDate, Rating ratingObj) {
        super(context);
        this.selectedDate = selectedDate;
        this.ratingObj = ratingObj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_rating_dialog);

        TextView dateStr = findViewById(R.id.rating_dialog_date_str);
        dateStr.setText(selectedDate);

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

    }
}
