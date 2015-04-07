package org.pwr.tirt.plangen.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.Calendar;

public class EventListAdapter extends ArrayAdapter<Event> {
    private static final String LOG_TAG = "Event List Adapter";
    private static class EventHolder
    {
        TextView textViewTime;
        TextView textViewTitle;
        TextView textViewLocation;
        RelativeLayout layout;
    }

    private Context context;
    private int layoutResourceId;
    private Event data[] = null;

    public EventListAdapter(Context context, int layoutResourceId, Event[] data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        EventHolder holder= new EventHolder();
        Event event = data[position];

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder.textViewTitle = (TextView)row.findViewById(R.id.textViewTitle);
        holder.textViewTime = (TextView)row.findViewById(R.id.textViewTime);
        holder.textViewLocation = (TextView)row.findViewById(R.id.textViewLocation);
        holder.layout = (RelativeLayout)row.findViewById(R.id.layoutEventsItem);
        row.setTag(holder);

        /*
        Parse time
         */
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        try {
            timeStart.setTime(Constants.timeFormat.parse(event.timeStart));
            timeEnd.setTime(Constants.timeFormat.parse(event.timeEnd));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
        }

        /*
        Set height
         */
        long durationLong = (timeEnd.getTimeInMillis() - timeStart.getTimeInMillis()) / (long) (60000);
        int durationInt = 0;
        if (durationLong <= 1440)
            durationInt = (int) durationLong;
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, durationInt, context.getResources().getDisplayMetrics());
        height += 20; //TODO: constant
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) holder.layout.getLayoutParams();
        layoutParams.height = height;

        if(!event.title.equals(Constants.FREE_TIME_TAG)) {
            /*
            Set text values
             */
            holder.textViewTime.setText(event.timeStart + " - " + event.timeEnd);
            holder.textViewTitle.setText(event.title);
            holder.textViewLocation.setText(event.location);

            /*
            Set appearance depending on height
             */
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) holder.textViewTitle.getLayoutParams();
            RelativeLayout.LayoutParams timeParams = (RelativeLayout.LayoutParams) holder.textViewTime.getLayoutParams();

            if (durationInt < 65 && durationInt >= 50) {
                holder.textViewLocation.setVisibility(View.GONE);
                titleParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                timeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else if (durationInt < 50 && durationInt >= 30) {
                holder.textViewTime.setVisibility(View.GONE);
                holder.textViewLocation.setVisibility(View.GONE);
            } else if (durationInt < 30) {
                holder.textViewTitle.setVisibility(View.GONE);
                holder.textViewTime.setVisibility(View.GONE);
                holder.textViewLocation.setVisibility(View.GONE);
            }

            /*
            Set background color
             */
            int color = getColor(event.type);
            if (color != -1)
                holder.layout.setBackgroundColor(color);
        } else {
            /*
            Delete TextViews
             */
            holder.textViewTitle.setVisibility(View.GONE);
            holder.textViewTime.setVisibility(View.GONE);
            holder.textViewLocation.setVisibility(View.GONE);
        }
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
