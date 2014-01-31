package com.ubante.cameratools.models;

/**
 * Models the memory card in the camera.
 */
public class MemoryCard {

    private int capacity; // in GB

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

    MemoryCard (int capacity) {
        this.capacity = capacity;
    }
}
