package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.model.LocalisationModel;

/**
 * Created by Alexander on 11/10/2015.
 */
public class FingerprintView extends LocalisationObjectView {

    private LocalisationModel localisationModel;
    private TextView locationText;
    private TextView coordinateText;


    /**
     * Constructor to create an instance containing the given data.
     *
     * @param context the context of the parent
     */
    public FingerprintView(Context context) {
        super(context);
        this.localisationModel = LocalisationModel.INSTANCE;
        setTag("fingerprint");
        init();
        initComponents();
    }

    /**
     * Default constructor
     *
     * @param context the context of the parent
     * @param attrs   the attribute set
     */
    public FingerprintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTag("fingerprint");
        this.localisationModel = LocalisationModel.INSTANCE;
        locationText = (TextView) findViewById(R.id.locationText);
        coordinateText = (TextView) findViewById(R.id.coordinatesTextView);
        init();
        initComponents();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fingerprint_view, this, true);
        setOrientation(VERTICAL);

        CheckBox box = (CheckBox) findViewById(R.id.fingerprintBox);
        box.setChecked(localisationModel.drawMarkedLocationAllowed());
        box.setText("Mark on map");

        box = (CheckBox) findViewById(R.id.showNodesBox);
        box.setChecked(localisationModel.drawNodes());
        box.setText("Show nodes");

    }

    private void initComponents() {

        CheckBox box = (CheckBox) findViewById(R.id.fingerprintBox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localisationModel.setDrawMarkedLocation(isChecked);
                localisationModel.refreshView();
                if (!isChecked) {
                    localisationModel.setMarkedLocation(null);
                }
            }
        });

        box = (CheckBox) findViewById(R.id.showNodesBox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localisationModel.setDrawNodes(isChecked);
                localisationModel.refreshView();
            }
        });
    }

    public void updateCheckBox() {
        CheckBox box = (CheckBox) findViewById(R.id.fingerprintBox);
        box.setChecked(localisationModel.drawMarkedLocationAllowed());

        box = (CheckBox) findViewById(R.id.showNodesBox);
        box.setChecked(localisationModel.drawNodes());
    }
}
