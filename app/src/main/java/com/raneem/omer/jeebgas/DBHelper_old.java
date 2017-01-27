package com.raneem.omer.jeebgas;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;


public class DBHelper_old extends SQLiteOpenHelper {

    private static final String DATABASE = "my_db";

    private static final String TABLE_CLIENT = "Client";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE= "phone";
    private static final String ID= "id";

    public DBHelper_old(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_CLIENT+" (" + ID + " integer primary key, "+NAME+" text, "+ADDRESS+" text, "+PHONE+" integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CLIENT);
// Creating tables again
        onCreate(db);
    }


    public Cursor getClient() {
        String selectQuery = "SELECT * FROM " + TABLE_CLIENT + " LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }
}
