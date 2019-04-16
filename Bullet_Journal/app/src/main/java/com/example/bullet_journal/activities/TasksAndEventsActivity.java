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
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TasksAndEventsActivity extends RootActivity {
    final Context context = this;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDayDisplay;
    private TextView weekDisplay;

    private String choosenDate = "";
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_events);
        getSupportActionBar().setTitle("Task and Events");

        ImageButton addHabitBtn = (ImageButton) findViewById(R.id.add_task);
        addHabitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewTaskEventActivity.class);
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

                DatePickerDialog dialog = new DatePickerDialog(TasksAndEventsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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

        TextView task1 = (TextView) findViewById(R.id.task1);
        task1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskActivity.class);
                startActivity(intent);
            }
        });

        TextView task2 = (TextView) findViewById(R.id.task2);
        task2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskActivity.class);
                startActivity(intent);
            }
        });

        TextView task3 = (TextView) findViewById(R.id.task3);
        task3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout event = (LinearLayout) findViewById(R.id.event);
        event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                startActivity(intent);
            }
        });
    }
}