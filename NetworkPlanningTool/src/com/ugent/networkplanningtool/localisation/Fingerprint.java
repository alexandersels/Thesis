package com.ugent.networkplanningtool.localisation;

import java.util.List;

/**
 * Created by Alexander on 23/11/2015.
 * Class representing a fingerprint of the current location.
 */
public class Fingerprint {

    private List<Double> rssi;
    private int x;
    private int y;

    public Fingerprint(int x, int y, List<Double> rssi) {
        this.rssi = rssi;
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate the distance between the current fingerprint and the list of RSSI-values.
     * @param otherRssi the RSSI-values found by the wifi-scan.
     * @return the distance.
     */
    public double calcDistance(List<Double> otherRssi) {
        double sum = 0.0;
        for (int i = 0; i < rssi.size(); i++) {
            Double fraction = Math.pow(rssi.get(i) - otherRssi.get(i),2);
            sum = sum + fraction;
        }
        return Math.sqrt(sum);
    }

    /**
     * Get the X coordinate of the fingerprint.
     * @return the coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Y coordinate of the fingerprint.
     * @return the coordinate.
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        String output = "";
        output = "Marked x: " + x + "\n"
                + "Marked y: " + y + "\n";
        for (int i = 0; i < rssi.size(); i++) {
            output = output + rssi.get(i) + "\n";
        }
        output = output + "\n";
        return output;
    }
}
