package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.predefinedClasses.CustomAppBarLayoutBehavior;
import com.example.bullet_journal.predefinedClasses.LinedEditText;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    final Context context = this;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dayDisplay;
    private TextView dateDisplay;
    private LinedEditText diaryContent;
    private RelativeLayout editTextToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout.LayoutParams layoutParams;

    private String choosenDate = "";
    private int dayNum = 6;
    private String textContent;
    private String title = "Diary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("Diary");
        layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior)layoutParams.getBehavior()).setScrollBehavior(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar_collapse);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.image_collapse_bar);

        floatingActionButton= findViewById(R.id.gallery);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, AlbumActivity.class);
                startActivity(intent);
            }
        });

        diaryContent = findViewById(R.id.diary_text);
        textContent = diaryContent.getText().toString();

        dayDisplay = findViewById(R.id.day_display_only);
        dateDisplay = findViewById(R.id.date_display_only);

        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
        dateDisplay.setText(choosenDate);
        dayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis())); //+" "+choosenDate);

//=======
//        dateDayDisplay = (TextView) findViewById(R.id.day_date_display);
//        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
//        dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis())+" "+choosenDate);
//>>>>>>> 59c3576de8bd6db8a7843e20d562c7c8d9172ce6

        RelativeLayout dateSwitchPannel = findViewById(R.id.current_date_layout_2);

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
                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
                choosenDate = targetFormat.format(newDate);
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

                dayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime()));//+" "+choosenDate);
                dateDisplay.setText(choosenDate);
//=======
//                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
//                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

//                choosenDate = targetFormat.format(newDate);
//                dateDayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime())+" "+choosenDate);
//>>>>>>> 59c3576de8bd6db8a7843e20d562c7c8d9172ce6
            }
        };

//        ImageButton takePhotoBtn = (ImageButton) findViewById(R.id.take_a_picture);
//        ImageButton attachPicBtn = (ImageButton) findViewById(R.id.attach_picture);
//        ImageButton goToGallery= (ImageButton) findViewById(R.id.go_to_gallery);
//        goToGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(context, AlbumActivity.class);
//                startActivity(intent);
//            }
//        });
//        ImageButton attachLocationBtn = (ImageButton) findViewById(R.id.attach_location);

//        String lang= PreferencesHelper.getLanguage(this);
//        Toast.makeText(this, lang, Toast.LENGTH_LONG).show();
        editTextToolbar = findViewById(R.id.edit_text_toolbar);
        diaryContent.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(diaryContent.hasFocus()){
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        diaryContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    appBarLayout.setExpanded(false, true);
                    ((CustomAppBarLayoutBehavior)layoutParams.getBehavior()).setScrollBehavior(false);
                    editTextToolbar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "blabla", Toast.LENGTH_LONG).show();
                } else {
                    ((CustomAppBarLayoutBehavior)layoutParams.getBehavior()).setScrollBehavior(true);
                    editTextToolbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "its goooone", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.add_pic).setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.add_pic).setVisible(true);
        return true;
    }

    public void disableCollapse() {
//        disableScroll();
//        appBarLayout.setEnabled(false);
//        appBarLayout.setActivated(false);

//        imageView.setVisibility(View.GONE);
//        collapsingToolbarLayout.setTitleEnabled(false);

//        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
//        layoutParams.setScrollFlags(0);
//        collapsingToolbarLayout.setLayoutParams(layoutParams);
//        collapsingToolbarLayout.setActivated(false);
//
//        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
//        appBarLayout.requestLayout();

//        toolbar.setTitle(title);
//        appBarLayout.setExpanded(false, false);
//        appBarLayout.setLiftable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.add_pic: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return false;
    }
}