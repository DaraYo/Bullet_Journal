package com.example.bullet_journal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetFollowingTasksAndEventsAsyncTask;
import com.example.bullet_journal.async.GetTasksForDayAsyncTask;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetUserAsyncTask;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.helpClasses.PreferencesHelper;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.model.User;
import com.example.bullet_journal.services.PushToFirestoreJobService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

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
    private TextView navigationNameLastname;
    private TextView navigationUsername;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private User currentUser;

    private DatabaseClient databaseClient;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private String choosenDate = "";
    private long dateMillis;
    private final Context context= this;
    private List<Task> followingTasks = new ArrayList<>();

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final Context context = this;

        if(fAuth.getCurrentUser() == null){
            Intent intent= new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle= new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                navigationUsername= findViewById(R.id.username);
                navigationUsername.setText(PreferencesHelper.getUsername(context));
                navigationNameLastname= findViewById(R.id.name_and_lastname);
                navigationNameLastname.setText(PreferencesHelper.getNameLastname(context));
            }

//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                navigationUsername= findViewById(R.id.username);
//                navigationUsername.setText(PreferencesHelper.getUsername(context));
//                navigationNameLastname= findViewById(R.id.name_and_lastname);
//                navigationNameLastname.setText(PreferencesHelper.getNameLastname(context));
//            }
        };
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

        eventAdapter = new FollowingEventsDisplayAdapter(MainActivity.this, followingTasks);
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
                fetchFollowingEvents();
            }
        };

        setupSharedPreferences();

        if(fAuth.getCurrentUser() != null){
            scheduleJob();
            fetchFollowingEvents();
        }
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
            Bundle bundle = new Bundle();
            bundle.putLong("date", dateMillis);
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (id == R.id.nav_spendings) {
            Intent intent= new Intent(this, WalletActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent= new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_logout) {
            PreferencesHelper.saveUsername(this, "");
            PreferencesHelper.saveNameLastname(this, "");
            fAuth.signOut();
            Intent intent= new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void fetchFollowingEvents(){

        AsyncTask<Long, Void, List<Task>> getFollowingTasksAsyncTask = new GetFollowingTasksAndEventsAsyncTask(MainActivity.this, new AsyncResponse<List<Task>>(){
            @Override
            public void taskFinished(List<Task> retVal) {
                followingTasks.clear();
                followingTasks.addAll(retVal);
                eventAdapter = new FollowingEventsDisplayAdapter(MainActivity.this, followingTasks);
                eventAdapter.notifyDataSetChanged();
                eventListView.setAdapter(eventAdapter);
            }
        }).execute(dateMillis);
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
                updateLocale(myLocale);
                break;
            }
        }
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
        fetchFollowingEvents();
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, PushToFirestoreJobService.class);
        JobInfo jobInfoObj = new JobInfo.Builder(777, componentName)
                .setPeriodic(PushToFirestoreJobService.REDO_FIRESTORE_PULL)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(jobInfoObj);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("DB JOB", "SCHEDULED");
        } else {
            Log.d("DB JOB", "FAILED");
        }
    }

}
