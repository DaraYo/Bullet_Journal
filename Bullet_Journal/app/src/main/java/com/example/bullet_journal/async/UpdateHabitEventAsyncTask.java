package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;
import java.util.List;

public class UpdateHabitEventAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public UpdateHabitEventAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(HabitRemindersWrapper... habitRemindersWrappers) {

        try{
            Habit habit  = habitRemindersWrappers[0].getHabitEvent();
            ArrayList<Reminder> reminders = habitRemindersWrappers[0].getReminders();

            database.getHabitDao().update(habit);

            List<Reminder> dbReminedrs = database.getReminderDao().getAllRemindersForTask(habit.getId());
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
                    }
                }
            }

            // Insert new reminders
            if(!reminders.isEmpty()){
                for(Reminder reminder : reminders){
                    if(reminder.getId() == null){
                        reminder.setTaskId(id);
                        database.getReminderDao().insert(reminder);
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
}