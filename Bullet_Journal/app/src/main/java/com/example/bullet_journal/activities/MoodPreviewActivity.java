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
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_mood_preview);

        Bundle bundle = getIntent().getExtras();
        dateStr = bundle.getString("date");

        moodsAdapter = new MoodDisplayAdapter(this, buildMoods());
        listView = (ListView) findViewById(R.id.moods_preview_list);
        listView.setAdapter(moodsAdapter);
    }

    private List<Mood> buildMoods(){
        List<Mood> retVal = new ArrayList<>();

        Mood mood1 = new Mood(dateStr+" 08:22", 2, "Woke up with headache!");
        Mood mood2 = new Mood(dateStr+" 08:47", 4, "Coffee helps, yeah :D");
        Mood mood3 = new Mood(dateStr+" 14:50", 4, "Great day at school for change");
        Mood mood4 = new Mood(dateStr+" 20:10", 5, "Relaxing bath time :D");
        Mood mood5 = new Mood(dateStr+" 22:56", 1, "Can't belive it! GOT A TOOTHACHE!!!");

        retVal.add(mood1);
        retVal.add(mood2);
        retVal.add(mood3);
        retVal.add(mood4);
        retVal.add(mood5);

        return retVal;
    }
}
