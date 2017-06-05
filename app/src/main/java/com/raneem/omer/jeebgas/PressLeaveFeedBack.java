package com.raneem.omer.jeebgas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class PressLeaveFeedBack extends AppCompatActivity {

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_leave_feed_back);

        db = new DBHelper(getApplicationContext());
        Cursor c = db.getOrder();


        if(c.moveToFirst()) {

            TextView tv_DriverName = (TextView) findViewById(R.id.tvdrivername);
            TextView tv_Servicetype = (TextView) findViewById(R.id.tvservicetype);

            int name_index = c.getColumnIndex("drivername");
            int deliver_index = c.getColumnIndex("servicetype_deliver");
            int repair_index = c.getColumnIndex("servicetype_repair");

            String name = c.getString(name_index);
            int deliver = c.getInt(deliver_index);
            int repair = c.getInt(repair_index);

            tv_DriverName.setText(name);
            if (repair == 1 && deliver == 1){
                tv_Servicetype.setText("Gas & Repair");
            }
            else if (repair == 1 && deliver == 0) {
                tv_Servicetype.setText("Repair");
            }
            else if (repair == 0 && deliver == 1) {
                tv_Servicetype.setText("Gas");
            }

        }
    }


    public void ClickSend(View v) {

        db = new DBHelper(getApplicationContext());
        Cursor c = db.getOrder();

        RatingBar StarRating = (RatingBar) findViewById(R.id.StarsRB);
        EditText UserComment = (EditText) findViewById(R.id.etcomment);


        if(c.moveToFirst()) {


            int name_index = c.getColumnIndex("drivername");
            int deliver_index = c.getColumnIndex("servicetype_deliver");
            int repair_index = c.getColumnIndex("servicetype_repair");
            int area_index = c.getColumnIndex("workingarea");

            String name = c.getString(name_index);
            String area = c.getString(area_index);
            int deliver = c.getInt(deliver_index);
            int repair = c.getInt(repair_index);
            float stars = StarRating.getRating();
            String comment = String.valueOf(UserComment.getText());

            Log.d("FeedBack Results  ", name +"  "+ area +"  "+ deliver +"  "+ repair +"  "+ stars +"  "+ comment);


            db.SaveFeedBack(name,area,deliver,repair,stars,comment);
        }

        db.empty_OrderTable();

        // toast ------> to save or update your info:
        Context context = getApplicationContext();
        CharSequence text = "Thank You For The FeedBack";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration) .show();



        Intent sendclicked = new Intent(this, MainActivity.class);
        startActivity(sendclicked);
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
