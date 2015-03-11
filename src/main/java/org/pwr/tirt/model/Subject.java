package org.pwr.tirt.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Subject implements Serializable {

    private static final long serialVersionUID = 5515541047891075287L;

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String lector;

    private String type;

    private double start;

    private double end;

    private int day;

    private String place;

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getLector() {
        return lector;
    }


    public void setLector(String lector) {
        this.lector = lector;
    }


    public double getStart() {
        return start;
    }


    public void setStart(double start) {
        this.start = start;
    }


    public double getEnd() {
        return end;
    }


    public void setEnd(double end) {
        this.end = end;
    }


    public int getDay() {
        return day;
    }


    public void setDay(int day) {
        this.day = day;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getPlace() {
        return place;
    }


    public void setPlace(String place) {
        this.place = place;
    }


}
