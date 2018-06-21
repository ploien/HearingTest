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
                } catch (Exception ignored) {}
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


    public void saveResults(String results){

    }

    public void displayResults(String results) {

    }


//     Below is an alpha example of how the test should run. Here are the steps for the code:
//
//     1. A boolean value is created to act as a gate, preventing testing on a specific frequency
//        from ending before testing conditions are met. A mock boolean conditionsMet currently
//        allows the while loop to always complete.
//
//     2. A for loop, simulating 16 tone tests is created.
//
//     3. The boolean, testingFrequency, is flagged as true in the beginning of each iteration of
//        the for loop. This ensures that each tone is tested.
//
//     4. A sleep command is used to simulate the playing of the tone.
//
//     5. The booleans 'waiting' and 'buttonClicked' are set in order to allow the user's input
//        to the yes / no buttons. The selection of either button allows the test to continue.
//        the test currently uses an infinite while loop to simulate waiting for user input.
//
//     6. If the user clicked yes or no, reactions in the test would take place. For now, it simply
//        changes the booleans declared at the beginning of TestActivity.
//
//     7. Since the conditions are always "Met", the testingFrequency boolean is switched to false,
//        indicating the completion of the current frequency.

    public String test() {

        boolean testingFrequency;
        boolean conditionsMet = false; // placeholder for conditions for frequency met (3/5 tones heard at appropriate level)

        PlaySound play;
        //yesButton.setEnabled(false);
        //noButton.setEnabled(false);

        double frequencies[] = {1000, 2000, 4000, 8000, 1000, 500, 250, 125};

        for (double frequency : frequencies) {


            play = new PlaySound();
            play.setFrequency(frequency);
            testingFrequency = true;

            boolean firstNo = false;
            boolean yesAfterNo = false;

            int count = 0;
            int yesCount = 0;
            int noCount = 0;


            while (testingFrequency) {

                conditionsMet = false;

                // Play the sound here, simulated by Thread sleeping
                play.genTone();
                play.playSound();
                //yesButton.setEnabled(true);
                //noButton.setEnabled(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitForThread(); // Current placeholder for test is waiting


                if (yesClicked) {
                    // do yes conditions (lower volume or tally result)
                    yesClicked = false;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Conditions after we hear no the first time
                    if (firstNo && (!yesAfterNo)) {
                        yesAfterNo = true;
                        ++yesCount;
                        ++count;
                    //After getting our first yes when increasing in volume
                    } else if (firstNo) {
                        ++yesCount;
                        ++count;
                    } else {
                        play.decreaseVolume();
                    }

                } else if (noClicked) {
                    // increase volume, reset tally results
                    noClicked = false;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Conditions after we hear no the first time
                    if (!firstNo) {
                        firstNo = true;
                        play.increaseVolume();
                    //start going back up after hearing no for the first time
                    } else {
                        if(!yesAfterNo) {
                            play.increaseVolume();
                        } else {
                            ++noCount;
                            ++count;
                        }
                    }

                }

                //Once we've found the lowest hearable volume
                //evaluate whether or no they've heard it 3/5 times
                if ((count == 5) && (yesCount > 2)) {
                    conditionsMet = true;
                } else if (noCount > 2) {
                    count = 0;
                    yesCount = 0;
                    noCount = 0;
                    play.increaseVolume();
                }


                if (conditionsMet) {
                    testingFrequency = false;

                }

            }

        }

        return "done";

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
    }

    public String screen() {
        String strongTestResults = test();
        String weakTestResults = test();

        return "Screening Succesful";
    }
}
