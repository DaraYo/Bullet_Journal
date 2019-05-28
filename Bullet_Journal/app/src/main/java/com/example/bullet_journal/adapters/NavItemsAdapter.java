package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.example.bullet_journal.R;
import com.example.bullet_journal.model.NavItem;

import java.util.ArrayList;
import java.util.List;

public class NavItemsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NavItem> listOfNavItems;
    private MenuInflater menuInflater;

    public NavItemsAdapter(Context context, ArrayList<NavItem> items) {

        this.context = context;
        this.listOfNavItems= items;
    }

    @Override
    public int getCount() {
        return listOfNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
//        View view= menuInflater.inflate(R.layout.adapter, parent, false);
        return null;
    }
}
