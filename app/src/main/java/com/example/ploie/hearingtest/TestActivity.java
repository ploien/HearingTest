package com.example.ploie.hearingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.*;

public class TestActivity extends AppCompatActivity {

    private boolean waiting = false;
    private boolean buttonClicked = false;
    private boolean yesClicked = false;
    private boolean noClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();

    }

    public void onYesClick(View view) {
        if (waiting == true && buttonClicked == false) {
            yesClicked = true;
            buttonClicked = true;
        }
    }

    public void onNoClick(View view) {
        if (waiting == true && buttonClicked == false) {
            noClicked = true;
            buttonClicked = true;
        }
    }


    public void onStartClick(View view) {
        /*
        String results = this.screen();
        saveResults(results);
        displayResults(results);
        */

        threadTester(view);
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
        boolean conditionsMet = true; // placeholder for conditions for frequency met (3/5 tones heard at appropriate level)

        for (int i = 0; i < 16; ++i ) {

            testingFrequency = true;
            while (testingFrequency == true) {
                // Play the sound here, simulated by Thread sleeping
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waiting = true;
                buttonClicked = false;

                while (buttonClicked == false); // Current placeholder for test is waiting

                if (yesClicked == true) {
                    // do yes conditions (lower volume or tally result)
                    waiting = false;
                    yesClicked = false;
                }
                else if (noClicked == true) {
                    // increase volume, reset tally results
                    waiting = false;
                    noClicked = false;
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
    public String buttonOnClick(View v) {
        String button = "";
        switch (v.getId()) {
            case R.id.yesButton:
                button = "yes ";
                break;
            case R.id.noButton:
                button = "no ";
                break;
        }

        return button;
    }
    public String threadTester(View v) {

        String string = "";

        for(int i = 0; i < 10; i++) {
            string += buttonOnClick(v);
        }

        System.out.println(string);
        return string;

    }
}
