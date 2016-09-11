package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.model.LocalisationModel;
import com.ugent.networkplanningtool.utils.UniqueIdGenerator;

/**
 * Created by Alexander on 11/10/2015.
 */
public class LocateView extends LocalisationObjectView {

    private RadioGroup locationGroup;
    private RadioGroup localisationOption;

    private LocalisationModel localisationModel;

    /**
     * Constructor to create an instance containing the given data.
     *
     * @param context the context of the parent
     */
    public LocateView(Context context) {
        super(context);
        setTag("localisation");
        localisationModel = LocalisationModel.INSTANCE;
        init();
        initComponents();
    }

    /**
     * Default constructor
     *
     * @param context the context of the parent
     * @param attrs   the attribute set
     */
    public LocateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTag("localisation");
        localisationModel = LocalisationModel.INSTANCE;
        init();
        initComponents();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.location_view, this, true);
        setOrientation(VERTICAL);

        locationGroup = (RadioGroup) findViewById(R.id.localisationGroup);

        //Create the localisation options
        RadioButton button;
        String[] options = new String[]{"Current location", "Show history"};
        for (String option : options) {
            button = new RadioButton(getContext());
            button.setText(option);
            button.setTag(option);
            button.setId(UniqueIdGenerator.generateViewId());
            locationGroup.addView(button);
        }
        if ( localisationModel.drawPreviousLocations()) {
            button = (RadioButton) locationGroup.getChildAt(1);
        }
        else {
            button = (RadioButton) locationGroup.getChildAt(0);
        }
        button.setChecked(true);

        localisationOption = (RadioGroup) findViewById(R.id.localisationOptionGroup);
        options = new String[]{"Smoothening", "Averaging"};
        for (String option: options) {
            button = new RadioButton(getContext());
            button.setText(option);
            button.setTag(option);
            button.setId(UniqueIdGenerator.generateViewId());
            localisationOption.addView(button);
        }
        if (localisationModel.useSmoothing()) {
            button = (RadioButton) localisationOption.getChildAt(0);
        }
        else {
            button = (RadioButton) localisationOption.getChildAt(1);
        }
        button.setChecked(true);

        boolean setEnabled = localisationModel.getMaxScans() > 1 ? true : false;
        for (int i = 0; i < localisationOption.getChildCount(); i++) {
            localisationOption.getChildAt(i).setEnabled(setEnabled);
        }


        String useFingerprint = "Use Fingerprint";
        CheckBox box = (CheckBox) findViewById(R.id.useFingerprintCheckbox);
        box.setText(useFingerprint);
        box.setTag(useFingerprint);
        box.setChecked(localisationModel.locateUsingFingerprint());

        //Create the kalibration options
        String kalibration = "Use Calibration";
        box = (CheckBox) findViewById(R.id.calibrationButton);
        box.setText(kalibration);
        box.setTag(kalibration);
        box.setChecked(localisationModel.useCalibration());

        //Initialize the seekbar
        SeekBar bar = (SeekBar) findViewById(R.id.scanSlider);
        bar.setMax(10);
        bar.setProgress(localisationModel.getMaxScans());

        //Initialize the text used by seekbar
        TextView amountOfScans = (TextView) findViewById(R.id.amountOfScansText);
        amountOfScans.setText("Amount of scans: " + localisationModel.getMaxScans());

    }

    private void initComponents() {
        initFingerprintComponents();
        initCalibrationComponents();
        initLocalisationComponents();
        initSliderComponents();
    }

    private void initFingerprintComponents() {
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                CheckBox button = (CheckBox) v;
                if (button.isChecked()) {
                    localisationModel.setLocateUsingFingerprint(true);
                } else {
                    localisationModel.setLocateUsingFingerprint(false);
                }
            }
        };
        CheckBox button = (CheckBox) findViewById(R.id.useFingerprintCheckbox);
        button.setOnClickListener(listener);
    }

    private void initLocalisationComponents() {
        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.indexOfChild(findViewById(checkedId));
                if (index == 0) {
                    //only draw current location
                    localisationModel.setDrawPreviousLocations(false);
                } else {
                    localisationModel.setDrawPreviousLocations(true);
                }
            }
        };
        locationGroup.setOnCheckedChangeListener(listener);

        listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.indexOfChild(findViewById(checkedId));
                if (index == 0) {
                    localisationModel.setSmoothing(true);
                }
                else {
                    localisationModel.setSmoothing(false);
                }
            }
        };
        localisationOption.setOnCheckedChangeListener(listener);
    }

    private void initCalibrationComponents() {
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                CheckBox button = (CheckBox) v;
                if (button.isChecked()) {
                    localisationModel.setUseCalibration(true);
                } else {
                    localisationModel.setUseCalibration(false);
                }
            }
        };
        CheckBox button = (CheckBox) findViewById(R.id.calibrationButton);
        button.setOnClickListener(listener);
    }

    private void initSliderComponents() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            TextView amountOfScans = (TextView) findViewById(R.id.amountOfScansText);
            RadioGroup group = (RadioGroup) findViewById(R.id.localisationOptionGroup);
            int scans;
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                scans = progress == 0 ? 1 : progress;
                amountOfScans.setText("Amount of scans: " + scans);
                boolean setEnabled = scans > 1 ? true : false;
                for (int i = 0; i < localisationOption.getChildCount(); i++) {
                    localisationOption.getChildAt(i).setEnabled(setEnabled);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                scans = seekBar.getProgress() == 0 ? 1 : seekBar.getProgress();
                amountOfScans.setText("Amount of scans: " + scans);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                scans = seekBar.getProgress() == 0 ? 1 : seekBar.getProgress();
                amountOfScans.setText("Amount of scans: " + scans);
                localisationModel.setMaxScans(scans);
                boolean setEnabled = localisationModel.getMaxScans() > 1 ? true : false;
                for (int i = 0; i < localisationOption.getChildCount(); i++) {
                    localisationOption.getChildAt(i).setEnabled(setEnabled);
                }
            }
        };
        SeekBar bar = (SeekBar) findViewById(R.id.scanSlider);
        bar.setOnSeekBarChangeListener(listener);
    }
}