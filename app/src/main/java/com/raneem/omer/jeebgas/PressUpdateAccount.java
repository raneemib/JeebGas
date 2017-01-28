package com.raneem.omer.jeebgas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.raneem.omer.jeebgas.R.id.nametf;
import static com.raneem.omer.jeebgas.R.id.phonenumbertf;
import static com.raneem.omer.jeebgas.R.id.addresstf;


public class PressUpdateAccount extends AppCompatActivity {


    double latitude;
    double longitude;

    EditText nametf;
    EditText phonenumbertf;
    EditText addresstf;

    Button savebt;
    Button savelocationbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_press_updateaccount);


        nametf = (EditText) findViewById(R.id.nametf);
        addresstf = (EditText) findViewById(R.id.addresstf);
        phonenumbertf = (EditText) findViewById(R.id.phonenumbertf);

        savebt = (Button) findViewById(R.id.savebt);
        savelocationbt = (Button) findViewById(R.id.savelocationbt);


        JeebGasClient jeebGasClient = new JeebGasClient(getApplicationContext());
        nametf.setText(jeebGasClient.getName());
        phonenumbertf.setText(jeebGasClient.getPhone());



    }


// >>>>>>>>>>>>>>>>> START LOCATION TESTING <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public void ClickgetLocation(View v) {

        longitude = 0;
        latitude = 0;
        GetAddress getAddress = new GetAddress(longitude, latitude);
        getAddress.execute();
        Log.d("lng,lat","   " + longitude + "  " + latitude);
    }

// >>>>>>>>>>>>>>>>> END LOCATION TESTING <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




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
        jeebGasClient.setLng( Double.toString(longitude));
        jeebGasClient.setLat( Double.toString(latitude));
        jeebGasClient.setPhone(phonenumbertf.getText().toString());

        db.insertClient(jeebGasClient);
    }



    //*******************************************************
    // INNER ASYNCTASK CLASS ------- CONVERTS LAT,LNG TO ADDRESS
    //*******************************************************
    class GetAddress extends AsyncTask<Void, Void, String> {

        // Declaring variables
        private double lat;
        private double lng;
        private ProgressDialog progress;

        // Constructor for initializing variables
        public GetAddress(double lat, double lng) {

            this.lat = lat;
            this.lng = lng;
        }


        // Method that converts lat lng to address using GEOCODER,
        // returns list with 1 cell
        public List<Address> getFromLocation(double lat, double lng, int maxResult) throws IOException {

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(PressUpdateAccount.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, maxResult);

            return addresses;
        }

        // onPreExcecute : prepare progress dialog
        protected void onPreExecute() {

            progress = new ProgressDialog(PressUpdateAccount.this);
            progress.setMessage("Getting address");
            progress.setIndeterminate(true);
            progress.show();
        }

        // onackGround : get location
        @Override
        protected String doInBackground(Void... params) {
            String address_string = "";
            try {

                if (getFromLocation(lat, lng, 1).size() > 0) {

                    Address address = getFromLocation(lat, lng, 1).get(0);
                    System.out.println("Address : " + address); // Bilding the string to set in the textview
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        address_string += address.getAddressLine(i).toString() + ", ";
                    }
                    if (address_string.endsWith(", ")) {
                        address_string = address_string.substring(0, address_string.length() - 2);
                    }
                    address_string += ".";
                } else {
                    // if list is empty then write Unavailable
                    address_string = "Unavailable";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return address_string;
        }

        // onPost close progress dialog
        protected void onPostExecute(String content) {

            System.out.println("Content onPostExecute : " + content);
            addresstf.setText(content);
            progress.dismiss();
        }
    }

}
