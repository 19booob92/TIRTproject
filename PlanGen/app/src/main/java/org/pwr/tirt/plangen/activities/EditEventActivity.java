package org.pwr.tirt.plangen.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class EditEventActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String LOG_TAG = "Edit Event Activity";

    private EditText editTextDate, editTextTitle, editTextTutor, editTextLocation, editTextTimeStart, editTextTimeEnd;
    private Spinner spinnerEvents, spinnerType, spinnerDay, spinnerWeekType;

    private DBAdapter dbAdapter;
    private ArrayList<Event> eventsList = new ArrayList<>();

    private int editedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(String.valueOf(editTextDate.getText()));
            }
        });
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextTutor = (EditText) findViewById(R.id.editTextTutor);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextTimeStart = (EditText) findViewById(R.id.editTextTimeStart);
        editTextTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedTime = 1;
                changeTime(String.valueOf(editTextTimeStart.getText()));
            }
        });
        editTextTimeEnd = (EditText) findViewById(R.id.editTextTimeEnd);
        editTextTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedTime = 2;
                changeTime(String.valueOf(editTextTimeEnd.getText()));
            }
        });

        spinnerEvents = (Spinner) findViewById(R.id.spinnerEvents);
        spinnerEvents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadSingleEvent(eventsList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        spinnerWeekType = (Spinner) findViewById(R.id.spinnerWeekType);

        initDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        clear();
        loadEvents(Constants.dateFormat.format(c.getTime()));
        monthOfYear++;
        editTextDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch(editedTime){
            case 1:
                editTextTimeStart.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                break;
            case 2:
                editTextTimeEnd.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                break;
        }
    }

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    private void changeDate(String date){
        Calendar c = Calendar.getInstance();
        if(date != null) {
            try {
                c.setTime(Constants.dateFormat.parse(date));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
        }
        new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void changeTime(String time) {
        Calendar c = Calendar.getInstance();
        if(time != null) {
            try {
                c.setTime(Constants.timeFormat.parse(time));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
        }
        new TimePickerDialog(this, this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void loadEvents(String date){
        ArrayList<Event> tempEventsList = dbAdapter.getDailyEvents(date);
        for(Event event : tempEventsList)
            if(!event.title.equals(Constants.FREE_TIME_TAG))
                eventsList.add(event);
        ArrayList<String> eventsTitlesList = new ArrayList<>();
        for(Event event : eventsList)
            eventsTitlesList.add(event.title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventsTitlesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvents.setAdapter(adapter);
    }

    private void loadSingleEvent(Event event) {
        editTextTitle.setText(event.title);
        editTextTutor.setText(event.tutor);
        editTextLocation.setText(event.location);
        editTextTimeStart.setText(event.timeStart);
        editTextTimeEnd.setText(event.timeEnd);

        spinnerType.setSelection(getSpinnerIndex(spinnerType, event.type));
        spinnerDay.setSelection(getSpinnerIndex(spinnerDay, getWeekdayName(event.date)));
        spinnerWeekType.setSelection(getSpinnerIndex(spinnerWeekType, "Co tydzień"));
    }

    private int getSpinnerIndex(Spinner spinner, String value){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++)
            if (spinner.getItemAtPosition(i).equals(value)) index = i;
        return index;
    }

    private String getWeekdayName(String date){
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Constants.dateFormat.parse(date));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
        }
        switch (c.get(Calendar.DAY_OF_WEEK)){
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
                return null;
        }
    }

    private void clear() {
        eventsList.clear();
        editTextTitle.setText("");
        editTextTutor.setText("");
        editTextLocation.setText("");
        editTextTimeStart.setText("");
        editTextTimeEnd.setText("");

        ArrayList<String> eventsTitlesList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventsTitlesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvents.setAdapter(adapter);

        spinnerType.setSelection(getSpinnerIndex(spinnerType, Constants.LECTURE));
        spinnerDay.setSelection(getSpinnerIndex(spinnerDay, getString(R.string.monday_long)));
        spinnerWeekType.setSelection(getSpinnerIndex(spinnerWeekType, getString(R.string.every_week)));
    }

    public void onClickSaveEvent(View view) {
    }
}
