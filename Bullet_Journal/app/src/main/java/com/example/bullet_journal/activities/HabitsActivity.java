package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.HabitDisplayAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetDayAsyncTask;
import com.example.bullet_journal.async.GetHabitsForDayAsyncTask;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitsActivity extends RootActivity {
    final Context context = this;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDisplay;
    private TextView weekDisplay;
    private Day dayObj;

    HabitDisplayAdapter habitAdapter;

    private String choosenDate = "";
    private long dateMillis;

    private List<Habit> habits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_tracker);
        getSupportActionBar().setTitle("Habit Tracker");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        dateMillis =  System.currentTimeMillis();
        if(bundle != null){
            if(bundle.containsKey("date")){
                dateMillis = bundle.getLong("date");
            }
        }

        final long dayMillis = bundle.getLong("date");

        ImageButton addHabitBtn = (ImageButton) findViewById(R.id.add_habit);
        addHabitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                dateMillis =  System.currentTimeMillis();

                Intent intent = new Intent(context, NewHabitActivity.class);
                Bundle newbundle = new Bundle();
                newbundle.putLong("date", dateMillis);
                intent.putExtras(newbundle);

                startActivity(intent);
            }
        });

        dateDisplay = (TextView) findViewById(R.id.date_display);
        choosenDate = CalendarCalculationsUtils.dateMillisToString(dateMillis);
        dateDisplay.setText(choosenDate);

        weekDisplay = (TextView) findViewById(R.id.day_of_week);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis(), context));

        LinearLayout dateSwitchPanel = (LinearLayout) findViewById(R.id.current_date_layout);

        dateSwitchPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(HabitsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,  year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
                dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(newDate.getTime());
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

                choosenDate = targetFormat.format(newDate);
                dateDisplay.setText(choosenDate);
                weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime(), context));

                fetchHabits();
            }
        };

        AsyncTask<Long, Void, Day> getDayTask = new GetDayAsyncTask(context, new AsyncResponse<Day>() {

            @Override
            public void taskFinished(Day retVal) {
                if (retVal == null) {
                    Toast.makeText(context, R.string.selected_date_missing_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
                dayObj = retVal;
//                Toast.makeText(context, "day: "+dayObj.toString(), Toast.LENGTH_LONG).show();
                habitAdapter = new HabitDisplayAdapter(context, habits, dayObj);
                ListView habitsListView = findViewById(R.id.habits_list_view);
                habitsListView.setAdapter(habitAdapter);

                fetchHabits();
            }
        }).execute(dayMillis);



    }

    private void fetchHabits(){

        AsyncTask<Long, Void, List<Habit>> getHabit = new GetHabitsForDayAsyncTask(new AsyncResponse<List<Habit>>(){
            @Override
            public void taskFinished(List<Habit> retVal) {
                habits.clear();
                habits.addAll(retVal);
                habitAdapter.notifyDataSetChanged();
            }
        }).execute(dateMillis);

    }

}