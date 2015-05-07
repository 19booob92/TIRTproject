package org.pwr.tirt.plangen.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.logic.EventListAdapter;
import org.pwr.tirt.plangen.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekViewActivity extends ActionBarActivity {

    private DBAdapter dbAdapter;
    private Event[][] eventsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        eventsArray = new Event[7][];
        initDatabase();
        getData();

        EventListAdapter adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[0], true);
        ListView listViewMonday = (ListView) findViewById(R.id.listViewMonday);
        listViewMonday.setAdapter(adapter);
        listViewMonday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[1], true);
        ListView listViewTuesday = (ListView) findViewById(R.id.listViewTuesday);
        listViewTuesday.setAdapter(adapter);
        listViewTuesday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[2], true);
        ListView listViewWednesday = (ListView) findViewById(R.id.listViewWednesday);
        listViewWednesday.setAdapter(adapter);
        listViewWednesday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[3], true);
        ListView listViewThursday = (ListView) findViewById(R.id.listViewThursday);
        listViewThursday.setAdapter(adapter);
        listViewThursday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[4], true);
        ListView listViewFriday = (ListView) findViewById(R.id.listViewFriday);
        listViewFriday.setAdapter(adapter);
        listViewFriday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[5], true);
        ListView listViewSaturday = (ListView) findViewById(R.id.listViewSaturday);
        listViewSaturday.setAdapter(adapter);
        listViewSaturday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray[6], true);
        ListView listViewSunday = (ListView) findViewById(R.id.listViewSunday);
        listViewSunday.setAdapter(adapter);
        listViewSunday.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
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

        for(int i = 0; i < 7; i++){
            String date = Constants.dateFormat.format(calendar.getTime());
            ArrayList<Event> eventsList = dbAdapter.getDailyEvents(date);
            eventsArray[i] = new Event[eventsList.size()];
            eventsArray[i] = eventsList.toArray(eventsArray[i]);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
