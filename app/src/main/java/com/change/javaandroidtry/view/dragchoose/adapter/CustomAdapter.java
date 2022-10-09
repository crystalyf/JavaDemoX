package com.change.javaandroidtry.view.dragchoose.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.change.javaandroidtry.R;

import java.util.ArrayList;
import java.util.Collections;


public class CustomAdapter extends DragAdapter {


    ArrayList<String> list;

    public CustomAdapter(ArrayList<String> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rea_rec, null);
            viewHolder.tv_mid = convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_mid.setText(list.get(position));
        return convertView;
    }

    @Override
    public void onDataModelMove(int from, int to) {
        Collections.swap(list, from, to);
    }

    @Override
    public String getSwapData(int position) {
        return list.get(position);
    }

    @Override
    public void removeData(int position) {
        list.remove(position);
    }

    @Override
    public void addNewData(Object data) {
        if (data instanceof String) {
            list.add((String) data);
        }
    }

    static class ViewHolder {
        public TextView tv_mid;
    }

}
