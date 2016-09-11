package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ugent.networkplanningtool.model.DrawingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 24/05/2016.
 */
public class Navigation {

    private int x;
    private int y;
    private List<Navigation> neighbours = new ArrayList<Navigation>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private String name;

    public Navigation(int x, int y) {
        this.x = x;
        this.y = y;
        name = "";
    }

    public Navigation(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public List<Navigation> getNeighbours() {
        return neighbours;
    }

    public void addNeighbours(Navigation point) {
        neighbours.add(point);
    }

}
