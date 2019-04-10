package com.example.bullet_journal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

        if (id == R.id.action_settings) {
            Intent intent= new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
