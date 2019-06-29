package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Mood;

import java.util.List;

public class CalculateNewAverageMoodAsyncTask extends AsyncTask<Mood, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public CalculateNewAverageMoodAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Mood... moods) {

        try {
            double sum = 0;
            int num = 0;

            List<Mood> dayMoods = database.getMoodDao().getAllMoodsForDay(moods[0].getDayId());

            for (Mood tempMood : dayMoods) {
                sum = sum + tempMood.getRating();
                num++;
            }

            Day day = database.getDayDao().get(moods[0].getDayId());
            day.setAvgMood((num <= 0) ? 0 : sum/num);

            day.setSynced(false);
            database.getDayDao().update(day);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }
}
