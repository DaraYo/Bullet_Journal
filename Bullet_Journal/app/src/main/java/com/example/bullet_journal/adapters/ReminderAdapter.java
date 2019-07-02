package com.example.bullet_journal.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.DeleteReminderAsyncTask;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Reminder;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private String choosenDate = "";
    private Context context;
    private View view;

    public ReminderAdapter(Context context, List<Reminder> objects) {
        super(context, R.layout.reminder_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        view = LayoutInflater.from(getContext()).inflate(R.layout.reminder_adapter, parent, false);

        final Reminder reminderObj = getItem(position);

        TextView reminderTitle = view.findViewById(R.id.reminder);
        reminderTitle.setText(reminderObj.getName());

        ImageButton deleteBtn = view.findViewById(R.id.reminder_delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(reminderObj);
                cancelAlarm(reminderObj);
                AsyncTask<Reminder, Void, Boolean> deleteHabitAsyncTask = new DeleteReminderAsyncTask(
                        new AsyncResponse<Boolean>() {
                            @Override
                            public void taskFinished(Boolean retVal) {
                                if(!retVal) {
                                    Toast.makeText(context, R.string.basic_error, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                ).execute(reminderObj);
                notifyDataSetChanged();

            }
        });

        TextView reminderDate = view.findViewById(R.id.reminder_time);
        choosenDate = CalendarCalculationsUtils.dateMillisToStringDateAndTime(reminderObj.getDate());
        reminderDate.setText(choosenDate);

        return view;

    }

    private void cancelAlarm(Reminder reminderObj) {
        AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);

        Toast.makeText(context,reminderObj.getId()+"", Toast.LENGTH_LONG).show();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderObj.getId().intValue(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}