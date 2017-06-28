package com.raneem.omer.jeebgas;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NotificaitonService extends Service {

    private DBHelper db;
    private NotificaitonService ns;
    private String curStatus;
    private Map<String, Map<String, String>> Order_Hashmap;
    private boolean first = false;


    public NotificaitonService() {
        Log.d("testing", "OrderService");



    }


    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;

    /**
     * interface for clients that bind
     */
    IBinder mBinder;

    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        Log.d("testing", "onCreate");
        db = new DBHelper(getApplicationContext());
        DatabaseReference DBrefArchive;

        String OrderDriverID = " ";
        OrderDriverID = db.getDriverID();
        String ClientID = " ";
        ClientID = db.getClientIDstring();


        //try catch ! needed
        DBrefArchive = FirebaseDatabase.getInstance().getReference().child("Archive").child(OrderDriverID).child(ClientID).child("STATUS");


        try {
            //FireBase

            final DatabaseReference firebaseRef_Order = DBrefArchive;
            Log.d("After DB REF", "  ");
            final String finalOrderDriverID = OrderDriverID;
            firebaseRef_Order.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    Log.d("Service Snapshot", dataSnapshot.toString());

                    curStatus = (String) dataSnapshot.getValue();
                    Log.d("Service Status ", "  "+curStatus);
                    db.updateStatus(curStatus, finalOrderDriverID);
                    if(first)
                        SendNotificaitons(curStatus);
                    first = true;
                    if(db.isOnline()){
                        Intent intent = new Intent(getApplicationContext(),PressOrderStatus.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Firebase Error", databaseError.toString());
                }
            });

        } catch (Exception e) {
            Log.e("Notify Service", e.toString());
        }
    }




    public void SendNotificaitons(String state){

        if(TextUtils.isEmpty(state))
            return;

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        Notification mBuilder = new Notification.Builder(this)

                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("JeebGas")
                .setContentText("Your Order Status Updated To :" + state)
                .getNotification();
        mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.defaults |= Notification.DEFAULT_SOUND;
        mNotificationManager.notify(1, mBuilder);
    }


    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("testing", "onStartCommand");
        // Let it continue running until it is stopped.
        return START_STICKY;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {


    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
