package com.raneem.omer.jeebgas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class DBHelper extends SQLiteOpenHelper {

    private static final DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "jeebGas";
    private static final String TABLE_CLIENT = "Client";
    public static final String TABLE_ORDER = "_Order";

    // the Client unquie ID
    private static String ClientID = mDataBaseRef.child("Client").push().getKey();
    private static String DriverID;//(TODO) need to get the driver id from Firebase to mark his orders

    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";

    private static final String TABLE_DRIVER = "Driver";
    private static final String DRIVERNAME = "drivername";
    private static final String DRIVERPHONE = "driverphone";
    private static final String WORKINGAREA = "workingarea";
    private static final String WORKINGHOURS = "workinghours";
    private static final String WORKINGHOURS_FROM = "workinghours_from";
    private static final String WORKINGHOURS_TILL = "workinghours_till";
    private static final String GASPRICE = "gasprice";
    private static final String GASPRICE_SMALL = "gasprice_small";
    private static final String GASPRICE_BIG = "gasprice_big";
    private static final String SERVICETYPE = "servicetype";
    private static final String SERVICETYPE_DELIVER = "servicetype_deliver";
    private static final String SERVICETYPE_REPAIR = "servicetype_repair";
    private static final String DRIVER_RATING = "rating";


    private String CREATE_ACCOUNT_TABLE = "create table if not exists " + TABLE_CLIENT +
            " (_id integer primary key AUTOINCREMENT, " + NAME + " text, " + ADDRESS + " text, " + PHONE + " integer)";

    private String CREATE_DRIVER_TABLE = "create table if not exists " + TABLE_DRIVER +
            " (_id integer primary key AUTOINCREMENT, "
            + DRIVERNAME + " text, "
            + DRIVERPHONE + " integer, "
            + WORKINGAREA + " text, "
            + WORKINGHOURS_FROM + " text, "
            + WORKINGHOURS_TILL + " text, "
            + GASPRICE_SMALL + " integer, "
            + GASPRICE_BIG + " integer, "
            + DRIVER_RATING + " float, "
            + SERVICETYPE_DELIVER + " integer, "
            + SERVICETYPE_REPAIR + " integer)";

    private String CREATE_ORDER_TABLE = "create table if not exists " + TABLE_ORDER +
            " (_id integer primary key AUTOINCREMENT, "
            + DRIVERNAME + " text, "
            + DRIVERPHONE + " integer, "
            + WORKINGAREA + " text, "
            + WORKINGHOURS_FROM + " text, "
            + WORKINGHOURS_TILL + " text, "
            + GASPRICE + " integer, "
            + DRIVER_RATING + " float, "
            + SERVICETYPE_DELIVER + " integer, "
            + SERVICETYPE_REPAIR + " integer)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    public boolean insertClient(JeebGasClient jeebgasclient) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            String deleteQuery = "delete from " + TABLE_CLIENT + ";";
            getWritableDatabase().execSQL(deleteQuery);

            contentValues.put(NAME, jeebgasclient.getName());
            contentValues.put(ADDRESS, jeebgasclient.getAddress());
            contentValues.put(PHONE, jeebgasclient.getPhone());
            db.insert(TABLE_CLIENT, null, contentValues);


            //Save in firebase
            Map<String, String> FBmap = new HashMap<String, String>();
            FBmap.put("NAME",jeebgasclient.getName());
            FBmap.put("ADDRESS",jeebgasclient.getAddress());
            FBmap.put("PHONE",jeebgasclient.getPhone());

            mDataBaseRef.child("Client").child(ClientID).setValue(FBmap);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public Cursor getClient() {
        String selectQuery = "SELECT * FROM " + TABLE_CLIENT + " LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }

    //**********************DRIVER********************************


   /* public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DRIVER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_DRIVER_TABLE);
    }*/

    public Cursor pointDriver() {

        String selectQuery = "SELECT * FROM " + TABLE_DRIVER + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }

    // show all drivers in the pressJeebGasButton
    public Cursor getDriversList() {

            String driversList = "SELECT * FROM " + TABLE_DRIVER;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(driversList, null);
            cursor.moveToFirst();
            return cursor;
    }

    //get all drivers from server DB
    public boolean insertDriversList() {


        try {


            return true;
        } catch (Exception e) {

            return false;
        }
    }


    // after choosing order display it using a primary key or id
    public Cursor getDriver(long id) {

        String driversList = "SELECT * FROM " + TABLE_DRIVER + " WHERE _id = " + id + " LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(driversList, null);
        cursor.moveToFirst();
        return cursor;
    }


    //******************************* MOCKDATA ***************************************//
    public boolean emptyDriverTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            String deleteQuery = "delete from " + TABLE_DRIVER + ";";
            getWritableDatabase().execSQL(deleteQuery);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean insertDriver_mock(String name, String phone, String workingArea, String workingHours_from, String workingHours_till,
                                     int gasPrice_small, int gasPrice_big, int serviceType_deliver, int serviceType_repair, float rating) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put(DRIVERNAME, name);
            contentValues.put(DRIVERPHONE, phone);
            contentValues.put(WORKINGAREA, workingArea);
            contentValues.put(WORKINGHOURS_FROM, workingHours_from);
            contentValues.put(WORKINGHOURS_TILL, workingHours_till);
            contentValues.put(GASPRICE_SMALL, gasPrice_small);
            contentValues.put(GASPRICE_BIG, gasPrice_big);
            contentValues.put(SERVICETYPE_DELIVER, serviceType_deliver);
            contentValues.put(SERVICETYPE_REPAIR, serviceType_repair);
            contentValues.put(DRIVER_RATING, rating);
            db.insert(TABLE_DRIVER, null, contentValues);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    //empty the current order
    public boolean empty_OrderTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            String deleteQuery = "delete from " + TABLE_ORDER + ";";
            getWritableDatabase().execSQL(deleteQuery);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    //fill the new order aftr pressing ordernow
    public boolean insertOrder(String name, String phone, String workingArea, String hours_from, String hours_till, int order_price, int deliver,
                              int repair, float rating) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            contentValues.put(DRIVERNAME, name);
            contentValues.put(DRIVERPHONE, phone);
            contentValues.put(WORKINGAREA, workingArea);
            contentValues.put(WORKINGHOURS_FROM, hours_from);
            contentValues.put(WORKINGHOURS_TILL, hours_till);
            contentValues.put(GASPRICE, order_price);
            contentValues.put(SERVICETYPE_DELIVER, deliver);
            contentValues.put(SERVICETYPE_REPAIR, repair);
            contentValues.put(DRIVER_RATING, rating);
            db.insert(TABLE_ORDER, null, contentValues);

            //Save in firebase order/driver info
            Map<String, String> FBmap = new HashMap<String, String>();
            FBmap.put("ClientID",ClientID);
            FBmap.put("DRIVERNAME",name);
            FBmap.put("DRIVERPHONE",phone);
            FBmap.put("WORKINGAREA",workingArea);
            FBmap.put("WORKINGHOURSFROM",hours_from);
            FBmap.put("WORKINGHOURSTILL",hours_till);
            String Dstr = String.valueOf(deliver);
            String Rstr = String.valueOf(repair);
            FBmap.put("DELIVER",Dstr);
            FBmap.put("REPAIR",Rstr);


            // Save the Order info + the Driver info the order is from
            mDataBaseRef.child("Orders").child(DriverID).setValue(FBmap);

            return true;
        } catch( Exception e) {
            return false;
        }
    }

    public Cursor getOrder() {
        String driversList = "SELECT * FROM " + TABLE_ORDER + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(driversList, null);
        cursor.moveToFirst();
        return cursor;
    }


}
