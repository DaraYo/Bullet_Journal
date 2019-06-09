package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.decorators.DayViewMoodDecorator;
import com.example.bullet_journal.dialogs.AddEditMoodDialog;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    private FirebaseFirestore firestore;
    private FirebaseAuth fAuth;
    private CollectionReference dayCollectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dates = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");

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

        dayCollectionRef.whereGreaterThan("date", CalendarCalculationsUtils.getBeginingOfTheMonth(day.getMonth()-1, day.getYear()))
                .whereLessThan("date", CalendarCalculationsUtils.getEndOfTheMonth(day.getMonth()-1, day.getYear())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    dates.clear();
                    if(task.getResult().size() > 0){
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Day tempDay = snapshot.toObject(Day.class);
                            dates.add(tempDay);
                            bindDecorators();
                        }
                    }
                }else{
                    Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
