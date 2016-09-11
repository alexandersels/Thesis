package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ugent.networkplanningtool.data.enums.LocationType;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Alexander on 1/12/2015.
 */
public class Node {

    private int x;
    private int y;
    private String name;

    public Node(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Node() {

    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getName() {
        return name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        paint.reset();
        paint.setColor(Color.CYAN);
        float pixelsX1 = drawingModel.convertCoordinateToLocation(true, x);
        float pixelsY1 = drawingModel.convertCoordinateToLocation(false, y);
        canvas.drawCircle(pixelsX1, pixelsY1, 10, paint);
        paint.reset();
    }

    @Override
    public String toString() {
        return name + " x: " + x + " y: " + y;
    }
}
