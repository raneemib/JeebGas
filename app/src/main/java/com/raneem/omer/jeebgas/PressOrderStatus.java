package com.raneem.omer.jeebgas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PressOrderStatus extends AppCompatActivity {

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_press_orderstatus_or_ordernow);
        db = new DBHelper(getApplicationContext());
        Cursor c = db.getOrder();
        db.online=true;

        if(c.moveToFirst()) {
            TextView tv_companyName = (TextView) findViewById(R.id.tv_companyName);
            TextView tv_price = (TextView) findViewById(R.id.tv_price);
            TextView tv_serviceType = (TextView) findViewById(R.id.tv_serviceType);
            TextView tv_workingArea = (TextView) findViewById(R.id.tv_workingArea);
            TextView tv_workingHours = (TextView) findViewById(R.id.tv_workingHours);
            //TextView tv_driverRating = (TextView) findViewById(R.id.tv_driverRating);
            TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
            TextView tv_orderStatus = (TextView) findViewById(R.id.tv_Status);

            //String orderstatus =db.OrderStatus();
            int id_index = c.getColumnIndex("_id");
            long orderid = c.getInt(id_index);
            Log.d("id befor ", String.valueOf(orderid));

            //db.updateStatus(orderstatus ,orderid);

            int name_index = c.getColumnIndex("drivername");
            int phone_index = c.getColumnIndex("driverphone");
            int area_index = c.getColumnIndex("workingarea");
            int workingfromIndex = c.getColumnIndex("workinghours_from");
            int workingtill_index = c.getColumnIndex("workinghours_till");
            int gasPrice_index = c.getColumnIndex("gasprice");
            int deliver_index = c.getColumnIndex("servicetype_deliver");
            int repair_index = c.getColumnIndex("servicetype_repair");
            //int rating_index = c.getColumnIndex("rating");
            int status_index = c.getColumnIndex("order_status");


            String name = c.getString(name_index);
            String phone = c.getString(phone_index);
            String area = c.getString(area_index);
            String workingfrom = c.getString(workingfromIndex);
            String workingtill = c.getString(workingtill_index);
            int gasPrice = c.getInt(gasPrice_index);
            int deliver = c.getInt(deliver_index);
            int repair = c.getInt(repair_index);
            //String rating = c.getString(rating_index);
            String status = c.getString(status_index);

            tv_companyName.setText(name);
            tv_price.setText(gasPrice + " NIS");
            if (repair == 1 && deliver == 1){
                tv_serviceType.setText("Gas & Repair");
            }
            else if (repair == 1 && deliver == 0) {
                tv_serviceType.setText("Repair");
            }
            else if (repair == 0 && deliver == 1) {
                tv_serviceType.setText("Gas");
            }

            tv_workingArea.setText(area);
            tv_workingHours.setText(workingfrom + " - " + workingtill);
            //tv_driverRating.setText(rating);
            tv_phone.setText(phone);


            if (status != null) {
                if (status.equals("Done")) {
                    Log.d("its DONE woo ", "  ");
                    Intent orderDone = new Intent(this, PressLeaveFeedBack.class);
                    startActivity(orderDone);
                }
            }

                Log.d("Status is ", status + "  ");
                tv_orderStatus.setText(status);

        }

    }


    public void onPause(){
        super.onPause();

        db.online=false;
    }

    public void onResume() {
        super.onResume();

        db.online=true;
    }



    public void ifStatusdone(View v){
        Intent feedbackclicked = new Intent(this,PressLeaveFeedBack.class);
        startActivity(feedbackclicked);
    }

    public void ClickCancelOrder(View v){
        //Cancel Order toaste
        db.empty_OrderTable();
        Context context = getApplicationContext();
        CharSequence text = "Your Order is Canceled";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context,text, duration).show();

        Intent cancelorderclicked = new Intent(this,PressJeebGasButton.class);
        startActivity(cancelorderclicked);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        Intent saveclicked = new Intent(this, MainActivity.class);
        startActivity(saveclicked);

        return;
    }
}
