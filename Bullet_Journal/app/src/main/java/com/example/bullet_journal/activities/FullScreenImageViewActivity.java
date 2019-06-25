package com.example.bullet_journal.activities;

import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.FullScreenImageAdapter;
import com.example.bullet_journal.helpClasses.AlbumItem;

import java.util.ArrayList;

public class FullScreenImageViewActivity extends RootActivity {
    ViewPager viewPager;
    private ArrayList<AlbumItem> images;
    private MenuItem deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();

//        ArrayList<AlbumItem> images = (ArrayList<AlbumItem>)getIntent().getSerializableExtra("list");
//        int position= getIntent().getIntExtra("selected", 0);
        viewPager= (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this, images));
        viewPager.setCurrentItem(0);

    }

    private void initData(){
        images= new ArrayList<AlbumItem>(){
            {
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
            }
        };
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
                Toast.makeText(getApplicationContext(), "brisi brisi suze s' lica", Toast.LENGTH_LONG).show();
//                    items.remove(selected);
//                imagesAdapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
