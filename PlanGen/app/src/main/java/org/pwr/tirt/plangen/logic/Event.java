package org.pwr.tirt.plangen.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Event {
    public String title;
    public String type;
    public String date;
    public String timeStart;
    public String timeEnd;
    public String location;
    public String tutor;

    public Event() {
        this.title = "No data";
        this.type = "No data";
        this.date = "No data";
        this.timeStart = "No data";
        this.timeEnd = "No data";
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
        this.date = details.getString("dayOfWeek"); //TODO: needed date not day of week
        this.timeStart = details.getString("start");
        this.timeEnd = details.getString("end");
        this.location = details.getString("building") + " " + details.getString("room"); //TODO: separate?
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
