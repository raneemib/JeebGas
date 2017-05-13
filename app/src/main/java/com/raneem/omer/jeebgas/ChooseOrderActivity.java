package com.raneem.omer.jeebgas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChooseOrderActivity extends AppCompatActivity {

    DBHelper db;
    List<String> listviewservicetype;

    private String driverid;
    private String name;
    private String phone;
    private String area;
    private String workingfrom;
    private String workingtill;
    private int gassmall;
    private int gasbig;
    private int deliver;
    private int repair;
    private String service;
    private String selected;
    private Spinner spinner;
    private float rating;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_choose_order);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("id", -1);
        Log.d("Position Passed", id + "");

        if (id >= 0) {
            db = new DBHelper(getApplicationContext());
            Cursor c = db.getDriver(id);

            /*
             + DRIVERNAME + " text, "
            + DRIVERPHONE + " integer, "
            + WORKINGAREA + " text, "
            + WORKINGHOURS_FROM + " text, "
            + WORKINGHOURS_TILL + " text, "
            + GASPRICE_SMALL + " integer, "
            + GASPRICE_BIG + " integer, "
            + SERVICETYPE_DELIVER + " integer, "
            + SERVICETYPE_REPAIR + " integer)";
             */
            int driverid_index = c.getColumnIndex("driverid"); // doesnt display
            int name_index = c.getColumnIndex("drivername");
            int phone_index = c.getColumnIndex("driverphone");
            int area_index = c.getColumnIndex("workingarea");
            int workingfromIndex = c.getColumnIndex("workinghours_from");
            int workingtill_index = c.getColumnIndex("workinghours_till");
            int gassmall_index = c.getColumnIndex("gasprice_small");
            int gasbig_index = c.getColumnIndex("gasprice_big");
            int deliver_index = c.getColumnIndex("servicetype_deliver");
            int repair_index = c.getColumnIndex("servicetype_repair");
            //int rating_index = c.getColumnIndex("rating");
            if( c != null && c.moveToFirst() ) { // if the info is empty avoid crash
                driverid = c.getString(driverid_index); // doesnt display
                name = c.getString(name_index);
                phone = c.getString(phone_index);
                area = c.getString(area_index);
                workingfrom = c.getString(workingfromIndex);
                workingtill = c.getString(workingtill_index);
                gassmall = c.getInt(gassmall_index);
                gasbig = c.getInt(gasbig_index);
                deliver = c.getInt(deliver_index);
                repair = c.getInt(repair_index);
                //rating = c.getFloat(rating_index);
                service = "";
                selected="";

            Log.d("DriverID ",driverid);
            Log.d("name", name);
            Log.d("phone", phone);
            Log.d("deliver", deliver + "");
            }
            TextView tv_companyName = (TextView) findViewById(R.id.tv_companyName);
            TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
            TextView tv_price = (TextView) findViewById(R.id.tv_price);
            TextView tv_serviceType = (TextView) findViewById(R.id.tv_serviceType);
            TextView tv_workingArea = (TextView) findViewById(R.id.tv_workingArea);
            TextView tv_workingHours = (TextView) findViewById(R.id.tv_workingHours);
            //TextView tv_rating = (TextView) findViewById(R.id.tv_driverRating);
            Spinner sp_service = (Spinner) findViewById(R.id.spservicetype);
            tv_companyName.setText(name);
            tv_phone.setText(phone);
            //tv_price.setText("Small. " + gassmall + " - Big. " + gasbig);
            tv_price.setText(gassmall + " NIS");
            /*if (repair == 1) {
                service += "Repair   ";
            }
            if (deliver == 1) {
                service += "Deliver";
            }*/
            //tv_serviceType.setText(service);
            String[] serviceArray = null;
            if (repair == 1 && deliver == 1){
                serviceArray= new String[]{"Gas & Repair","Repair","Gas"};
            }
            else if (repair == 1 && deliver == 0) {
                serviceArray= new String[]{"Repair"};
            }
            else if (repair == 0 && deliver == 1) {
                serviceArray= new String[]{"Gas"};
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.activity_listview, serviceArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_service.setAdapter(adapter);
            tv_workingArea.setText(area);
            tv_workingHours.setText(workingfrom + " - " + workingtill);
            spinner=sp_service;
        }
    }

    /*private void prepareListData() {
        expandservicetype = new ArrayList<String>();

        // Adding child data
        expandservicetype.add("Gas");
        expandservicetype.add("Repair");
        expandservicetype.add("Gas & Repair");
    }*/


    public void ClickOrderNow(View v) {
        selected=spinner.getSelectedItem().toString();
        Log.d("SpinnerSelect ",selected);
        if (selected.equals("Gas & Repair")){
            repair =1;
            deliver = 1;
        }
        else if (selected.equals("Repair")) {
            repair =1;
            deliver = 0;
        }
        else if (selected.equals("Gas")) {
            repair =0;
            deliver = 1;
        }
        Log.d("repair is now  ", String.valueOf((repair)));
        Log.d("deliver is now ", String.valueOf(deliver));
        db.empty_OrderTable();
        Log.d("Order Now  ", driverid + "");
        // driverid is null to avoid changing or deleting it instead we skip it.
        db.insertOrder( driverid, name, phone, area, workingfrom, workingtill, gassmall, deliver, repair, rating,"Pending");
        Intent ordernowclicked = new Intent(this, PressOrderStatus.class);
        startActivity(ordernowclicked);
        finish();
    }
}
