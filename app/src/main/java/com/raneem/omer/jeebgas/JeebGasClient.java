package com.raneem.omer.jeebgas;


import android.content.Context;
import android.database.Cursor;

public class JeebGasClient {

    private static String name;
    private static String lng;
    private static String lat;
    private static String phone;



    private static String address;


    public JeebGasClient(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        Cursor cursor = dbHelper.getClient();
        if(cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int lngIndex = cursor.getColumnIndex("lng");
            int latIndex = cursor.getColumnIndex("lat");
            int phoneIndex = cursor.getColumnIndex("phone");
            int addressIndex = cursor.getColumnIndex("address");

            name = cursor.getString(nameIndex);
            lng = cursor.getString(lngIndex);
            lat = cursor.getString(latIndex);
            phone = cursor.getString(phoneIndex);
            address = cursor.getString(addressIndex);
        }
    }


    public static String getLng() {
        return lng;
    }

    public static void setLng(String lng) {
        JeebGasClient.lng = lng;
    }

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        JeebGasClient.lat = lat;
    }
    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        JeebGasClient.address = address;
    }

}
