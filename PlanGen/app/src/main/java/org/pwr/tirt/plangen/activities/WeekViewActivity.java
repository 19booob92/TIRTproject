package org.pwr.tirt.plangen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Activity shows weekly events
 */
public class WeekViewActivity extends ActionBarActivity {
    private static final String LOG_TAG = "Week View Activity";

    private DBAdapter dbAdapter;
    private LinearLayout[] linearLayouts;
    private Calendar mondayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        mondayDate = Calendar.getInstance();
        mondayDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

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
        Intent intent;
        if(id==R.id.action_campus_map){
            intent = new Intent(this, Mapa.class);
            startActivity(intent);
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

    /**
     * Method that initiates database
     */
    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    /**
     * Method that gets {@link Event}s from database for week
     * and creates layouts for displaying them
     */
    private void getData() {
        for(int i = 0; i < 7; i++) {
            if(linearLayouts[i].getChildCount() > 0)
                linearLayouts[i].removeAllViews();

            String date = Constants.dateFormat.format(mondayDate.getTime());
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
            mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * Method that returns {@link Event} background color for its type
     *
     * @param type Event type
     * @return Color number
     */
    private int getColor(String type) {
        switch (type) {
            case Constants.LECTURE:
                return getResources().getColor(R.color.lecture);
            case Constants.EXERCISES:
                return getResources().getColor(R.color.exercises);
            case Constants.LABORATORY:
                return getResources().getColor(R.color.laboratory);
            case Constants.PROJECT:
                return getResources().getColor(R.color.project);
            case Constants.SEMINAR:
                return getResources().getColor(R.color.seminar);
            case Constants.OTHER:
                return getResources().getColor(R.color.other);
            default:
                return getResources().getColor(R.color.white);
        }
    }
}
