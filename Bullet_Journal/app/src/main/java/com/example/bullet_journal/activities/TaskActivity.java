package com.example.bullet_journal.activities;

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
import com.example.bullet_journal.model.Reminder;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends RootActivity {

    final Context context = this;

    private Button btn_back;
    private Button btn_edit;
    private Button btn_save;
    private EditText titleET;
    private EditText descET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
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

        Reminder r1 = new Reminder(null, null, "Reminder1", System.currentTimeMillis() + 10000, false, null, null, false);
        Reminder r2 = new Reminder(null, null, "Reminder2", System.currentTimeMillis() + 20000, false, null, null, false);
        Reminder r3 = new Reminder(null, null, "Reminder3", System.currentTimeMillis() + 30000, false, null, null, false);
        Reminder r4 = new Reminder(null, null, "Reminder4", System.currentTimeMillis() + 40000, false, null, null, false);
        Reminder r5 = new Reminder(null, null, "Reminder5", System.currentTimeMillis() + 50000, false, null, null, false);
        Reminder r6 = new Reminder(null, null, "Reminder6", System.currentTimeMillis() + 60000, false, null, null, false);

        retVal.add(r1);
        retVal.add(r2);
        retVal.add(r3);
        retVal.add(r4);
        retVal.add(r5);
        retVal.add(r6);

        return retVal;
    }
}
