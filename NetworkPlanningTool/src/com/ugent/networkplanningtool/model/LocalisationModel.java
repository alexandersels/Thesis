package com.ugent.networkplanningtool.model;

import android.net.wifi.ScanResult;
import android.widget.Toast;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.data.Location;
import com.ugent.networkplanningtool.data.LocationObject;
import com.ugent.networkplanningtool.data.enums.LocationType;
import com.ugent.networkplanningtool.localisation.Fingerprint;
import com.ugent.networkplanningtool.data.Node;
import com.ugent.networkplanningtool.localisation.data.FingerprintData;
import com.ugent.networkplanningtool.localisation.data.RSSIData;
import com.ugent.networkplanningtool.localisation.io.XmlWriter;
import com.ugent.networkplanningtool.localisation.io.NodeReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import be.ugent.intec.wica.deus.model.GridPoint;
import be.ugent.intec.wica.viterbi.Lokalisatie;

/**
 * Created by Alexander on 10/10/2015.
 * Model containing functions to perform localisation, fingerprinting and measuring.
 */
public class LocalisationModel extends Observable {

    private Lokalisatie lokalisatie;

    public static LocalisationModel INSTANCE = new LocalisationModel();
    private StepModel stepModel = StepModel.INSTANCE;

    private List<String> nodes;
    private List<Location> oldLocations;
    private List<Node> nodeObjects;
    private double[] weight_interval = new double[101];

    private RSSIData rssiData;
    private FingerprintData fingerprintData;

    private Location currentLocation;
    private Location markedLocation;

    private boolean firstMeasure = true;

    private boolean drawPreviousLocations = false;
    private boolean useCalibration = true;
    private boolean locateUsingFingerprint = false;

    private boolean localisationMode = true;
    private boolean drawMarkedLocation = false;
    private boolean fingerPrintMode = false;
    private boolean measureMode = false;
    private boolean smoothing = true;
    private boolean drawNodes = false;

    private String navigateTo ="None";

    private int counter = 1;
    private int maxScans = 1;
    private double calibration = -11.77;

    private String measureFileName = "";

    private XmlWriter xmlWriter = new XmlWriter();

    public LocalisationModel() {

        NodeReader reader = new NodeReader();
        nodes = reader.getNodes();

        nodeObjects = reader.getNodeObjects();
        System.out.println("Nodes created:" + nodeObjects.size());

        oldLocations = new ArrayList<Location>();
        createSmoothnessInterval();

        rssiData = new RSSIData(this);
        fingerprintData = new FingerprintData(this);

    }

    /**
     * Fill the RSSIMap with results of the wifiScanner.
     * @param results of the wifi-scan
     */
    public void fillRssiMap(List<ScanResult> results) {
        if (isLocalisationMode()) {
            rssiData.fillRSSIMap(results);
        }
        if (isFingerPrintMode()) {
            fingerprintData.fillRSSIMap(results);
        }
    }

    /**
     * Calculate the current location of the device based on the rssiMap.
     */
    public void calculateLocation() {
        Location current = null;
        if (!locateUsingFingerprint) {

            lokalisatie.aantalAPs = 5;

            /** Using Path building **/
            if (firstMeasure) {
                List<List<Double>> rssiValues = new ArrayList<List<Double>>();
                rssiValues.add(rssiData.createRSSIArray());
                lokalisatie.getPaden().add(Lokalisatie.bepaalBestePad(rssiValues));
                firstMeasure = false;
            } else {
                /** Adaptive speed **/
                stepModel.setParameters(lokalisatie);
                Lokalisatie.bepaalBestePad(lokalisatie.getPaden(), lokalisatie.getLink(), lokalisatie.getBesteDist(), rssiData.createRSSIArray(), lokalisatie.getPadVersprongen(), null, 0);

                /** Static speed
                 * stepModel.setStaticParameters(lokalisatie);
                 Lokalisatie.bepaalBestePad(lokalisatie.getPaden(), lokalisatie.getLink(), lokalisatie.getBesteDist(), rssiData.createRSSIArray(), lokalisatie.getPadVersprongen(), null, 0);
                 */
            }

            /** Using Euclidian
             List<List<Double>> rssiValues = new ArrayList<List<Double>>();
             rssiValues.add(rssiData.createRSSIArray());
             lokalisatie.getPaden().add(Lokalisatie.bepaalBestePad(rssiValues));
             **/

            int last = lokalisatie.getPaden().size() - 1;
            List<GridPoint> point = lokalisatie.getPaden().get(last);
            Double x = point.get(0).getX();
            Double y = point.get(0).getY();

            current = new Location(LocationType.CURRENT, x.intValue(), y.intValue());
            stepModel.resetSteps();
        } else {
            Fingerprint bestMatch = fingerprintData.findBestMatch(rssiData.createRSSIArray());
            current = new Location(LocationType.CURRENT, bestMatch.getX(), bestMatch.getY());
        }
        addNewLocalisation(current);
        rssiData.clearRSSI();
        fingerprintData.clearRSSI();

        //When we are in measure mode we want to log the current coordinate vs the estimated coordinate
        if (measureMode) {
            logMeasure();
        }
        counter = 1;
    }

