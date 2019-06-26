package com.example.bullet_journal.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.FullScreenImageAdapter;
import com.example.bullet_journal.helpClasses.MockupData;
import com.example.bullet_journal.model.AlbumItem;

import java.util.ArrayList;
import java.util.Date;

public class FullScreenImageViewActivity extends RootActivity {
    ViewPager viewPager;
    private ArrayList<AlbumItem> images;
    private MenuItem deleteButton;
    private FullScreenImageAdapter fullScreenImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long milis= getIntent().getLongExtra("date", 0);
        Date date = new Date(milis);
        images= (ArrayList<AlbumItem>) MockupData.getDiary(date).getAlbumItems();

        int position= getIntent().getIntExtra("selected", 0);
        viewPager= (ViewPager) findViewById(R.id.pager);
        fullScreenImageAdapter= new FullScreenImageAdapter(this, milis);
        viewPager.setAdapter(fullScreenImageAdapter);
        viewPager.setCurrentItem(position);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        deleteButton= menu.findItem(R.id.delete_pics);
        deleteButton.setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete_pics: {
                Toast.makeText(getApplicationContext(), "brisi brisi suze s' lica", Toast.LENGTH_LONG).show();;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
