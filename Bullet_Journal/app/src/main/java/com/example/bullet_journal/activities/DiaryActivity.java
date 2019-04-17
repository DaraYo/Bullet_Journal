package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaryActivity extends RootActivity {
    final Context context = this;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDayDisplay;
    private TextView weekDisplay;

    private String choosenDate = "";
    private int dayNum = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateDayDisplay = (TextView) findViewById(R.id.day_date_display);
        choosenDate = CalendarCalculationsUtils.setCurrentDate("");
        dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(choosenDate)+" "+choosenDate);

        LinearLayout dateSwitchPannel = (LinearLayout) findViewById(R.id.current_date_layout_2);

        dateSwitchPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DiaryActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

                Date date = null;
                try {
                    date = originalFormat.parse(month + "/" + day + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                choosenDate = targetFormat.format(date);
                dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(choosenDate)+" "+choosenDate);
            }
        };

//        ImageButton takePhotoBtn = (ImageButton) findViewById(R.id.take_a_picture);
//        ImageButton attachPicBtn = (ImageButton) findViewById(R.id.attach_picture);
        ImageButton goToGallery= (ImageButton) findViewById(R.id.go_to_gallery);
        goToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, AlbumActivity.class);
                startActivity(intent);
            }
        });
        ImageButton attachLocationBtn = (ImageButton) findViewById(R.id.attach_location);

    }
}