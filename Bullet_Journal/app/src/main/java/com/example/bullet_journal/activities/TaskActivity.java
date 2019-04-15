package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.dialogs.AddReminderDialog;

public class TaskActivity extends RootActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TasksAndEventsActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_reminder);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new AddReminderDialog(context, null);
                dialog.show();
            }
        });
    }
}
