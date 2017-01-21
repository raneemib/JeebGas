package com.raneem.omer.jeebgas;


import android.content.Context;
import android.database.Cursor;

public class JeebGasClient {

    private static String name;
    private static String address;
    private static String phone;


    public JeebGasClient(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        Cursor cursor = dbHelper.getClient();
        if(cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int addressIndex = cursor.getColumnIndex("address");
            int phoneIndex = cursor.getColumnIndex("phone");

            name = cursor.getString(nameIndex);
            address = cursor.getString(addressIndex);
            phone = cursor.getString(phoneIndex);
        }
    }

    public static String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

}
