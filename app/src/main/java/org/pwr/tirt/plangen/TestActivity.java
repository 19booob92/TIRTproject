package org.pwr.tirt.plangen;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TestActivity extends ActionBarActivity {
    private Button button;
    private EditText editText;
    private TextView textView;

    private DBAdapter dbAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView2);

        initDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

    private void initDatabase() {
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
        cursor = dbAdapter.getAllData();
        if(cursor != null) {
            //startManagingCursor(cursor); //TODO: deprecated
            cursor.moveToFirst();
        }
        textView.setText(getData());
    }

    private String getData() {
        String allData = "";
        cursor = dbAdapter.getAllData();
        if(cursor != null && cursor.moveToFirst()) {
            do {
                String data1 = cursor.getString(DBAdapter.DATA1_COLUMN);
                String data2 = cursor.getString(DBAdapter.DATA2_COLUMN);
                allData += "data1: " + data1 + " data2: " + data2 + "\n";
            } while(cursor.moveToNext());
        }
        return allData;
    }

    public void onClickAddData(View view) {
        Long number = dbAdapter.insertData(editText.getText().toString());
        Toast.makeText(getApplicationContext(), number.toString(), Toast.LENGTH_SHORT).show();
        String data = getData();
        textView.setText(data);
    }
}
