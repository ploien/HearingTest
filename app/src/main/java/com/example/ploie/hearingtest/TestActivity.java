package com.example.ploie.hearingtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String results = this.Screen();
        saveResults(results);
        displayResults(results);
    }

    public void saveResults(String results){

    }
    
    public void displayResults(String results) {

    }

    public String Screen() {
        String strongTestResults = test();
        String weakTestResults = test();
    }
}
