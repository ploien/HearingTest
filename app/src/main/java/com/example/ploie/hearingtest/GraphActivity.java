package com.example.ploie.hearingtest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.graphview.DataPoint;
import com.example.graphview.GraphView;
import com.example.graphview.GridLabelRenderer;
import com.example.graphview.LineGraphSeries;
import com.example.graphview.PointsGraphSeries;
import com.example.graphview.StaticLabelsFormatter;


import java.util.ArrayList;



public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_graph);
        setTitle("Test Results");

        createGraph();
    }

    public void viewLegendButton(View view) {
        Intent intent = new Intent(this, GraphLegendActivity.class);
        startActivity(intent);
    }

    private void createGraph() {

        //DataPoint dataPoints[] = results.sortedPoints();
                /*{
                new DataPoint(125,0),
                new DataPoint(250, 25),
                new DataPoint(500, 15),
                new DataPoint(1000, 15),
                new DataPoint(2000, 30),
                new DataPoint(4000,15),
                new DataPoint(8000,20),
        }; */

        TestResults results = new TestResults();

        ArrayList<String> frequencies =  (ArrayList<String>) getIntent().getSerializableExtra("testFrequencies");

        // dummy values for testing
        /*
        frequencies.add("1000");
        frequencies.add("250");
        frequencies.add("125");
        frequencies.add("4000");
        frequencies.add("8000");
        frequencies.add("2000");
        frequencies.add("500");
        frequencies.add("1000");
        */



        ArrayList<String> decibels = (ArrayList<String>)getIntent().getSerializableExtra("testDecibels");

        //Dummy values
        /*
        decibels.add("80");
        decibels.add("30");
        decibels.add("45");
        decibels.add("60");
        decibels.add("125");
        decibels.add("85");
        decibels.add("105");
        decibels.add("55");
        */



        results.setFrequencies(frequencies);
        results.setDecibels(decibels);

        DataPoint[] dataPoints = results.sortedPoints();

        GraphView graph = findViewById(R.id.graph);

        //Set as many x-axis labels as there are points on the graph
        String[] horizontalLabels = new String[dataPoints.length];


        //Set label for each corresponding x-value
        for (int i = 0; i < dataPoints.length; i++) {

            Double d = dataPoints[i].getX();

            //value is divided by 1000 to display kHz, X-Values are in Hz
            horizontalLabels[i] = Double.toString((double)d.intValue()/1000.0);

        }

        //format and display labels to desired specifications

        GridLabelRenderer labels = graph.getGridLabelRenderer();
        StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graph);
        labels.setHorizontalLabelsVisible(true);
        labelsFormatter.setHorizontalLabels(horizontalLabels);
        labels.setNumHorizontalLabels(dataPoints.length);
        labels.setHumanRounding(false);
        labels.setLabelFormatter(labelsFormatter);
        labels.setNumVerticalLabels(7);
        labels.setPadding(64);
        labels.setTextSize(36);
        labels.setHorizontalAxisTitle("Low                             Frequency (kHz)                              High");
        labels.setVerticalAxisTitle(getString(R.string.GraphYAxisTitle));

        //these bounds should not need to be changed.
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(8000);

        graph.getViewport().setMinY(-20);
        graph.getViewport().setMaxY(120);


        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        //adds a line-graph to the graphView
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);
        series.setColor(Color.DKGRAY);

        //adds a point-graph to the graphView
        PointsGraphSeries<DataPoint> series3 = new PointsGraphSeries<>(dataPoints);
        series3.setShape(PointsGraphSeries.Shape.TRIANGLE);
        graph.addSeries(series3);

        graph.setTitle(getString(R.string.GraphTitle));





    }
}
