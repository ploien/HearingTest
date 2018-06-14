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


    // TODO:
    // 1. Get button input to work for yes/no
    // 2. Store results (String? ArrayList?)
    // 3. While loop for decibel testing

    public String test() {
        double frequencies[] = {1000, 2000, 4000, 8000, 1000, 500, 250, 125};

        PlaySound sound = new PlaySound();

        for (double frequency : frequencies) {

            sound.setFrequency(frequency);
            sound.playSound();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return "done";
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
