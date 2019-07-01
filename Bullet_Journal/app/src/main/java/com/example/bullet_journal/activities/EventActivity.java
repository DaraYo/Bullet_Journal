package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ReminderAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.UpdateTaskEventAsyncTask;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

public class EventActivity extends RootActivity {

    final Context context = this;

    private Button btn_back;
    private Button btn_save;
    private Button btn_edit;

    private EditText title;
    private EditText description;
    private EditText eventTime;

    private TaskEventRemindersWrapper taskEventObj;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("taskEventInfo")) {
            if (bundle.getSerializable("taskEventInfo") instanceof TaskEventRemindersWrapper) {
                taskEventObj = (TaskEventRemindersWrapper) bundle.getSerializable("taskEventInfo");
            }
        }

        isEdit = bundle.getBoolean("isEdit");

        if (taskEventObj == null) {
            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT);
            finish();
        }

        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);

        title.setText(taskEventObj.getTaskEvent().getTitle());
        description.setText(taskEventObj.getTaskEvent().getText());

        TextView eventTime = findViewById(R.id.event_time);
        eventTime.setText(CalendarCalculationsUtils.dateMillisToStringTime(taskEventObj.getTaskEvent().getDate()));

        final ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_reminder);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bindChanges();

                Intent intent = new Intent(context, AddReminderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskEventInfo", taskEventObj);
                bundle.putInt("mode", 2);
                bundle.putBoolean("isEdit", isEdit);
                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            }
        });

        ReminderAdapter remAdapter = new ReminderAdapter(this, this.taskEventObj.getReminders());
        ListView reminderListView = findViewById(R.id.task_reminders_list_view);
        reminderListView.setAdapter(remAdapter);

        btn_edit = findViewById(R.id.btn_edit);
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title.setEnabled(!title.isEnabled());
                description.setEnabled(!description.isEnabled());
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindChanges();

                if(!isEdit){
                    startActivity(resolvePreviousPanel());
                    finish();
                }else{
                    AsyncTask<TaskEventRemindersWrapper, Void, Boolean> updateTaskEventAsyncTask = new UpdateTaskEventAsyncTask(EventActivity.this, new AsyncResponse<Boolean>(){
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if(retVal){
                                startActivity(resolvePreviousPanel());
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.basic_error, Toast.LENGTH_SHORT);
                            }
                        }
                    }).execute(taskEventObj);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(resolvePreviousPanel());
                finish();
            }
        });
    }

    private Intent resolvePreviousPanel(){

        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong("date", CalendarCalculationsUtils.trimTimeFromDateMillis(taskEventObj.getTaskEvent().getDate()));

        if(isEdit){
            intent = new Intent(context, TasksAndEventsActivity.class);
            intent.putExtras(bundle);
        }else {
            intent = new Intent(context, NewTaskEventActivity.class);
            bundle.putSerializable("taskEventInfo", taskEventObj);
            intent.putExtras(bundle);
        }

        return intent;
    }

    private void bindChanges(){
        taskEventObj.getTaskEvent().setTitle(title.getText().toString());
        taskEventObj.getTaskEvent().setText(description.getText().toString());
    }


    /*
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
    */
}
