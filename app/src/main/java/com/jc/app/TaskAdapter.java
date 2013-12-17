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

            feedRow.setTag(holder);
        } else {
            holder = (FeedItemHolder) feedRow.getTag();
        }

        Task item = data.get(position);
        holder.name.setText(item.name);

        return feedRow;
    }
}

