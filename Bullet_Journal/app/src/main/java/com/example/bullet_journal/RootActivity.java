package com.example.bullet_journal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bullet_journal.activities.HabitActivity;
import com.example.bullet_journal.activities.SettingsActivity;

/*
Dodato kako bi se isti gornji meni nalazio u svim aktivnostima.
Posto RootActivity vec nasledjuje AppCompatActivity i redefinisana je metoda - onCreateOptionsMenu(Menu menu)
ostale aktivnosti trebaju naslediti RootActivity
*/

public class RootActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings : {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_habits:
            {
                Intent intent = new Intent(this, HabitActivity.class);
                startActivity(intent);
                return true;
            }
            case android.R.id.home : {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
