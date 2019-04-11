package com.example.bullet_journal.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

public class MoodTrackerActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
