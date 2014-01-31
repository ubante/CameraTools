package com.ubante.cameratools.models;

/**
 * Models the resulting movie made from the timelapse.
 */
public class Movie {

    static int MAXFRAMESALLOWEDBYFREETLTIMELAPSE = 400;
    static float DEFAULTFPS = 30;
    static String DEFAULTTITLE = "Default movie title (you should set this)";
    private float FPS = DEFAULTFPS;
    private float speedUp;
    private float durationSeconds;
    private int numberOfFrames;
    private String title;

    /** Constructors */
    Movie (Intervalometer s, String title) {
        numberOfFrames = s.getNumberOfFrames();
        this.title = title;
    }

    public Movie (Intervalometer s) {
        this(s, DEFAULTTITLE);
    }

    public float getDurationMinutes() {
        return getDurationSeconds() / 60;
    }

    public float getDurationSeconds() {
        durationSeconds = numberOfFrames / FPS;
        return durationSeconds;
    }

    public String getTitle() {
        return title;
    }

    public float getFPS() {
        return FPS;
    }

    public void setFPS(float FPS) {
        this.FPS = FPS;
    }

    public float getSpeedUp(float realElaspedTimeSeconds) {
        speedUp = realElaspedTimeSeconds / durationSeconds;
        return speedUp;
    }

    void setTitle(String title) {
        this.title = title;
    }

    boolean isTooLongForFreeloaders () {
        if (numberOfFrames > MAXFRAMESALLOWEDBYFREETLTIMELAPSE) {
            return true;
        } else {
            return false;
        }
    }
}