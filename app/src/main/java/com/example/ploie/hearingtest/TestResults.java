package com.example.ploie.hearingtest;

import java.util.List;

/**
 * TestResults stores the information for the User's test results.
 */
public class TestResults {

    private List<String> frequencies;
    private List<String> decibels;
    private String participant_name;

    public String getParticipant_name() {
        return participant_name;
    }

    public void setParticpant_name(String participant_name) {
        this.participant_name = participant_name;
    }

    /**
     * This returns a list of strings of the decibels the user was able to hear specific tones at.
     * @return
     */
    public List<String> getDecibels() {
        return decibels;
    }

    /**
     * Used for adding a list of dB results.
     * @param decibels
     */
    public void setDecibels(List<String> decibels) {
        this.decibels = decibels;
    }

    /**
     * Returns the list of frequencies the user tested at.
     * @return
     */
    public List<String> getFrequencies() {
        return frequencies;
    }

    /**
     * Set the tested frequencies to a list of strings that represent frequencies.
     * @param frequencies
     */
    public void setFrequencies(List<String> frequencies) {
        this.frequencies = frequencies;
    }
}