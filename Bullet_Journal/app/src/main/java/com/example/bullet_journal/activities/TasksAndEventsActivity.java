package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetEventsForDayAsyncTask;
import com.example.bullet_journal.async.GetTasksForDayAsyncTask;
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

    TaskEventDisplayAdapter taskAdapter;
    TaskEventDisplayAdapter eventAdapter;

    private String choosenDate = "";
    private long dateMillis;

    private List<Task> tasks = new ArrayList<>();
    private List<Task> events = new ArrayList<>();

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
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        dateMillis =  System.currentTimeMillis();
        if(bundle != null){
            if(bundle.containsKey("date")){
                dateMillis = bundle.getLong("date");
            }
        }

        dateDisplay = (TextView) findViewById(R.id.date_display);
        choosenDate = CalendarCalculationsUtils.dateMillisToString(dateMillis);
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

                fetchTasks();
                fetchEvents();
            }
        };

        taskAdapter = new TaskEventDisplayAdapter(this, tasks, TaskType.TASK);
        ListView tasksListView = findViewById(R.id.tasks_list_view);
        tasksListView.setAdapter(taskAdapter);

        eventAdapter = new TaskEventDisplayAdapter(this, events, TaskType.EVENT);
        ListView eventsListView = findViewById(R.id.events_list_view);
        eventsListView.setAdapter(eventAdapter);

        fetchTasks();
        fetchEvents();

    }

    private void fetchTasks(){

        AsyncTask<Long, Void, List<Task>> getTasksForDayAsyncTask = new GetTasksForDayAsyncTask(new AsyncResponse<List<Task>>(){
            @Override
            public void taskFinished(List<Task> retVal) {
                tasks.clear();
                tasks.addAll(retVal);
                taskAdapter.notifyDataSetChanged();
            }
        }).execute(dateMillis);

    }

    private void fetchEvents(){

        AsyncTask<Long, Void, List<Task>> getEventsForDayAsyncTask = new GetEventsForDayAsyncTask(new AsyncResponse<List<Task>>(){
            @Override
            public void taskFinished(List<Task> retVal) {
                events.clear();
                events.addAll(retVal);
                eventAdapter.notifyDataSetChanged();
            }
        }).execute(dateMillis);
    }
}