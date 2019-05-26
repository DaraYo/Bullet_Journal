package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

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

public class MoodTrackerActivity extends RootActivity {

    final Context context = this;
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = (MaterialCalendarView) findViewById(R.id.mood_calendar_view);
        calendarView.setSelectedDate(LocalDate.now());

        ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_mood);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CalendarDay selectedDate = calendarView.getSelectedDate();
                final Dialog dialog = new AddEditMoodDialog(context, selectedDate.getDay()+"/"+selectedDate.getMonth()+"/"+selectedDate.getYear(), null);
                dialog.show();
            }
        });

        ImageButton openMoodBtn = (ImageButton) findViewById(R.id.show_in_new_mood);
        openMoodBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MoodPreviewActivity.class);
                CalendarDay selectedDay = calendarView.getSelectedDate();
                Bundle bundle = new Bundle();
                bundle.putString("date", selectedDay.getDay()+"/"+selectedDay.getMonth()+"/"+selectedDay.getYear());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(7, 15, 4.72, 4.56), MoodType.AWESOME));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(5, 24, 3.72, 4.25), MoodType.GOOD));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(16, 27, 3.00, 3.12), MoodType.AVERAGE));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(13, 3, 2.15, 2.00), MoodType.BAD));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(2, 20, 1.00, 1.5), MoodType.TERRIBLE));

    }

    private ArrayList<MoodWrapper> buildMood(int date1, int date2, double val1, double val2){
        Calendar calendar = Calendar.getInstance();

        calendar.set(2019, Calendar.MAY, date1);
        MoodWrapper mw1 = new MoodWrapper(val1, new Date(calendar.getTimeInMillis()));

        calendar.set(2019, Calendar.MAY, date2);
        MoodWrapper mw2 = new MoodWrapper(val2, new Date(calendar.getTimeInMillis()));

        ArrayList<MoodWrapper> retVal = new ArrayList<>();
        retVal.add(mw1);
        retVal.add(mw2);

        return retVal;
    }

}
