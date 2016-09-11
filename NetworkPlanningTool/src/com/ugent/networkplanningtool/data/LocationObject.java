package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Alexander on 10-10-2015.
 */
public abstract class LocationObject {

    /**
     * All localisation objects need al least one point (location) to draw
     */
    protected Point point;

    /**
     * Default constructor
     */
    public LocationObject() {
        point = null;
    }

    /**
     * Constructor that sets the location of the object
     * Can be used by extending classes
     * @param point the location of the object
     */
    public LocationObject(Point point) {
        this.point = point;
    }

    /**
     * Returns the location of the object
     * @return the location of the object
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Sets the location of the object
     * @param point the location of the object
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Sets the location of the object using its coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setPoint(int x, int y) {
        if (x >= 0 && y >= 0) {
            if (point == null) {
                point = new Point(x, y);
            } else {
                point.set(x, y);
            }
        }
    }

    /**
     * Lets the object draw itself on the given canvas that belongs to a LocationView
     * To be implemented by extending classes
     * @param canvas the canvas to draw on
     * @param drawingModel the DrawingModel used for information how to draw exactly
     * @param paint the Paint used for drawing styles and colors
     * @param touch whether the object is currently touched by the user on the screen
     */
    public abstract void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch);


}
