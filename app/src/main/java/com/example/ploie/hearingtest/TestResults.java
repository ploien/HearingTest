package com.example.ploie.hearingtest;

import java.util.List;

public class TestResults {

    private List<String> frequencies;
    private List<String> decibels;


    public List<String> getDecibels() {
        return decibels;
    }

    public void setDecibels(List<String> decibels) {
        this.decibels = decibels;
    }

    public List<String> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(List<String> frequencies) {
        this.frequencies = frequencies;
    }
}