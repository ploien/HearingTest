package com.example.ploie.hearingtest;

public class TestResults {

    private String ID;
    private String[] frequencies = {"1000", "2000", "4000", "8000", "1000", "500", "250", "125", "1500", "3000", "6000", "750", "375", "187"};
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