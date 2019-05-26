package com.example.bullet_journal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bullet_journal.activities.DiaryActivity;
import com.example.bullet_journal.activities.HabitsActivity;
import com.example.bullet_journal.activities.LoginActivity;
import com.example.bullet_journal.activities.MoodTrackerActivity;
import com.example.bullet_journal.activities.RatingActivity;
import com.example.bullet_journal.activities.SettingsActivity;
import com.example.bullet_journal.activities.SignUpActivity;
import com.example.bullet_journal.activities.TasksAndEventsActivity;
import com.example.bullet_journal.activities.WalletActivity;
import com.example.bullet_journal.adapters.FollowingEventsDisplayAdapter;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends RootActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDisplay;
    private TextView weekDisplay;
    private ListView eventListView;
    private FollowingEventsDisplayAdapter eventAdapter;

    private String choosenDate = "";
    private int dayNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dateDisplay = (TextView) findViewById(R.id.date_display_1);
        choosenDate = CalendarCalculationsUtils.setCurrentDate(System.currentTimeMillis());
        dateDisplay.setText(choosenDate);

        weekDisplay = (TextView) findViewById(R.id.day_of_week_1);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis()));

        eventAdapter = new FollowingEventsDisplayAdapter(MainActivity.this, buildEvents(choosenDate));
        eventListView = findViewById(R.id.event_preview_list_view);
        eventListView.setAdapter(eventAdapter);

        LinearLayout dateSwitchPanel = (LinearLayout) findViewById(R.id.current_date_layout);

        dateSwitchPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,  year, month, day);
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
                dateDisplay.setText(choosenDate);
                weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime()));

                eventAdapter = new FollowingEventsDisplayAdapter(MainActivity.this, buildEvents(choosenDate));
                eventAdapter.notifyDataSetChanged();
                eventListView.setAdapter(eventAdapter);
            }
        };



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tracker) {
            Intent intent= new Intent(this, MoodTrackerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_diary) {
            Intent intent= new Intent(this, DiaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ratings) {
            Intent intent= new Intent(this, RatingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tasks) {
            Intent intent= new Intent(this, TasksAndEventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_habbits) {
            Intent intent= new Intent(this, HabitsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_spendings) {
            Intent intent= new Intent(this, WalletActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent= new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_login) {
            Intent intent= new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signup) {
            Intent intent= new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public List<Task> buildEvents(String theDate){
        List<Task> retVal = new ArrayList<>();

        Task event1 = new Task("Event 1", "About event 1...", true, theDate+" 11:20", TaskType.EVENT);
        Task event2 = new Task("Event 2", "About event 2...", false, theDate+" 15:40", TaskType.EVENT);
        Task event3 = new Task("Event 3", "About event 3...", true, theDate+" 16:00", TaskType.EVENT);
        Task event4 = new Task("Event 4", "About event 4...", false, theDate+" 18:35", TaskType.EVENT);
        Task event5 = new Task("Event 5", "About event 5...", false, theDate+" 19:10", TaskType.EVENT);
        Task event6 = new Task("Event 6", "About event 6...", false, theDate+" 20:30", TaskType.EVENT);
        Task event7 = new Task("Event 7", "About event 7...", false, theDate+" 22:00", TaskType.EVENT);


        retVal.add(event1);
        retVal.add(event2);
        retVal.add(event3);
        retVal.add(event4);
        retVal.add(event5);
        retVal.add(event6);
        retVal.add(event7);

        return retVal;
    }

}
