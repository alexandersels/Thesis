package com.ugent.networkplanningtool.localisation.data;

import android.net.wifi.ScanResult;

import com.ugent.networkplanningtool.model.LocalisationModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 21/12/2015.
 * Class to handle data that can be used to calculate the location.
 */
public abstract class LocalisationData {

    protected HashMap<String, Double> rssiMap = new HashMap<String, Double>();
    protected LocalisationModel localisationModel;

    public LocalisationData(LocalisationModel localisationModel) {
        this.localisationModel = localisationModel;
    }

    /**
     * Fill the RSSI-map with the results passed on through the parameter. If need aggragate them using the proper approach.
     * @param results the values received from the wifiManager.
     */
    public abstract void fillRSSIMap(List<ScanResult> results);

    /**
     * Initialize the array to be used.
     * @return the array.
     */
    public abstract List<Double> createRSSIArray();

    /**
     * Clear the data from the array .
     */
    public abstract void clearRSSI();

}
