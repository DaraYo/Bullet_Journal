package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetDayAsyncTask;
import com.example.bullet_journal.async.InsertTaskEventAsyncTask;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.ArrayList;

public class NewTaskEventActivity extends RootActivity {

    final Context context = this;

    private Spinner dropdown;
    private TimePicker picker;

    private TextView selecetdDateTextView;
    private TextView title;
    private TextView description;

    private Day dayObj;
    private String choosenDate;
    private String selectedLabel;

    private TaskEventRemindersWrapper taskEventObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);

        selecetdDateTextView = findViewById(R.id.task_event_dialog_date_str);
        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Task", "Event"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        picker = findViewById(R.id.task_time_picker);
        picker.setIs24HourView(true);

        Bundle bundle = getIntent().getExtras();

        long dayMillis = bundle.getLong("date");

        AsyncTask<Long, Void, Day> getDayTask = new GetDayAsyncTask(new AsyncResponse<Day>() {

            @Override
            public void taskFinished(Day retVal) {
                if (retVal == null) {
                    Toast.makeText(context, R.string.selected_date_missing_error, Toast.LENGTH_SHORT);
                    finish();
                }
                dayObj = retVal;
                choosenDate = CalendarCalculationsUtils.dateMillisToString(dayObj.getDate());
                selecetdDateTextView.setText(choosenDate);
            }
        }).execute(dayMillis);

        if (bundle.containsKey("taskEventInfo")) {
            if (bundle.getSerializable("taskEventInfo") instanceof TaskEventRemindersWrapper) {
                taskEventObj = (TaskEventRemindersWrapper) bundle.getSerializable("taskEventInfo");

                title.setText(taskEventObj.getTaskEvent().getTitle());
                description.setText(taskEventObj.getTaskEvent().getText());
                picker.setCurrentHour((int) ((taskEventObj.getTaskEvent().getDate() / (1000 * 60 * 60)) % 24 + 2));
                picker.setCurrentMinute((int) ((taskEventObj.getTaskEvent().getDate() / (1000 * 60)) % 60));

                if(taskEventObj.getTaskEvent().getType() == TaskType.TASK){
                    dropdown.setSelection(0);
                    selectedLabel = "Task";
                }else{
                    dropdown.setSelection(1);
                    selectedLabel = "Event";
                }
            }
        }

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLabel = adapterView.getItemAtPosition(i).toString();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TasksAndEventsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buildWrapper();

                AsyncTask<TaskEventRemindersWrapper, Void, Boolean> insertTaskEventAsyncTask = new InsertTaskEventAsyncTask(new AsyncResponse<Boolean>() {
                    @Override
                    public void taskFinished(Boolean retVal) {
                        if(retVal){
                            Intent intent = new Intent(context, TasksAndEventsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        Toast.makeText(context, R.string.basic_error, Toast.LENGTH_LONG);
                    }
                }).execute(taskEventObj);
            }
        });

        Button btn_reminders = (Button) findViewById(R.id.btn_reminders);
        btn_reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildWrapper();

                Bundle bundle = new Bundle();
                bundle.putSerializable("taskEventInfo", taskEventObj);
                bundle.putBoolean("isEdit", false);

                if (selectedLabel.equals("Event")) {
                    Intent intent = new Intent(context, EventActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                finish();
            }
        });
    }

    private TaskType getType(String input) {
        switch (input.toLowerCase()) {
            case "task":
                return TaskType.TASK;
            case "event":
                return TaskType.EVENT;
        }
        return null;
    }

    private void buildWrapper(){
        long selectedTime = CalendarCalculationsUtils.addHoursAndMinutesToDate(dayObj.getDate(), picker.getCurrentHour(), picker.getCurrentMinute());

        if (taskEventObj == null) {
            Task dataObj = new Task(null, null, title.getText().toString(), description.getText().toString(), dayObj.getId(), selectedTime, false, false, getType(selectedLabel));
            taskEventObj = new TaskEventRemindersWrapper(dataObj, new ArrayList<Reminder>());
        }else{
            taskEventObj.getTaskEvent().setTitle(title.getText().toString());
            taskEventObj.getTaskEvent().setText(description.getText().toString());
            taskEventObj.getTaskEvent().setDate(selectedTime);
            taskEventObj.getTaskEvent().setType(getType(selectedLabel));
        }
    }

}

//    private void cancelAlarm() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        alarmManager.cancel(pendingIntent);
//    }
