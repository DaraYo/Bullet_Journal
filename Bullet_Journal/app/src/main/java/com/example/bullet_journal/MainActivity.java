package com.example.bullet_journal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.helpClasses.PreferencesHelper;
import com.example.bullet_journal.model.Task;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends RootActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dateDisplay;
    private TextView weekDisplay;
    private ListView eventListView;
    private FollowingEventsDisplayAdapter eventAdapter;
    private SharedPreferences sharedPreferences;

    private DatabaseClient databaseClient;

    private String choosenDate = "";
    private long dateMillis;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        databaseClient = DatabaseClient.getInstance(getApplicationContext());

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dateDisplay = (TextView) findViewById(R.id.date_display_1);
        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
        dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(System.currentTimeMillis());
        dateDisplay.setText(choosenDate);

        weekDisplay = (TextView) findViewById(R.id.day_of_week_1);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis(), context));

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
                dateMillis = CalendarCalculationsUtils.trimTimeFromDateMillis(newDate.getTime());
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");

                choosenDate = targetFormat.format(newDate);
                dateDisplay.setText(choosenDate);

                weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime(), context));

                eventAdapter = new FollowingEventsDisplayAdapter(MainActivity.this, buildEvents(choosenDate));
                eventAdapter.notifyDataSetChanged();
                eventListView.setAdapter(eventAdapter);
            }
        };

        setupSharedPreferences();



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
            Bundle bundle = new Bundle();
            bundle.putLong("date", dateMillis);
            intent.putExtras(bundle);
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

        Task event1 = new Task(null, null, "Event 1", "About event 1", null, System.currentTimeMillis() + 30000, false, false, TaskType.EVENT);
        Task event2 = new Task(null, null, "Event 2", "About event 1", null, System.currentTimeMillis() + 50000, false, false, TaskType.EVENT);
        Task event3 = new Task(null, null, "Event 3", "About event 1", null, System.currentTimeMillis() + 70000, true, false, TaskType.EVENT);
        Task event4 = new Task(null, null, "Event 4", "About event 1", null, System.currentTimeMillis() + 90000, false, false, TaskType.EVENT);
        Task event5 = new Task(null, null, "Event 5", "About event 1", null, System.currentTimeMillis() + 120000, false, false, TaskType.EVENT);
        Task event6 = new Task(null, null, "Event 6", "About event 1", null, System.currentTimeMillis() + 180000, true, false, TaskType.EVENT);
        Task event7 = new Task(null, null, "Event 7", "About event 1", null, System.currentTimeMillis() + 210000, false, false, TaskType.EVENT);

        retVal.add(event1);
        retVal.add(event2);
        retVal.add(event3);
        retVal.add(event4);
        retVal.add(event5);
        retVal.add(event6);
        retVal.add(event7);

        return retVal;
    }

    public void setupSharedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "language_list": {
                Toast.makeText(this, PreferencesHelper.getLanguage(getApplicationContext()), Toast.LENGTH_LONG).show();
                Locale myLocale= new Locale(PreferencesHelper.getLanguage(getApplicationContext()));
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
//                conf.locale = myLocale;
//                res.updateConfiguration(conf, dm);
                updateLocale(myLocale);
                break;
            }
        }
//        Toast.makeText(getApplicationContext(), "Promenjena podesavanja", Toast.LENGTH_LONG).show();
        hideDrawerMenu();
    }

    private void hideDrawerMenu(){
        Menu nav_Menu = navigationView.getMenu();
        Set<String> items = PreferencesHelper.getMenuItems(this);
        if(!items.contains("not_initialized")) {
            String[] defaultItems = getResources().getStringArray(R.array.pref_options_values);

            for (String item : defaultItems) {
                boolean visibility = items.contains(item);
                nav_Menu.findItem(getMenuId(item)).setVisible(visibility);
            }
        }
    }

    private int getMenuId(String item){
        switch (item){
            case "mood_tracker":
                return R.id.nav_tracker;
            case "diary":
                return R.id.nav_diary;
            case "ratings":
                return R.id.nav_ratings;
            case "tasks":
                return R.id.nav_tasks;
            case "habbits":
                return R.id.nav_habbits;
            case "spendings":
                return R.id.nav_spendings;
            default:
                return  -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideDrawerMenu();
    }
}
