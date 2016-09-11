package com.ugent.networkplanningtool.localisation.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.ugent.networkplanningtool.model.StepModel;

/**
 * Created by Alexander on 5/01/2016.
 * Steplistener will implement the SensorEvent listener and filter on accelerometer data.
 */
public class StepListener implements SensorEventListener {

    private StepModel model;

    public StepListener(StepModel model) {
        this.model = model;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            model.incrementCounter();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
