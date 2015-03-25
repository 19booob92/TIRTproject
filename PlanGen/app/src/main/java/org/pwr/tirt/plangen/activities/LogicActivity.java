package org.pwr.tirt.plangen.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.ITaskListener;
import org.pwr.tirt.plangen.logic.ServerClientTask;

import java.util.ArrayList;

public class LogicActivity extends ActionBarActivity implements ITaskListener {
    private static final String LOG_TAG = "Logic Activity";

    private EditText editText;
    private TextView textView;
    private Button buttonDownload;

    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logic);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView2);
        buttonDownload = (Button) findViewById(R.id.button);

        initDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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


    @Override
    public void dataDownloaded(String data) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            events = Event.deserializeArray(data);
            Toast.makeText(getApplicationContext(), R.string.download_success, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Deserialization failed " + e.getMessage());
            Toast.makeText(getApplicationContext(), R.string.download_failed, Toast.LENGTH_SHORT).show();
        }
        if(!events.isEmpty()) {
            dbAdapter.deleteAllData();
            for(Event event : events) dbAdapter.insertData(event);
        }
        textView.setText(getData());
        buttonDownload.setEnabled(true);
    }

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
        textView.setText(getData());
    }

    private String getData() {
        String allData = "";
        ArrayList<Event> events = dbAdapter.getDailyEvents(null);
        for(Event event : events) {
            allData += "title: " + event.title +
                    " type: " + event.type +
                    " date: " + event.date +
                    " start: " + event.timeStart +
                    " end: " + event.timeEnd +
                    " location: " + event.location +
                    " tutor: " + event.tutor +
                    "\n";
        }
        return allData;
    }

    public void onClickDownloadData(View view) {
        ServerClientTask serverClientTask = new ServerClientTask(getApplicationContext(), this);
        serverClientTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, editText.getText().toString());
        buttonDownload.setEnabled(false);
    }
}
