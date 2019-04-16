package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.HabitDisplayAdapter;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Habit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitsActivity extends RootActivity {
    final Context context = this;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDayDisplay;

    private String choosenDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_tracker);
        getSupportActionBar().setTitle("Habit Tracker");

        ImageButton addHabitBtn = (ImageButton) findViewById(R.id.add_habit);
        addHabitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewHabitActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateDayDisplay = (TextView) findViewById(R.id.day_date_display);
        choosenDate = CalendarCalculationsUtils.setCurrentDate("");
        dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(choosenDate)+" "+choosenDate);

        LinearLayout dateSwitchPannel = (LinearLayout) findViewById(R.id.current_date_layout_2);

        dateSwitchPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(HabitsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

                Date date = null;
                try {
                    date = originalFormat.parse(month + "/" + day + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                choosenDate = targetFormat.format(date);
                dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(choosenDate)+" "+choosenDate);
            }
        };

        HabitDisplayAdapter habitsAdapter = new HabitDisplayAdapter(this, buildHabits());
        ListView habitsListView = findViewById(R.id.habits_list_view);
        habitsListView.setAdapter(habitsAdapter);

    }

    private List<Habit> buildHabits(){
        List<Habit> retVal = new ArrayList<>();

        Habit habit1 = new Habit("Habbit 1", "About habbit 1...", false);
        Habit habit2 = new Habit("Habbit 2", "About habbit 2...", true);
        Habit habit3 = new Habit("Habbit 3", "About habbit 3...", false);

        retVal.add(habit1);
        retVal.add(habit2);
        retVal.add(habit3);

        return retVal;
    }
}