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

public class TimeListAdapter extends ArrayAdapter<Event> {
    private static final String LOG_TAG = "Time List Adapter";
    private static class EventHolder
    {
        private TextView textViewTime;
        private RelativeLayout layout;
    }

    private Context context;
    private int layoutResourceId;
    private Event data[] = null;

    public TimeListAdapter(Context context, int layoutResourceId, Event[] data) {
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

        holder.textViewTime = (TextView)row.findViewById(R.id.textViewTime);
        holder.layout = (RelativeLayout)row.findViewById(R.id.layoutTimeItem);
        row.setTag(holder);

        Calendar timeStart = Calendar.getInstance();
        try {
            timeStart.setTime(Constants.timeFormat.parse(event.timeStart));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
        }

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
        height += 20; //TODO: constant
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) holder.layout.getLayoutParams();
        layoutParams.height = height;
        holder.textViewTime.setText(event.timeStart);

        return row;
    }
}
