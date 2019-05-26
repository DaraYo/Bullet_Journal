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
import com.example.bullet_journal.adapters.TaskEventDisplayAdapter;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Task;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksAndEventsActivity extends RootActivity {
    final Context context = this;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDayDisplay;

    private String choosenDate = "";

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
        choosenDate = CalendarCalculationsUtils.setCurrentDate(System.currentTimeMillis());
        dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis())+" "+choosenDate);

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
                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

                choosenDate = targetFormat.format(newDate);
                dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime())+" "+choosenDate);
            }
        };

        TaskEventDisplayAdapter taskAdapter = new TaskEventDisplayAdapter(this, buildTasks(), TaskType.TASK);
        ListView tasksListView = findViewById(R.id.tasks_list_view);
        tasksListView.setAdapter(taskAdapter);

        TaskEventDisplayAdapter eventAdapter = new TaskEventDisplayAdapter(this, buildEvents(), TaskType.EVENT);
        ListView eventsListView = findViewById(R.id.events_list_view);
        eventsListView.setAdapter(eventAdapter);

    }

    private List<Task> buildTasks(){
        List<Task> retVal = new ArrayList<>();

        Task task1 = new Task("Task 1", "About task 1...", false, null, TaskType.TASK);
        Task task2 = new Task("Task 2", "About task 2...", true, null, TaskType.TASK);
        Task task3 = new Task("Task 3", "About task 3...", false, null, TaskType.TASK);

        retVal.add(task1);
        retVal.add(task2);
        retVal.add(task3);

        return retVal;
    }

    private List<Task> buildEvents(){
        List<Task> retVal = new ArrayList<>();

        Task event1 = new Task("Event 1", "About event 1...", true, null, TaskType.EVENT);
        Task event2 = new Task("Event 2", "About event 2...", false, null, TaskType.EVENT);

        retVal.add(event1);
        retVal.add(event2);

        return retVal;
    }
}