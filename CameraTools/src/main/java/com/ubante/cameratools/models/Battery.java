package com.ubante.cameratools.models;

/**
 * Models the camera battery.
 */
public class Battery {
    private int mah;
    private int mahPerFrame;
    private int mahPerMinute;


    float getConsumption(int frameCount, float durationMinutes) {
        int powerForFrames = frameCount * mahPerFrame;
        float powerForBeingOn = durationMinutes * mahPerMinute;

        float consumption = (powerForFrames+powerForBeingOn)/mah;
        return consumption;
    }

    /** Constructor */
    // Generic values
    Battery () {
        mah = 2500;
        mahPerMinute = 3;
        mahPerFrame = 2;
    }
}
