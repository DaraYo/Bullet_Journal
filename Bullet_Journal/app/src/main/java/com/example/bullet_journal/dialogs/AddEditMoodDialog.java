package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.bullet_journal.R;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.CalculateNewAverageMoodAsyncTask;
import com.example.bullet_journal.async.GetDayAsyncTask;
import com.example.bullet_journal.async.InsertMoodAsyncTask;
import com.example.bullet_journal.async.UpdateMoodAsyncTask;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Mood;

public class AddEditMoodDialog extends Dialog {

    private Context context;
    private long selectedDate;
    private int selectedMoodVal;
    private Mood moodObj;

    private TextView dialogDescription;
    private LinearLayout selectedView;
    private TimePicker picker;

    private MainDatabase database;

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

        database = DatabaseClient.getInstance(null).getDatabase();

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

        AsyncTask<Long, Void, Day> detDayTask = new GetDayAsyncTask(new AsyncResponse<Day>(){

            @Override
            public void taskFinished(Day retVal) {
                dayObj = retVal;
            }
        }).execute(selectedDate);

        Button dialogOkBtn = findViewById(R.id.mood_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = dialogDescription.getText().toString();
                long newTime = CalendarCalculationsUtils.addHoursAndMinutesToDate(selectedDate, picker.getCurrentHour(), picker.getCurrentMinute());

                if(selectedMoodVal <= 0 || selectedMoodVal > 5){
                    Toast.makeText(getContext(), R.string.mood_select_mood_warning, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(moodObj == null){
                    moodObj = new Mood(null, null, dayObj.getId(), newTime, selectedMoodVal, description, false);

                    AsyncTask<Mood, Void, Boolean> insertMoodAsyncTask = new InsertMoodAsyncTask(new AsyncResponse<Boolean>(){

                        @Override
                        public void taskFinished(Boolean retVal) {
                            if(retVal){
                                calculateNewAverage();
                            }
                        }
                    }).execute(moodObj);

                }else{
                    moodObj.setDescription(description);
                    moodObj.setRating(selectedMoodVal);
                    moodObj.setDate(newTime);

                    AsyncTask<Mood, Void, Boolean> insertMoodAsyncTask = new UpdateMoodAsyncTask(new AsyncResponse<Boolean>(){

                        @Override
                        public void taskFinished(Boolean retVal) {
                            if(retVal){
                                calculateNewAverage();
                            }
                        }
                    }).execute(moodObj);

                }

                Toast.makeText(context, R.string.rating_saved, Toast.LENGTH_SHORT).show();
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

    private void calculateNewAverage() {

        AsyncTask<Mood, Void, Boolean> calculateNewAverageMoodAsyncTask = new CalculateNewAverageMoodAsyncTask(new AsyncResponse<Boolean>() {
            @Override
            public void taskFinished(Boolean retVal) {
                if(retVal){
                    Toast.makeText(context, R.string.mood_saved, Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(moodObj);
    }
}