package com.ubante.cameratools.models;

import android.support.v7.appcompat.R;

/**
 * Models the settings of the intervalometer.
 */
public class Intervalometer {

    private int prefocusTime = 2; // time intervalometer uses to prefocus before shutter release
    private float shutterSpeed;
    private int intervalBetweenFrames, numberOfFrames;
    private int focalLength = 50; // use a default value
    private int iso = 100;        // use a default value
    private int aperature = 8;    // use a default value
    private int startDelay = 0;   // use a default value in seconds

    /** Constructors */
    Intervalometer(int intervalBetweenFrames, int numberOfFrames, float shutterSpeed) {
        this.intervalBetweenFrames = intervalBetweenFrames;
        this.numberOfFrames = numberOfFrames;
        this.shutterSpeed = shutterSpeed;
    }

    // Android loves strings
    public Intervalometer(String interval, String num, String ss) {
        this.intervalBetweenFrames = Integer.parseInt(interval);
        this.numberOfFrames = Integer.parseInt(num);
        this.shutterSpeed = Float.parseFloat(ss);
    }

    /** Methods */
    // This is based on the Sunny 16 rule
    float getExposure () {
        float exposure = 1f;
        exposure *= (iso/100);
        exposure *= (shutterSpeed/(1f/125f));
        exposure *= (16f/aperature) * (16f/aperature);

        return exposure;
    }

    static double exposureToStops(float exp) {
        double stops10 = Math.log10(exp);
        double ten2two = Math.pow(10,0.5);

        return stops10 * ten2two;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public float getIntervalBetweenFrames() {
        return intervalBetweenFrames;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public float getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(float shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    float calculateTotalElapsedTime () {
        return intervalBetweenFrames * numberOfFrames +
                (shutterSpeed * (numberOfFrames-1));
    }

    void setFocalLength(int f) {
        this.focalLength = f;
    }

    int getFocalLength() {
        return this.focalLength;
    }

    void setStartDelay(int delay) {
        this.startDelay = delay;
    }

    public void setStartDelayHours(int delayHours) {
        setStartDelay(delayHours*3600);
    }

    int getStartDelay() {
        return startDelay;
    }

    boolean isIntervalTooShort() {
        if (intervalBetweenFrames < prefocusTime) {
            return true;
        } else {
            return false;
        }
    }
}
