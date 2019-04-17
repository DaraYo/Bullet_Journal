package com.example.bullet_journal.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.dialogs.AddReminderDialog;

public class EventActivity extends RootActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btn_done = (Button) findViewById(R.id.btn_done);
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
                final Dialog dialog = new AddReminderDialog(context, null);
                dialog.show();
            }
        });
    }
}
