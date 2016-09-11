package com.ugent.networkplanningtool.data;

/**
 * Created by Alexander on 24/05/2016.
 */
public class Edge {
    private Navigation source;
    private Navigation destination;
    private double weight;

    public Edge(Navigation source, Navigation destination) {
        this.source = source;
        this.destination = destination;
        weight = Math.sqrt(Math.pow((source.getX() - destination.getX()),2) + Math.pow((source.getY() - destination.getY()),2));
    }

    public Navigation getDestination() {
        return destination;
    }

    public Navigation getSource() {
        return source;
    }

    public double getWeight() {
        return weight;
    }
}
