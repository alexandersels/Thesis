package com.ugent.networkplanningtool.localisation.data;

import android.net.wifi.ScanResult;

import com.ugent.networkplanningtool.localisation.Fingerprint;
import com.ugent.networkplanningtool.localisation.io.XmlReader;
import com.ugent.networkplanningtool.model.LocalisationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 21/12/2015.
 * Class used to handle fingerprint data.
 */
public class FingerprintData extends LocalisationData {

    public FingerprintData(LocalisationModel model) {
        super(model);
    }

    @Override
    public void fillRSSIMap(List<ScanResult> results) {
        //Initialize the initial map
        List<String> nodes = localisationModel.getNodes();
        for (String node: nodes) {
            rssiMap.put(node, 1000.00);
        }

        //Add relevent measures
        for (ScanResult result: results) {
            if (nodes.contains(result.SSID)) {
                rssiMap.put(result.SSID, (double) -1 * result.level);
            }
        }
    }

    @Override
    public List<Double> createRSSIArray() {

        List<Double> rssiValues = new ArrayList<Double>();
        List<String> nodes = localisationModel.getNodes();
        for (String node : nodes) {
            double rssi = rssiMap.get(node);
            rssiValues.add(rssi);
        }
        return rssiValues;
    }

    @Override
    public void clearRSSI() {
        rssiMap = new HashMap<String, Double>();
    }

    public Fingerprint findBestMatch(List<Double> rssiValues) {
        XmlReader reader = new XmlReader();
        List<Fingerprint> fingerprints = reader.readFingerprintsFromXml();
        List<Double> measuredRssi = rssiValues;
        double distance = Double.MAX_VALUE;
        Fingerprint bestMatch = null;
        for (int i = 0; i < fingerprints.size(); i++) {
            double calculated_distance = fingerprints.get(i).calcDistance(measuredRssi);
            if (calculated_distance < distance) {
                distance = calculated_distance;
                bestMatch = fingerprints.get(i);
            }
        }
        return bestMatch;
    }
}
