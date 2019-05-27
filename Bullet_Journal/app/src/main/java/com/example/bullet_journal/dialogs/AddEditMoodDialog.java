package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Mood;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEditMoodDialog extends Dialog {

    private Context context;
    private long selectedDate;
    private Mood moodObj;

    private TextView dialogDescription;

    private FirebaseFirestore firestore;
    private CollectionReference colRef;

    public AddEditMoodDialog(Context context, long selectedDate, Mood moodObj){
        super(context);
        this.context = context;
        this.selectedDate = selectedDate;
        this.moodObj = moodObj;
        this.firestore = FirebaseFirestore.getInstance();
        this.colRef = this.firestore.collection("Moods");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_mood_dialog);

        TextView dateStr = findViewById(R.id.add_mood_dialog_date_str);
        dateStr.setText(CalendarCalculationsUtils.dateMillisToString(selectedDate));

        dialogDescription = findViewById(R.id.description);

        if(moodObj != null){
            TextView dialogTitle = findViewById(R.id.add_mood_dialog_title);
            dialogTitle.setText(R.string.edit_mood_dialog_title);
            dialogDescription.setHint(R.string.edit_mood_dialog_description);
        }

        Button dialogOkBtn = findViewById(R.id.mood_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(moodObj == null){
                    String description = dialogDescription.getText().toString();
                    Mood mood = new Mood(selectedDate, 5, description);

                    colRef.document().set(mood).addOnSuccessListener(
                        new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Mood saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Greska!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

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
