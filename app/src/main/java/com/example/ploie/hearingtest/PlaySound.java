package com.example.ploie.hearingtest;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * PlaySound is used to generate tones at specific frequencies for the screening.
 */
public class PlaySound extends Activity {

    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>. Also modified by Peter Oien and Andrew Lundgren
    private final int duration = 2; // seconds
    private final int sampleRate = 44100;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private double freqOfTone = 1000; // hz
    private static final double MAX_AMPLITUDE = 32768;
    private double volume = (MAX_AMPLITUDE/Math.pow(Math.sqrt(10),7)); //30 decibels
    private double increase = Math.pow(Math.sqrt(10), (.5)); //increase by 5 decibels
    private double decrease = Math.sqrt(10); //Decrease by 10 decibels
    private int decibel = 30;
    private final String TAG = "PlaySound";


    private final byte generatedSnd[] = new byte[2 * numSamples];

    /**
     * Set the frequency that the tone should be played at.
     * @param newFreq
     */
    public void setFrequency(double newFreq) {
        freqOfTone = newFreq;
    }

    /**
     * Get the current decibel level of the tone that may be produced.
     * @return decibel
     */
    public int getDecibel() {
        return decibel;
    }


    /**
     * Used to increase the volume of the tone by 5dB at a time.
     */
    public void increaseVolume() {

        if((volume * increase) <= MAX_AMPLITUDE) {
            volume *= increase;
            decibel += 5;
        } else {
            volume = MAX_AMPLITUDE;
        }
    }

    /**
     * Used to decrease the volume of a tone by 10dB at a time.
     */
    public void decreaseVolume() {

        volume /= decrease;
        decibel -= 10;
    }

    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Use a new tread as this can take a while
        genTone();
        playSound();
//        final Thread thread = new Thread(new Runnable() {
//            public void run() {
//                genTone();
//                handler.post(new Runnable() {
//
//                    public void run() {
//                        playSound();
//                    }
//                });
//            }
//        });
//        thread.start();
    }

    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * volume));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
        genTone();
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error line 113, Thread interrupted");
        }
        audioTrack.release();
    }



}
