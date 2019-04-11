package com.example.bullet_journal.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.decorators.DayViewMoodDecorator;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MoodTrackerActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.mood_calendar_view);

        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(7, 15, 4.72, 4.56), MoodType.AWESOME));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(5, 24, 3.72, 4.25), MoodType.GOOD));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(16, 27, 3.00, 3.12), MoodType.AVERAGE));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(13, 3, 2.15, 2.00), MoodType.BAD));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(2, 20, 1.00, 1.5), MoodType.TERRIBLE));

    }

    private ArrayList<MoodWrapper> buildMood(int date1, int date2, double val1, double val2){
        Calendar calendar = Calendar.getInstance();

        calendar.set(2019, Calendar.APRIL, date1);
        MoodWrapper mw1 = new MoodWrapper(val1, new Date(calendar.getTimeInMillis()));

        calendar.set(2019, Calendar.APRIL, date2);
        MoodWrapper mw2 = new MoodWrapper(val2, new Date(calendar.getTimeInMillis()));

        ArrayList<MoodWrapper> retVal = new ArrayList<>();
        retVal.add(mw1);
        retVal.add(mw2);

        return retVal;
    }

}
