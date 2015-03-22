package org.pwr.tirt.plangen.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.pwr.tirt.plangen.logic.DBAdapter;
import org.pwr.tirt.plangen.logic.Event;
import org.pwr.tirt.plangen.R;
import org.pwr.tirt.plangen.logic.ITaskListener;
import org.pwr.tirt.plangen.logic.ServerClientTask;

import java.util.ArrayList;

public class LogicActivity extends ActionBarActivity implements ITaskListener {
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
        buttonDownload = (Button) findViewById(R.id.button2);

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
        textView.setText(data);
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
                    " datetime: " + event.dateTime +
                    " location: " + event.location +
                    " tutor: " + event.tutor +
                    "\n";
        }
        return allData;
    }

    public void onClickAddData(View view) {
        Event event = new Event();
        if(editText.getText().length() > 0)
            event.title = editText.getText().toString();
        else
            event.title = "Random";
        event.type = "T";
        event.dateTime = "0000-00-00 00:00";
        event.location = "105, C3";
        event.tutor = "dr Somebody";

        if(dbAdapter.insertData(event))
            Toast.makeText(getApplicationContext(), R.string.insert_success, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), R.string.insert_failure, Toast.LENGTH_SHORT).show();

        String data = getData();
        textView.setText(data);
    }

    public void onClickDownloadData(View view) {
        ServerClientTask serverClientTask = new ServerClientTask(getApplicationContext(), this);
        serverClientTask.execute();
        buttonDownload.setEnabled(false);
    }
}
