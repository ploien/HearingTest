package com.example.ploie.hearingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.*;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();

    }

    public void onStartClick(View view) {
        String results = this.screen();
        saveResults(results);
        displayResults(results);
    }
    public void saveResults(String results){

    }

    public void displayResults(String results) {

    }

    public boolean onYesClick(View view) {
        return true;
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
}
