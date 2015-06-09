package org.pwr.tirt.plangen.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.logic.GVServerClient;
import org.pwr.tirt.plangen.logic.ITaskListener;
import org.pwr.tirt.plangen.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Activity for managing application settings
 */
public class SettingsActivity extends ActionBarActivity implements ITaskListener, DatePickerDialog.OnDateSetListener{
    private static final String LOG_TAG = "Logic Activity";

    private EditText editTextIndexNumber, editTextSemesterStart, editTextSemesterEnd;
    private Button buttonDownload;

    private DBAdapter dbAdapter;
    private boolean serverOn = false;
    private int editedDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextIndexNumber = (EditText) findViewById(R.id.editTextIndexNumber);
        editTextSemesterStart = (EditText) findViewById(R.id.editTextSemesterStartDate);
        editTextSemesterStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedDate = 1;
                changeDate(editTextSemesterStart.getText().toString());
            }
        });
        editTextSemesterEnd = (EditText) findViewById(R.id.editTextSemesterEndDate);
        editTextSemesterEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedDate = 2;
                changeDate(editTextSemesterEnd.getText().toString());
            }
        });

        buttonDownload = (Button) findViewById(R.id.buttonDownload);

        SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        Constants.INDEX_NUMBER = settings.getString(Constants.SHARED_PREFERENCES_INDEX_NUMBER_KEY, "000000");
        Constants.SEMESTER_START_DATE = settings.getString(Constants.SHARED_PREFERENCES_SEMESTER_START_KEY, "2015-02-25");
        Constants.SEMESTER_END_DATE = settings.getString(Constants.SHARED_PREFERENCES_SEMESTER_END_KEY, "2015-06-17");

        editTextIndexNumber.setText(Constants.INDEX_NUMBER);
        editTextSemesterStart.setText(Constants.SEMESTER_START_DATE);
        editTextSemesterEnd.setText(Constants.SEMESTER_END_DATE);

        initDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(dbAdapter != null)
            dbAdapter.closeConnection();
        super.onDestroy();
    }

    /**
     * Method that parses server response to {@link Event}s and saves them in database
     *
     * @param data Server response
     */
    @Override
    public void dataDownloaded(String data) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            events = Event.deserializeArray(data);
            Toast.makeText(getApplicationContext(), R.string.download_success, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Deserialization failed " + e.getMessage());
            Toast.makeText(getApplicationContext(), R.string.failed_message, Toast.LENGTH_SHORT).show();
        }
        if(!events.isEmpty()) {
            dbAdapter.deleteAllData();
            for(Event event : events) dbAdapter.insertData(event);
        }
        buttonDownload.setEnabled(true);
    }

    /**
     * Method that is called when server request fails
     */
    @Override
    public void downloadingFailed() {
        Toast.makeText(getApplicationContext(), R.string.failed_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method that fulfills selected EditText with selected date
     * in DatePicker view
     *
     * @param view Used DatePicker
     * @param year Selected year
     * @param monthOfYear Selected month
     * @param dayOfMonth Selected day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        if(editedDate == 1)
            editTextSemesterStart.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        else
            editTextSemesterEnd.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
    }

    /**
     * Method that creates DatePicker view with date
     *
     * @param date Date to set in DatePicker
     */
    private void changeDate(String date){
        Calendar c = Calendar.getInstance();
        if(date != null) {
            try {
                c.setTime(Constants.dateFormat.parse(date));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parsing time failed. " + e.getMessage());
            }
        }
        new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Method that initiates database
     */
    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
    }

    /**
     * Method that saves typed index numbed in SharedPreferences
     * and depending on setting calls {@link GVServerClient} to download data
     * or loads hardcoded data
     */
    public void onClickDownloadData(View view) {
        Constants.INDEX_NUMBER = editTextIndexNumber.getText().toString();
        SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.SHARED_PREFERENCES_INDEX_NUMBER_KEY, Constants.INDEX_NUMBER);
        editor.apply();
        if(serverOn) {
            GVServerClient.connectToServer(getApplicationContext(), this, editTextIndexNumber.getText().toString());
            buttonDownload.setEnabled(false);
        } else {
            String json = "[" +
                    "{\"id\":0,\"name\":\"Modelowanie i anal. biznesowa\",\"lector\":\"Dr inż. Anita Walkowiak\",\"type\":\"Zajęcia laboratoryjne\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-05\",\"start\":\"18:40\",\"end\":\"22:55\",\"building\":\"B-4\",\"room\":\"226\"}}," +
                    "{\"id\":0,\"name\":\"Modelowanie i anal. biznesowa\",\"lector\":\"Prof. dr hab. inż. Zbigniew Huzar\",\"type\":\"Wykład\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-05\",\"start\":\"09:15\",\"end\":\"11:15\",\"building\":\"B-4\",\"room\":\"409\"}}," +
                    "{\"id\":0,\"name\":\"Teoria i inż. ruchu teleinf.\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-05\",\"start\":\"15:00\",\"end\":\"16:00\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-06\",\"start\":\"15:00\",\"end\":\"16:00\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-05\",\"start\":\"16:10\",\"end\":\"16:15\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-05\",\"start\":\"16:30\",\"end\":\"17:00\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"2015-06-05\",\"start\":\"17:30\",\"end\":\"18:35\",\"building\":\"A-1\",\"room\":\"329\"}}" +
                    "]";
            /*String json = "[" +
                    "{\"id\":0,\"name\":\"Modelowanie i anal. biznesowa\",\"lector\":\"Dr inż. Anita Walkowiak\",\"type\":\"Zajęcia laboratoryjne\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"pn\",\"start\":\"18:40\",\"end\":\"22:55\",\"building\":\"B-4\",\"room\":\"226\"}}," +
                    "{\"id\":0,\"name\":\"Modelowanie i anal. biznesowa\",\"lector\":\"Prof. dr hab. inż. Zbigniew Huzar\",\"type\":\"Wykład\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"wt/TP\",\"start\":\"09:15\",\"end\":\"11:15\",\"building\":\"B-4\",\"room\":\"409\"}}," +
                    "{\"id\":0,\"name\":\"Teoria i inż. ruchu teleinf.\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"sr/TN\",\"start\":\"15:00\",\"end\":\"16:00\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"cw/TP\",\"start\":\"15:00\",\"end\":\"16:00\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"cw/TN\",\"start\":\"16:10\",\"end\":\"16:15\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"pt\",\"start\":\"16:30\",\"end\":\"17:00\",\"building\":\"A-1\",\"room\":\"329\"}}," +
                    "{\"id\":0,\"name\":\"TIRT\",\"lector\":\"Prof. dr hab. inż. Adam Grzech\",\"type\":\"Ćwiczenia\",\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"sb\",\"start\":\"17:30\",\"end\":\"18:35\",\"building\":\"A-1\",\"room\":\"329\"}}" +
                    "]";*/
            dataDownloaded(json);
        }
    }

    /**
     * Method that changes semester start and end dates in SharedPreferences
     */
    public void onClickSemesterSettings(View view) {
        Constants.SEMESTER_START_DATE = editTextSemesterStart.getText().toString();
        Constants.SEMESTER_END_DATE = editTextSemesterEnd.getText().toString();
        SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.SHARED_PREFERENCES_SEMESTER_START_KEY, Constants.SEMESTER_START_DATE);
        editor.putString(Constants.SHARED_PREFERENCES_SEMESTER_END_KEY, Constants.SEMESTER_END_DATE);
        editor.apply();
        Toast.makeText(getApplicationContext(), R.string.semester_settings_changed, Toast.LENGTH_SHORT).show();
    }
}
