package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.ugent.networkplanningtool.data.enums.LocationType;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Alexander on 10-10-2015.
 */
public class Location extends LocationObject {

    private LocationType type;

    public Location(LocationType type, int x, int y) {
        super(new Point(x, y));
        this.type = type;
    }

    public Location(LocationType type, Point point) {
        super(point);
        this.type = type;
    }

    public LocationType getLocationType() {
        return type;
    }

    public void setLocationType(LocationType type) {
        this.type = type;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        paint.reset();
        paint.setColor(type.getColor());
        float pixelsX1 = drawingModel.convertCoordinateToLocation(true, point.x);
        float pixelsY1 = drawingModel.convertCoordinateToLocation(false, point.y);
        //Testing purposes
        canvas.drawCircle(pixelsX1, pixelsY1, 4, paint);
        //canvas.drawCircle(pixelsX1, pixelsY1, 10, paint);

    }

    @Override
    public String toString() {
        return getPoint().x + "," + getPoint().y;
    }

}
