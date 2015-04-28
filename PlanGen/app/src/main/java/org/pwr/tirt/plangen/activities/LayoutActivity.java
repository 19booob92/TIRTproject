package org.pwr.tirt.plangen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.logic.EventListAdapter;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class LayoutActivity extends ActionBarActivity {
    private static final String LOG_TAG = "Layout Activity";

    private DBAdapter dbAdapter;
    private Event[] eventsArray;
    private String date;
    private TextView dayOfWeek;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        //ActionBar bar = getActionBar();
        //bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2015); //TODO: delete
        c.set(Calendar.MONTH, 2);
        c.set(Calendar.DAY_OF_MONTH, 27);
        date = Constants.dateFormat.format(c.getTime());

        initDatabase();
        getData();

        final Activity activity = this;

        final EventListAdapter adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray);
        listView = (ListView) findViewById(R.id.listViewEvents);
        View header = getLayoutInflater().inflate(R.layout.listview_event_header, null);
        dayOfWeek = (TextView) header.findViewById(R.id.textViewHeader);
        dayOfWeek.setText(getDayOfWeek());
        listView.addHeaderView(header);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedFromList = adapter.getItem(position - 1);
                if(!selectedFromList.title.equals(Constants.FREE_TIME_TAG)) {
                    Intent intent = new Intent(activity, DetailsActivity.class);
                    intent.putExtra("eventID", selectedFromList.id);
                    startActivity(intent);
                }
            }
        });
        scrollList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_edit_event:
                Intent intent = new Intent(this, EditEventActivity.class);
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

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    private void getData() {
        ArrayList<Event> eventsList = dbAdapter.getDailyEvents(date);
        eventsArray = new Event[eventsList.size()];
        eventsArray = eventsList.toArray(eventsArray);
    }

    private String getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        if(date != null) {
            try {
                c.setTime(Constants.dateFormat.parse(date));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return getString(R.string.monday_long);
            case Calendar.TUESDAY:
                return getString(R.string.tuesday_long);
            case Calendar.WEDNESDAY:
                return getString(R.string.wednesday_long);
            case Calendar.THURSDAY:
                return getString(R.string.thursday_long);
            case Calendar.FRIDAY:
                return getString(R.string.friday_long);
            case Calendar.SATURDAY:
                return getString(R.string.saturday_long);
            case Calendar.SUNDAY:
                return getString(R.string.sunday_long);
            default:
                return Constants.NO_DATA;
        }
    }

    private void setDate(int dayNumber) {
        Calendar calendar = Calendar.getInstance();
        int diff = dayNumber - calendar.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0))
            diff += 7;
        calendar.add(Calendar.DAY_OF_MONTH, diff);
        date = Constants.dateFormat.format(calendar.getTime());
        if (dayNumber == Calendar.FRIDAY) { //TODO: delete
            calendar.set(Calendar.YEAR, 2015);
            calendar.set(Calendar.MONTH, 2);
            calendar.set(Calendar.DAY_OF_MONTH, 27);
        }
        date = Constants.dateFormat.format(calendar.getTime());
        dayOfWeek.setText(getDayOfWeek());
        getData();
        listView.setAdapter(new EventListAdapter(this, R.layout.listview_event_item, eventsArray));
        scrollList();
    }

    private void scrollList() {
        int position = 0;
        Calendar now = Calendar.getInstance();
        for(int i = 0; i < eventsArray.length; i++) {
            if (!eventsArray[i].title.equals(Constants.FREE_TIME_TAG)) {
                Calendar eventStart = Calendar.getInstance();
                try {
                    eventStart.setTime(Constants.timeFormat.parse(eventsArray[i].timeStart));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
                }
                eventStart.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
                eventStart.set(Calendar.MONTH, now.get(Calendar.MONTH));
                eventStart.set(Calendar.YEAR, now.get(Calendar.YEAR));
                if (now.after(eventStart)) {
                    if (i < eventsArray.length)
                        position = i + 1;
                    else
                        position = i;
                }
            }
        }
        if(position == 0 && eventsArray.length > 1)
            position = 2;
        listView.setSelection(position);
    }

    public void onClickMonday(View view) {
        setDate(Calendar.MONDAY);
    }

    public void onClickTuesday(View view) {
        setDate(Calendar.TUESDAY);
    }

    public void onClickWednesday(View view) {
        setDate(Calendar.WEDNESDAY);
    }

    public void onClickThursday(View view) {
        setDate(Calendar.THURSDAY);
    }

    public void onClickFriday(View view) {
        setDate(Calendar.FRIDAY);
    }

    public void onClickSaturday(View view) {
        setDate(Calendar.SATURDAY);
    }

    public void onClickSunday(View view) {
        setDate(Calendar.SUNDAY);
    }
}
