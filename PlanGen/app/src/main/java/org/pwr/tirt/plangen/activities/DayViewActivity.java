package org.pwr.tirt.plangen.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class DayViewActivity extends ActionBarActivity {
    private static final String LOG_TAG = "Day View Activity";

    private DBAdapter dbAdapter;
    private String date;
    private LinearLayout linearLayoutBar;
    private TextView textViewWeekdayName, textViewEventTitle;
    private SeekBar seekBarDayProgress;
    private ArrayList<Event> eventsList;
    private ArrayList<Float> weights = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2015); //TODO: delete
        c.set(Calendar.MONTH, 2);
        c.set(Calendar.DAY_OF_MONTH, 27);
        date = Constants.dateFormat.format(c.getTime());

        linearLayoutBar = (LinearLayout) findViewById(R.id.linearLayoutBar);
        textViewWeekdayName = (TextView) findViewById(R.id.textViewWeekdayName);
        textViewEventTitle = (TextView) findViewById(R.id.textViewEventTitle);
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
        getEventData(0);
        textViewWeekdayName.setText(getWeekdayName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_day_view, menu);
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

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

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

    private int getColor(String type) {
        switch (type) {
            case Constants.LECTURE:
                return getResources().getColor(R.color.lecture);
            case Constants.EXECRISES:
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
        textViewWeekdayName.setText(getWeekdayName());
        populateBar();
        seekBarDayProgress.setProgress(0);
        getEventData(0);
    }

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
            textViewEventTitle.setText(selectedEvent.title);
        else
            textViewEventTitle.setText("");
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
