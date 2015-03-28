package org.pwr.tirt.plangen.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.logic.EventListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LayoutActivity extends ActionBarActivity {
    private DBAdapter dbAdapter;
    private Event[] eventsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        initDatabase();
        getData();

        EventListAdapter adapter = new EventListAdapter(this, R.layout.listview_event_item, eventsArray);
        ListView listView = (ListView) findViewById(R.id.listViewEvents);
        View header = getLayoutInflater().inflate(R.layout.listview_event_header, null);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "teskt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
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
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2015);
        date.set(Calendar.MONTH, 2);
        date.set(Calendar.DAY_OF_MONTH, 27);
        ArrayList<Event> eventsList = dbAdapter.getDailyEvents(date);
        eventsArray = new Event[eventsList.size()];
        eventsArray = eventsList.toArray(eventsArray);
    }
}
