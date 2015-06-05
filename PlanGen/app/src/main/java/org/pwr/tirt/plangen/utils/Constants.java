package org.pwr.tirt.plangen.utils;

import java.text.SimpleDateFormat;

public class Constants {
    /**
    Returned after download fail
     */
    public static final String FAIL_MESSAGE = "FAILED";

    /**
     {@link org.pwr.tirt.plangen.logic.Event} title which informs about that this event is placeholder between really existing events
     */
    public static final String FREE_TIME_TAG = "FREE_TIME";

    /**
    Value for empty {@link org.pwr.tirt.plangen.logic.Event} field
     */
    public static final String NO_DATA = "No data";

    /**
    Lecture in Polish
     */
    public static final String LECTURE = "Wykład";
    /**
     Exercises in Polish
     */
    public static final String EXERCISES = "Ćwiczenia";
    /**
     Project in Polish
     */
    public static final String PROJECT = "Projekt";
    /**
     Seminar in Polish
     */
    public static final String SEMINAR = "Seminarium";
    /**
     Lecture in Polish
     */
    public static final String LABORATORY = "Zajęcia laboratoryjne";
    /**
     Other in Polish
     */
    public static final String OTHER = "Inne";

    /**
     Semester start date
     */
    public static String SEMESTER_START_DATE = "2015-02-25";
    /**
     Semester end date
     */
    public static String SEMESTER_END_DATE = "2015-06-17";
    /**
     * Student's index number
     */
    public static String INDEX_NUMBER = "";
    /**
     Date format - yyyy-MM-dd
     */
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     Time format - HH:mm
     */
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    /**
     * Shared preferences name
     */
    public static final String SHARED_PREFERENCES_NAME = "PlanGenSettingsFile";
    /**
     * Shared preferences semester start field name
     */
    public static final String SHARED_PREFERENCES_SEMESTER_START_KEY = "SemesterStart";
    /**
     * Shared preferences semester end field name
     */
    public static final String SHARED_PREFERENCES_SEMESTER_END_KEY = "SemesterEnd";
    /**
     * Shared preferences index number field name
     */
    public static final String SHARED_PREFERENCES_INDEX_NUMBER_KEY = "IndexNumber";
}
