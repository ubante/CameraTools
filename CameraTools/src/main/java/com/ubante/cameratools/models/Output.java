package com.ubante.cameratools.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This handles the output.  It creates a standard report and a special report of warnings if any found.
 */
public class Output {

    private Intervalometer i;
    private Movie m;
    private Reality r;

    /** This is the message displayed before the user enters intervalometer settings.
     *  It will give reminders for setting up.
     */
    public static String getReminders() {
        String reminders = "Reminders:\n\n";

        reminders += "- Make sure you are on manual focus and IS is disabled.\n";
        reminders += "- Put your hand in the frame to mark the sequence start to make it easier "
                + "in post.\n";
        reminders += "- Give the tripod a nudge to make sure it won't shift on its own.\n";

        return reminders;
    }

    /** This will consider possible problems. */
    String getSpecialReport() {
        List<String> notes = new ArrayList<String>();
        String specialReport = "";

        // check for over exposure
        float exposure = i.getExposure();
        double stops = Intervalometer.exposureToStops(exposure);
        notes.add(String.format("The exposure is %1.1f times (%1.1f stops) that of the Sunny 16 rule.",
                exposure, stops));

        // check for space usage on memory card
        float totalCardSpaceUsed = i.getNumberOfFrames()*r.getImageSize();
        int cardCapacity = r.getCardCapacity();
        float percent = r.getCardConsumption(totalCardSpaceUsed);
        notes.add(String.format("This sequence will take %.1f MB on your %d GB card (%.1f%%).",
                totalCardSpaceUsed,
                cardCapacity,
                percent));
        if (percent > 50) {
            notes.add("WARNING: make sure you have space on your memory card.");
        }

        // check for blur from long shutter speeds
        // XXX need to implement Lens class
        if (Reality.RULEOFSIXHUNDRED/i.getFocalLength() < i.getShutterSpeed()) {
            notes.add(String.format(
                    "WARNING: shutter speed too slow for focal length (%d mm) and will cause motion blur",
                    i.getFocalLength()));
        }

        // check for 400 frame limit of LRTimelapse
//        if (i.getNumberOfFrames() > Movie.MAXFRAMESALLOWEDBYFREETLTIMELAPSE) {
//            notes.add(String.format(
//                    "WARNING: %d is too many frames for one movie made with the free version of TLTimelapse",
//                    i.getNumberOfFrames()));
//        }
        if (m.isTooLongForFreeloaders()) {
            notes.add(String.format(
                    "WARNING: %d is too many frames for one movie made with the free version of TLTimelapse",
                    i.getNumberOfFrames()));
        }

        // my intervalometers starts two seconds early for focusing - make a note
//        if (i.getIntervalBetweenFrames() <= 2) {
//            notes.add("WARNING: your delay between frames may get in the way of the intervalometer pre-focusing.");
//        }
        if (i.isIntervalTooShort()) {
            notes.add("WARNING: your delay between frames may get in the way of the intervalometer pre-focusing.");
        }

        // check for long sequences
        if (r.getDurationHours() > 2) {
            notes.add(String.format("WARNING: your duration (%.2f) is >2hours - consider Aperature priority.",
                    r.getDurationHours()));
        }

        // check for batteries
        float batteriesNeeded = r.getBatteryUsage(i.getNumberOfFrames());
        if (batteriesNeeded > 1) {
            notes.add(String.format("FATAL: insufficient battery power; you need %f batteries.",
                    batteriesNeeded));
        } else if (batteriesNeeded > 0.8) {
            notes.add(String.format("WARNING: your battery usage is very high (%.1f%%) - consider fresh batteries.",
                    batteriesNeeded*100));
        }

        if (! notes.isEmpty()) {
            specialReport = "-------------------------------\nNotes:\n";
            for ( String note : notes ) {
                specialReport += note+"\n";
//                System.out.println(note);
            }
        }

        return specialReport;
    }

    String getStandardReport() {
        String standardReport = "";

        // XXX for when we save movie titles for laaater.
//        standardReport += "\n===============================\n";
//        standardReport += String.format("%s\n", m.getTitle());
        standardReport += "-------------------------------\nIntervalometer:";
        standardReport += String.format("\tnumber of frames\t%8d\n", i.getNumberOfFrames());
        standardReport += String.format("\tshutter speed\t\t\t%10.5f\n", i.getShutterSpeed());
        standardReport += String.format("\tdelay\t\t\t\t%10.1f\n", i.getIntervalBetweenFrames()); // weird that this wants a float
        standardReport += "-------------------------------\nMovie:";
        standardReport += String.format("\tduration (seconds)\t%10.1f\n", m.getDurationSeconds());
        standardReport += String.format("\tduration (minutes)\t%10.1f\n", m.getDurationMinutes());
        standardReport += String.format("\tFPS\t\t\t\t\t%10.1f\n", m.getFPS());
        standardReport += String.format("\tspeedup\t\t\t\t%10.1f\n", m.getSpeedUp(r.getDurationSeconds()));
        standardReport += String.format("-------------------------------\nReality:");
        standardReport += String.format("\tduration (seconds)\t%10.1f\n", r.getDurationSeconds());
        standardReport += String.format("\tduration (minutes)\t%10.1f\n", r.getDurationMinutes());
        standardReport += String.format("\tduration (hours)\t%10.1f\n", r.getDurationHours());
        standardReport += String.format("\ttime now\t\t\t\t%s\n",r.getNow().getTime());
        standardReport += String.format("\tsequence start\t\t\t%s\n",r.getSequenceStart().getTime());
        standardReport += String.format("\tsequence finish\t\t\t%s\n",r.getSequenceFinish().getTime());
        standardReport += String.format("\ttotal elapsed time\t%10.1f\n",r.getTotalElapsedTimeSeconds());

        return standardReport;
    }

    public String getOutput() {
        String out = getStandardReport();
        out += getSpecialReport();
        return out;
    }

    /** Constructor */
    public Output (Intervalometer s, Movie m, Reality r) {
        this.i = s;
        this.m = m;
        this.r = r;
    }
}
