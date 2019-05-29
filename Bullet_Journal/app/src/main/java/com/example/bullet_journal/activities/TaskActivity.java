package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ReminderAdapter;
import com.example.bullet_journal.dialogs.DeleteReminderDialog;
import com.example.bullet_journal.model.Reminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskActivity extends RootActivity {
    final Context context = this;
    private Button btn_delete;
    private Button btn_save;
    private Button btn_edit;
    private Button btn_done;
    private EditText titleET;
    private EditText descET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_done = (Button) findViewById(R.id.btn_done);
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
        ListView reminderListView = findViewById(R.id.task_reminders_list_view);
        reminderListView.setAdapter(remAdapter);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_save = (Button) findViewById(R.id.btn_save);

        titleET = (EditText) findViewById(R.id.title);
        descET = (EditText) findViewById(R.id.desc);

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_done.setVisibility(View.GONE);
                btn_delete.setVisibility(View.VISIBLE);
                btn_edit.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
                titleET.setEnabled(true);
                descET.setEnabled(true);
            }
        });


        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text= titleET.getText().toString();
                final Dialog dialog = new DeleteReminderDialog(context, text);
                dialog.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn_done.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.GONE);
                btn_edit.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                titleET.setEnabled(false);
                descET.setEnabled(false);
            }
        });

    }

    private List<Reminder> buildReminders(){
        List<Reminder> retVal = new ArrayList<>();

        Reminder r1 = new Reminder("Reminder 2", new Date(), false);
        Reminder r2 = new Reminder("Reminder 343242342342 ", new Date(), false);

        retVal.add(r1);
        retVal.add(r2);
        return retVal;
    }
}
