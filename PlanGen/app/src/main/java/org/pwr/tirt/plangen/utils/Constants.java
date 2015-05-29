package org.pwr.tirt.plangen.utils;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String FAIL_MESSAGE = "FAILED";
    public static final String FREE_TIME_TAG = "FREE_TIME";
    public static final String NO_DATA = "No data";

    public static final String LECTURE = "Wykład";
    public static final String EXECRISES = "Ćwiczenia";
    public static final String PROJECT = "Projekt";
    public static final String SEMINAR = "Seminarium";
    public static final String LABORATORY = "Zajęcia laboratoryjne";
    public static final String OTHER = "Inne";

    public static String SEMESTER_START_DATE = "2015-02-25";
    public static String SEMESTER_END_DATE = "2015-06-17";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
}