    /**
     * Execute a fingerprint of the current location.
     */
    public void doFingerprint() {
        //reload fingerprint map
        if (markedLocation == null) {
            Toast.makeText(LocalisationActivity.getInstance(), "Select a location to perform fingerprinting", Toast.LENGTH_SHORT).show();
        } else {
            xmlWriter.printFingerprintToXmlFile(markedLocation, fingerprintData.createRSSIArray(), nodes);
            Toast.makeText(LocalisationActivity.getInstance(), "Fingerprint saved", Toast.LENGTH_SHORT).show();
            fingerprintData.clearRSSI();
        }
    }

    /**
     * Get the list of visited locations.
     * @return the list of locations.
     */
    public List<Location> getOldLocationList() {
        return oldLocations;
    }

    /**
     * Return the current location of the device.
     * @return the current location.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Set the new current location.
     * @param location the location to be set.
     */
    public void addNewLocalisation(LocationObject location) {
        if (location instanceof Location) {
            setCurrentLocationAsOld();
            currentLocation = (Location) location;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Set the marked location.
     * @param markedLocation the location that was marked by the user on the display.
     */
    public void setMarkedLocation(Location markedLocation) {
        this.markedLocation = markedLocation;
        setChanged();
        notifyObservers();
    }

    /**
     * Return the marked location.
     * @return the marked location.
     */
    public Location getMarkedLocation() {
        return markedLocation;
    }

    /**
     * Set the current location as an older location because of the updated current location.
     */
    private void setCurrentLocationAsOld() {
        if (currentLocation != null) {
            currentLocation.setLocationType(LocationType.PAST);
            oldLocations.add(currentLocation);
        }
    }

    /**
     * Check whether the old locations should be drawn or not.
     * @return the drawn old location boolean.
     */
    public boolean drawPreviousLocations() {
        return drawPreviousLocations;
    }

    /**
     * Set whether the old locations should be drawn or not.
     * @param drawPreviousLocations boolean to draw older locationd.
     */
    public void setDrawPreviousLocations(boolean drawPreviousLocations) {
        this.drawPreviousLocations = drawPreviousLocations;
        setChanged();
        notifyObservers();
    }

    /**
     * Reset the current and older locations.
     */
    public void reset() {
        oldLocations = new ArrayList<Location>();
        currentLocation = null;
        drawPreviousLocations = false;
    }

    /**
     * Returns the amount of scans that where currently conducted.
     * @return integer respresenting the amount of scans passed.
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Increase the amount of scans done.
     */
    public void incrementCounter() {
        counter++;
    }

    /**
     * Return the calibration used on the RSSI-values.
     * @return the value of the calibration.
     */
    public double getCalibration() {
        return calibration;
    }

    /**
     * Set the maximum amount of scans to be conducted.
     * @param maxScans the amount of scans.
     */
    public void setMaxScans(int maxScans) {
        this.maxScans = maxScans;
    }

    /**
     * Get the maximum amount of scans that should be done.
     * @return the amount of scans.
     */
    public int getMaxScans() {
        return maxScans;
    }

    /**
     * Set the instance of the localisation algorithm.
     * @param lokalisatie the algorithm
     */
    public void setAlgorithm(Lokalisatie lokalisatie) {
        this.lokalisatie = lokalisatie;
        firstMeasure = true;
    }

    /**
     * Set whether or not to use the calibration value.
     * @param useCalibration the boolean.
     */
    public void setUseCalibration(boolean useCalibration) {
        this.useCalibration = useCalibration;
    }

    /**
     * Get whether or not to use the calibration value.
     * @return the boolean.
     */
    public boolean useCalibration() {
        return useCalibration;
    }

    /**
     * Returns whether or not to draw the marked location on the floorplan.
     * @return the boolean.
     */
    public boolean drawMarkedLocationAllowed() {
        return drawMarkedLocation;
    }

    /**
     * Set whether or not to draw the marked location on the floorplab.
     * @param enableFingerprint boolean respresenting the value.
     */
    public void setDrawMarkedLocation(boolean enableFingerprint) {
        this.drawMarkedLocation = enableFingerprint;
    }

    /**
     * Check if the localisation model is in localisation mode.
     * @return true if in localisation mode, false otherwise.
     */
    public boolean isLocalisationMode() {
        return localisationMode;
    }

    /**
     * Check if the localisation model is in fingerprint mode.
     * @return true if in fingerprint mode, false otherwise.
     */
    public boolean isFingerPrintMode() {
        return fingerPrintMode;
    }

    /**
     * Signals the views to update.
     */
    public void refreshView() {
        setChanged();
        notifyObservers();
    }

    /**
     * Set the model in localisation mode.
     */
    public void initializeLocalisationMode() {
        fingerPrintMode = false;
        drawMarkedLocation = false;
        localisationMode = true;
        measureMode = false;
        drawNodes = false;
    }

    /**
     * Set the model in fingerprint mode.
     */
    public void initializeFingerprintMode() {
        localisationMode = false;
        fingerPrintMode = true;
        measureMode = false;
    }

    /**
     * Set the model in measure mode.
     */
    public void initializeMeasureMode() {
        fingerPrintMode = false;
        drawMarkedLocation = true;
        localisationMode = true;
        measureMode = true;
        drawNodes = false;
    }

    /**
     * Print the measures to a log in a given position.
     */
    private void logMeasure() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("fingerprint", locateUsingFingerprint + "");
        parameters.put("calibration", useCalibration + "");
        parameters.put("scans", maxScans + "");

        if (!measureFileName.isEmpty()) {
            File root = new File("/sdcard/Download/logs/");
            File measureFile = new File(root, measureFileName + ".xml");
            xmlWriter.printMeasuresToXmlFile(measureFile, currentLocation, markedLocation, parameters);
            Toast.makeText(LocalisationActivity.getInstance(), "Measure saved", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get the name of the measure file.
     * @return the name of the file.
     */
    public String getMeasureFileName() {
        return measureFileName;
    }

    /**
     * Set the name of the measure file.
     * @param measureFileName the name of the file.
     */
    public void setMeasureFileName(String measureFileName) {
        this.measureFileName = measureFileName;
    }

    /**
     * Return the list of all available nodes on the floorplan.
     * @return the list of nodes.
     */
    public List<String> getNodes() {
        return nodes;
    }

    /**
     * Check whether or not to use the fingerprinting file to calculate the current location.
     * @return the boolean whether or not to use the file.
     */
    public boolean locateUsingFingerprint() {
        return locateUsingFingerprint;
    }

    /**
     * Set whether or not to use the fingerprinting file to calculate the current location.
     * @param locateUsingFingerprint respresenting the boolean.
     */
    public void setLocateUsingFingerprint(boolean locateUsingFingerprint) {
        this.locateUsingFingerprint = locateUsingFingerprint;
    }

    /**
     * Return the lost of nodes on the floorplan contained as Node classes.
     * @return the list of nodes.
     */
    public List<Node> getNodeObjects() {
        return nodeObjects;
    }

    /**
     * Check if smoothening should be applied.
     * @return the boolean.
     */
    public boolean useSmoothing() {
        return smoothing;
    }

    /**
     * Set whether or not if smoothening should be applied.
     * @param smoothing the boolean variable.
     */
    public void setSmoothing(boolean smoothing) {
        this.smoothing = smoothing;
    }

    /**
     * Return whether or not the nodes should be drawn.
     * @return the boolean respresenting the draw function.
     */
    public boolean drawNodes() {
        return drawNodes;
    }

    /**
     * Set whether or not the nodes should be drawn on the floorplan.
     * @param drawNodes the boolean.
     */
    public void setDrawNodes(boolean drawNodes) {
        this.drawNodes = drawNodes;
    }

    /**
     * Return the weights used for the Kalman-filter.
     * @return the weights.
     */
    public double[] getWeights() {
        return weight_interval;
    }

    /**
     * Create the array of weights with there respective values.
     */
    private void createSmoothnessInterval() {
        for (int i = 0; i < weight_interval.length; i++) {
            if (0 <= i && i <= 5) {
                weight_interval[i] = 1.00;
            } else if (5 < i && i <= 10) {
                weight_interval[i] = 0.75;
            } else if (10 < i && i <= 15) {
                weight_interval[i] = 0.70;
            } else if (15 < i && i <= 25) {
                weight_interval[i] = 0.60;
            } else if (25 < i && i <= 50) {
                weight_interval[i] = 0.35;
            } else if (50 < i && i <= 75) {
                weight_interval[i] = 0.10;
            } else if (75 < i && i <= 100) {
                weight_interval[i] = 0.05;
            }
        }
    }

    /**
     * Change the value where the user wants to navigate to.
     * @param navigateTo the chosen value
     */
    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
        setChanged();
        notifyObservers();
    }

    /**
     * Get the value where to navigate to
     * @return the value
     */
    public String getNavigateTo() {
        return navigateTo;
    }

}
