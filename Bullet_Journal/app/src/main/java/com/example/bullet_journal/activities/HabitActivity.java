package com.example.bullet_journal.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ReminderAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetHabitDayByHabitAsyncTask;
import com.example.bullet_journal.async.InsertHabitAsyncTask;
import com.example.bullet_journal.async.UpdateHabitEventAsyncTask;
import com.example.bullet_journal.decorators.DayViewMoodDecorator;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitActivity extends RootActivity {
    final Context context = this;
    private Button btn_save;
    private Button btn_edit;
    private Button btn_back;
    private int heigt;
    private Button chooseMonth;
    private MaterialCalendarView calendarView;
    private EditText te_desc;
    private LinearLayout reminder_title;
    private LinearLayout reminders_list;
    private LinearLayout current_month_layout;

    private EditText title;
    private EditText description;

    private HabitRemindersWrapper habitObj;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        getSupportActionBar().setTitle("Habit Tracker");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_save = (Button) findViewById(R.id.btn_save);
        te_desc = (EditText) findViewById(R.id.desc);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("habitInfo")) {
            if (bundle.getSerializable("habitInfo") instanceof HabitRemindersWrapper) {
                habitObj = (HabitRemindersWrapper) bundle.getSerializable("habitInfo");
            }
        }

        isEdit = bundle.getBoolean("isEdit");

        if(habitObj == null){
            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT);
            finish();
        }

        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);

        title.setText(habitObj.getHabitEvent().getTitle());
        description.setText(habitObj.getHabitEvent().getText());


        final ImageButton showDialogBtn = (ImageButton) findViewById(R.id.add_reminder);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bindChanges();

                Intent intent = new Intent(context, AddReminderHabitActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habitInfo", habitObj);
                bundle.putInt("mode", 2);
                bundle.putBoolean("isEdit", isEdit);
                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            }
        });

        ReminderAdapter remAdapter = new ReminderAdapter(this, habitObj.getReminders());
        ListView reminderListView = findViewById(R.id.habit_reminders_list_view);
        reminderListView.setAdapter(remAdapter);


        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title.setEnabled(!title.isEnabled());
                description.setEnabled(!description.isEnabled());
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bindChanges();

                if(!isEdit){
                    startActivity(resolvePreviousPanel());
                    finish();
                }else{
                    AsyncTask<HabitRemindersWrapper, Void, Boolean> editTaskEventAsyncTask = new UpdateHabitEventAsyncTask(context, new AsyncResponse<Boolean>(){
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if(retVal){
                                startActivity(resolvePreviousPanel());
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.basic_error, Toast.LENGTH_SHORT);
                            }
                        }
                    }).execute(habitObj);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(resolvePreviousPanel());
                finish();
            }
        });

//        current_month_layout  = (LinearLayout) findViewById(R.id.current_month_layout_habit);
//        chooseMonth = (Button) findViewById(R.id.choose_another_month);
        calendarView = (MaterialCalendarView) findViewById(R.id.habit_calendar_view_habit);
        Button btn_view = (Button) findViewById(R.id.btn_monthly_view);

        heigt =te_desc.getHeight();
        btn_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (calendarView.getVisibility()==View.GONE)
                {
                    calendarView.setVisibility(View.VISIBLE);

//                    Toast.makeText(context, "height:"+description.getHeight(), Toast.LENGTH_SHORT).show();
                    te_desc.setHeight(55);
//                    current_month_layout.setVisibility(View.VISIBLE);
//                    chooseMonth.setVisibility(View.VISIBLE);
//                    te_desc.setVisibility(View.GONE);
                } else {
                    calendarView.setVisibility(View.GONE);
                    te_desc.setHeight(heigt);
//                    current_month_layout.setVisibility(View.GONE);
//                    chooseMonth.setVisibility(View.GONE);
//                    te_desc.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "height:"+description.getHeight(), Toast.LENGTH_SHORT).show();

                }
            }
        });


//        Date today = new Date();
//        DateFormat format = new SimpleDateFormat("MMM, yyyy");
//        String currentMonth = format.format(today);
//
//        TextView currentMonthText = (TextView) findViewById(R.id.current_month);
//        currentMonthText.setText(currentMonth);
//
//        chooseMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createDialogWithoutDateField().show();
//            }
//        });

        calendarView.setSelectedDate(LocalDate.now());
        calendarView.clearSelection();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);

        buildHabit(this);

    }


    ArrayList<MoodWrapper> daysOfHabit = new ArrayList<>();
    private void buildHabit(final Activity act){

        //TODO: get all day by habit


        AsyncTask<Habit, Void, List<Long>> getHabitDayByHabitAsyncTask =
                new GetHabitDayByHabitAsyncTask(new AsyncResponse<List<Long>>() {
            @Override
            public void taskFinished(List<Long> retVal) {
                for (Long date: retVal ) {
                    // get Datum

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(date);

                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                    // creat mood wapper
                    MoodWrapper mw1 = new MoodWrapper(5, new Date(calendar.getTimeInMillis()));

                    // add to retval

                    daysOfHabit.add(mw1);
                }
                calendarView.addDecorator(new DayViewMoodDecorator(act,daysOfHabit, MoodType.AWESOME));
            }
        }).execute(habitObj.getHabitEvent());
    }


    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2019, 4, 18);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    private void bindChanges(){
        habitObj.getHabitEvent().setTitle(title.getText().toString());
        habitObj.getHabitEvent().setText(description.getText().toString());
    }


    private Intent resolvePreviousPanel(){

        Intent intent;
        Bundle bundle = new Bundle();

        if(isEdit){
            intent = new Intent(context, HabitsActivity.class);
            intent.putExtras(bundle);
        }else {
            intent = new Intent(context, NewHabitActivity.class);
            bundle.putSerializable("habitInfo", habitObj);
            intent.putExtras(bundle);
        }

        return intent;
    }


}
