package org.pwr.tirt.plangen.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Activity that displays daily {@link Event}s
 */
public class DayViewActivity extends ActionBarActivity {
    private static final String LOG_TAG = "Day View Activity";

    private DBAdapter dbAdapter;
    private String date;
    private LinearLayout linearLayoutBar;
    private TextView textViewWeekdayName, textViewEventTitle, textViewEventTime, textViewEventLocation, textViewEventType, textViewEventTutor;
    private SeekBar seekBarDayProgress;
    private ArrayList<Event> eventsList;
    private ArrayList<Float> weights = new ArrayList<>();
    private Button[] dayButtons = new Button[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        Calendar c = Calendar.getInstance();
        date = Constants.dateFormat.format(c.getTime());

        dayButtons[0] = (Button) findViewById(R.id.buttonSunday);
        dayButtons[1] = (Button) findViewById(R.id.buttonMonday);
        dayButtons[2] = (Button) findViewById(R.id.buttonTuesday);
        dayButtons[3] = (Button) findViewById(R.id.buttonWednesday);
        dayButtons[4] = (Button) findViewById(R.id.buttonThursday);
        dayButtons[5] = (Button) findViewById(R.id.buttonFriday);
        dayButtons[6] = (Button) findViewById(R.id.buttonSaturday);

        linearLayoutBar = (LinearLayout) findViewById(R.id.linearLayoutBar);
        textViewWeekdayName = (TextView) findViewById(R.id.textViewWeekdayName);
        textViewEventTitle = (TextView) findViewById(R.id.textViewEventTitle);
        textViewEventTime = (TextView) findViewById(R.id.textViewEventTime);
        textViewEventLocation = (TextView) findViewById(R.id.textViewEventLocation);
        textViewEventType = (TextView) findViewById(R.id.textViewEventType);
        textViewEventTutor = (TextView) findViewById(R.id.textViewEventTutor);
        seekBarDayProgress = (SeekBar) findViewById(R.id.seekBarDayProgress);
        seekBarDayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getEventData(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        initDatabase();
        populateBar();
        seekBarDayProgress.setProgress(getActualTimeSeekBarPosition(c));
        //getEventData(0);
        textViewWeekdayName.setText(getWeekdayName());
        setDayButtonsSelection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_day_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add_event:
                intent = new Intent(this, AddEventActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_edit_event:
                intent = new Intent(this, EditEventActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_week_view:
                intent = new Intent(this, WeekViewActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_campus_map:
                intent = new Intent(this, Mapa.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
     * Method that populates horizontal bar with daily {@link Event}s
     */
    private void populateBar() {
        eventsList = dbAdapter.getDailyEvents(date);

        if(linearLayoutBar.getChildCount() > 0)
            linearLayoutBar.removeAllViews();
        weights.clear();

        for(Event event : eventsList) {
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
            params.width = 0;
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            layout.setLayoutParams(params);
            layout.setBackgroundColor(getColor(event.type));

            weights.add(weight);
            linearLayoutBar.addView(layout);
        }
    }

    /**
     * Method that returns weekday name
     *
     * @return Weekday name
     */
    private String getWeekdayName() {
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

    /**
     * Method that returns {@link Event}s background color
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

    /**
     * Method that returns {@link Event}s type letter to show
     *
     * @param type Event type
     * @return Letter surrounded with spaces
     */
    private String getTypeLetter(String type) {
        String result = " ";
        switch (type) {
            case Constants.LECTURE:
                result += getString(R.string.lecture_short);
                break;
            case Constants.EXERCISES:
                result += getString(R.string.exercises_short);
                break;
            case Constants.LABORATORY:
                result += getString(R.string.laboratory_short);
                break;
            case Constants.PROJECT:
                result+= getString(R.string.project_short);
                break;
            case Constants.SEMINAR:
                result += getString(R.string.seminar_short);
                break;
            case Constants.OTHER:
                result += getString(R.string.other_short);
                break;
        }
        result += " ";
        return result;
    }

    /**
     * Method that changes activity view after date selection
     *
     * @param dayNumber String to deserialize
     */
    private void setDate(int dayNumber) {
        Calendar calendar = Calendar.getInstance();
        int diff = dayNumber - calendar.get(Calendar.DAY_OF_WEEK);
        if (diff < 0)
            diff += 7;
        calendar.add(Calendar.DAY_OF_MONTH, diff);
        date = Constants.dateFormat.format(calendar.getTime());
        textViewWeekdayName.setText(getWeekdayName());
        populateBar();
        getActualTimeSeekBarPosition(calendar);
        setDayButtonsSelection();
    }

    /**
     * Method that sets selected on horizontal bar {@link Event}
     *
     * @param position Bar thumb position
     */
    private void getEventData(int position) {
        float weightSelectedPosition = (float)position / 10000f;
        float weightSum = 0f;
        Event selectedEvent = new Event();
        for(int i = 0; i < weights.size(); i++) {
            weightSum+=weights.get(i);
            if(weightSum >= weightSelectedPosition) {
                selectedEvent = eventsList.get(i);
                break;
            }
        }
        if(!selectedEvent.title.equals(Constants.FREE_TIME_TAG))
            showEventData(selectedEvent);
        else
            showEmptyEvent();
    }

    /**
     * Method that populates TextViews with selected {@link Event} data
     *
     * @param event Selected Event
     */
    private void showEventData(Event event) {
        textViewEventTitle.setText(event.title);
        textViewEventTime.setText(event.timeStart + " - " + event.timeEnd);
        textViewEventLocation.setText(event.location);
        textViewEventType.setText(getTypeLetter(event.type));
        textViewEventType.setBackgroundColor(getColor(event.type));
        textViewEventTutor.setText(event.tutor);
    }

    /**
     * Method that shows empty {@link Event}
     */
    private void showEmptyEvent() {
        textViewEventTitle.setText("");
        textViewEventTime.setText("");
        textViewEventLocation.setText("");
        textViewEventType.setText("");
        textViewEventType.setBackgroundColor(getColor(Constants.FREE_TIME_TAG));
        textViewEventTutor.setText("");
    }

    /**
     * Method that changes weekdays button states
     */
    private void setDayButtonsSelection() {
        Calendar c = Calendar.getInstance();
        if(date != null) {
            try {
                c.setTime(Constants.dateFormat.parse(date));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
        }
        for(int i = 0; i < 7; i++) {
            if(i == c.get(Calendar.DAY_OF_WEEK)-1)
                dayButtons[i].setSelected(true);
            else
                dayButtons[i].setSelected(false);
        }
    }

    /**
     * Method that calculates horizontal bar thum position based on actual time
     */
    private int getActualTimeSeekBarPosition(Calendar c) {
        int position = ((c.get(Calendar.HOUR_OF_DAY) * 60) + c.get(Calendar.MINUTE))*7;
        if(position > 10000)
            position = 10000;
        return position;
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
