package com.example.bullet_journal.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ReminderAdapter;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.example.bullet_journal.model.Reminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventActivity extends RootActivity {

    final Context context = this;

    private Button btn_back;
    private Button btn_save;
    private Button btn_edit;


    private EditText titleET;
    private EditText descET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_reminder);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddReminderActivity.class);
                startActivity(intent);
            }
        });

        ReminderAdapter remAdapter = new ReminderAdapter(this, buildReminders());
        ListView reminderListView = findViewById(R.id.task_reminders_list_view);
        reminderListView.setAdapter(remAdapter);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_save = (Button) findViewById(R.id.btn_save);

        titleET = (EditText) findViewById(R.id.title);
        descET = (EditText) findViewById(R.id.desc);

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                titleET.setEnabled(!titleET.isEnabled());
                descET.setEnabled(!descET.isEnabled());
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleET = (EditText) findViewById(R.id.title);
        descET = (EditText) findViewById(R.id.desc);

    }

    private List<Reminder> buildReminders(){
        List<Reminder> retVal = new ArrayList<>();

        Reminder r1 = new Reminder("Reminder 1", new Date().getTime(), false);

        retVal.add(r1);

        return retVal;
    }

        private void cancelAlarm() {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

            alarmManager.cancel(pendingIntent);
    }
}
