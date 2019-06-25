package com.example.bullet_journal.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.AlbumItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagesDisplayAdapter extends BaseAdapter {
    Context context;
//    private List<Uri> listOfImages;
    private List<AlbumItem> listOfImages;
    private LayoutInflater layoutInflater;
    ImageView image;

    private int pos;

    public ImagesDisplayAdapter(Context context, List<AlbumItem> images){// List<Uri> images) {
        this.listOfImages= images;
        this.context = context;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listOfImages.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        pos = position;
        List<Integer> selectedPositions = new ArrayList<>();
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.album_item, parent, false);
        image= (ImageView) view.findViewById(R.id.album_image);


//        image.setImageURI(listOfImages.get(position));

        Uri imageSource= this.listOfImages.get(position).getImageUri();
        if(imageSource.toString().contains("http")){
            Picasso.get()
                    .load(imageSource)
                    .into(image);
        }
        else{
            image.setImageURI(imageSource);
        }
        if(this.listOfImages.get(position).isSelected()){
            image.setAlpha(0.5f);
        }else{
            image.setAlpha(1f);
        }
//        image.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
////                image.setSelected(true);
//                Toast.makeText(context, "Ovo je toast za klik na jednu sliku", Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });
        return view;
    }
}
