package com.example.bullet_journal.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.MoodDisplayAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetMoodsForDayAsyncTask;
import com.example.bullet_journal.model.Mood;

import java.util.ArrayList;
import java.util.List;

public class MoodPreviewActivity extends RootActivity {

    private ListView listView;
    private MoodDisplayAdapter moodsAdapter;
    private long dateMillis;

    private List<Mood> moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_mood_preview);

        Bundle bundle = getIntent().getExtras();
        dateMillis = bundle.getLong("date");

        moods = new ArrayList<>();

        listView = (ListView) findViewById(R.id.moods_preview_list);
        moodsAdapter = new MoodDisplayAdapter(this, moods);
        listView.setAdapter(moodsAdapter);

        AsyncTask<Long, Void, List<Mood>> getMoodsForDayAsyncTask = new GetMoodsForDayAsyncTask(new AsyncResponse<List<Mood>>() {
            @Override
            public void taskFinished(List<Mood> retVal) {
                moods.clear();
                if(retVal != null){
                    moods.addAll(retVal);
                }
                moodsAdapter.notifyDataSetChanged();
            }
        }).execute(this.dateMillis);
    }

}
