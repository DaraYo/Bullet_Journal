package com.example.bullet_journal.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import com.example.bullet_journal.activities.EventActivity;
import com.example.bullet_journal.activities.HabitsActivity;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Mood;
import com.example.bullet_journal.model.Reminder;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddReminderDialog extends Dialog {
    final Context context;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDayDisplay;
    private TextView weekDisplay;

    private String choosenDate = "";
    private String dateStr="";
    private String selectedDate;
    private Reminder remObj;

    public AddReminderDialog(Context context, Reminder remObj){
        super(context);
        this.context = context;
        this.remObj = remObj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_reminder_dialog);
//
//        final TextView dateStr = findViewById(R.id.add_mood_dialog_date_str);
//        dateStr.setText(selectedDate);
//
//        dateDayDisplay = (TextView) findViewById(R.id.day_date_display);
//        choosenDate = CalendarCalculationsUtils.setCurrentDate("");
//        dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(choosenDate)+" "+choosenDate);
//
//        final LinearLayout dateSwitchPannel = (LinearLayout) findViewById(R.id.current_date_layout_2);
//
//        final TextView time = findViewById(R.id.time);
////
////        dateSwitchPannel.setOnClickListener(new View.OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////                Calendar mcurrentTime = Calendar.getInstance();
////                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
////                int minute = mcurrentTime.get(Calendar.MINUTE);
////                TimePickerDialog mTimePicker;
////                mTimePicker = new TimePickerDialog(AddReminderDialog.this, new TimePickerDialog.OnTimeSetListener() {
////                    @Override
////                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
////                        final String time1 = selectedHour + ":" + selectedMinute;
////                    }
////                }, hour, minute, true);//Yes 24 hour time
////                mTimePicker.setTitle("Select Time");
////                mTimePicker.show();
////            }
////        });
//
//
//        Button dialogOkBtn = findViewById(R.id.reminder_dialog_btn_ok);
//        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        Button dialogCancelBtn = findViewById(R.id.reminder_dialog_btn_cancel);
//        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }
}
