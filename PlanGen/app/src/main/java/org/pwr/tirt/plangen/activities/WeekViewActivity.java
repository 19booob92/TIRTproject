package org.pwr.tirt.plangen.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.logic.EventListAdapter;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class WeekViewActivity extends ActionBarActivity {
    private static final String LOG_TAG = "Week View Activity";

    private DBAdapter dbAdapter;
    private LinearLayout[] linearLayouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        initDatabase();

        linearLayouts = new LinearLayout[7];
        linearLayouts[0] = (LinearLayout) findViewById(R.id.linearLayoutMonday);
        linearLayouts[1] = (LinearLayout) findViewById(R.id.linearLayoutTuesday);
        linearLayouts[2] = (LinearLayout) findViewById(R.id.linearLayoutWednesday);
        linearLayouts[3] = (LinearLayout) findViewById(R.id.linearLayoutThursday);
        linearLayouts[4] = (LinearLayout) findViewById(R.id.linearLayoutFriday);
        linearLayouts[5] = (LinearLayout) findViewById(R.id.linearLayoutSaturday);
        linearLayouts[6] = (LinearLayout) findViewById(R.id.linearLayoutSunday);

        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_week_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(dbAdapter != null)
            dbAdapter.closeConnection();
        super.onDestroy();
    }

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    private void getData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for(int i = 0; i < 7; i++) {
            String date = Constants.dateFormat.format(calendar.getTime());
            ArrayList<Event> eventsList = dbAdapter.getDailyEvents(date);
            for (Event event : eventsList) {
                Calendar timeStart = Calendar.getInstance();
                Calendar timeEnd = Calendar.getInstance();
                try {
                    timeStart.setTime(Constants.timeFormat.parse(event.timeStart));
                    timeEnd.setTime(Constants.timeFormat.parse(event.timeEnd));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
                }
                long duration = (timeEnd.getTimeInMillis() - timeStart.getTimeInMillis()) / (long) (60000);
                float weight = (float) duration / 1440f;

                LinearLayout layout = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                params.weight = weight;
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = 0;
                layout.setLayoutParams(params);
                layout.setBackgroundColor(getColor(event.type));

                linearLayouts[i].addView(layout);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private int getColor(String type) {
        switch (type) {
            case Constants.LECTURE:
                return Color.BLUE;
            case Constants.EXECRISES:
                return Color.CYAN;
            case Constants.LABORATORY:
                return Color.RED;
            case Constants.PROJECT:
                return Color.GRAY;
            case Constants.SEMINAR:
                return Color.GREEN;
            case Constants.OTHER:
                return Color.YELLOW;
            default:
                return -1;
        }
    }
}
