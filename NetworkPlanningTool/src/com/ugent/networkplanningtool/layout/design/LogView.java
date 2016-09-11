package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.localisation.Measure;
import com.ugent.networkplanningtool.localisation.adapter.MeasureAdapter;

import java.util.List;

/**
 * The view for linking drawn access points with real device detected access points
 */
public class LogView extends LinearLayout {

    private ListView lv;
    private List<Measure> measures;

    /**
     * Constructor setting the list of detected access points
     * @param context the parents context
     * @param measures the list of detected access points
     */
    public LogView(Context context, List<Measure> measures) {
        super(context);
        this.measures = measures;
        init();
    }


    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.log_visualisation, this, true);

        lv = (ListView) findViewById(R.id.log_visualiser);
        MeasureAdapter adapter = new MeasureAdapter(LocalisationActivity.getInstance(),R.layout.log_visualisation_row,measures);
        lv.setAdapter(adapter);
    }
}
