package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;

import java.util.ArrayList;
import java.util.List;

public class DeleteHabitEventAsyncTask extends AsyncTask<HabitRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public DeleteHabitEventAsyncTask(AsyncResponse delegate){
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

            // Delete reminders
            if(!dbReminedrs.isEmpty()){
                for(Reminder tempDbReminder : dbReminedrs){
                    database.getReminderDao().delete(tempDbReminder);
                }
            }

            database.getHabitDao().delete(habit);

            //delete habit/day
            List<HabitDay> hb= (List<HabitDay>) database.getHabitDayDao().getAllHabitsDayByHabit(habit.getId());

            for (HabitDay d:hb ) {
                database.getHabitDayDao().delete(d);
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