package com.example.bullet_journal.activities;

import android.os.Bundle;
import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;


public class AlbumActivity extends RootActivity {
    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Name of album");
    }
}
