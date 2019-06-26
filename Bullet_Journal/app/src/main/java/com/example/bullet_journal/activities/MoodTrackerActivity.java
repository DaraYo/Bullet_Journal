package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetDaysBetweenAsyncTask;
import com.example.bullet_journal.decorators.DayViewMoodDecorator;
import com.example.bullet_journal.dialogs.AddEditMoodDialog;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoodTrackerActivity extends RootActivity {

    final Context context = this;
    private MaterialCalendarView calendarView;

    private List<Day> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dates = new ArrayList<>();

        calendarView = (MaterialCalendarView) findViewById(R.id.mood_calendar_view);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                calendarView.removeDecorators();
                fetchDays(date);
            }
        });

        ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_mood);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CalendarDay selectedDate = calendarView.getSelectedDate();
                final Dialog dialog = new AddEditMoodDialog(context, CalendarCalculationsUtils.convertCalendarDialogDate(selectedDate.getDay(), selectedDate.getMonth(), selectedDate.getYear()).getTime(), null);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        calendarView.removeDecorators();
                        fetchDays(calendarView.getSelectedDate());
                    }
                });

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
                bundle.putLong("date", CalendarCalculationsUtils.convertCalendarDialogDate(selectedDay.getDay(), selectedDay.getMonth(), selectedDay.getYear()).getTime());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        fetchDays(calendarView.getSelectedDate());

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDays(this.calendarView.getSelectedDate());
    }

    private void fetchDays(CalendarDay day){

        AsyncTask<Long, Void, List<Day>> getDaysBetweenAsyncTask = new GetDaysBetweenAsyncTask(new AsyncResponse<List<Day>>() {
            @Override
            public void taskFinished(List<Day> retVal) {
                dates.clear();
                if(retVal != null){
                    dates.addAll(retVal);
                    Log.i("IMAAAAAAAAAAA", "Ovoliko: "+retVal.size());
                }
                bindDecorators();
            }
        }).execute(new Long[] {new Long(CalendarCalculationsUtils.getBeginningOfTheMonth(day.getMonth()-1, day.getYear())), new Long(CalendarCalculationsUtils.getEndOfTheMonth(day.getMonth()-1, day.getYear()))});
    }

    private void bindDecorators(){

        List<MoodWrapper> awesome = new ArrayList<>();
        List<MoodWrapper> good = new ArrayList<>();
        List<MoodWrapper> ok = new ArrayList<>();
        List<MoodWrapper> bad = new ArrayList<>();
        List<MoodWrapper> terrible = new ArrayList<>();

        for(Day tempDay : dates){
            MoodWrapper mw = new MoodWrapper(tempDay.getAvgMood(), new Date(tempDay.getDate()));
            if(mw.getAvgValue() > 4){
                awesome.add(mw);
                continue;
            }if(mw.getAvgValue() > 3){
                good.add(mw);
                continue;
            }
            if(mw.getAvgValue() > 2){
                ok.add(mw);
                continue;
            }
            if(mw.getAvgValue() > 1){
                bad.add(mw);
                continue;
            }
            if(mw.getAvgValue() > 0){
                terrible.add(mw);
                continue;
            }
        }

        calendarView.addDecorator(new DayViewMoodDecorator(this, awesome, MoodType.AWESOME));
        calendarView.addDecorator(new DayViewMoodDecorator(this, good, MoodType.GOOD));
        calendarView.addDecorator(new DayViewMoodDecorator(this, ok, MoodType.AVERAGE));
        calendarView.addDecorator(new DayViewMoodDecorator(this, bad, MoodType.BAD));
        calendarView.addDecorator(new DayViewMoodDecorator(this, terrible, MoodType.TERRIBLE));

    }


}
