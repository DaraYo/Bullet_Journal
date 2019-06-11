package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;

public class NewTaskEventActivity extends RootActivity {

    final Context context = this;

    private Spinner dropdown;
    private TimePicker picker;

    private long dateMillis;
    private String choosenDate;
    private String selectedLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        dateMillis = bundle.getLong("date");
        choosenDate = CalendarCalculationsUtils.dateMillisToString(dateMillis);

        picker = findViewById(R.id.task_time_picker);
        picker.setIs24HourView(true);

        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Task", "Event"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        TextView selecetdDateTextView = findViewById(R.id.task_event_dialog_date_str);
        selecetdDateTextView.setText(choosenDate);

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
                finish();
            }
        });

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btn_reminders = (Button) findViewById(R.id.btn_reminders);
        btn_reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLabel.equals("Event")) {
                    Intent intent = new Intent(context, EventActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TaskActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}

//    private void cancelAlarm() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        alarmManager.cancel(pendingIntent);
//    }
