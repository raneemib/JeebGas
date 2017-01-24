package com.raneem.omer.jeebgas;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ChooseOrderActivity extends AppCompatActivity {

    DBHelper db;

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
            int name_index = c.getColumnIndex("drivername");
            int phone_index = c.getColumnIndex("driverphone");
            int area_index = c.getColumnIndex("workingarea");
            int workingfromIndex = c.getColumnIndex("workinghours_from");
            int workingtill_index = c.getColumnIndex("workinghours_till");
            int gassmall_index = c.getColumnIndex("gasprice_small");
            int gasbig_index = c.getColumnIndex("gasprice_big");
            int deliver_index = c.getColumnIndex("servicetype_deliver");
            int repair_index = c.getColumnIndex("servicetype_repair");
            int rating_index = c.getColumnIndex("rating");

            name = c.getString(name_index);
            phone = c.getString(phone_index);
            area = c.getString(area_index);
            workingfrom = c.getString(workingfromIndex);
            workingtill = c.getString(workingtill_index);
            gassmall = c.getInt(gassmall_index);
            gasbig = c.getInt(gasbig_index);
            deliver = c.getInt(deliver_index);
            repair = c.getInt(repair_index);
            rating = c.getFloat(rating_index);
            service = "";

            Log.d("name", name);
            Log.d("phone", phone);
            Log.d("deliver", deliver + "");

            TextView tv_companyName = (TextView) findViewById(R.id.tv_companyName);
            TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
            TextView tv_price = (TextView) findViewById(R.id.tv_price);
            TextView tv_serviceType = (TextView) findViewById(R.id.tv_serviceType);
            TextView tv_workingArea = (TextView) findViewById(R.id.tv_workingArea);
            TextView tv_workingHours = (TextView) findViewById(R.id.tv_workingHours);
            TextView tv_rating = (TextView) findViewById(R.id.tv_driverRating);

            tv_companyName.setText(name);
            tv_phone.setText(phone);
            //tv_price.setText("Small. " + gassmall + " - Big. " + gasbig);
            tv_price.setText(gassmall + " NIS");
            if (repair == 1) {
                service += "Repair   ";
            }
            if (deliver == 1) {
                service += "Deliver";
            }
            tv_serviceType.setText(service);
            tv_workingArea.setText(area);
            tv_workingHours.setText(workingfrom + " - " + workingtill);
            tv_rating.setText(rating + "");
        }
    }

    public void ClickOrderNow(View v) {
        db.empty_OrderTable();
        Intent intent = getIntent();
        Long id = intent.getLongExtra("id", -1);
        String driverid = db.getDriver(id).toString();
        Log.d("Order Now", id + "");
        // driverid is null to avoid changing or deleting it instead we skip it.
        db.insertOrder(driverid,name, phone, area, workingfrom, workingtill, gassmall, deliver, repair, rating);
        Intent ordernowclicked = new Intent(this, PressOrderStatus.class);
        startActivity(ordernowclicked);
        finish();
    }
}
