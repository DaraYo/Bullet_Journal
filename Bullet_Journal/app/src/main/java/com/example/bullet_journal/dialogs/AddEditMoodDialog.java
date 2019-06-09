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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Mood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddEditMoodDialog extends Dialog {

    private Context context;
    private long selectedDate;
    private int selectedMoodVal;
    private Mood moodObj;

    private TextView dialogDescription;
    private LinearLayout selectedView;
    private TimePicker picker;

    private FirebaseFirestore firestore;
    private FirebaseAuth fAuth;
    private CollectionReference dayCollectionRef;
    private CollectionReference moodsCollectionRef;

    private Day dayObj;

    public AddEditMoodDialog(Context context, long selectedDate, Mood moodObj){
        super(context);
        this.context = context;
        this.selectedDate = CalendarCalculationsUtils.trimTimeFromDateMillis(selectedDate);
        this.selectedMoodVal = -1;
        this.moodObj = moodObj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_mood_dialog);

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");

        TextView dateStr = findViewById(R.id.add_mood_dialog_date_str);
        dateStr.setText(CalendarCalculationsUtils.dateMillisToString(selectedDate));

        picker = findViewById(R.id.mood_time_picker);
        picker.setIs24HourView(true);

        dialogDescription = findViewById(R.id.description);

        if(moodObj != null){
            TextView dialogTitle = findViewById(R.id.add_mood_dialog_title);
            dialogTitle.setText(R.string.edit_mood_dialog_title);
            dialogDescription.setHint(R.string.edit_mood_dialog_description);
            dialogDescription.setText(moodObj.getDescription());
            resolveSelection(moodObj.getRating());
            picker.setCurrentHour((int) ((moodObj.getDate() / (1000*60*60)) % 24));
            picker.setCurrentMinute((int) ((moodObj.getDate() / (1000*60)) % 60));
        }

        getDay();

        Button dialogOkBtn = findViewById(R.id.mood_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = dialogDescription.getText().toString();
                long newTime = CalendarCalculationsUtils.addHoursAndMinutesToDate(selectedDate, picker.getCurrentHour(), picker.getCurrentMinute());
                final boolean isEdit;

                if(selectedMoodVal <= 0 || selectedMoodVal > 5){
                    Toast.makeText(getContext(), R.string.mood_select_mood_warning, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(moodObj == null){
                    isEdit = false;
                    moodObj = new Mood(moodsCollectionRef.document().getId(), newTime, selectedMoodVal, description);
                }else{
                    isEdit = true;
                    moodObj.setDescription(description);
                    moodObj.setRating(selectedMoodVal);
                    moodObj.setDate(newTime);
                }

                moodsCollectionRef.document(moodObj.getId()).set(moodObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, R.string.rating_saved, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                        }
                        calculateNewAverage();
                    }
                });
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

    private Day getDay(){

        dayCollectionRef.document(""+selectedDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    dayObj = document.toObject(Day.class);
                } else {
                    initializeDay();
                }

                moodsCollectionRef = dayCollectionRef.document(""+selectedDate).collection("Mood");
            }
        });

        return dayObj;
    }

    private void initializeDay(){
        //long selectedDateTrim = CalendarCalculationsUtils.trimTimeFromDateMillis(selectedDate);
        dayObj = new Day(selectedDate, null);

        dayCollectionRef.document(""+selectedDate).set(dayObj);
    }

    private void calculateNewAverage() {

        moodsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        double sum = 0;
                        int num = 0;

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Mood tempMood = snapshot.toObject(Mood.class);
                            sum = sum + tempMood.getRating();
                            num++;
                        }
                        dayObj.setAvgMood(sum/num);
                        dayCollectionRef.document("" + dayObj.getDate()).set(dayObj);
                        dismiss();
                    }
                }
            }
        });
    }
}