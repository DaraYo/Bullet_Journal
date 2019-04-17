package com.example.bullet_journal.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.fragments.ImagesPreviewFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesDisplayAdapter extends BaseAdapter {
    Context context;
    private List<String> listOfImages;
    private LayoutInflater layoutInflater;
//    private ItemClickListener itemClickListener;

    public ImagesDisplayAdapter(Context context, List<String> images) {
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
        View view= layoutInflater.inflate(R.layout.album_item, parent, false);
        ImageView image= (ImageView) view.findViewById(R.id.album_image);
        String imageSource= this.listOfImages.get(position);
        Picasso.get()
                .load(imageSource)
                .into(image);
        return view;
    }
}
