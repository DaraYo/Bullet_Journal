package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ReminderAdapter;
import com.example.bullet_journal.decorators.DayViewMoodDecorator;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitActivity extends RootActivity {
    final Context context = this;
    private Button btn_done;
    private Button chooseMonth;
    private MaterialCalendarView calendarView;
    private EditText te_desc;
    private LinearLayout reminder_title;
    private LinearLayout reminders_list;
    private LinearLayout current_month_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        getSupportActionBar().setTitle("Habit Tracker");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_done = (Button) findViewById(R.id.btn_done);
        te_desc = (EditText) findViewById(R.id.desc);
        reminder_title = (LinearLayout) findViewById(R.id.current_date_layout_2);
        reminders_list  = (LinearLayout) findViewById(R.id.habit_reminders_list_layout);
        current_month_layout  = (LinearLayout) findViewById(R.id.current_month_layout);
        chooseMonth = (Button) findViewById(R.id.choose_another_month);
        calendarView = (MaterialCalendarView) findViewById(R.id.habit_calendar_view);


        Button btn_view = (Button) findViewById(R.id.btn_monthly_view);

        btn_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (calendarView.getVisibility()==View.GONE)
                {
                    calendarView.setVisibility(View.VISIBLE);
                    current_month_layout.setVisibility(View.VISIBLE);
                    chooseMonth.setVisibility(View.VISIBLE);
                    te_desc.setVisibility(View.GONE);
                    reminder_title.setVisibility(View.GONE);
                    reminders_list.setVisibility(View.GONE);
                } else {
                    calendarView.setVisibility(View.GONE);
                    current_month_layout.setVisibility(View.GONE);
                    chooseMonth.setVisibility(View.GONE);
                    te_desc.setVisibility(View.VISIBLE);
                    reminder_title.setVisibility(View.VISIBLE);
                    reminders_list.setVisibility(View.VISIBLE);

                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_reminder);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddReminderActivity.class);
                startActivity(intent);
            }
        });

        ReminderAdapter remAdapter = new ReminderAdapter(this, buildReminders());
        ListView reminderListView = findViewById(R.id.habit_reminders_list_view);
        reminderListView.setAdapter(remAdapter);

        Date today = new Date();
        DateFormat format = new SimpleDateFormat("MMM, yyyy");
        String currentMonth = format.format(today);

        TextView currentMonthText = (TextView) findViewById(R.id.current_month);
        currentMonthText.setText(currentMonth);

        chooseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField().show();
            }
        });
        
        calendarView.setSelectedDate(LocalDate.now());
        calendarView.clearSelection();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);

        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(7, 15, 4.72, 4.56), MoodType.AWESOME));
        calendarView.addDecorator(new DayViewMoodDecorator(this, buildMood(5, 24, 3.72, 4.25), MoodType.AWESOME));

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

    private List<Reminder> buildReminders(){
        List<Reminder> retVal = new ArrayList<>();

        Reminder r1 = new Reminder(null, null, "Reminder1", System.currentTimeMillis(), false, null, null, false);

        retVal.add(r1);

        return retVal;
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2019, 4, 18);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

}
