package com.example.bullet_journal.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.InsertReminderAsyncTask;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.Calendar;
import java.util.Date;

public class AddReminderHabitActivity extends RootActivity {
    final Context context = this;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePicker timePicker;

    private TextView title;
    private long dateMillis;


    private Calendar calendar;
    private Long reminderId;
    private HabitRemindersWrapper habitObj;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_reminder_habit);
        getSupportActionBar().setTitle(R.string.reminder_str);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = findViewById(R.id.reminder_time_picker);
        timePicker.setIs24HourView(true);

        dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(System.currentTimeMillis());

        title = findViewById(R.id.reminder_title);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("habitInfo")) {
            if(bundle.getSerializable("habitInfo") instanceof HabitRemindersWrapper){
                habitObj = (HabitRemindersWrapper) bundle.getSerializable("habitInfo");
            }
        }

        isEdit = bundle.getBoolean("isEdit");

        Button dialogOkBtn = findViewById(R.id.reminder_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                c.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                c.set(Calendar.SECOND, 0);

                Intent intent = resolveReturn(false);
                if(intent != null){
                    startActivity(intent);
                }

                finish();
            }
        });

        Button dialogCancelBtn = findViewById(R.id.reminder_dialog_btn_cancel);
        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = resolveReturn(true);
                if(intent != null){
                    startActivity(intent);
                }

                finish();
            }
        });
    }

    private Intent resolveReturn(boolean isCancel){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month, year);
        dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(newDate.getTime());
        long selectedDate = CalendarCalculationsUtils.addHoursAndMinutesToDate(dateMillis, timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        Log.i("SELECTED DATE: ", "Is : "+selectedDate);
        Toast.makeText(context, ""+selectedDate, Toast.LENGTH_LONG);

        Reminder reminder = new Reminder(null, null, title.getText().toString(), selectedDate, false, null, null, false);

        Bundle bundle = new Bundle();
        if(!isCancel){
            habitObj.getReminders().add(reminder);
        }
        bundle.putSerializable("habitInfo", habitObj);
        bundle.putBoolean("isEdit", isEdit);

        Intent intent = new Intent(context, HabitActivity.class);
        intent.putExtras(bundle);
        return intent;

    }


    private void startAlarm(Habit h) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("text", h.getTitle());
        intent.putExtra("title", "Habit Reminder");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminderId.intValue(), intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
