package com.example.bullet_journal.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.bullet_journal.helpClasses.AlbumItem;
import com.example.bullet_journal.predefinedClasses.TouchImageView;

import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {
    private Activity _activity;
    private Context context;
    private ArrayList<AlbumItem> _images;
    private LayoutInflater inflater;

//    public FullScreenImageAdapter(Activity activity, ArrayList<AlbumItem> images){
//        this._activity = activity;
//        this._images = images;
//    }

        public FullScreenImageAdapter(Context context, ArrayList<AlbumItem> images){
        this.context = context;
        this._images = images;
    }


    @Override
    public int getCount() {
        return _images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view== ((RelativeLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TouchImageView imageDisplay;
        Button buttonClose;

//        inflater= (LayoutInflater) _activity
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewLayout= inflater.inflate(R.layout.layout_fullscreen_image, container, false);
//        imageDisplay= (ImageView) viewLayout.findViewById(R.id.imgDisplay);
//        buttonClose= (Button) viewLayout.findViewById(R.id.btnClose);
        imageDisplay= new TouchImageView(context);
        imageDisplay.setImageURI(_images.get(position).getImageUri());
//        BitmapFactory.Options options= new BitmapFactory.Options();
//        options.inPreferredConfig= Bitmap.Config.ARGB_8888;
//        Bitmap bitmap= BitmapFactory.decodeFile(_imagePaths.get(position), options);
//        imageDisplay.setImageBitmap(bitmap);
//
//        buttonClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _activity.finish();
//            }
//        });

//        ((ViewPager) container).addView(viewLayout);
        return imageDisplay;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((TouchImageView)object);
    }
}
