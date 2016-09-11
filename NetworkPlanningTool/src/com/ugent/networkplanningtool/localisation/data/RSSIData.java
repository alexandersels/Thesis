package com.ugent.networkplanningtool.localisation.data;

import android.net.wifi.ScanResult;
import android.os.Handler;
import android.widget.Toast;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.model.LocalisationModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 21/12/2015.
 * Class used to handle RSSI-data.
 */
public class RSSIData extends LocalisationData {

    public RSSIData(LocalisationModel model) {
        super(model);
    }

    @Override
    public void fillRSSIMap(List<ScanResult> results) {

        //Create Map of the new measures
        HashMap<String, Double> newMeasures = new HashMap<>();

        //Set all current measures to 100
        List<String> nodes = localisationModel.getNodes();
        for (String node : nodes) {
            newMeasures.put(node, 100.00);
        }

        //Fill in the actual measures
        for (ScanResult result : results) {
            if (nodes.contains(result.SSID)) {
                    double measure = -1 * result.level;
                    //If the calibration value should be used, add it here
                    if (localisationModel.useCalibration()) {
                        //No point in adding bias to invalid signal
                        if (measure != 100) {
                            measure = measure + localisationModel.getCalibration();
                        }
                    }
                    newMeasures.put(result.SSID, measure);
            }
        }

        final Toast toast = Toast.makeText(LocalisationActivity.getInstance(), "Scan (" + localisationModel.getCounter() + "/" + localisationModel.getMaxScans() + ") done", Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 800);

        for (String node : newMeasures.keySet()) {
            double current = newMeasures.get(node);
            //On the first iteration, add the current value
            if (localisationModel.getCounter() == 1) {
                rssiMap.put(node, current);
            } else {
                //Smoothen the current rssi values
                double previous = rssiMap.get(node);
                if (localisationModel.useSmoothing()) {
                    int difference = (int) Math.abs(previous - current);
                    double weight = localisationModel.getWeights()[difference];
                    double smoothened = current * weight + (1 - weight) * previous;
                    rssiMap.put(node, smoothened);
                }
                //When not smoothing, we are averaging the result
                else {
                    rssiMap.put(node, previous + current);
                    if (localisationModel.getCounter() == localisationModel.getMaxScans()) {
                        rssiMap.put(node, rssiMap.get(node) / localisationModel.getMaxScans());
                    }
                }
            }
        }
        localisationModel.incrementCounter();
    }

    @Override
    public List<Double> createRSSIArray() {

        //String[] allowedNodes = {"inode033", "inode030", "inode046", "inode025", "inode015"};
        //String[] allowedNodes = {"inode033", "inode030", "inode046", "inode025", "inode015", "inode043", "inode045", "inode054", "inode029", "inode021"};
        //String[] allowedNodes = {"inode033", "inode030", "inode046", "inode025", "inode015", "inode043", "inode045", "inode054", "inode029", "inode021",
        //        "inode036", "inode040", "inode053", "inode050", "inode020"};
        //String[] allowedNodes = {"inode033", "inode030", "inode046", "inode025", "inode015", "inode043", "inode045", "inode054", "inode029", "inode021",
        //        "inode036", "inode040", "inode053", "inode050", "inode020", "inode041", "inode049", "inode031", "inode027", "inode012"};
        //String[] allowedNodes = {"inode033", "inode030", "inode046", "inode025", "inode015", "inode043", "inode045", "inode054", "inode029", "inode021",
        //        "inode036", "inode040", "inode053", "inode050", "inode020", "inode041", "inode049", "inode031", "inode027", "inode012","inode047", "inode042",
        //        "inode023", "inode028", "inode008"};
        //String[] allowedNodes = {"inode033", "inode030", "inode046", "inode025", "inode015", "inode043", "inode045", "inode054", "inode029", "inode021",
        //        "inode036", "inode040", "inode053", "inode050", "inode020", "inode041", "inode049", "inode031", "inode027", "inode012","inode047", "inode042",
        //        "inode023", "inode028", "inode008","inode044", "inode052", "inode022", "inode017", "inode009"};
        //ArrayList<String> test = new ArrayList<>();

        //for(String node: allowedNodes) {
        //    test.add(node);
        //}


        List<Double> rssiValues = new ArrayList<Double>();
        List<String> nodes = localisationModel.getNodes();
        for (String node : nodes) {
            if (rssiMap.containsKey(node)) {
                double rssi = rssiMap.get(node);
                //100 means invalid signal
                if (rssi == 100.00) {
                    rssi = 1000.00;
                }
                rssiValues.add(rssi);
            }
        }
        printMap(rssiValues);
        return rssiValues;
    }

    @Override
    public void clearRSSI() {
        rssiMap = new HashMap<String, Double>();
    }

    private void printMap(List<Double> rssiValues) {
        List<String> nodes = localisationModel.getNodes();

        System.out.println("Using calibration:" + localisationModel.useCalibration());
        if (localisationModel.useCalibration()) {
            System.out.println("Calibration value: " + localisationModel.getCalibration());
        }

        System.out.println("Amount of scans: " + localisationModel.getMaxScans());
        if (localisationModel.getMaxScans() > 1) {
            if (localisationModel.useSmoothing())
                System.out.println("Using smoothening");
            else
                System.out.println("Using averaging");
        }

        int counter = 0;
        for (String node : nodes) {
            System.out.println("Node: " + node + " with rssi: " + rssiValues.get(counter));
            counter++;
        }
    }
}

