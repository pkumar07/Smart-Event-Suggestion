package com.example.kunal.smarteventsuggestion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kunal on 22-01-2017.
 */

public class CustomAdapter extends BaseAdapter{
    Context context; ArrayList<Event> events;
    LayoutInflater inflater;
    public CustomAdapter(Context context, ArrayList<Event> events){
        this.context = context;
        this.events = events;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }



        mViewHolder.tvName.setText(events.get(position).getName().toString());
        mViewHolder.tvDesc.setText(events.get(position).getDescription().toString());
        mViewHolder.tvTime.setText(events.get(position).getStartTime().toString());
        mViewHolder.tvLocation.setText(events.get(position).getLocation().toString());

        return convertView;



    }

    private class MyViewHolder {
        TextView tvName, tvDesc,tvTime, tvLocation;


        public MyViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.name);
            tvDesc = (TextView) item.findViewById(R.id.description);
            tvTime = (TextView) item.findViewById(R.id.startTime);
            tvLocation = (TextView) item.findViewById(R.id.location);
        }
    }



}
