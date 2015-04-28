package org.pwr.tirt.plangen.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class AddEventActivity extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String LOG_TAG = "Add Event Activity";

    private EditText editTextTitle, editTextTutor, editTextLocation, editTextTimeStart, editTextTimeEnd;
    private Spinner spinnerType, spinnerDay, spinnerWeekType;

    private DBAdapter dbAdapter;

    private int editedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextTutor = (EditText) findViewById(R.id.editTextTutor);
        editTextLocation = (EditText) findViewById(R.id.editTextlocation);
        editTextTimeStart = (EditText) findViewById(R.id.editTexttimeStart);
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

        spinnerType = (Spinner) findViewById(R.id.spinnertype);
        spinnerDay = (Spinner) findViewById(R.id.spinnerdni);
        spinnerWeekType = (Spinner) findViewById(R.id.spinnerjakitydzien);

        initDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
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

    public void OnClickAddEvent(View view) {
        if(!addEvents())
            Toast.makeText(getApplication(), R.string.error, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplication(), R.string.event_added, Toast.LENGTH_SHORT).show();
    }

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    private boolean addEvents() {
        ArrayList<String> dates = new ArrayList<>();

        if(spinnerWeekType.getSelectedItem().toString().equals(getString(R.string.every_week)))
            dates = getDates(-1);
        else if(spinnerWeekType.getSelectedItem().toString().equals(getString(R.string.even_week)))
            dates = getDates(1);
        else if(spinnerWeekType.getSelectedItem().toString().equals(getString(R.string.odd_week)))
            dates = getDates(0);
        else if(spinnerWeekType.getSelectedItem().toString().equals(getString(R.string.one_time))) {
            int dayNumber = getWeekdayNumber();
            if(dayNumber != -1){
                Calendar calendar = Calendar.getInstance();
                int diff = dayNumber - calendar.get(Calendar.DAY_OF_WEEK);
                if (!(diff > 0))
                    diff += 7;
                calendar.add(Calendar.DAY_OF_MONTH, diff);
                dates.add(Constants.dateFormat.format(calendar.getTime()));
            } else
                return false;
        } else
            return false;

        Event event = new Event();
        event.title = String.valueOf(editTextTitle.getText());
        event.tutor = String.valueOf(editTextTutor.getText());
        event.location = String.valueOf(editTextLocation.getText());

        Calendar time = Calendar.getInstance();
        try {
            time.setTime(Constants.timeFormat.parse(String.valueOf(editTextTimeStart.getText())));
            time.setTime(Constants.timeFormat.parse(String.valueOf(editTextTimeEnd.getText())));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            return false;
        }

        event.timeStart = String.valueOf(editTextTimeStart.getText());
        event.timeEnd = String.valueOf(editTextTimeEnd.getText());
        event.type = getType();

        boolean result = true;
        for (String date : dates)
            if (result) {
                event.date = date;
                result = dbAdapter.insertData(event);
            }

        return true;
    }

    private ArrayList<String> getDates(int weekType) {
        ArrayList<String> dates = new ArrayList<>();
        int daysToAdd = 7;
        if(weekType != -1)
            daysToAdd = 14;

        Calendar semesterStart = Calendar.getInstance();
        Calendar semesterEnd = Calendar.getInstance();
        try {
            semesterStart.setTime(Constants.dateFormat.parse(Constants.SEMESTER_START_DATE));
            semesterEnd.setTime(Constants.dateFormat.parse(Constants.SEMESTER_END_DATE));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parsing date failed. " + e.getMessage());
        }
        semesterStart.add(Calendar.DAY_OF_MONTH, -1);
        int dayNumber = getWeekdayNumber();
        if(dayNumber != -1) {
            int diff = dayNumber - semesterStart.get(Calendar.DAY_OF_WEEK);
            if (!(diff > 0))
                diff += 7;
            semesterStart.add(Calendar.DAY_OF_MONTH, diff);
            if(weekType != -1 && semesterStart.get(Calendar.WEEK_OF_YEAR) % 2 == weekType)
                semesterStart.add(Calendar.DAY_OF_MONTH, 7);
            dates.add(Constants.dateFormat.format(semesterStart.getTime()));
        } else
            return null;
        semesterStart.add(Calendar.DAY_OF_MONTH, daysToAdd);
        while(semesterStart.before(semesterEnd)) {
            dates.add(Constants.dateFormat.format(semesterStart.getTime()));
            semesterStart.add(Calendar.DAY_OF_MONTH, daysToAdd);
        }
        return dates;
    }

    private String getType(){
        String type = spinnerType.getSelectedItem().toString();
        if(type.equals(Constants.LECTURE)
                || type.equals(Constants.EXECRISES)
                || type.equals(Constants.PROJECT)
                || type.equals(Constants.SEMINAR)
                || type.equals(Constants.LABORATORY)
                || type.equals(Constants.OTHER))
            return type;
        else
            return Constants.OTHER;
    }

    private int getWeekdayNumber(){
        if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.monday_long)))
            return Calendar.MONDAY;
        else if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.tuesday_long)))
            return Calendar.TUESDAY;
        else if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.wednesday_long)))
            return Calendar.WEDNESDAY;
        else if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.thursday_long)))
            return Calendar.THURSDAY;
        else if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.friday_long)))
            return Calendar.FRIDAY;
        else if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.saturday_long)))
            return Calendar.SATURDAY;
        else if(spinnerDay.getSelectedItem().toString().equals(getString(R.string.sunday_long)))
            return Calendar.SUNDAY;
        else
            return -1;
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
}