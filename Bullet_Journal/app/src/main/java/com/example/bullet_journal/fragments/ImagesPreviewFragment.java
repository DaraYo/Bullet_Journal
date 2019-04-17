package com.example.bullet_journal.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.bullet_journal.R;
import com.example.bullet_journal.adapters.ImagesDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagesPreviewFragment extends Fragment {
    GridView gridView;
    private List<String> images= new ArrayList<String>(){
        {
            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
        }
    };

    public static ImagesPreviewFragment newInstance(){
        ImagesPreviewFragment imagesPreview= new ImagesPreviewFragment();
        return imagesPreview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_images_preview, container, false);
        gridView= (GridView)view.findViewById(R.id.imagesGridview);
        ImagesDisplayAdapter imagesAdapter= new ImagesDisplayAdapter(this.getActivity(), images);
        gridView.setAdapter(imagesAdapter);
        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
}
