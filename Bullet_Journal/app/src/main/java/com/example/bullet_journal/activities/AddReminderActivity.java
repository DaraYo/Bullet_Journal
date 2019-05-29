package com.example.bullet_journal.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends RootActivity {
    final Context context = this;
    private MaterialCalendarView calendarView;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private TextView dateDayDisplay;

    private TextView timeDisplay;
    LinearLayout timeSwitchPannel;
    LinearLayout dateSwitchPannel;
    private Spinner dropdown;

    private Date choosenTime;
    private Date dateChoosen;
    private String choosenDate = "";
    private String choosenDate2 = "";
    private String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_reminder);
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//
//        dateDayDisplay = (TextView) findViewById(R.id.day_date_display);
//        choosenDate = CalendarCalculationsUtils.setCurrentDate("");
//        dateDayDisplay.setText(choosenDate);
//
//        dateSwitchPannel = (LinearLayout) findViewById(R.id.current_date_layout_2);
//
//        dateSwitchPannel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(AddReminderActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//
//        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//
//                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
//                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
//
//                Date date = null;
//                try {
//                    date = originalFormat.parse(month + "/" + day + "/" + year);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                dateChoosen= date;
//                choosenDate = targetFormat.format(date);
//                dateDayDisplay.setText(choosenDate);
//            }
//        };
//
//        timeDisplay = (TextView) findViewById(R.id.day_time_display);
//        DateFormat targetFormat = new SimpleDateFormat("HH:mm");
//
//        Date date = null;
//        try {
//            date = targetFormat.parse(00 + ":" + 00);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        choosenDate2 = targetFormat.format(date);
//        timeDisplay.setText(choosenDate2);
//
//        timeSwitchPannel = (LinearLayout) findViewById(R.id.current_time_layout);
//
//        timeSwitchPannel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int hour = cal.get(Calendar.HOUR_OF_DAY);
//                int minute = cal.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this, onTimeSetListener, hour, minute, true);
////                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                timePickerDialog.show();
//            }
//        });
//
//
//
//        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
//
//
//                DateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//                DateFormat targetFormat = new SimpleDateFormat("HH:mm");
//
//                Date date = null;
//                try {
//                    date = originalFormat.parse(hour + ":" + minutes);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                choosenTime = date;
//                choosenDate2 = targetFormat.format(date);
//                timeDisplay.setText(choosenDate2);
//            }
//        };
//
//
//
//
//        Button dialogOkBtn = findViewById(R.id.reminder_dialog_btn_ok);
//        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar c = Calendar.getInstance();
//                c.set(Calendar.HOUR_OF_DAY, choosenTime.getHours());
//                c.set(Calendar.MINUTE, choosenTime.getMinutes());
//                c.set(Calendar.SECOND, 0);
//                startAlarm(c);
//                finish();
//            }
//        });
//
//        Button dialogCancelBtn = findViewById(R.id.reminder_dialog_btn_cancel);
//        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
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
