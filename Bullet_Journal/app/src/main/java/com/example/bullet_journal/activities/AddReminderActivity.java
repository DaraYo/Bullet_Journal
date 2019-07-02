package com.example.bullet_journal.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.InsertReminderAsyncTask;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

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
    private TextView title;
    private Calendar calendar;
    private Long reminderId;
    Reminder reminder;
    Bundle bundle;

    private String choosenDate = "";
    private long dateMillis;

    /* Da znamo za sta se kreira Reminder : Task => 1, Event => 2, Habit => 3 */
    private int mode;
    private TaskEventRemindersWrapper taskEventObj;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_reminder);
        getSupportActionBar().setTitle(R.string.reminder_str);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = findViewById(R.id.reminder_time_picker);
        timePicker.setIs24HourView(true);

        dateDisplay = (TextView) findViewById(R.id.date_display);
        dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(System.currentTimeMillis());
        choosenDate = CalendarCalculationsUtils.dateMillisToString(dateMillis);
        dateDisplay.setText(choosenDate);

        weekDisplay = (TextView) findViewById(R.id.day_of_week);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis(), context));

        title = findViewById(R.id.reminder_title);

        /* Da znamo za sta se kreira Reminder : Task => 1, Event => 2, Habit => 3 */
        bundle = getIntent().getExtras();
        if (bundle.containsKey("mode")) {
            this.mode = bundle.getInt("mode");
        }else{
            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT);
            finish();
        }

        isEdit = bundle.getBoolean("isEdit");

        if (bundle.containsKey("taskEventInfo")) {
            if(bundle.getSerializable("taskEventInfo") instanceof TaskEventRemindersWrapper){
                taskEventObj = (TaskEventRemindersWrapper) bundle.getSerializable("taskEventInfo");
            }
        }

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
                weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime(), getApplicationContext()));
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


                Intent intent = resolveReturn(false, c);
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
                Intent intent = resolveReturn(true, null);
                if(intent != null){
                    startActivity(intent);
                }

                finish();
            }
        });
    }

    private Intent resolveReturn(boolean isCancel, Calendar c){
        long selectedDate = CalendarCalculationsUtils.addHoursAndMinutesToDate(dateMillis, timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        Log.i("SELECTED DATE: ", "Is : "+selectedDate);
        Toast.makeText(context, ""+selectedDate, Toast.LENGTH_LONG);


        Bundle bundle = new Bundle();
        calendar = c;
        switch (mode) {
            case 1 : {
                if(!isCancel){
                    reminder = new Reminder(null, null, title.getText().toString(), selectedDate, false, null, taskEventObj.getTaskEvent().getId(), false);

                    //TODO: save reminder
                    AsyncTask<Reminder, Void, Long> insertHabitAsyncTask = new InsertReminderAsyncTask(new AsyncResponse<Long>() {
                        @Override
                        public void taskFinished(Long retVal) {
                            if(retVal==0L) {
                                Toast.makeText(context, R.string.basic_error, Toast.LENGTH_LONG).show();
                            } else {
                                reminderId = retVal;
//                                Toast.makeText(context, "reminderId: "+reminderId, Toast.LENGTH_LONG).show();
                                startAlarm(taskEventObj.getTaskEvent());
                            }
                        }
                    }).execute(reminder);
                    reminder.setId(reminderId);
                    taskEventObj.getReminders().add(reminder);


                }
                bundle.putSerializable("taskEventInfo", taskEventObj);
                bundle.putBoolean("isEdit", isEdit);

                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtras(bundle);
                return intent;
            }
            case 2 : {
                if(!isCancel){
                    reminder = new Reminder(null, null, title.getText().toString(), selectedDate, false, null, taskEventObj.getTaskEvent().getId(), false);

                    //TODO: save reminder
                    AsyncTask<Reminder, Void, Long> insertHabitAsyncTask = new InsertReminderAsyncTask(new AsyncResponse<Long>() {
                        @Override
                        public void taskFinished(Long retVal) {
                            if(retVal==0L) {
                                Toast.makeText(context, R.string.basic_error, Toast.LENGTH_LONG).show();
                            } else {
                                reminderId = retVal;
//                                Toast.makeText(context, "reminderId: "+reminderId, Toast.LENGTH_LONG).show();
                                startAlarm(taskEventObj.getTaskEvent());
                                reminder.setId(reminderId);
                                taskEventObj.getReminders().add(reminder);
                            }
                        }
                    }).execute(reminder);


                }
                bundle.putSerializable("taskEventInfo", taskEventObj);
                bundle.putBoolean("isEdit", isEdit);

                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtras(bundle);
                return intent;
            }
            case 3 : {

                /* To Do... */
                Intent intent = new Intent(context, HabitActivity.class);

                return intent;
            }
        }
        return null;
    }


    private void startAlarm( Task t) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("text", t.getTitle());
        if (t.getType()== TaskType.TASK) {
            intent.putExtra("title", "Task Reminder");
        } else {
            intent.putExtra("title", "Event Reminder");

        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminderId.intValue(), intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
