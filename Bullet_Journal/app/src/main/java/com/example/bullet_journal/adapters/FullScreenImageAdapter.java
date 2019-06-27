package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.AlbumItem;
import com.example.bullet_journal.predefinedClasses.TouchImageView;

import java.util.List;

public class FullScreenImageAdapter extends PagerAdapter {
    private List<AlbumItem> _images;
    private LayoutInflater inflater;
    TouchImageView imageDisplay;

        public FullScreenImageAdapter(Context context, List<AlbumItem> items){
        this._images= items;
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
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
