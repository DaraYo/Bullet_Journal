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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksAndEventsActivity extends RootActivity {

    final Context context = this;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDisplay;
    private TextView weekDisplay;

    private String choosenDate = "";
    private long dateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_events);
        getSupportActionBar().setTitle("Task and Events");

        ImageButton addTaskBtn = (ImageButton) findViewById(R.id.add_task);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewTaskEventActivity.class);

                Bundle bundle = new Bundle();
                bundle.putLong("date", dateMillis);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateDisplay = (TextView) findViewById(R.id.date_display);
        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
        dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(System.currentTimeMillis());
        dateDisplay.setText(choosenDate);

        weekDisplay = (TextView) findViewById(R.id.day_of_week);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis()));

        LinearLayout dateSwitchPanel = (LinearLayout) findViewById(R.id.current_date_layout);

        dateSwitchPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TasksAndEventsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,  year, month, day);
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
                weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime()));
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

        Task task1 = new Task("Task 1", "About task 1...", false, 0, TaskType.TASK);
        Task task2 = new Task("Task 2", "About task 2...", true, 0, TaskType.TASK);
        Task task3 = new Task("Task 3", "About task 3...", false, 0, TaskType.TASK);

        retVal.add(task1);
        retVal.add(task2);
        retVal.add(task3);

        return retVal;
    }

    private List<Task> buildEvents(){
        List<Task> retVal = new ArrayList<>();

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        Task event1 = new Task("Event 1", "About event 1...", true, cal.getTime().getTime(), TaskType.EVENT);
        cal.add(Calendar.HOUR_OF_DAY, 12); // adds twelve hour
        Task event2 = new Task("Event 2", "About event 2...", false,  cal.getTime().getTime(), TaskType.EVENT);

        retVal.add(event1);
        retVal.add(event2);

        return retVal;
    }
}