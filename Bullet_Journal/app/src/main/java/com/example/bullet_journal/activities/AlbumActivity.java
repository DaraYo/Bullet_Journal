package com.example.bullet_journal.activities;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.fragments.ImagesPreviewFragment;

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
