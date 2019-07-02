package com.example.bullet_journal.async;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.AlertReceiver;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.ArrayList;

public class InsertTaskEventAsyncTask extends AsyncTask<TaskEventRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private Context context;

    public InsertTaskEventAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(TaskEventRemindersWrapper... taskEventRemindersWrappers) {
        try{
            Task taskEvent = taskEventRemindersWrappers[0].getTaskEvent();
            ArrayList<Reminder> reminders = taskEventRemindersWrappers[0].getReminders();

            long id = database.getTaskEventDao().insert(taskEvent);

            for(Reminder reminder : reminders){
                reminder.setTaskId(id);
                long idRem = database.getReminderDao().insert(reminder);
                reminder.setId(idRem);
                startAlarm(taskEvent, reminder);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void startAlarm( Task t, Reminder r) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("text", t.getTitle());
        if (t.getType()== TaskType.TASK) {
            intent.putExtra("title", "Task Reminder");
        } else {
            intent.putExtra("title", "Event Reminder");

        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, r.getId().intValue(), intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, r.getDate(), pendingIntent);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }
}