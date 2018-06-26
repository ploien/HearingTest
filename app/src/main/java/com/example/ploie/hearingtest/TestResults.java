package com.example.ploie.hearingtest;

public class TestResults {

    private String ID;
    private String[] frequencies;
    private String[] decibels;

    public String[] getFrequencies() {
        return frequencies;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String[] getDecibels() {
        return decibels;
    }

    public void setDecibels(String[] decibels) {
        this.decibels = decibels;
    }
}