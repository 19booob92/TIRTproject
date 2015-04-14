package org.pwr.tirt.plangen.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.AddEventDateChangeListAdapter;
import org.pwr.tirt.plangen.logic.IAddEventDateChangeListAdapterListener;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.Calendar;

public class AddEventCorrectDatesActivity extends ActionBarActivity implements IAddEventDateChangeListAdapterListener, DatePickerDialog.OnDateSetListener {
    private static final String LOG_TAG = "AECD Activity";

    private String[][] dates = new String[][]{{"",""}};
    private ListView listView;
    private int positionToSet;
    private int rowPositionToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_correct_dates);

        final AddEventDateChangeListAdapter adapter = new AddEventDateChangeListAdapter(this, R.layout.listview_date_change_item, dates, this);
        listView = (ListView) findViewById(R.id.listViewDateChange);
        View header = getLayoutInflater().inflate(R.layout.listview_date_change_header, null);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event_correct_dates, menu);
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

    public void onClickAddNewDateChange(View view) {
        String[][] temp = new String[dates.length+1][2];
        for(int i = 0; i < dates.length; i++) {
            temp[i][0] = dates[i][0];
            temp[i][1] = dates[i][1];
        }
        temp[dates.length][0] = "";
        temp[dates.length][1] = "";
        dates = temp;
        listView.setAdapter(new AddEventDateChangeListAdapter(this, R.layout.listview_date_change_item, dates, this));
    }

    @Override
    public void itemDeleted(int position) {
        String[][] temp = new String[dates.length-1][2];
        for(int i = 0, j = 0; i < dates.length; i++, j++)
            if(i != position) {
                temp[j][0] = dates[i][0];
                temp[j][1] = dates[i][1];
            } else
                j--;
        dates = temp;
        listView.setAdapter(new AddEventDateChangeListAdapter(this, R.layout.listview_date_change_item, dates, this));
    }

    @Override
    public void changeDate(int position, int rowPosition) {
        positionToSet = position;
        rowPositionToSet = rowPosition;
        Calendar c = Calendar.getInstance();
        if(dates[position][rowPosition] != null) {
            try {
                c.setTime(Constants.dateFormat.parse(dates[position][rowPosition]));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
        }
        new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dates[positionToSet][rowPositionToSet] = year + "-" + monthOfYear + "-" + dayOfMonth;
        listView.setAdapter(new AddEventDateChangeListAdapter(this, R.layout.listview_date_change_item, dates, this));
    }
}
