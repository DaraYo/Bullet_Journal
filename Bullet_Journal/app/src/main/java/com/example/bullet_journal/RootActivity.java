package com.example.bullet_journal;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

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

}
