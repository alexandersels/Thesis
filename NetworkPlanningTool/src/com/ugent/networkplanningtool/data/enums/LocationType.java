package com.ugent.networkplanningtool.data.enums;

import android.graphics.Color;

/**
 * Created by Alexander on 10-10-2015.
 */
public enum LocationType {
    CURRENT(Color.RED),
    PAST(Color.BLUE),
    NAVIGATION(Color.BLACK),
    MARKED(Color.GREEN);

    private int color;

    private LocationType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
