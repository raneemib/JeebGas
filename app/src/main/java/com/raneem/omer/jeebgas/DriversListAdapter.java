package com.raneem.omer.jeebgas;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Omer on 1/14/2017.
 */

public class DriversListAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public DriversListAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.drivers_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        TextView driver_nameTV = (TextView) view.findViewById(R.id.driver_name);
        TextView driver_locationTV = (TextView) view.findViewById(R.id.driver_location);
        TextView driver_working_hoursTV = (TextView) view.findViewById(R.id.driver_working_hours);

        int nameIndex = cursor.getColumnIndex("drivername");
        int locationIndex = cursor.getColumnIndex("workingarea");
        int hoursIndex_from = cursor.getColumnIndex("workinghours_from");
        int hoursIndex_till = cursor.getColumnIndex("workinghours_till");

        String name = cursor.getString(nameIndex);
        String area = cursor.getString(locationIndex);
        String from = cursor.getString(hoursIndex_from);
        String till = cursor.getString(hoursIndex_till);

        Log.d("name", name);

        driver_nameTV.setText(name);
        driver_locationTV.setText(area);
        driver_working_hoursTV.setText(from + " - " + till);
    }
}
