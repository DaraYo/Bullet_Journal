package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private int selectedMoodVal;
    private Mood moodObj;

    private TextView dialogDescription;
    private LinearLayout selectedView;

    private FirebaseFirestore firestore;
    private CollectionReference colRef;

    public AddEditMoodDialog(Context context, long selectedDate, Mood moodObj){
        super(context);
        this.context = context;
        this.selectedDate = selectedDate;
        this.selectedMoodVal = -1;
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
                    if(selectedMoodVal > 0){
                        Mood mood = new Mood(selectedDate, selectedMoodVal, description);

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
                    }else{
                        Toast.makeText(context, "Please select mood", Toast.LENGTH_SHORT).show();
                    }

                }

                dismiss();
            }
        });

        ImageButton awesomeImageBtn = (ImageButton) findViewById(R.id.mood_awesome_btn);
        awesomeImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(5);
            }
        });

        ImageButton goodImageBtn = (ImageButton) findViewById(R.id.mood_good_btn);
        goodImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(4);
            }
        });

        ImageButton okImageBtn = (ImageButton) findViewById(R.id.mood_ok_btn);
        okImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(3);
            }
        });

        ImageButton badImageBtn = (ImageButton) findViewById(R.id.mood_bad_btn);
        badImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(2);
            }
        });

        ImageButton terribleImageBtn = (ImageButton) findViewById(R.id.mood_terrible_btn);
        terribleImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resolveSelection(1);
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

    private void resolveSelection(int newMoodVal){

        this.selectedMoodVal = newMoodVal;

        if(selectedView != null){
            selectedView.setBackgroundResource(0);
        }

        switch(this.selectedMoodVal) {
            case (5) :
                selectedView = findViewById(R.id.awesome_layout);
                break;
            case (4) :
                selectedView = findViewById(R.id.good_layout);
                break;
            case (3) :
                selectedView = findViewById(R.id.ok_layout);
                break;
            case (2) :
                selectedView = findViewById(R.id.bad_layout);
                break;
            case (1) :
                selectedView = findViewById(R.id.terrible_layout);
                break;
        }

        selectedView.setBackground(ContextCompat.getDrawable(context, R.drawable.mood_border));
    }

}
