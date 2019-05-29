package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

import java.util.Date;

public class NewTaskEventActivity extends RootActivity {
    final Context context = this;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setTitle("Tasks and Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Task", "Event"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String label = adapterView.getItemAtPosition(i).toString();
                if (label=="Event"){
                    timeSwitchPannel.setVisibility(View.VISIBLE);
                    dateSwitchPannel.setVisibility(View.VISIBLE);
                } else{
                    timeSwitchPannel.setVisibility(View.GONE);
                    dateSwitchPannel.setVisibility(View.GONE);
                }


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

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                finish();

                String label = dropdown.getSelectedItem().toString();
                if (label=="Event"){

//                    Calendar c = Calendar.getInstance();
//                    c.set(Calendar.HOUR_OF_DAY, choosenTime.getHours());
//                    c.set(Calendar.MINUTE, choosenTime.getMinutes());
//                    c.set(Calendar.SECOND, 0);
//                    startAlarm(c);

                    Intent intent = new Intent(context, EventActivity.class);
                    startActivity(intent);

                } else{
                    Intent intent = new Intent(context, TaskActivity.class);
                    startActivity(intent);
                }
            }
        });

//        dateDayDisplay = (TextView) findViewById(R.id.day_date_display);
//        choosenDate = CalendarCalculationsUtils.setCurrentDate("");
//        dateDayDisplay.setText(choosenDate);
//
//        dateSwitchPannel = (LinearLayout) findViewById(R.id.current_date_layout_2);
//        dateSwitchPannel.setVisibility(View.GONE);
//
//        dateSwitchPannel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(NewTaskEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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
//        timeSwitchPannel.setVisibility(View.GONE);
//
//        timeSwitchPannel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int hour = cal.get(Calendar.HOUR_OF_DAY);
//                int minute = cal.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTaskEventActivity.this, onTimeSetListener, hour, minute, true);
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



    }



//
//    private void cancelAlarm() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        alarmManager.cancel(pendingIntent);
//    }
}
