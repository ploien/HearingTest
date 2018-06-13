package com.example.ploie.hearingtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.Screen();
    }

    public void Screen() {
        PlaySound sound = new PlaySound();
        sound.playSound();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sound.setFrequency(500);
        sound.playSound();
    }
}
