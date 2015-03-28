package org.pwr.tirt.plangen.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.SimpleDateFormat;

public class EventListAdapter extends ArrayAdapter<Event> {
    static class EventHolder
    {
        TextView textViewTime;
        TextView textViewTitle;
        TextView textViewLocation;
        LinearLayout linearLayout;
    }

    Context context;
    int layoutResourceId;
    Event data[] = null;

    public EventListAdapter(Context context, int layoutResourceId, Event[] data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EventHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EventHolder();
            holder.textViewTitle = (TextView)row.findViewById(R.id.textViewTitle);
            holder.textViewTime = (TextView)row.findViewById(R.id.textViewTime);
            holder.textViewLocation = (TextView)row.findViewById(R.id.textViewLocation);
            holder.linearLayout = (LinearLayout)row.findViewById(R.id.linearLayoutEventsItem);

            row.setTag(holder);
        } else
            holder = (EventHolder)row.getTag();

        Event event = data[position];
        String time = "";
        time+=Constants.timeFormat.format(event.timeStart.getTime());
        time+=" - ";
        time+=Constants.timeFormat.format(event.timeEnd.getTime());
        holder.textViewTime.setText(time);
        holder.textViewTitle.setText(event.title);
        holder.textViewLocation.setText(event.location);

        //holder.linearLayout.setMinimumHeight(250);
        int color = getColor(event.type);
        if(color != -1)
            holder.linearLayout.setBackgroundColor(color);
        return row;
    }

    private int getColor(String type) {
        switch (type) {
            case Constants.W:
                return Color.BLUE;
            case Constants.C:
                return Color.CYAN;
            case Constants.L:
                return Color.RED;
            case Constants.P:
                return Color.GRAY;
            case Constants.S:
                return Color.GREEN;
            case Constants.I:
                return Color.YELLOW;
            default:
                return -1;
        }
    }
}
