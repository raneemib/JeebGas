package com.raneem.omer.jeebgas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PressJeebGasButton extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lv;
    private DBHelper db; // DBHelper objecr
    private DriversListAdapter driversListAdapter;

    private Map<String, Map<String, String>> drivers_hashmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_press_jeebgas_button);

        lv = (ListView) findViewById(R.id.listView);
        db = new DBHelper(getApplicationContext()); // initialize DBHelper object





        //FireBase
        final DatabaseReference firebaseRef_Driver =  FirebaseDatabase.getInstance().getReference().child("Driver");
        drivers_hashmap = new HashMap<>();
        firebaseRef_Driver.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("Snapshot", dataSnapshot.toString());
                drivers_hashmap = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                Set<String> keys = drivers_hashmap.keySet();
                for (String i : keys) {

                    String driverid = i;
                    String drivername = drivers_hashmap.get(i).get("DRIVERNAME");
                    String driverphone = drivers_hashmap.get(i).get("DRIVERPHONE");
                    String workingarea = drivers_hashmap.get(i).get("WORKINGAREA");
                    String workinghoursfrom = drivers_hashmap.get(i).get("WORKINGHOURSFROM");
                    String workinghourstill = drivers_hashmap.get(i).get("WORKINGHOURSTILL");

                    // int deliver = Integer.parseInt(drivers_hashmap.get(i).get("DELIVER"));
                    // String repair = drivers_hashmap.get(i).get("REPAIR");
                    // String gassmall = drivers_hashmap.get(i).get("GASSMALL");
                    // String gasbig = drivers_hashmap.get(i).get("GASBIG");
                    /*
                    int deliverINT = 0;
                    int repairINT = 0;
                    int gassmallINT = 0;
                    int gasbigINT = 0;
                    Log.d("Key",i + "  " + drivername);


                    try {
                        deliverINT = Integer.parseInt(deliver);
                        repairINT = Integer.parseInt(repair);
                        gassmallINT = Integer.parseInt(gassmall);
                        gasbigINT = Integer.parseInt(gasbig);

                    } catch(NumberFormatException nfe) {
                        Log.e("JeebGasButton.onCreate",i + "  " + drivername);
                    }*/
                    db.insertDriver( driverid, drivername, driverphone, workingarea, workinghoursfrom, workinghourstill,
                            10, 20, 0, 1,0);//TODO Disable Rating to Avoid Shaming

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.d("Firebase Error", databaseError.toString());
            }
        });
        // Firebase END



        //show drivers names in the list view
        Cursor c = db.getDriversList();
        driversListAdapter = new DriversListAdapter(getApplicationContext(), c);
        lv.setAdapter(driversListAdapter);
        lv.setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getApplicationContext(), ChooseOrderActivity.class);
        Log.d("position", position + "");
        Log.d("id", id + "");
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
