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

        picker = findViewById(R.id.mood_time_picker);
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

        Button btn_continue = (Button) findViewById(R.id.btn_save);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLabel.equals("Event")){
                    Intent intent = new Intent(context, EventActivity.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(context, TaskActivity.class);
                    startActivity(intent);
                }
            }
        });



//        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
//        dateDayDisplay.setText(choosenDate);
//
//
//        dateSwitchPannel = (LinearLayout) findViewById(R.id.current_date_layout_2);
//        dateSwitchPannel.setVisibility(View.GONE);
//
//        dateSwitchPanel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(NewTaskEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,  year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//
//        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//
//                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
//                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
//
//                choosenDate = targetFormat.format(newDate);
//                dateDayDisplay.setText(choosenDate);
//
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