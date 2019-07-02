package com.example.bullet_journal.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullet_journal.MainActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetUserAsyncTask;
import com.example.bullet_journal.helpClasses.PreferencesHelper;
import com.example.bullet_journal.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //remove from backstack
        finish();
    }
}
