package org.pwr.tirt.plangen.logic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class represents single event from schedule
 */
public class Event {
    private static final String LOG_TAG = "Event";
    private static final boolean serverOn = true;

    public int id;
    public String title;
    public String type;
    public String date;
    public String timeStart;
    public String timeEnd;
    public String location;
    public String tutor;

    public Event() {
        this.id = -1;
        this.title = Constants.FREE_TIME_TAG;
        this.type = Constants.NO_DATA;
        this.date = "1970-01-01";
        this.timeStart = "00:00";
        this.timeEnd = "23:59";
        this.location = Constants.NO_DATA;
        this.tutor = Constants.NO_DATA;
    }

    public Event(JSONObject obj) throws JSONException {
        deserializeFromObj(obj);
    }

    /**
     * Method that deserializes {@link Event} object from JSON
     *
     * @param obj JSONObject to deserialize
     * @throws org.json.JSONException
     */
    private void deserializeFromObj(JSONObject obj) throws JSONException {
        this.title = obj.getString("name");
        this.type = obj.getString("type");
        this.tutor = obj.getString("lector");
        JSONObject details = obj.getJSONObject("details");
        this.date = details.getString("dayOfWeek");
        this.timeStart = details.getString("start");
        this.timeEnd = details.getString("end");
        this.location = details.getString("building") + " " + details.getString("room");
    }

    /**
     * Method that deserializes {@link Event}s ArrayList object from String
     *
     * @param serialized String to deserialize
     * @return {@link Event}s ArrayList from schedule
     * @throws org.json.JSONException
     */
    public static ArrayList<Event> deserializeArray(String serialized) throws JSONException {
        if(serverOn) {
            JSONArray jsonObjs = new JSONArray(serialized);
            ArrayList<Event> events = new ArrayList<>();
            for (int i=0; i<jsonObjs.length(); i++) {
                JSONObject eventSerialized = jsonObjs.getJSONObject(i);
                Event eventTemplate = new Event(eventSerialized);
                ArrayList<String> dates = getDates(eventTemplate.date);
                for (String date : dates) {
                    Event event = new Event();
                    event.title = eventTemplate.title;
                    event.type = eventTemplate.type;
                    event.timeStart = eventTemplate.timeStart;
                    event.timeEnd = eventTemplate.timeEnd;
                    event.tutor = eventTemplate.tutor;
                    event.location = eventTemplate.location;
                    event.date = date;
                    events.add(event);
                }
            }
            return events;
        } else {
            JSONArray jsonObjs = new JSONArray(serialized);
            ArrayList<Event> events = new ArrayList<>();
            for (int i=0; i<jsonObjs.length(); i++) {
                JSONObject eventSerialized = jsonObjs.getJSONObject(i);
                events.add(new Event(eventSerialized));
            }
            return events;
        }
    }

    /**
     * Method that generates dates from semester start to end
     * for value of JSON field
     *
     * @param dayOfWeek JSON field
     * @return List of generated dates
     */
    private static ArrayList<String> getDates(String dayOfWeek) {
        int weekType;
        if(dayOfWeek.contains("TP"))
            weekType = 1;
        else if(dayOfWeek.contains("TN"))
            weekType = 0;
        else
            weekType = -1;

        int weekdayNumber;
        switch(dayOfWeek.substring(0, 2)) {
            case "pn":
                weekdayNumber = Calendar.MONDAY;
                break;
            case "wt":
                weekdayNumber = Calendar.TUESDAY;
                break;
            case "śr":
                weekdayNumber = Calendar.WEDNESDAY;
                break;
            case "cz":
                weekdayNumber = Calendar.THURSDAY;
                break;
            case "pt":
                weekdayNumber = Calendar.FRIDAY;
                break;
            case "sb":
                weekdayNumber = Calendar.SATURDAY;
                break;
            case "nd":
                weekdayNumber = Calendar.SUNDAY;
                break;
            default:
                weekdayNumber = -1;
                break;
        }

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
        if(weekdayNumber != -1) {
            int diff = weekdayNumber - semesterStart.get(Calendar.DAY_OF_WEEK);
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
}
