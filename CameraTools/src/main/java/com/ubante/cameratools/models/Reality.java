package com.ubante.cameratools.models;

import java.util.Calendar;

/**
 * Models realtime time.
 */
public class Reality {

    static int RULEOFSIXHUNDRED = 600;
    private float durationSeconds;
    private Calendar now;
    private Calendar sequenceStart;
    private Calendar sequenceFinish;
    private float imageSize = 15; // in MB
    private MemoryCard card;
    private Battery battery;
    private float totalElapsedTimeSeconds; // includes the delay before sequence start


    float getDurationSeconds() {
        return durationSeconds;
    }

    float getDurationMinutes() {
        return durationSeconds / 60;
    }

    float getDurationHours() {
        return durationSeconds / 3600;
    }

    float getImageSize() {
        return imageSize;
    }

    Calendar getNow() {
        return now;
    }

    int getCardCapacity() {
        return card.getCapacity();
    }

    /**
     * Returns a precentage of the card consumed by the photos from the sequence.
     * @param spaceUsedByPhotos
     * @return
     */
    float getCardConsumption(float spaceUsedByPhotos) {
        return card.getConsumption(spaceUsedByPhotos);
    }

    float getBatteryUsage(int frames) {
//        return battery.getConsumption(frames, getDurationMinutes());
        return battery.getConsumption(frames, totalElapsedTimeSeconds/60);
    }

    void computeTimes(Intervalometer s) {
        // The below is not precise since the two different calls to getInstance will return
        // slightly different time values.
        sequenceStart = Calendar.getInstance();
        sequenceStart.add(Calendar.SECOND, s.getStartDelay());
        sequenceFinish = Calendar.getInstance();
        sequenceFinish.add(Calendar.SECOND, s.getStartDelay());
        sequenceFinish.add(Calendar.SECOND, (int) getDurationSeconds());
        totalElapsedTimeSeconds = durationSeconds + s.getStartDelay();
    }

    Calendar getSequenceStart() {
        return sequenceStart;
    }

    Calendar getSequenceFinish() {
        return sequenceFinish;
    }

    float getTotalElapsedTimeSeconds() {
        return totalElapsedTimeSeconds;
    }

    /** Constructor */
    public Reality (Intervalometer s) {
        durationSeconds = s.calculateTotalElapsedTime();
        card = new MemoryCard(32); // this and battery may need to go to a new class, like Equipment
        battery = new Battery();
        now = Calendar.getInstance();
        computeTimes(s);
    }
}

