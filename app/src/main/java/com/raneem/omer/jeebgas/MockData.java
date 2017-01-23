package com.raneem.omer.jeebgas;

import android.content.Context;

/**
 * Created by Omer on 1/13/2017.
 * This Class Contains All The MetaData That Will Be Used For Testing
 * Untill The Data Become Dynamic From The SERVER!!!
 */

public class MockData {

    private DBHelper db;
    private Context context;

    public MockData(Context context){
        this.context = context;
        db = new DBHelper(context);
    }

    public void populateMockData() {
        db.emptyDriverTable();
        db.insertDriver_mock("214923159fewjfjw","John", "972541238631", "Jerusalem", "8:00", "17:00", 100, 200, 1, 0, 4.5f);
        db.insertDriver_mock("qj23j53fm32i8f23","Jack", "972525684326", "Jerusalem", "8:00", "15:00", 80, 180, 1, 1, 4.0f);
        db.insertDriver_mock("T3wgfg23325tfg","Adam", "972549973421", "Jerusalem", "12:00", "19:00", 100, 220, 1, 0, 4.5f);
        db.insertDriver_mock("fakeidFGb3462DF4g54g","Bill", "972547684452", "Jerusalem", "8:00", "17:00", 120, 250, 1, 0, 3.0f);
    }
}
