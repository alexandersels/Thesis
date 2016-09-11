package com.ugent.networkplanningtool.model;

import java.util.Observable;

import be.ugent.intec.wica.viterbi.Lokalisatie;

/**
 * Created by Alexander on 5/01/2016.
 * Class to contain and save the received accelerometer data
 */
public class StepModel extends Observable{

    public static StepModel INSTANCE = new StepModel();

    private int counter;
    private long start;
    private long stop;

    public StepModel() {
        counter = 0;
        start = 0;
        stop = 0;
    }


    /**
     * Reset the amount of steps taken between two localisation operations
     */
    public void resetSteps() {
        counter = 0;
        start = System.currentTimeMillis();
    }

    /**
     * Increase the amount of steps taken by one
     */
    public void incrementCounter() {
        counter++;
    }

    /**
     * Stop measuring the time between localisation operations
     */
    public void stopMeasuring() {
        stop = System.currentTimeMillis();
    }

    /**
     * Set the derived information about the movement speed and sampleRate in the algorithm.
     * @param lokalisatie an instance of the localisation algorithm.
     */
    public void setParameters(Lokalisatie lokalisatie) {
        double timePassed = (stop-start)/1000;
        double velocity = (counter * 0.80)/timePassed;

        lokalisatie.sampleRate = 1/timePassed;
        lokalisatie.maxSnelheid = velocity;

        System.out.println("Time between measures: " + 1/lokalisatie.sampleRate + " s");
        System.out.println("Average speed: " + lokalisatie.maxSnelheid + " m/s");
        System.out.println("Distance made: " + ((1/lokalisatie.sampleRate) * lokalisatie.maxSnelheid));

    }

    /**
     * For testing purposes: only set the sampling rate and not the movement speed.
     * @param lokalisatie an instance of the localisation algorithm.
     */
    public void setStaticParameters(Lokalisatie lokalisatie) {
        double timePassed = (stop-start)/1000;

        lokalisatie.sampleRate = 1/timePassed;

        System.out.println("Time between measures: " + 1/lokalisatie.sampleRate + " s");
        System.out.println("Average speed: " + lokalisatie.maxSnelheid + " m/s");
        System.out.println("Distance made: " + ((1/lokalisatie.sampleRate) * lokalisatie.maxSnelheid));

    }
}
