package com.jc.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by evan on 9/25/13.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    DBHandler db;
    DoneActivity intToTime;

    private final Context context;
    private final List<Task> data;

    public TaskAdapter(Context context, List<Task> data){
        super(context, R.layout.main_task, data);
        this.context = context;
        this.data = data;
    }

    private class FeedItemHolder{
        TextView name;
        TextView complete;
        TextView length;
        TextView time;
        TextView when;
        TextView avgn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FeedItemHolder holder;
        View feedRow = convertView;

        if(feedRow == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            feedRow = inflater.inflate(R.layout.main_task, parent, false);
            holder = new FeedItemHolder();
            holder.name = (TextView) feedRow.findViewById(R.id.taskText);
            holder.avgn = (TextView) feedRow.findViewById(R.id.avgnumText);

            feedRow.setTag(holder);
        } else {
            holder = (FeedItemHolder) feedRow.getTag();
        }

        Task item = data.get(position);
        String shorter = item.name;
        if (item.name.length() > 20) {
            shorter = item.name.substring(0,20)+"...";
        }
        Integer average;
        if (item.total != 0) {
            average = item.allTime/item.total;
        } else {
            average = 0;
        }

        String av = DoneActivity.intToTime(average);

        holder.name.setText(shorter);
        holder.avgn.setText(av);

        return feedRow;
    }
}

