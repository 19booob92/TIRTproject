package org.pwr.tirt.plangen.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pwr.tirt.plangen.utils.Constants;

import java.util.ArrayList;
/**
Class represents single event from schedule
 */
public class Event {
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
        JSONArray jsonObjs = new JSONArray(serialized);
        ArrayList<Event> events = new ArrayList<>();
        for (int i=0; i<jsonObjs.length(); i++) {
            JSONObject eventSerialized = jsonObjs.getJSONObject(i);
            events.add(new Event(eventSerialized));
        }
        return events;
    }
}
