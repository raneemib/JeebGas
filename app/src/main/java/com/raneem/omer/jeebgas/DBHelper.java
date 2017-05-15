package com.raneem.omer.jeebgas;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DBHelper extends SQLiteOpenHelper {

    private static final DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference LastOrderDBRef;
    private static DatabaseReference LastOrderDBRefArchive;

    //private static DatabaseReference StatusRef;
    private Map<String, Map<String, String>> order_hashmap;


    private static String  clientstatus=null;
    private static boolean isNull = true;

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "jeebGas";
    private static final String TABLE_CLIENT = "Client";
    public static final String TABLE_ORDER = "_Order";

    // the Client unquie ID
    private static String ClientID;
   // private static String DriverID;

    private static final String NAME = "name";
    private static final String LNG = "lng";
    private static final String LAT = "lat";
    private static final String PHONE = "phone";
    private static final String ADRESS = "address";


    private static final String TABLE_DRIVER = "Driver";
    private static final String DRIVERID = "driverid";
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
    private static final String STATUS = "order_status";
    //private static final String ORDERREF = "order_ref";
    //private static final String ARCHIVEREF = "archive_ref";


    private static final String TABLE_CLIENTID = "_ClientID";
    private static final String CLIENT_ID =  "Client_ID";


    private String CREATE_ACCOUNT_TABLE = "create table if not exists " + TABLE_CLIENT +
            " (_id integer primary key AUTOINCREMENT, " + NAME + " text, " + ADRESS + " text, "  + LNG + " double, " + LAT + " double, " + PHONE + " integer)";

    private String CREATE_DRIVER_TABLE = "create table if not exists " + TABLE_DRIVER +
            " (_id integer primary key AUTOINCREMENT, "
            + DRIVERID + " text unique, " // added for the unquie id in the firebase
            + DRIVERNAME + " text, "
            + DRIVERPHONE + " integer, "
            + WORKINGAREA + " text, "
            + WORKINGHOURS_FROM + " text, "
            + WORKINGHOURS_TILL + " text, "
            + GASPRICE_SMALL + " integer, "
            + GASPRICE_BIG + " integer, "
            + STATUS + " text, "
            + SERVICETYPE_DELIVER + " integer, "
            + SERVICETYPE_REPAIR + " integer)";

    private String CREATE_KEY_TABLE = "create table if not exists " + TABLE_CLIENTID
            + " (_id integer primary key AUTOINCREMENT, "
            + CLIENT_ID + " text)";

    private String CREATE_ORDER_TABLE = "create table if not exists " + TABLE_ORDER +
            " (_id integer primary key AUTOINCREMENT, "
            + DRIVERID + " text unique, " // added for the unquie id in the firebase
            + DRIVERNAME + " text, "
            + DRIVERPHONE + " integer, "
            + WORKINGAREA + " text, "
            + WORKINGHOURS_FROM + " text, "
            + WORKINGHOURS_TILL + " text, "
            + GASPRICE + " integer, "
            + STATUS + " text, "
            //+ ORDERREF + " text, "
            //+ ARCHIVEREF + " text, "
            + SERVICETYPE_DELIVER + " integer, "
            + SERVICETYPE_REPAIR + " integer)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /**TODO
         * 1- Check if the driver has his own key in the database
         * 2- If the driver doesnt have any key => create a key, insert into database, assign the variable to this key
         * 3- If the driver does have a key => assign the variable to this key
         */

        Cursor c = getClientID();
        if(c != null && c.getCount() > 0) {
            int clientID_Index = c.getColumnIndex(CLIENT_ID);
            ClientID = c.getString(clientID_Index);
        } else {
            insertClientID();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_KEY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CLIENTID + "';");
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_KEY_TABLE);
    }


    public boolean insertClientID() {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            String ClientID = mDataBaseRef.child("Driver").push().getKey();
            Log.d("ClientID", ClientID);
            contentValues.put(CLIENT_ID, ClientID);
            db.insert(TABLE_CLIENTID, null, contentValues);
            return true;
        } catch (Exception e) {
            Log.e("InsertDriverID", e.toString());
            return false;
        }
    }

    public Cursor getClientID() {

        String selectQuery = "SELECT * FROM " + TABLE_CLIENTID + " LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }


    public boolean insertClient(JeebGasClient jeebgasclient) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            String deleteQuery = "delete from " + TABLE_CLIENT + ";";
            getWritableDatabase().execSQL(deleteQuery);

            contentValues.put(NAME, jeebgasclient.getName());
            contentValues.put(LNG, jeebgasclient.getLng());
            contentValues.put(LAT, jeebgasclient.getLat());
            contentValues.put(ADRESS, jeebgasclient.getAddress());
            contentValues.put(PHONE, jeebgasclient.getPhone());
            db.insert(TABLE_CLIENT, null, contentValues);


            //Save in firebase
            Map<String, String> FBmap = new HashMap<String, String>();
            FBmap.put("NAME",jeebgasclient.getName());
            FBmap.put("LNG",jeebgasclient.getLng());
            FBmap.put("LAT",jeebgasclient.getLat());
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

    public static void setIsNull(boolean isNull) {
        DBHelper.isNull = isNull;
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

    // this is the mocked data DELETE WHEN NOT NEEDED
    public boolean insertDriver_mock(String driverid,String name, String phone, String workingArea, String workingHours_from, String workingHours_till,
                                     int gasPrice_small, int gasPrice_big, int serviceType_deliver, int serviceType_repair, float rating) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put(DRIVERID, driverid);
            contentValues.put(DRIVERNAME, name);
            contentValues.put(DRIVERPHONE, phone);
            contentValues.put(WORKINGAREA, workingArea);
            contentValues.put(WORKINGHOURS_FROM, workingHours_from);
            contentValues.put(WORKINGHOURS_TILL, workingHours_till);
            contentValues.put(GASPRICE_SMALL, gasPrice_small);
            contentValues.put(GASPRICE_BIG, gasPrice_big);
            contentValues.put(SERVICETYPE_DELIVER, serviceType_deliver);
            contentValues.put(SERVICETYPE_REPAIR, serviceType_repair);
           // contentValues.put(DRIVER_RATING, rating);
            db.insert(TABLE_DRIVER, null, contentValues);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    // insert data from firebase to sqlite
    public boolean insertDriver(String driverid,String name, String phone, String workingArea, String workingHours_from, String workingHours_till,
                                     int gasPrice_small, int gasPrice_big, int serviceType_deliver, int serviceType_repair, float rating)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put(DRIVERID, driverid);
            contentValues.put(DRIVERNAME, name);
            contentValues.put(DRIVERPHONE, phone);
            contentValues.put(WORKINGAREA, workingArea);
            contentValues.put(WORKINGHOURS_FROM, workingHours_from);
            contentValues.put(WORKINGHOURS_TILL, workingHours_till);
            contentValues.put(GASPRICE_SMALL, gasPrice_small);
            contentValues.put(GASPRICE_BIG, gasPrice_big);
            contentValues.put(SERVICETYPE_DELIVER, serviceType_deliver);
            contentValues.put(SERVICETYPE_REPAIR, serviceType_repair);
            //contentValues.put(DRIVER_RATING, rating);//TODO DISABLE RATING TO AVOID SHAMING
            db.insert(TABLE_DRIVER, null, contentValues);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    //empty the current order
    public boolean empty_OrderTable() {
        if(!isNull) {// if table is allready empty... so we avoid crashes
            LastOrderDBRef.removeValue(); // delete the old order befor inserting the new one
            LastOrderDBRefArchive.removeValue();
        }
        isNull = true;// set isnull to true since we are emptying the order table
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
    public boolean insertOrder(String driverId,String name, String phone, String workingArea, String hours_from, String hours_till, int order_price, int deliver,
                              int repair, float rating, String orderStatus) {
        if(!isNull) {// first time its empty ... so we avoid crashes
            LastOrderDBRef.removeValue(); // delete the old order befor inserting the new one
            LastOrderDBRefArchive.removeValue();
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        try {
            if(!driverId.isEmpty()) //some times we need to insert null to just save the other data
                contentValues.put(DRIVERID, driverId);
                contentValues.put(DRIVERNAME, name);
                contentValues.put(DRIVERPHONE, phone);
                contentValues.put(WORKINGAREA, workingArea);
                contentValues.put(WORKINGHOURS_FROM, hours_from);
                contentValues.put(WORKINGHOURS_TILL, hours_till);
                contentValues.put(GASPRICE, order_price);
                contentValues.put(SERVICETYPE_DELIVER, deliver);
                contentValues.put(SERVICETYPE_REPAIR, repair);
                contentValues.put(STATUS, orderStatus);//TODO DISABLE RATING TO AVOID SHAMING
                db.insert(TABLE_ORDER, null, contentValues);

                //Save in firebase order/driver info
                Map<String, String> FBmap = new HashMap<String, String>();
                //FBmap.put("ClientID",ClientID);
                //FBmap.put("DRIVERNAME",name);
                //FBmap.put("DRIVERPHONE",phone);
                //FBmap.put("WORKINGAREA",workingArea);
                //FBmap.put("WORKINGHOURSFROM",hours_from);
                //FBmap.put("WORKINGHOURSTILL",hours_till);
                Cursor cursor = getClient();
                if(cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex("name");
                    int lngIndex = cursor.getColumnIndex("lng");
                    int latIndex = cursor.getColumnIndex("lat");
                    int phoneIndex = cursor.getColumnIndex("phone");
                    int addressIndex = cursor.getColumnIndex("address");

                    String namec = cursor.getString(nameIndex);
                    String lngc = cursor.getString(lngIndex);
                    String latc = cursor.getString(latIndex);
                    String phonec = cursor.getString(phoneIndex);
                    String addressc = cursor.getString(addressIndex);

                    FBmap.put("NAME",namec);
                    FBmap.put("LNG",lngc);
                    FBmap.put("LAT",latc);
                    FBmap.put("PHONE",phonec);
                    FBmap.put("ADDRESS",addressc);
                }

                String Dstr = String.valueOf(deliver);
                String Rstr = String.valueOf(repair);
                FBmap.put("DELIVER",Dstr);
                FBmap.put("REPAIR",Rstr);
                FBmap.put("STATUS","Pending");


                // Save the Order info + the Driver info the order is from
                mDataBaseRef.child("Orders").child(driverId).child(ClientID).setValue(FBmap);
                mDataBaseRef.child("Archive").child(driverId).child(ClientID).setValue(FBmap);
                isNull = false;
                //this is like a back up of the last order to kno the path for deleting
                LastOrderDBRef =FirebaseDatabase.getInstance().getReference().child("Orders").child(driverId).child(ClientID);
                LastOrderDBRefArchive=FirebaseDatabase.getInstance().getReference().child("Archive").child(driverId).child(ClientID);
                //StatusRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(driverId);

                /*contentValues.put(ORDERREF, LastOrderDBRef.toString());
                contentValues.put(ARCHIVEREF, LastOrderDBRefArchive.toString());
                db.insert(TABLE_ORDER, null, contentValues);*/

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


    public String OrderStatus(){    //TODO IF STATUS IS DONE REDIRECT !

       /* String archiveREF="";
        Cursor c = getOrder();
        if(c.moveToFirst()) {

            int ref_index = c.getColumnIndex("archive_ref");
            archiveREF = c.getString(ref_index);
            LastOrderDBRefArchive = archiveREF;
        }*/
        DatabaseReference DBrefArchive = FirebaseDatabase.getInstance().getReference();
        Cursor cursor = getOrder();
        if(cursor != null && cursor.moveToFirst()) {
            int DriverIDIndex = cursor.getColumnIndex("driverid");

            String OrderDriverID = cursor.getString(DriverIDIndex);
            DBrefArchive = FirebaseDatabase.getInstance().getReference().child("Archive").child(OrderDriverID).child(ClientID).child("STATUS");

        }

        try {
            //FireBase

            final DatabaseReference firebaseRef_Order = DBrefArchive;
            Log.d("After DB REF", "  ");
            firebaseRef_Order.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    Log.d("Snapshot", dataSnapshot.toString());

                    clientstatus = (String) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Firebase Error", databaseError.toString());
                }
            });

            Log.d("im Sending ",clientstatus);
            return clientstatus;
    } catch (Exception e) {
        Log.e("InsertDriverID", e.toString());
        return "Please Refresh";
    }
    }

    public void updateStatus(String orderstatus,long id){

        SQLiteDatabase db = this.getWritableDatabase();
        String StatusOrder = orderstatus;

        Log.d("I REACHED UPDATESTATUS", orderstatus+"  ");

        ContentValues args = new ContentValues();
        args.put(STATUS,StatusOrder);
        db.update(TABLE_ORDER, args, "_id" + "='" + id
                + "'", null);

    }
}
