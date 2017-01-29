package com.raneem.omer.jeebgas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class PressUpdateAccount extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient googleApiClient;

    double latitude = 0;
    double longitude = 0;

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
        addresstf.setText(jeebGasClient.getAddress());
        phonenumbertf.setText(jeebGasClient.getPhone());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


    }


// >>>>>>>>>>>>>>>>> START LOCATION TESTING <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            Log.d("lng,lat #0","   " + longitude + "  " + latitude);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

// get and display the clients location
    public void ClickgetLocation(View v) {


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            Log.d("lng,lat #-1", "   " + latitude + "  " + longitude);

            try {
                longitude = lastLocation.getLongitude(); // get location
                latitude = lastLocation.getLatitude();
                Log.d("lng,lat #0", "   " + latitude + "  " + longitude);
            }catch (Exception e){
                Log.e("Get Location ", "Error");
            }

            GetAddress getAddress = new GetAddress(latitude, longitude);// translate and display it
            getAddress.execute();
            Log.d("lng,lat #1", "   " + latitude + "  " + longitude);
        }
    }
// >>>>>>>>>>>>>>>>> END LOCATION TESTING <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




    //"Save" Button Functionality
    public void ClickSave(View v){

        if(latitude ==0 && longitude == 0) {
            //Save location reminder toaste
            Context context = getApplicationContext();
            CharSequence text = "You Did Not Save Your Location                    (Click The Button Above)";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
            return;
        }

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
        jeebGasClient.setAddress(addresstf.getText().toString());

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
