package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.model.Mood;

public class AddEditMoodDialog extends Dialog {

    private Context context;
    private String selectedDate;
    private Mood moodObj;

    public AddEditMoodDialog(Context context, String selectedDate, Mood moodObj){
        super(context);
        this.context = context;
        this.selectedDate = selectedDate;
        this.moodObj = moodObj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_mood_dialog);

        TextView dateStr = findViewById(R.id.add_mood_dialog_date_str);
        dateStr.setText(selectedDate);

        if(moodObj != null){
            TextView dialogTitle = findViewById(R.id.add_mood_dialog_title);
            dialogTitle.setText(R.string.edit_mood_dialog_title);

            TextView dialogDescription = findViewById(R.id.add_mood_dialog_description);
            dialogDescription.setText(R.string.edit_mood_dialog_description);
        }

        Button dialogOkBtn = findViewById(R.id.mood_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button dialogCancelBtn = findViewById(R.id.mood_dialog_btn_cancel);
        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
