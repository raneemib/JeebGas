package com.raneem.omer.jeebgas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //  MockData OBJECT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopService();
        startService();

    }

    public void startService() {
        startService(new Intent(getBaseContext(), NotificaitonService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), NotificaitonService.class));
    }



    public void ClickJeebGas(View v){
        // do somthing when the "jeebgas" button is clicked go to pressjeebgas activity
        Intent jeebgasclicked = new Intent(this,PressJeebGasButton.class);
        startActivity(jeebgasclicked);
    }

    public void ClickOrderStatus(View v){
        Intent orderstatusclicked = new Intent(this,PressOrderStatus.class);
        startActivity(orderstatusclicked);
    }

    public void ClickUpdateAccount(View v){
        Intent updateaccountclicked = new Intent(this,PressUpdateAccount.class);
        startActivity(updateaccountclicked);
    }


   public void onDestroy() {

       super.onDestroy();
       stopService();

   }
}
