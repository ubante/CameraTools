package com.ubante.cameratools;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubante.cameratools.models.Intervalometer;
import com.ubante.cameratools.models.Movie;
import com.ubante.cameratools.models.Output;
import com.ubante.cameratools.models.Reality;

/**
 * To do:
 * - change background to something dark
 * -- add this as an option
 * - handle NPE on empty et fields
 * - scrollable
 * -- change from FrameLayout?
 * - clear screen
 * - make it clear what the inputted fields are for
 * - if shutter speed is too fast (read FPS),
 * -- warn and suggest exposure changes and ND
 * - maybe accept the photo's index number so the user can have it recorded somewhere
 * - maybe accept a countdown timer
 * - have the intervalometer changable to make delay be between exposure starts instead of between stops and starts
 */
public class TimelapseActivity extends ActionBarActivity {

    public EditText etDelayBetweenFrames, etNumberOfFrames, etShutterSpeed,
            etFps, etDelayToFirstFrame;
    public TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse);

        etDelayBetweenFrames = (EditText) findViewById(R.id.etDelayBetweenFrames);
        etNumberOfFrames = (EditText) findViewById(R.id.etNumberOfFrames);
        etShutterSpeed = (EditText) findViewById(R.id.etShutterSpeed);
        etFps = (EditText) findViewById(R.id.etFps);
        etDelayToFirstFrame = (EditText) findViewById(R.id.etDelayToFirstFrame);
        tvOutput = (TextView) findViewById(R.id.tvOutput);

        tvOutput.setText(Output.getReminders());
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intervalometry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_timelapse, container, false);
//            return rootView;
//        }
//    }

    public void onCompute(View v) {
        // what happens when the Compute button is clicked
        String delay, numberOfFrames, shutterSpeed, delayToFirstFrame, fps;

        try {
            delay = etDelayBetweenFrames.getText().toString();
            numberOfFrames = etNumberOfFrames.getText().toString();
            shutterSpeed = etShutterSpeed.getText().toString();
            delayToFirstFrame = etDelayToFirstFrame.getText().toString();
            fps = etFps.getText().toString();
        } catch (NullPointerException npe) {
            Toast.makeText(this, "found some empty fields, using dummy values", Toast.LENGTH_SHORT).show();
            delay = "5";
            numberOfFrames = "300";
            shutterSpeed = "1";
            delayToFirstFrame = "1";
            fps = "30";
        }

        Intervalometer intervalometer = new Intervalometer(delay,numberOfFrames,shutterSpeed);
        intervalometer.setStartDelayHours(Integer.parseInt(delayToFirstFrame));
        Movie movie = new Movie(intervalometer);
        movie.setFPS(Float.parseFloat(fps));
        Reality reality = new Reality(intervalometer);
        Output output = new Output(intervalometer,movie,reality);
        String longOutput = output.getOutput();
        String simpleOutput = String.format("You gave %s delay for %s frames with %s shutter speed.",delay,numberOfFrames,shutterSpeed);
        String combinedOutput = String.format("%s\n%s\n",simpleOutput,longOutput);
        tvOutput.setText(longOutput);
    }
}

/*
===============================
Shooting in your sleep
-------------------------------
Settings:
	number of frames	    1000
	shutter speed			   1.00000
	delay				      10.0
-------------------------------
Movie:
	duration (seconds)	      33.3
	duration (minutes)	       0.6
	FPS					      30.0
	speedup				     330.0
-------------------------------
Reality:
	duration (seconds)	   10999.0
	duration (minutes)	     183.3
	duration (hours)	       3.1
	time now				Mon Jan 27 23:10:33 PST 2014
	sequence start			Tue Jan 28 05:10:33 PST 2014
	sequence finish			Tue Jan 28 08:13:52 PST 2014
	total elapsed time	   32599.0
-------------------------------
Notes:
The exposure is 500.0 times (8.5 stops) that of the Sunny 16 rule.
This sequence will take 15000.0 MB on your 32 GB card (45.8%).
Warning: 1000 is too many frames for one movie made with the free version of TLTimelapse
Warning: your duration (3.06) is >2hours - consider Aperature priority.
Fatal: insufficient battery power; you need 1.451980 batteries.
 */