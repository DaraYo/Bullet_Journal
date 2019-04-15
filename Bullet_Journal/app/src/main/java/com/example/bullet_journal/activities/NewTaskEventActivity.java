package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

public class NewTaskEventActivity extends RootActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Task", "Event"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
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
            }
        });

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TasksAndEventsActivity.class);
                startActivity(intent);
            }
        });

    }
}
