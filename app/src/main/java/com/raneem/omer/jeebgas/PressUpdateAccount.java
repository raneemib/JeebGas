package com.raneem.omer.jeebgas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.raneem.omer.jeebgas.R.id.nametf;
import static com.raneem.omer.jeebgas.R.id.phonenumbertf;
import static com.raneem.omer.jeebgas.R.id.addresstf;


public class PressUpdateAccount extends AppCompatActivity {

    EditText nametf;
    EditText phonenumbertf;
    EditText addresstf;

    Button savebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_press_updateaccount);

        nametf = (EditText) findViewById(R.id.nametf);
        addresstf = (EditText) findViewById(R.id.addresstf);
        phonenumbertf = (EditText) findViewById(R.id.phonenumbertf);

        savebt = (Button) findViewById(R.id.savebt);


        JeebGasClient jeebGasClient = new JeebGasClient(getApplicationContext());
        nametf.setText(jeebGasClient.getName());
        addresstf.setText(jeebGasClient.getAddress());
        phonenumbertf.setText(jeebGasClient.getPhone());

    }



    //"Save" Button Functionality
    public void ClickSave(View v){

        SaveAccInfo();

        //Save Successfull toaste
        Context context = getApplicationContext();
        CharSequence text = "Your Info Have Been Saved";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context,text, duration).show();

        Intent saveclicked = new Intent(this,MainActivity.class);
        startActivity(saveclicked);
    }


    private void SaveAccInfo(){
        DBHelper db = new DBHelper(this);
        JeebGasClient jeebGasClient = new JeebGasClient(getApplicationContext());

        jeebGasClient.setName(nametf.getText().toString());
        jeebGasClient.setAddress(addresstf.getText().toString());
        jeebGasClient.setPhone(phonenumbertf.getText().toString());

        db.insertClient(jeebGasClient);
    }

}
