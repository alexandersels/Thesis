package com.ugent.networkplanningtool.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.Edge;
import com.ugent.networkplanningtool.data.Location;
import com.ugent.networkplanningtool.data.Navigation;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.Node;
import com.ugent.networkplanningtool.data.enums.LocationType;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.model.LocalisationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Set;

/**
 * The view (and controller) containing the drawing area.
 */
public class LocalisationView extends View implements Observer {

    private static final String TAG = LocalisationView.class.getName();

    private Paint paint = new Paint();
    private List<Navigation> points = new ArrayList<Navigation>();
    private List<Edge> edges = new ArrayList<Edge>();
    private DrawingModel drawingModel = null;
    private Set<Navigation> settledNodes;
    private Set<Navigation> unSettledNodes;
    private Map<Navigation, Navigation> predecessors;
    private Map<Navigation, Double> distance;

    /**
     * The default constructor
     *
     * @param context context of parent view
     * @param attrs   AttributeSet
     */
    public LocalisationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        FloorPlanModel.LOCALISATION_INSTANCE.addObserver(this);
        LocalisationModel.INSTANCE.addObserver(this);
        setDrawingCacheEnabled(true);
        createNavigationPoints();
    }

    /**
     * Execute Dijkstra
     * code based on http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
     * @param source the starting point
     */
    public void execute(Navigation source) {
        settledNodes = new HashSet<Navigation>();
        unSettledNodes = new HashSet<Navigation>();
        distance = new HashMap<Navigation, Double>();
        predecessors = new HashMap<Navigation, Navigation>();
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Navigation node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Navigation node) {
        List<Navigation> adjacentNodes = getNeighbors(node);
        for (Navigation target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    /**
     * Get distance between current node and destination
     * @param node the current point
     * @param target the target
     * @return the distance between
     */
    private double getDistance(Navigation node, Navigation target) {
        for (Edge edge : edges) {
            if (edge.getSource().getName().equals(node.getName())
                    && edge.getDestination().getName().equals(target.getName())) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    /**
     * Get the neighbours of a node
     * @param node the current node
     * @return the neighbours
     */
    private List<Navigation> getNeighbors(Navigation node) {
        List<Navigation> neighbors = new ArrayList<Navigation>();
        for (Edge edge : edges) {
            if (edge.getSource().getName().equals(node.getName())
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    /**
     * Get the shortest distance from a given set of neighbours.
     * @param vertexes the neighbours
     * @return the closest point
     */
    private Navigation getMinimum(Set<Navigation> vertexes) {
        Navigation minimum = null;
        for (Navigation vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    /**
     * Check if the current point still has some values left to check
     * @param vertex the current point on the graph
     * @return true is not fully checked
     */
    private boolean isSettled(Navigation vertex) {
        return settledNodes.contains(vertex);
    }

    /**
     * Check the shortest distance to the destination.
     * @param destination the destination
     * @return the distance
     */
    private double getShortestDistance(Navigation destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /**
     * Return the path from source to target
     * @param target the destination
     * @return the path
     */
    public LinkedList<Navigation> getPath(Navigation target) {
        LinkedList<Navigation> path = new LinkedList<Navigation>();
        Navigation step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

    /**
     * Create the points and edges to make perform the Dijksta algorithm.
     */
    private void createNavigationPoints() {
        Navigation room1 = new Navigation(1575, 1810, "1");
        Navigation room2 = new Navigation(2930, 1310, "2");
        Navigation room3 = new Navigation(1500, 2800, "3");
        Navigation room4 = new Navigation(4410, 2068, "4");
        Navigation room5 = new Navigation(4785, 1483, "5");
        Navigation room6 = new Navigation(5363, 1930, "6");
        Navigation room7 = new Navigation(5825, 1183, "7");
        Navigation room8 = new Navigation(6135, 2065, "8");
        Navigation room9 = new Navigation(6958, 1950, "9");
        Navigation room10 = new Navigation(7408, 1890, "10");
        Navigation room11 = new Navigation(7783, 1838, "11");
        Navigation room12 = new Navigation(8305, 1760, "12");
        Navigation room13 = new Navigation(8535, 1205, "13");
        Navigation room14 = new Navigation(8710, 1673, "14");

        Navigation door1 = new Navigation(1980, 1923, "door1");
        Navigation door2 = new Navigation(2068, 1823, "door2");
        Navigation door3 = new Navigation(3748, 1553, "door3");
        Navigation door4 = new Navigation(2065, 2540, "door4");
        Navigation door5 = new Navigation(4360, 1818, "door5");
        Navigation door6 = new Navigation(4820, 1625, "door6");
        Navigation door7 = new Navigation(5295, 1668, "door7");
        Navigation door8 = new Navigation(5868, 1368, "door8");
        Navigation door9 = new Navigation(6105, 1905, "door9");
        Navigation door10 = new Navigation(6920, 1778, "door10");
        Navigation door11 = new Navigation(7370, 1703, "door11");
        Navigation door12 = new Navigation(7750, 1648, "door12");
        Navigation door13 = new Navigation(8260, 1563, "door13");
        Navigation door14 = new Navigation(8570, 1360, "door14");
        Navigation door15 = new Navigation(8668, 1505, "door15");

        Navigation point1 = new Navigation(2088, 1915, "point1");
        Navigation point2 = new Navigation(2253, 1890, "point2");
        Navigation point3 = new Navigation(2468, 1855, "point3");
        Navigation point4 = new Navigation(2695, 1815, "point4");
        Navigation point5 = new Navigation(2905, 1783, "point5");
        Navigation point6 = new Navigation(3118, 1750, "point6");
        Navigation point7 = new Navigation(3310, 1718, "point7");
        Navigation point8 = new Navigation(3498, 1685, "point8");
        Navigation point9 = new Navigation(3650, 1658, "point9");
        Navigation point10 = new Navigation(3770, 1635, "point10");
        Navigation point11 = new Navigation(3805, 1793, "point11");
        Navigation point12 = new Navigation(3880, 2160, "point12");
        Navigation point13 = new Navigation(2160, 2438, "point13");
        Navigation point14 = new Navigation(4115, 1713, "point14");
        Navigation point15 = new Navigation(4348, 1758, "point15");
        Navigation point16 = new Navigation(4835, 1683, "point16");
        Navigation point17 = new Navigation(5280, 1608, "point17");
        Navigation point18 = new Navigation(5898, 1515, "point18");
        Navigation point19 = new Navigation(5963, 1853, "point19");
        Navigation point20 = new Navigation(6090, 1825, "point20");
        Navigation point21 = new Navigation(6903, 1700, "point21");
        Navigation point22 = new Navigation(7350, 1628, "point22");
        Navigation point23 = new Navigation(7735, 1565, "point23");
        Navigation point24 = new Navigation(8243, 1490, "point24");
        Navigation point25 = new Navigation(8593, 1435, "point25");

        Edge edge1 = new Edge(room1,door1);
        Edge edge2 = new Edge(room2,door2);
        Edge edge3 = new Edge(room2,door3);
        Edge edge4 = new Edge(room3,door4);
        Edge edge5 = new Edge(room4,door5);
        Edge edge6 = new Edge(room5,door6);
        Edge edge7 = new Edge(room6,door7);
        Edge edge8 = new Edge(room7,door8);
        Edge edge9 = new Edge(room8,door9);
        Edge edge10 = new Edge(room9,door10);
        Edge edge11 = new Edge(room10,door11);
        Edge edge12 = new Edge(room11,door12);
        Edge edge13 = new Edge(room12,door13);
        Edge edge14 = new Edge(room13,door14);
        Edge edge15 = new Edge(room14,door15);

        Edge edge16 = new Edge(door1,room1);
        Edge edge17 = new Edge(door1,point1);
        Edge edge18 = new Edge(door2,room2);
        Edge edge19 = new Edge(door2,point1);
        Edge edge20 = new Edge(door3,room2);
        Edge edge21 = new Edge(door3,point10);
        Edge edge22 = new Edge(door4,room3);
        Edge edge23 = new Edge(door4,point13);
        Edge edge24 = new Edge(door5,room4);
        Edge edge25 = new Edge(door5,point15);
        Edge edge26 = new Edge(door6,room5);
        Edge edge27 = new Edge(door6,point16);
        Edge edge28 = new Edge(door7,room6);
        Edge edge29 = new Edge(door7,point17);
        Edge edge30 = new Edge(door8,room7);
        Edge edge31 = new Edge(door8,point18);
        Edge edge32 = new Edge(door9,room8);
        Edge edge33 = new Edge(door9,point20);
        Edge edge34 = new Edge(door10,room9);
        Edge edge35 = new Edge(door10,point21);
        Edge edge36 = new Edge(door11,room10);
        Edge edge37 = new Edge(door11,point22);
        Edge edge38 = new Edge(door12,room11);
        Edge edge39 = new Edge(door12,point23);
        Edge edge40 = new Edge(door13,room12);
        Edge edge41 = new Edge(door13,point24);
        Edge edge42 = new Edge(door14,room13);
        Edge edge43 = new Edge(door14,point25);
        Edge edge44 = new Edge(door15,room14);
        Edge edge45 = new Edge(door15,point25);

        Edge edge46 = new Edge(point1,door1);
        Edge edge47 = new Edge(point1,door2);
        Edge edge48 = new Edge(point1,point2);
        Edge edge49 = new Edge(point2,point1);
        Edge edge50 = new Edge(point2,point3);
        Edge edge51 = new Edge(point3,point2);
        Edge edge52 = new Edge(point3,point4);
        Edge edge53 = new Edge(point4,point5);
        Edge edge54 = new Edge(point4,point3);
        Edge edge55 = new Edge(point5,point6);
        Edge edge56 = new Edge(point5,point4);
        Edge edge57 = new Edge(point6,point7);
        Edge edge58 = new Edge(point6,point5);
        Edge edge59 = new Edge(point7,point8);
        Edge edge60 = new Edge(point7,point6);
        Edge edge61 = new Edge(point8,point7);
        Edge edge62 = new Edge(point8,point9);
        Edge edge63 = new Edge(point9,point8);
        Edge edge64 = new Edge(point9,point10);
        Edge edge65 = new Edge(point10,door3);
        Edge edge66 = new Edge(point10,point9);
        Edge edge67 = new Edge(point10,point11);
        Edge edge68 = new Edge(point11,point10);
        Edge edge69 = new Edge(point11,point14);
        Edge edge70 = new Edge(point11,point12);
        Edge edge71 = new Edge(point12,point11);
        Edge edge72 = new Edge(point12,point13);
        Edge edge73 = new Edge(point13,door4);
        Edge edge74 = new Edge(point13,point12);
        Edge edge75 = new Edge(point14,point11);
        Edge edge76 = new Edge(point14,point15);
        Edge edge77 = new Edge(point15,door5);
        Edge edge78 = new Edge(point15,point14);
        Edge edge79 = new Edge(point15,point16);
        Edge edge80 = new Edge(point16,door6);
        Edge edge81 = new Edge(point16,point15);
        Edge edge82 = new Edge(point16,point17);
        Edge edge83 = new Edge(point17,door7);
        Edge edge84 = new Edge(point17,point16);
        Edge edge85 = new Edge(point17,point18);
        Edge edge86 = new Edge(point18,door8);
        Edge edge87 = new Edge(point18,point17);
        Edge edge88 = new Edge(point18,point19);
        Edge edge89 = new Edge(point19,point18);
        Edge edge90 = new Edge(point19,point20);
        Edge edge91 = new Edge(point20,door9);
        Edge edge92 = new Edge(point20,point19);
        Edge edge93 = new Edge(point20,point21);
        Edge edge94 = new Edge(point21,door10);
        Edge edge95 = new Edge(point21,point20);
        Edge edge96 = new Edge(point21,point22);
        Edge edge97 = new Edge(point22,door11);
        Edge edge98 = new Edge(point22,point21);
        Edge edge99 = new Edge(point22,point23);
        Edge edge100 = new Edge(point23,door12);
        Edge edge101 = new Edge(point23,point22);
        Edge edge102 = new Edge(point23,point24);
        Edge edge103 = new Edge(point24,door13);
        Edge edge104 = new Edge(point24,point23);
        Edge edge105 = new Edge(point24,point25);
        Edge edge106 = new Edge(point25,door14);
        Edge edge107 = new Edge(point25,door15);
        Edge edge108 = new Edge(point25,point24);

        points.add(room1);
        points.add(room2);
        points.add(room3);
        points.add(room4);
        points.add(room5);
        points.add(room6);
        points.add(room7);
        points.add(room8);
        points.add(room9);
        points.add(room10);
        points.add(room11);
        points.add(room12);
        points.add(room13);
        points.add(room14);
        points.add(door1);
        points.add(door2);
        points.add(door3);
        points.add(door4);
        points.add(door5);
        points.add(door6);
        points.add(door7);
        points.add(door8);
        points.add(door9);
        points.add(door10);
        points.add(door11);
        points.add(door12);
        points.add(door13);
        points.add(door14);
        points.add(door15);
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(point7);
        points.add(point8);
        points.add(point9);
        points.add(point10);
        points.add(point11);
        points.add(point12);
        points.add(point13);
        points.add(point14);
        points.add(point15);
        points.add(point16);
        points.add(point17);
        points.add(point18);
        points.add(point19);
        points.add(point20);
        points.add(point21);
        points.add(point22);
        points.add(point23);
        points.add(point24);
        points.add(point25);

        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);
        edges.add(edge7);
        edges.add(edge8);
        edges.add(edge9);
        edges.add(edge10);
        edges.add(edge11);
        edges.add(edge12);
        edges.add(edge13);
        edges.add(edge14);
        edges.add(edge15);
        edges.add(edge16);
        edges.add(edge17);
        edges.add(edge18);
        edges.add(edge19);
        edges.add(edge20);
        edges.add(edge21);
        edges.add(edge22);
        edges.add(edge23);
        edges.add(edge24);
        edges.add(edge25);
        edges.add(edge26);
        edges.add(edge27);
        edges.add(edge28);
        edges.add(edge29);
        edges.add(edge30);
        edges.add(edge31);
        edges.add(edge32);
        edges.add(edge33);
        edges.add(edge34);
        edges.add(edge35);
        edges.add(edge36);
        edges.add(edge37);
        edges.add(edge38);
        edges.add(edge39);
        edges.add(edge40);
        edges.add(edge41);
        edges.add(edge42);
        edges.add(edge43);
        edges.add(edge44);
        edges.add(edge45);
        edges.add(edge46);
        edges.add(edge47);
        edges.add(edge48);
        edges.add(edge49);
        edges.add(edge50);
        edges.add(edge51);
        edges.add(edge52);
        edges.add(edge53);
        edges.add(edge54);
        edges.add(edge55);
        edges.add(edge56);
        edges.add(edge57);
        edges.add(edge58);
        edges.add(edge59);
        edges.add(edge60);
        edges.add(edge61);
        edges.add(edge62);
        edges.add(edge63);
        edges.add(edge64);
        edges.add(edge65);
        edges.add(edge66);
        edges.add(edge67);
        edges.add(edge68);
        edges.add(edge69);
        edges.add(edge70);
        edges.add(edge71);
        edges.add(edge72);
        edges.add(edge73);
        edges.add(edge74);
        edges.add(edge75);
        edges.add(edge76);
        edges.add(edge77);
        edges.add(edge78);
        edges.add(edge79);
        edges.add(edge80);
        edges.add(edge81);
        edges.add(edge82);
        edges.add(edge83);
        edges.add(edge84);
        edges.add(edge85);
        edges.add(edge86);
        edges.add(edge87);
        edges.add(edge88);
        edges.add(edge89);
        edges.add(edge90);
        edges.add(edge91);
        edges.add(edge92);
        edges.add(edge93);
        edges.add(edge94);
        edges.add(edge95);
        edges.add(edge96);
        edges.add(edge97);
        edges.add(edge98);
        edges.add(edge99);
        edges.add(edge100);
        edges.add(edge101);
        edges.add(edge102);
        edges.add(edge103);
        edges.add(edge104);
        edges.add(edge105);
        edges.add(edge106);
        edges.add(edge107);
        edges.add(edge108);
    }

    /**
     * Find the closest position near the current location on the graph
     * @return the closest match
     */
    private Navigation calculateStartPosition() {
        Location current = new Location(LocationType.CURRENT, 2468, 1855);
        //Location current = LocalisationModel.INSTANCE.getCurrentLocation();
        double best = Double.MAX_VALUE;
        Navigation found = null;
        for (Navigation nav : points) {
            double distance = Math.sqrt((nav.getX() - current.getPoint().x) ^ 2 + (nav.getY() - current.getPoint().y) ^ 2);
            if (distance < best) {
                best = distance;
                found = nav;
            }
        }
        return found;
    }

    /**
     * Draws the drawing area and everying on it.
     *
     * @param canvas the canvas to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        LocalisationModel model = LocalisationModel.INSTANCE;

        if (drawingModel != null) {
            drawWalls(canvas);
            if (model.isLocalisationMode()) {
                drawLocation(canvas);
            }
            if (model.drawMarkedLocationAllowed()) {
                drawMarkedLocation(canvas);
            }

            if (model.drawNodes()) {
                drawRouter(canvas);
            }
        }
        /**
         * draw path is used to test navigation
         */
        if (!model.getNavigateTo().equals("None")) {
            drawPathOnCanvas(shortestPath(model.getNavigateTo()),canvas);
        }
        super.onDraw(canvas);
    }

    /**
     * Draws the directions on the canvas
     * @param path the directions
     * @param canvas the canvas
     */
    private void drawPathOnCanvas(List<Navigation> path, Canvas canvas) {
        paint.reset();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        for (int i = 0; i < path.size() - 1; i++) {

            float pixelsX1 = drawingModel.convertCoordinateToLocation(true, path.get(i).getX());
            float pixelsY1 = drawingModel.convertCoordinateToLocation(false, path.get(i).getY());

            float pixelsX2 = drawingModel.convertCoordinateToLocation(true, path.get(i+1).getX());
            float pixelsY2 = drawingModel.convertCoordinateToLocation(false, path.get(i+1).getY());
            canvas.drawLine(pixelsX1, pixelsY1, pixelsX2, pixelsY2, paint);
        }
    }

    /**
     * Draws the Wall objects
     *
     * @param canvas the canvas to draw on
     */
    private void drawWalls(Canvas canvas) {
        List<Wall> wallList = FloorPlanModel.LOCALISATION_INSTANCE.getWallList();
        for (Wall w : wallList) {
            w.drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    /**
     * Draw the routers.
     *
     * @param canvas the canvas to draw on.
     */
    private void drawRouter(Canvas canvas) {
        LocalisationModel model = LocalisationModel.INSTANCE;
        for (Node node : model.getNodeObjects()) {
            node.drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    /**
     * Calculate shortest path from current to destination
     * @param location name of the destination
     * @return the path leading to the destination
     */
    private List<Navigation> shortestPath(String location) {
        Navigation destination = null;
        for (Navigation nav : points) {
            if (nav.getName().equals(location)) {
                destination = nav;
            }
        }
        Navigation source = calculateStartPosition();
        execute(source);

        return getPath(destination);
    }

    /**
     * Draw the locations on the canvas.
     *
     * @param canvas the canvas to draw on.
     */
    private void drawLocation(Canvas canvas) {
        LocalisationModel model = LocalisationModel.INSTANCE;
        //Draw the previous locations on the canvas
        if (model.drawPreviousLocations()) {
            List<Location> locationList = model.getOldLocationList();
            for (Location l : locationList) {
                l.drawOnCanvas(canvas, drawingModel, paint, false);
            }
        }

        //Draw the current location on the canvas
        Location currentLocation = model.getCurrentLocation();

        /**
         * For testing navigation purposes
         * remove for real usage
         */
        currentLocation = new Location(LocationType.CURRENT, 2468, 1855);
        if (currentLocation != null) {
            currentLocation.drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    /**
     * Draw the marked locations on the canvas.
     *
     * @param canvas the canvas to draw on.
     */
    private void drawMarkedLocation(Canvas canvas) {
        LocalisationModel model = LocalisationModel.INSTANCE;
        if (model.getMarkedLocation() != null) {
            model.getMarkedLocation().drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    /**
     * Contains all reactions for when the user touches the screen.
     *
     * @param event the MotionEvent triggering this method
     * @return whether to consume the event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MotionEvent movedEvent = MotionEvent.obtain(event);
        movedEvent.setLocation(event.getX() - 50, event.getY() - 50);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (drawingModel.isMoving()) {
                    drawingModel.move(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                drawingModel.moveStart(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (drawingModel.isMoving()) {
                    drawingModel.moveStop();
                    switch (drawingModel.getState()) {
                        case PLACING:
                            drawingModel.setPlaceMode();
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Updates this view. Triggered by a model.
     *
     * @param observable the observable that triggered the update
     * @param data       the data that has been updated
     */
    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }

    /**
     * Returns the drawing model
     *
     * @return the drawing model
     */
    public DrawingModel getModel() {
        return drawingModel;
    }

    /**
     * Sets the drawing model
     *
     * @param drawingModel the drawing model
     */
    public void setModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
        drawingModel.addObserver(this);
    }

    /**
     * Updates the drawing model and calls superclass
     *
     * @param w    then new width
     * @param h    the new height
     * @param oldw the old width
     * @param oldh the old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (drawingModel != null) {
            drawingModel.setViewSize(w, h);
            invalidate();
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

}
