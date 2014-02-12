package com.ubante.cameratools.models;

/**
 * Models the memory card in the camera.
 */
public class MemoryCard implements Equipment {

    private int capacity; // in GB
    float speedMbps; // in Mbps
    String formFactor; // SD or CF
    String busRate; // SD or SDHC or SDXC

    MemoryCard (int capacity) {
        this.capacity = capacity;
    }

    // See http://en.wikipedia.org/wiki/Secure_Digital#Speeds for a explanation.
    int getSpeedClass () {
        int speedClass = 2;
        float speedMBps = speedMbps / 8;

        if (speedMBps > 4) { speedClass = 4; }
        if (speedMBps > 6) { speedClass = 6; }
        if (speedMBps > 10) { speedClass = 10; }

        return speedClass;
    }

    // Factor of 150 KB/sec
    float getSpeedX() {
        return speedMbps * 1024 / 8;
    }


    int getCapacity() {
        return capacity;
    }

    /**
     * Accepts the total size in of space used in MB and returns a percentage of the card consumed
     * by those files.
     *
     * @param usedSize
     * @return
     */
    float getConsumption(float usedSize) {
        float percentageConsumed = usedSize / capacity * 100 / 1024;
        return percentageConsumed;
    }


}
