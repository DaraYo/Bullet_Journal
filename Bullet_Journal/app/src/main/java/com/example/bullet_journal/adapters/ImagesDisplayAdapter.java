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

import java.io.File;
import java.util.List;

public class ImagesDisplayAdapter extends BaseAdapter {
    Context context;

    private List<AlbumItem> listOfImages;
    private LayoutInflater layoutInflater;

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
        ImageViewHolder holder;
        if (convertView == null) { // if convertView is null
            convertView = layoutInflater.inflate(R.layout.album_item,
                    parent, false);
            holder = new ImageViewHolder();
            // initialize views
            convertView.setTag(holder);  // set tag on view
            holder.ImgView = (ImageView) convertView.findViewById(R.id.album_image);
        } else {
            holder = (ImageViewHolder) convertView.getTag();
            // if not null get tag
            // no need to initialize
        }

        Uri imageSource= this.listOfImages.get(position).getImageUri();
        if(holder.ImgView.getTag()!=imageSource.toString()){
            if(imageSource.toString().contains("http")){
                Picasso.get()
                        .load(imageSource)
                        .fit()
                        .centerCrop()
                        .into(holder.ImgView);
            }
            else{

                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(new File(imageSource.toString())).resize(100, 120).into(holder.ImgView);
            }
            holder.ImgView.setTag(imageSource.toString());
        }
        if(this.listOfImages.get(position).isSelected()){
            holder.ImgView.setAlpha(0.5f);
        }else{
            holder.ImgView.setAlpha(1f);
        }
        return convertView;
    }

    static class ImageViewHolder {
        ImageView ImgView;
    }
}
