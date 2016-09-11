package com.ugent.networkplanningtool.localisation;

import com.ugent.networkplanningtool.data.Location;

/**
 * Created by Alexander on 20/11/2015.
 * This class will represent a single measure.
 */
public class Measure {

    private Location current;
    private Location marked;
    private int distance;

    public Measure(Location current, Location marked) {
        this.current = current;
        this.marked = marked;
        calculateDistance();
    }

    /**
     * Calculate the distance between the marked and calculated location.
     */
    private void calculateDistance() {
        int x1 = current.getPoint().x;
        int y1 = current.getPoint().y;
        int x2 = marked.getPoint().x;
        int y2 = marked.getPoint().y;
        distance = (int) Math.sqrt(Math.pow(x1 - x2, 2) +  Math.pow(y1 - y2, 2) );
    }

    /**
     * Return the current location.
     * @return the current location.
     */
    public Location getCurrent() {
        return current;
    }

    /**
     * Return the marked location.
     * @return the marked location.
     */
    public Location getMarked() {
        return marked;
    }

    /**
     * Return the distance between the two points.
     * @return the distance between the two points.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Set the distance between two points.
     * @param distance between two points.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "current location: (" + current + ")\n" +
                "marked location: (" + marked + ")\n" +
                "distance: " + distance;
    }
}
