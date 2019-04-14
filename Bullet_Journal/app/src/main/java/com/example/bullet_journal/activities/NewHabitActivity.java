package com.example.bullet_journal.activities;

import android.os.Bundle;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

public class NewHabitActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        getSupportActionBar().setTitle("Habit Tracker");

    }
}
