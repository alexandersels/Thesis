package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.model.LocalisationModel;

/**
 * Created by Alexander on 11/10/2015.
 */
public class MeasureView extends LocalisationObjectView {

    private RadioGroup navigationGroup;

    private LocalisationModel localisationModel;

    /**
     * Constructor to create an instance containing the given data.
     *
     * @param context      the context of the parent
     */
    public MeasureView(Context context) {
        super(context);
        this.localisationModel = LocalisationModel.INSTANCE;
        setTag("measure");
        init();
        initComponents();
    }

    /**
     * Default constructor
     *
     * @param context the context of the parent
     * @param attrs   the attribute set
     */
    public MeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.localisationModel = LocalisationModel.INSTANCE;
        setTag("measure");
        init();
        initComponents();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.measure_view, this, true);
        setOrientation(VERTICAL);

        EditText text = (EditText) findViewById(R.id.measureBox);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        text.setText(LocalisationModel.INSTANCE.getMeasureFileName());

        loadText();

    }

    private void initComponents() {
        EditText text = (EditText) findViewById(R.id.measureBox);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                localisationModel.setMeasureFileName(s.toString());
            }
        });
    }

    public void loadText() {
        TextView parameters = (TextView) findViewById(R.id.parametersValue);
        String values = "Calibration: " + localisationModel.useCalibration() + "\n" +
                "Scans: " + localisationModel.getMaxScans() + "\n" +
                "Fingerprint: " + localisationModel.locateUsingFingerprint() + "\n";
        parameters.setText(values);
    }
}
