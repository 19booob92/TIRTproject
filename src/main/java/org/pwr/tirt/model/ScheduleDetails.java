package org.pwr.tirt.model;


public class ScheduleDetails {

    private String dayOfWeek;
    
    private String start;

    private String end;
    
    private String building;
    
    private String room;
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getBuilding() {
        return building;
    }

    
    public void setBuilding(String building) {
        this.building = building;
    }

    
    public String getRoom() {
        return room;
    }

    
    public void setRoom(String room) {
        this.room = room;
    }


    
    public String getStart() {
        return start;
    }


    
    public void setStart(String start) {
        this.start = start;
    }


    
    public String getEnd() {
        return end;
    }


    
    public void setEnd(String end) {
        this.end = end;
    }
    
}
