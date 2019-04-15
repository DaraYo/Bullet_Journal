package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

public class NewHabitActivity extends RootActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        getSupportActionBar().setTitle("Habit Tracker");

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HabitsActivity.class);
                startActivity(intent);
            }
        });

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HabitsActivity.class);
                startActivity(intent);
            }
        });
    }
}
