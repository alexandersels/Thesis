package com.ugent.networkplanningtool.localisation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.localisation.Measure;

import java.util.List;

/**
 * Created by Alexander on 20/11/2015.
 * Class used as adapter to provide the user with information of the measures on the screen.
 */
public class MeasureAdapter extends ArrayAdapter<Measure> {

    private Context context;
    private int layoutResourceId;
    private List<Measure> data = null;


    public MeasureAdapter(Context context, int layoutResourceId, List<Measure> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        MeasureHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MeasureHolder();
            holder.followNumber    = (TextView)row.findViewById(R.id.followNumber);
            holder.currentLocation = (TextView)row.findViewById(R.id.currentLocation);
            holder.markedLocation  = (TextView)row.findViewById(R.id.markedLocation);
            holder.distance        = (TextView)row.findViewById(R.id.distanceBetween);

            row.setTag(holder);
        }
        else
        {
            holder = (MeasureHolder)row.getTag();
        }

        Measure measure = data.get(position);
        int x1 = measure.getCurrent().getPoint().x;
        int y1 = measure.getCurrent().getPoint().y;

        int x2 = measure.getMarked().getPoint().x;
        int y2 = measure.getMarked().getPoint().y;

        holder.followNumber.setText(position+1 + ".");
        holder.currentLocation.setText("Calculated coordinate: ("+ x1 + "," + y1 + ")");
        holder.markedLocation.setText("Marked coordinate: (" + x2 + "," + y2 + ")");
        holder.distance.setText("Distance: " + measure.getDistance());

        return row;
    }

    private static class MeasureHolder
    {
        TextView followNumber;
        TextView currentLocation;
        TextView markedLocation;
        TextView distance;
    }

}
