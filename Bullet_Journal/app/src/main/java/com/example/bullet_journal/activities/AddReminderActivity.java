package com.example.bullet_journal.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends RootActivity {
    final Context context = this;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePicker timePicker;

    private TextView dateDisplay;
    private TextView weekDisplay;

    private String choosenDate = "";
    private long dateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_reminder);
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = findViewById(R.id.reminder_time_picker);
        timePicker.setIs24HourView(true);

        dateDisplay = (TextView) findViewById(R.id.date_display);
        dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(System.currentTimeMillis());
        choosenDate = CalendarCalculationsUtils.dateMillisToString(dateMillis);
        dateDisplay.setText(choosenDate);

        weekDisplay = (TextView) findViewById(R.id.day_of_week);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis()));

        LinearLayout dateSwitchPanel = (LinearLayout) findViewById(R.id.current_date_layout);

        dateSwitchPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddReminderActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,  year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
                dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(newDate.getTime());
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

                choosenDate = targetFormat.format(newDate);
                dateDisplay.setText(choosenDate);
                weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime()));
            }
        };

        Button dialogOkBtn = findViewById(R.id.reminder_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                c.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                c.set(Calendar.SECOND, 0);
                startAlarm(c);
                finish();
            }
        });

        Button dialogCancelBtn = findViewById(R.id.reminder_dialog_btn_cancel);
        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private int i=1;
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("text", i+"text");
        intent.putExtra("title", i+"text");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
        i++;
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
