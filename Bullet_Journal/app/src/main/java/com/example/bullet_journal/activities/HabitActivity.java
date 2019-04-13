package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.bullet_journal.MainActivity;
import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.decorators.DayViewMoodDecorator;
import com.example.bullet_journal.dialogs.AddEditMoodDialog;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HabitActivity extends RootActivity {
    final Context context = this;
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_tracker);
        getSupportActionBar().setTitle("Habit Tracker");

        ImageButton addHabitBtn = (ImageButton) findViewById(R.id.add_habit);
        addHabitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewHabitActivity.class);
                startActivity(intent);
            }
        });
    }
}