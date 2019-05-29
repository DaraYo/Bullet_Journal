package com.example.bullet_journal.activities;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.MoodDisplayAdapter;
import com.example.bullet_journal.model.Mood;

import java.util.ArrayList;
import java.util.List;

public class MoodPreviewActivity extends RootActivity {

    private ListView listView;
    private ListAdapter moodsAdapter;
    private long dateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_mood_preview);

        Bundle bundle = getIntent().getExtras();
        dateMillis = bundle.getLong("date");

        moodsAdapter = new MoodDisplayAdapter(this, buildMoods());
        listView = (ListView) findViewById(R.id.moods_preview_list);
        listView.setAdapter(moodsAdapter);
    }

    private List<Mood> buildMoods(){
        List<Mood> retVal = new ArrayList<>();

        Mood mood1 = new Mood("1", dateMillis, 2, "Woke up with headache!");
        Mood mood2 = new Mood("2", dateMillis, 4, "Coffee helps, yeah :D");
        Mood mood3 = new Mood("3", dateMillis, 4, "Great day at school for change");
        Mood mood4 = new Mood("4", dateMillis, 5, "Relaxing bath time :D");
        Mood mood5 = new Mood("5", dateMillis, 1, "Can't belive it! GOT A TOOTHACHE!!!");

        retVal.add(mood1);
        retVal.add(mood2);
        retVal.add(mood3);
        retVal.add(mood4);
        retVal.add(mood5);

        return retVal;
    }
}
