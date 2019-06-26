package com.example.bullet_journal.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.MockupData;
import com.example.bullet_journal.model.AlbumItem;
import com.example.bullet_journal.predefinedClasses.TouchImageView;

import java.util.ArrayList;
import java.util.Date;

public class FullScreenImageAdapter extends PagerAdapter {
    private ArrayList<AlbumItem> _images;
    private LayoutInflater inflater;
    TouchImageView imageDisplay;

        public FullScreenImageAdapter(Context context, long milis){
        Date date = new Date(milis);
        this._images= (ArrayList<AlbumItem>) MockupData.getDiary(date).getAlbumItems();
        inflater = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return _images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view== o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewGroup layout= (ViewGroup)inflater.inflate(R.layout.layout_fullscreen_image, container, false);
        imageDisplay= (TouchImageView) layout.findViewById(R.id.fullscreen_image);
        imageDisplay.setImageURI(_images.get(position).getImageUri());
        container.addView(layout);
//
//        BitmapFactory.Options options= new BitmapFactory.Options();
//        options.inPreferredConfig= Bitmap.Config.ARGB_8888;
//        Bitmap bitmap= BitmapFactory.decodeFile(_imagePaths.get(position), options);
//        imageDisplay.setImageBitmap(bitmap);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
