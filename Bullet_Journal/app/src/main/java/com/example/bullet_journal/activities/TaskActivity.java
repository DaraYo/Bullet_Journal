package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ReminderAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.EditTaskEventAsyncTask;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

public class TaskActivity extends RootActivity {

    final Context context = this;

    private Button btn_back;
    private Button btn_edit;
    private Button btn_save;

    private EditText title;
    private EditText description;
    private CheckBox statusCheck;

    private TaskEventRemindersWrapper taskEventObj;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey("taskEventInfo")){
            if(bundle.getSerializable("taskEventInfo") instanceof TaskEventRemindersWrapper){
                taskEventObj = (TaskEventRemindersWrapper) bundle.getSerializable("taskEventInfo");
            }
        }

        isEdit = bundle.getBoolean("isEdit");

        if(taskEventObj == null){
            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT);
            finish();
        }

        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);
        statusCheck = findViewById(R.id.task_status);

        title.setText(taskEventObj.getTaskEvent().getTitle());
        description.setText(taskEventObj.getTaskEvent().getText());
        statusCheck.setChecked(taskEventObj.getTaskEvent().isStatus());

        final ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_reminder);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bindChanges();

                Intent intent = new Intent(context, AddReminderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskEventInfo", taskEventObj);
                bundle.putInt("mode", 1);
                bundle.putBoolean("isEdit", isEdit);
                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            }
        });

        ReminderAdapter remAdapter = new ReminderAdapter(this, this.taskEventObj.getReminders());
        ListView reminderListView = findViewById(R.id.task_reminders_list_view);
        reminderListView.setAdapter(remAdapter);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_save = (Button) findViewById(R.id.btn_save);

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
                    Intent intent = returnToPreviousPanel();
                    startActivity(intent);
                }else{
                    AsyncTask<TaskEventRemindersWrapper, Void, Boolean> editTaskEventAsyncTask = new EditTaskEventAsyncTask(new AsyncResponse<Boolean>(){
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if(retVal){
                                Intent intent = new Intent(context, TasksAndEventsActivity.class);
                                startActivity(intent);
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
                if(!isEdit){
                    Intent intent = returnToPreviousPanel();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(context, TasksAndEventsActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

    }

    private Intent returnToPreviousPanel(){
        Intent intent = new Intent(context, NewTaskEventActivity.class);

        Bundle bundle = new Bundle();
        bundle.putLong("date", CalendarCalculationsUtils.trimTimeFromDateMillis(taskEventObj.getTaskEvent().getDate()));
        bundle.putSerializable("taskEventInfo", taskEventObj);
        intent.putExtras(bundle);

        return intent;
    }

    private void bindChanges(){
        taskEventObj.getTaskEvent().setTitle(title.getText().toString());
        taskEventObj.getTaskEvent().setText(description.getText().toString());
        taskEventObj.getTaskEvent().setStatus(statusCheck.isChecked());
    }

}
