package com.example.bullet_journal.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.adapters.ImagesDisplayAdapter;
import com.example.bullet_journal.model.AlbumItem;

import java.util.ArrayList;
import java.util.List;

public class ImagesPreviewFragment extends Fragment {
    GridView gridView;
    ImagesDisplayAdapter imagesAdapter;
    private List<AlbumItem> items;

    private boolean isSelectionMode;
//    private List<Uri> images= new ArrayList<Uri>(){
//        {
//            add(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"));
//            add(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"));
//            add(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"));
//            add(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"));
//        }
//    };
//    private List<String> images= new ArrayList<String>(){
//        {
//            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
//            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
//            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
//            add("http://images.math.cnrs.fr/IMG/png/section8-image.png");
//        }
//    };

    public static ImagesPreviewFragment newInstance(){
        ImagesPreviewFragment imagesPreview= new ImagesPreviewFragment();
        return imagesPreview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_images_preview, container, false);
        gridView= (GridView)view.findViewById(R.id.imagesGridview);
        imagesAdapter= new ImagesDisplayAdapter(this.getActivity(), items);
        gridView.setAdapter(imagesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "aaaaaaaaaa", Toast.LENGTH_LONG).show();
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                items.get(position).setSelected(!items.get(position).isSelected());
                imagesAdapter.notifyDataSetChanged();
                for (AlbumItem item: items
                     ) {
                    if(item.isSelected()){
                        isSelectionMode=true;
                        break;
                    }

                }
                return true;
            }
        });
        return view;


    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    private void initData(){
        items= new ArrayList<AlbumItem>(){
            {
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), true));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
            }
        };
    }
}
