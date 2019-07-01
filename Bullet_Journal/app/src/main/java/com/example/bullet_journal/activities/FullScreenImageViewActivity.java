package com.example.bullet_journal.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.FullScreenImageAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetDiaryImagesAsyncTask;
import com.example.bullet_journal.helpClasses.AlbumItem;
import com.example.bullet_journal.model.DiaryImage;

import java.util.ArrayList;
import java.util.List;

public class FullScreenImageViewActivity extends RootActivity {
    ViewPager viewPager;
    private ArrayList<AlbumItem> images;
    private FullScreenImageAdapter fullScreenImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);

        long dayId= getIntent().getLongExtra("dayId", 0);

        int position= getIntent().getIntExtra("selected", 0);
        viewPager= (ViewPager) findViewById(R.id.pager);
        initData(dayId, position);

    }

    private void initData(long dayId, final int position){
        AsyncTask<Long, Void, List<DiaryImage>> getImagesTask = new GetDiaryImagesAsyncTask(FullScreenImageViewActivity.this, new AsyncResponse<List<DiaryImage>>(){

            @Override
            public void taskFinished(List<DiaryImage> retVal) {
                images = new ArrayList<AlbumItem>();
                if(retVal!=null){
                    for (DiaryImage item: retVal
                    ) {
                        images.add(new AlbumItem(Uri.parse(item.getPath()), false, item.getId()));
                    }
                    fullScreenImageAdapter= new FullScreenImageAdapter(getBaseContext(), images);
                    viewPager.setAdapter(fullScreenImageAdapter);
                    viewPager.setCurrentItem(position);
                }
            }
        }).execute(dayId);
    }
}
