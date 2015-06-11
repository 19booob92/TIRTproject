package org.pwr.tirt.plangen.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Activity for editing {@link Event}
 */
public class EditEventActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String LOG_TAG = "Edit Event Activity";

    private EditText editTextDate, editTextTitle, editTextTutor, editTextLocation, editTextTimeStart, editTextTimeEnd, editTextNewDate;
    private Spinner spinnerEvents, spinnerType;
    private Button buttonSave;

    private DBAdapter dbAdapter;
    private ArrayList<Event> eventsList = new ArrayList<>();

    private int editedTime = 0;
    private int editedDate = 0;
    private int selectedEventPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedDate = 1;
                changeDate(String.valueOf(editTextDate.getText()));
            }
        });
        editTextNewDate = (EditText) findViewById(R.id.editTextNewDate);
        editTextNewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedDate = 2;
                changeDate(String.valueOf(editTextNewDate.getText()));
            }
        });
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextTitle.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                isSavePossible();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
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
                selectedEventPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerType = (Spinner) findViewById(R.id.spinnerType);

        buttonSave = (Button) findViewById(R.id.buttonSaveEvent);

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

        Intent intent;
        if(id==R.id.action_campus_map){
            intent = new Intent(this, MapActivity.class);
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
     * Method that fulfills selected EditText with selected date
     * in DatePicker view
     *
     * @param view Used DatePicker
     * @param year Selected year
     * @param monthOfYear Selected month
     * @param dayOfMonth Selected day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(editedDate == 1) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            clear();
            loadEvents(Constants.dateFormat.format(c.getTime()));
            monthOfYear++;
            editTextDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        } else {
            monthOfYear++;
            editTextNewDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        }
        isSavePossible();
    }

    /**
     * Method that fulfills selected EditText with selected time
     * in TimePicker view
     *
     * @param view Used DatePicker
     * @param hourOfDay Selected hour
     * @param minute Selected minutes
     */
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
        isSavePossible();
    }

    /**
     * Method that initiates database
     */
    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    /**
     * Method that creates DatePicker view with date
     *
     * @param date Date to set in DatePicker
     */
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

    /**
     * Method that creates TimePicker view with time
     *
     * @param time Time to set in TimePicker
     */
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

    /**
     * Method that loades {@link Event}s for date
     *
     * @param date Date to search for
     */
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

    /**
     * Method that loades and shows data of single {@link Event}
     *
     * @param event Event to load
     */
    private void loadSingleEvent(Event event) {
        editTextTitle.setText(event.title);
        editTextTutor.setText(event.tutor);
        editTextLocation.setText(event.location);
        editTextTimeStart.setText(event.timeStart);
        editTextTimeEnd.setText(event.timeEnd);

        spinnerType.setSelection(getSpinnerIndex(spinnerType, event.type));

        isSavePossible();
    }

    /**
     * Method that returns index of selected Spinner item
     *
     * @param spinner Selected Spinner
     * @param value Selected Spinner item
     * @return Number od selected item
     */
    private int getSpinnerIndex(Spinner spinner, String value){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++)
            if (spinner.getItemAtPosition(i).equals(value)) index = i;
        return index;
    }

    /**
     * Method that cleares view
     */
    private void clear() {
        eventsList.clear();
        editTextTitle.setText("");
        editTextTutor.setText("");
        editTextLocation.setText("");
        editTextTimeStart.setText("");
        editTextTimeEnd.setText("");
        editTextNewDate.setText(null);

        ArrayList<String> eventsTitlesList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventsTitlesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvents.setAdapter(adapter);

        spinnerType.setSelection(getSpinnerIndex(spinnerType, Constants.LECTURE));
    }

    /**
     * Method that testes if there are all necessary data
     * and enables Save button if yes
     */
    private void isSavePossible(){
        buttonSave.setEnabled(true);
        if(editTextDate.getText().length() <= 0)
            buttonSave.setEnabled(false);
        if(eventsList.isEmpty())
            buttonSave.setEnabled(false);
        if(editTextTitle.getText().toString().trim().length() <= 0)
            buttonSave.setEnabled(false);
        if(editTextTimeStart.getText().length() <= 0)
            buttonSave.setEnabled(false);
        if(editTextTimeEnd.getText().length() <= 0)
            buttonSave.setEnabled(false);

        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        try {
            timeStart.setTime(Constants.timeFormat.parse(editTextTimeStart.getText().toString()));
            timeEnd.setTime(Constants.timeFormat.parse(editTextTimeEnd.getText().toString()));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
        }

        if(timeEnd.before(timeStart)){
            buttonSave.setEnabled(false);
            Toast.makeText(getApplication(), R.string.end_before_start_warning, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that saves {@link Event} modifications in database after button is clicked
     *
     * @param view Clicked button
     */
    public void onClickSaveEvent(View view) {
        Event updatedEvent = new Event();
        updatedEvent.title = editTextTitle.getText().toString();
        updatedEvent.tutor = editTextTutor.getText().toString();
        updatedEvent.location = editTextLocation.getText().toString();
        updatedEvent.timeStart = editTextTimeStart.getText().toString();
        updatedEvent.timeEnd = editTextTimeEnd.getText().toString();
        updatedEvent.type = spinnerType.getSelectedItem().toString();
        Calendar c = Calendar.getInstance();
        if(editTextNewDate.getText().length() > 0) {
            try {
                c.setTime(Constants.dateFormat.parse(editTextNewDate.getText().toString()));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
            updatedEvent.date = Constants.dateFormat.format(c.getTime());
        } else {
            try {
                c.setTime(Constants.dateFormat.parse(editTextDate.getText().toString()));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
            updatedEvent.date = Constants.dateFormat.format(c.getTime());
        }
        boolean result;
        if(selectedEventPosition != -1) {
            result = dbAdapter.updateData(eventsList.get(selectedEventPosition), updatedEvent);
            if (result)
                Toast.makeText(getApplication(), R.string.edit_single_event_success, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplication(), R.string.failed_message, Toast.LENGTH_SHORT).show();
        }
    }
}
