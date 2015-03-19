package org.pwr.tirt.plangen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    private static final String LOG_TAG = "DBAdapter";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_TABLE_NAME = "COURSES";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_DATA1 = "data1";
    public static final String DATA1_OPTIONS = "TEXT NOT NULL";
    public static final int DATA1_COLUMN = 1;
    public static final String KEY_DATA2 = "data2";
    public static final String DATA2_OPTIONS = "TEXT";
    public static final int DATA2_COLUMN = 2;

    private static final String DB_CREATE_TABLE =
            "CREATE TABLE " + DB_TABLE_NAME + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_DATA1 + " " + DATA1_OPTIONS + ", " +
                    KEY_DATA2 + " " + DATA2_OPTIONS +
                    ");";
    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + DB_TABLE_NAME;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE);
            Log.d(LOG_TAG, "Table " + DB_TABLE_NAME + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE); //TODO: kopiowanie danych zamiast drop i create
            onCreate(db);
            Log.d(LOG_TAG, "Table " + DB_TABLE_NAME + " updated from ver." + oldVersion + " to ver." + newVersion);
        }
    }

    public DBAdapter(Context context) {
        this.context = context;
    }

    public DBAdapter openConnection(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void closeConnection() {
        dbHelper.close();
    }

    public long insertData(String data) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DATA1, data);
        newValues.put(KEY_DATA2, data + "2");
        return db.insert(DB_TABLE_NAME, null, newValues); //TODO: test zwroconej wartosci - -1 -> blad
    }

    public boolean deleteData(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TABLE_NAME, where, null) > 0;
    }

    public Cursor getAllData() {
        String[] columns = {KEY_ID, KEY_DATA1, KEY_DATA2};
        return db.query(DB_TABLE_NAME, columns, null, null, null, null, null);
    }

    public String getData(long id) {
        String[] columns = {KEY_ID, KEY_DATA1, KEY_DATA2};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_TABLE_NAME, columns, where, null, null, null, null);
        String data = "no data";
        if(cursor != null && cursor.moveToFirst()) {
            String d1 = cursor.getString(DATA1_COLUMN);
            String d2 = cursor.getString(DATA2_COLUMN);
            data = "data1: " + d1 + "data2: " + d2;
        }
        return data;
    }
}
