package com.raneem.omer.jeebgas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PressLeaveFeedBack extends AppCompatActivity {

    DBHelper db;
    private Uri imageUri;
    private File file;
    private static final DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageRef;
    private String encodedImage="NaN";
    private String driverID;
    private String clientID;


    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_leave_feed_back);

        mStorageRef = FirebaseStorage.getInstance().getReference();
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

        ImageButton photoButton = (ImageButton) this.findViewById(R.id.cameraBT);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Toast.makeText(this, "Picture was Successfully taken", Toast.LENGTH_SHORT);

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d("photo bitmap ", String.valueOf(photo));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteFormat = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            Log.d("encodedImage Is ", encodedImage);
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


            driverID = db.getDriverID();
            clientID = db.getClientIDstring();
            mDataBaseRef.child("FeedBack").child(driverID).child(clientID).child("PHOTO").setValue(encodedImage);

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
