package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetDayAsyncTask;
import com.example.bullet_journal.async.InsertHabitAsyncTask;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;

public class NewHabitActivity extends RootActivity {

    final Context context = this;


    private TextView selecetdDateTextView;
    private TextView title;
    private TextView description;

    private Day dayObj;
    private String choosenDate;
    private String selectedLabel;

    private HabitRemindersWrapper habbitObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        getSupportActionBar().setTitle("Habit Tracker");

        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);
        selecetdDateTextView = findViewById(R.id.task_event_dialog_date_str);

        Bundle bundle = getIntent().getExtras();

        final long dayMillis = bundle.getLong("date");
        AsyncTask<Long, Void, Day> getDayTask = new GetDayAsyncTask(new AsyncResponse<Day>() {

            @Override
            public void taskFinished(Day retVal) {
                if (retVal == null) {
                    Toast.makeText(context, R.string.selected_date_missing_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
                dayObj = retVal;
                choosenDate = CalendarCalculationsUtils.dateMillisToString(dayObj.getDate());
                selecetdDateTextView.setText(choosenDate);
            }
        }).execute(dayMillis);

        if (bundle.containsKey("habitInfo")) {
            if (bundle.getSerializable("habitInfo") instanceof HabitRemindersWrapper) {
                habbitObj = (HabitRemindersWrapper) bundle.getSerializable("habitInfo");

                title.setText(habbitObj.getHabitEvent().getTitle());
                description.setText(habbitObj.getHabitEvent().getText());
            }
        }

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HabitsActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putLong("date", dayMillis);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buildWrapper();

                AsyncTask<HabitRemindersWrapper, Void, Boolean> insertHabitAsyncTask = new InsertHabitAsyncTask(new AsyncResponse<Boolean>() {
                    @Override
                    public void taskFinished(Boolean retVal) {
                        if(retVal){
                            Intent intent = new Intent(context, HabitsActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_LONG).show();
                    }
                }).execute(habbitObj);
            }
        });

        Button btn_reminders = (Button) findViewById(R.id.btn_reminders);
        btn_reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildWrapper();

                Bundle bundle = new Bundle();
                bundle.putSerializable("habitInfo", habbitObj);
                bundle.putBoolean("isEdit", false);

                    Intent intent = new Intent(context, HabitActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);


                finish();
            }
        });
    }
    private void buildWrapper(){

        //TODO: userId

        if (habbitObj == null) {

            Bundle bundle = getIntent().getExtras();
            final long dayMillis = bundle.getLong("date");
            AsyncTask<Long, Void, Day> getDayTask = new GetDayAsyncTask(new AsyncResponse<Day>() {

                @Override
                public void taskFinished(Day retVal) {
                    if (retVal == null) {
                        Toast.makeText(context, R.string.selected_date_missing_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    dayObj = retVal;
                }
            }).execute(dayMillis);

            Habit dataObj = new Habit(null, null, title.getText().toString(), description.getText().toString(), 1L, false, false);
            habbitObj = new HabitRemindersWrapper(dataObj, new ArrayList<Reminder>());
            habbitObj.setDay(dayObj);
//            Toast.makeText(context, habbitObj.toString(), Toast.LENGTH_LONG).show();
        }else{
            habbitObj.getHabitEvent().setTitle(title.getText().toString());
            habbitObj.getHabitEvent().setText(description.getText().toString());
        }
    }


}
