package org.pwr.tirt.plangen.logic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Event {
    private static final String LOG_TAG = "Event";

    public String title;
    public String type;
    public Calendar date;
    public Calendar timeStart;
    public Calendar timeEnd;
    public String location;
    public String tutor;

    public Event() {
        this.title = "No data";
        this.type = "No data";
        this.date = Calendar.getInstance(); //TODO: test
        this.timeStart = Calendar.getInstance(); //TODO: test
        this.timeEnd = Calendar.getInstance(); //TODO: test
        this.location = "No data";
        this.tutor = "No data";
    }

    public Event(JSONObject obj) throws JSONException {
        deserializeFromObj(obj);
    }

    private void deserializeFromObj(JSONObject obj) throws JSONException {
        this.title = obj.getString("name");
        this.type = obj.getString("type");
        this.tutor = obj.getString("lector");
        JSONObject details = obj.getJSONObject("details");
        Calendar cal;
        try {
            cal = Calendar.getInstance();
            cal.setTime(Constants.dateFormat.parse(details.getString("dayOfWeek")));
            this.date = cal;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Date parsing failed: " + e.getMessage());
            this.date = null;
        }
        try {
            cal = Calendar.getInstance();
            cal.setTime(Constants.timeFormat.parse(details.getString("start")));
            this.timeStart = cal;
            cal = Calendar.getInstance();
            cal.setTime(Constants.timeFormat.parse(details.getString("end")));
            this.timeEnd = cal;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Time parsing failed: " + e.getMessage());
            this.timeStart = null;
            this.timeEnd = null;
        }
        this.location = details.getString("building") + " " + details.getString("room");
    }

    public static ArrayList<Event> deserializeArray(String serialized) throws JSONException {
        JSONArray jsonObjs = new JSONArray(serialized);
        ArrayList<Event> events = new ArrayList<>();
        for (int i=0; i<jsonObjs.length(); i++) {
            JSONObject eventSerialized = jsonObjs.getJSONObject(i);
            events.add(new Event(eventSerialized));
        }
        return events;
    }

}
