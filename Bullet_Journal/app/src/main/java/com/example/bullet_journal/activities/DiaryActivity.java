package com.example.bullet_journal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

public class DiaryActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
    }
}
