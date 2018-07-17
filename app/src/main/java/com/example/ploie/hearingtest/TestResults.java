package com.example.ploie.hearingtest;



import android.util.Log;

import com.example.graphview.DataPoint;
import java.util.ArrayList;


/**
 * TestResults stores the information for the User's test results.
 */
public class TestResults {

    private ArrayList<String> frequencies;
    private ArrayList<String> decibels;
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
    public ArrayList<String> getDecibels() {
        return decibels;
    }

    /**
     * Used for adding a list of dB results.
     * @param decibels
     */
    public void setDecibels(ArrayList<String> decibels) {
        this.decibels = decibels;
    }

    /**
     * Returns the list of frequencies the user tested at.
     * @return
     */
    public ArrayList<String> getFrequencies() {
        return frequencies;
    }

    /**
     * Set the tested frequencies to a list of strings that represent frequencies.
     * @param frequencies
     */
    public void setFrequencies(ArrayList<String> frequencies) {
        this.frequencies = frequencies;
    }



    /**
     * SortedPoints takes the list of decibels and frequencies and combines them
     * into a sorted list of DataPoints.
     * @return List<DataPoint> to be used in creating the graph that will display
     * the results
     */
    public DataPoint[] sortedPoints() {


        //Get rid of lower result of the two 1000 hZ tests
        //First 1000 is at index 0 and 4

        int firstIndex = 0;
        int secondIndex = 4;
        int value = 0;
        boolean isGreater = Double.parseDouble(decibels.get(firstIndex)) >= Double.parseDouble(decibels.get(secondIndex));

        //Variable for switch statement.
        //TODO: Make it work
        /*
        if (isGreater) {
            value = 1;
        }


        //Case 1, the value at the first index is greater, case 0, the value at secondIndex is greater.
        if (value == 1) {
            frequencies.remove(secondIndex);
            decibels.remove(secondIndex);
        } else if (value == 0){
                frequencies.remove(firstIndex);
                decibels.remove(firstIndex);
        }
        */



        //create array to store the sorted data points
        DataPoint[] dataPoints = new DataPoint[frequencies.size()];

        //Move the frequencies and decibels from their respective lists into an array of Point objects
        for(int i = 0; i < dataPoints.length; i++) {

                DataPoint p = new DataPoint(Double.parseDouble(frequencies.get(i)), Double.parseDouble(decibels.get(i)));
                dataPoints[i] = p;
        }


        //BubbleSort DataPoints in ascending order by decibel level.
        int n = dataPoints.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (dataPoints[j].getX() > dataPoints[j+1].getX())
                {
                    // swap temp and arr[i]
                    DataPoint temp = dataPoints[j];
                    dataPoints[j] = dataPoints[j+1];
                    dataPoints[j+1] = temp;

                }


        return dataPoints;
    }
}

