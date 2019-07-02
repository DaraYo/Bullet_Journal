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
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;
import java.util.List;

public class UpdateHabitEventAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private Context context;

    public UpdateHabitEventAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
        this.context = context;

    }


    @Override
    protected Boolean doInBackground(HabitRemindersWrapper... habitRemindersWrappers) {

        try{
            Habit habit  = habitRemindersWrappers[0].getHabitEvent();
            ArrayList<Reminder> reminders = habitRemindersWrappers[0].getReminders();

            database.getHabitDao().update(habit);

            List<Reminder> dbReminedrs = database.getReminderDao().getAllRemindersForHabit(habit.getId());
            Long id = habit.getId();

            // Delete removed reminders
            if(!dbReminedrs.isEmpty()){
                for(Reminder tempDbReminder : dbReminedrs){
                    boolean found = false;

                    if(!reminders.isEmpty()){
                        for(Reminder tempReminder : reminders){
                            if(tempDbReminder.getId().equals(tempReminder.getId())){
                                found = true;
                                break;
                            }
                        }
                    }

                    if(!found){
                        database.getReminderDao().delete(tempDbReminder);
                        cancelAlarm(tempDbReminder);
                    }
                }
            }

            // Insert new reminders
            if(!reminders.isEmpty()){
                for(Reminder reminder : reminders){
                    if(reminder.getId() == null){
                        reminder.setHabitId(id);
                        long idRem= database.getReminderDao().insert(reminder);
                        reminder.setId(idRem);
                        startAlarm(habit, reminder);
                    }
                }
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }

    private void startAlarm(Habit h, Reminder r) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("text", h.getTitle());
        intent.putExtra("title", "Habit Reminder");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, r.getId().intValue(), intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, r.getDate(),
                AlarmManager.INTERVAL_DAY, pendingIntent);    }

    private void cancelAlarm(Reminder r) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, r.getId().intValue(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}