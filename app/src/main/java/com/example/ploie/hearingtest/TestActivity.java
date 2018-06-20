package com.example.ploie.hearingtest;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.*;

public class TestActivity extends AppCompatActivity {

    public static final Object monitor = new Object();
    public static boolean monitorState = false;


    private boolean testing = false;
    private boolean yesClicked = false;
    private boolean noClicked = false;

    //Button yesButton = (Button) findViewById(R.id.yesButton);
    //Button noButton = (Button) findViewById(R.id.noButton);
    //Button startButton = (Button) findViewById(R.id.button3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();

    }

    public static void unlockWaiter() {
        synchronized (monitor) {
            monitorState = false;
            monitor.notifyAll();
        }
    }


    public void onYesClick(View view) {

        yesClicked = true;
        unlockWaiter();
    }

    public void onNoClick(View view) {

        noClicked = true;
        unlockWaiter();
    }

    public static void waitForThread() {

        monitorState = true;
        while (monitorState) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (Exception e) {
                }
            }
        }

    }


    public void onStartClick(View view) {

        //startButton.setEnabled(false);
        final Thread thread = new Thread(new Runnable() {
            public Handler mHandler;

            public void run() {
                Looper.prepare();
                test();
            }
        });
        thread.start();
    }


    public void saveResults(String results) {

    }

    public void displayResults(String results) {

    }


    // This is the second iteration of the hearing test. It is a bit more complicated than the previous one.
    // Firstly, there are now three loops in the test.
    // 1. The for loop will keep track of what frequency we are testing. At the beginning of each for loop
    //    iteration, the tone is reinitialized to a starting volume. All variables that track the testing
    //    environment are reset to their initial values.
    // 2. The first while loop finds what volume the tone should be tested at. The first time a user says yes,
    //    it is noted. Once the user says no, however, they can keep saying no until the tone can be heard again.
    //    Once the user states yes again, the loop ends.
    // 3. The third while loop tests the hearing of the user. If the tone can be heard 3 out of 5 times, the test
    //    moves on to the next frequency. If the tone was only heard 2 / 5 times, the frequency is retested.

    public String test() {

        boolean testingFrequency;
        boolean volumeFound; // placeholder for conditions for frequency met (3/5 tones heard at appropriate level)


        //yesButton.setEnabled(false);
        //noButton.setEnabled(false);

        double frequencies[] = {1000, 2000, 4000, 8000, 1000, 500, 250, 125};

        int yes; // How many times was the tone heard?
        int no; //  How many times was the tone not heard?
        boolean firstNo; // Has the user not heard the tone yet?
        boolean firstYes; // Has the user heard the tone yet?

        for (int i = 0; i < frequencies.length; ++i) {
            PlaySound play = new PlaySound(); // Initialize a new PlaySound object (I think that we can make this work without re-initializing this, but for now it does. - AL)
            play.initializeVolume(); // Initialize the volume of the tone
            play = null; // lose the object to be cleaned up by garbage collection
            yes = 0; // The user hasn't had a chance to hear a tone yet, so yes and no are initialized to 0
            no = 0;
            firstNo = false;
            firstYes = false;
            testingFrequency = true; // We are currently testing a new frequency!
            volumeFound = false; // The volume that the tone should be reproduced at has not yet been determined.

            while (!volumeFound) {

                play = new PlaySound(); // Initialize the playSound object
                play.setFrequency(frequencies[i]); // set the frequency

                play.playSound(); // play the tone

                // I commented this sleep out because I learned why our AudioTracks were crashing. We can only have 32 at a time, so I learned
                // that AudioTrack.release() will free up all of the tracks currently holding memory. However, if I clear it too early, the tone
                // will not play. I moved the thread.sleep into the .playSound() function.\

//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                this.waitForThread(); // The gate is locked until the user presses yes or no

                if (yesClicked == true) {
                    yesClicked = false;
                    firstYes = true;
                    if (!firstNo) {
                        play.decreaseVolume();
                    }
                } else if (noClicked == true) {
                    noClicked = false;
                    firstNo = true;
                    play.increaseVolume();
                    firstYes = false;
                }

                if (firstYes && firstNo) {
                    volumeFound = true;
                }
                play = null;
            }

            while (testingFrequency == true) {
                play = new PlaySound();
                play.setFrequency(frequencies[i]);
                play.playSound();

//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                this.waitForThread();

                if (yesClicked == true) {
                    yesClicked = false;
                    ++yes;
                } else if (noClicked == true) {
                    noClicked = false;
                    ++no;
                }

                if (no > 2) { // if the user heard the tone only 2 times
                    i--; // re-test the frequency
                    testingFrequency = false;
                } else if (yes > 2) { // if the user passed the test for the given tone
                    testingFrequency = false;
                }
                play = null;
            }

        }

        return "done";
    }

//        Below is starter code for the actual test. Above is the demo test.
//        double frequencies[] = {1000, 2000, 4000, 8000, 1000, 500, 250, 125};
//
//        PlaySound sound = new PlaySound();
//
//        for (double frequency : frequencies) {
//
//            sound.setFrequency(frequency);
//            sound.playSound();
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return "done";

    public String screen() {
        String strongTestResults = test();
        String weakTestResults = test();

        return "Screening Succesful";
    }
}
