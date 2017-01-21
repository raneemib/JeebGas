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

import java.util.ArrayList;

public class PressJeebGasButton extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lv;
    private DBHelper db; // DBHelper objecr
    private DriversListAdapter driversListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_press_jeebgas_button);

        lv = (ListView) findViewById(R.id.listView);
        db = new DBHelper(getApplicationContext()); // initialize DBHelper object

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
