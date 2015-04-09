package org.pwr.tirt.plangen.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;

import java.util.ArrayList;

public class DetailsActivity extends ActionBarActivity {
    private DBAdapter dbAdapter;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getData();

        TextView title = (TextView) findViewById(R.id.textViewDetailsTitleValue);
        title.setText(event.title);
        TextView time = (TextView) findViewById(R.id.textViewDetailsTimeValue);
        time.setText(event.timeStart + " - " + event.timeEnd);
        TextView location = (TextView) findViewById(R.id.textViewDetailsLocationValue);
        location.setText(event.location);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    private void getData() {
        Intent intent = getIntent();
        int eventID = intent.getIntExtra("eventID", -1);
        if(eventID >= 0) {
            dbAdapter = new DBAdapter(getApplicationContext());
            dbAdapter.openConnection();
            event = dbAdapter.getData(eventID);
        } else
            event = new Event();
    }
}
