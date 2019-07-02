package com.example.bullet_journal.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.HabitActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.DeleteHabitDayAsyncTask;
import com.example.bullet_journal.async.GetHabitDayAsyncTask;
import com.example.bullet_journal.async.GetRemindersForHabitAsyncTask;
import com.example.bullet_journal.async.InsertHabitDayAsyncTask;
import com.example.bullet_journal.dialogs.DeleteReminderDialog;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;
import java.util.List;

public class HabitDisplayAdapter extends ArrayAdapter<Habit>  {

    private Context context;
    private Day dayObj;

    public HabitDisplayAdapter(Context context, List<Habit> objects, Day dayObj) {
        super(context, R.layout.habit_preview_adapter, objects);
        this.context = context;
        this.dayObj=dayObj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.habit_preview_adapter, parent, false);

        final Habit habitObj = getItem(position);

        TextView habitTitle = view.findViewById(R.id.habit_title);

        habitTitle.setText(habitObj.getTitle());

        habitTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HabitActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<Reminder> rem = fetchReminders(habitObj.getId());
                bundle.putSerializable("habitInfo", new HabitRemindersWrapper(habitObj, rem));
                bundle.putBoolean("isEdit", true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        final CheckBox habitCheckBox = view.findViewById(R.id.habit_tracker_check);

        //TODO:show checked habits
        final HabitRemindersWrapper habit = new HabitRemindersWrapper(habitObj,fetchReminders(habitObj.getId()) );
        habit.setDay(dayObj);
        AsyncTask<HabitRemindersWrapper, Void, HabitDay> getHabitDayTask = new GetHabitDayAsyncTask(new AsyncResponse<HabitDay>() {

            @Override
            public void taskFinished(HabitDay retVal) {
                if (retVal!=null) {
                    habitCheckBox.setChecked(true);
                }
                else
                {
                    habitCheckBox.setChecked(false);
                }
            }
        }).execute(habit);

        habitCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (habitCheckBox.isChecked()){
                    //TODO: check day if exist; if not -> create; else-> add day to lista
                    AsyncTask<HabitRemindersWrapper, Void, Boolean> inserHabitDayTask = new InsertHabitDayAsyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {

                        }
                    }).execute(habit);

                } else{
                    // TODO: delete day from list
                    AsyncTask<HabitRemindersWrapper, Void, Boolean> inserHabitDayTask = new DeleteHabitDayAsyncTask(new AsyncResponse<Boolean>() {

                        @Override
                        public void taskFinished(Boolean retVal) {

                        }
                    }).execute(habit);
                }
            }
        });

        ImageButton delete_btn = view.findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DeleteReminderDialog dialog = new DeleteReminderDialog(context, habitObj);
                dialog.show();

            }
        });

        return view;
    }
    ArrayList<Reminder> reminders = new ArrayList<>();
    private ArrayList<Reminder>  fetchReminders(Long taskId) {


        AsyncTask<Long, Void, List<Reminder>> getRemindersForTaskEventAsyncTask = new GetRemindersForHabitAsyncTask(new AsyncResponse<List<Reminder>>() {
            @Override
            public void taskFinished(List<Reminder> retVal) {
                reminders.clear();
                reminders.addAll(retVal);

            }
        }).execute(taskId);
        return reminders;
    }


}
