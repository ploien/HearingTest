package com.example.ploie.hearingtest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.graphview.DataPoint;
import com.example.graphview.GraphView;
import com.example.graphview.GridLabelRenderer;
import com.example.graphview.LineGraphSeries;
import com.example.graphview.PointsGraphSeries;
import com.example.graphview.StaticLabelsFormatter;

import java.util.ArrayList;
import java.util.List;


public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_graph);

        createGraph();
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
//        frequencies.add("1000");
//        frequencies.add("250");
//        frequencies.add("125");
//        frequencies.add("4000");
//        frequencies.add("8000");
//        frequencies.add("2000");
//        frequencies.add("500");
//        frequencies.add("1000");

        ArrayList<String> decibels = (ArrayList<String>) getIntent().getSerializableExtra("testDecibels");
//        decibels.add("10");
//        decibels.add("30");
//        decibels.add("45");
//        decibels.add("60");
//        decibels.add("70");
//        decibels.add("85");
//        decibels.add("105");
//        decibels.add("35");


        results.setFrequencies(frequencies);
        results.setDecibels(decibels);
        results.sortedPoints();

        DataPoint dataPoints[] = results.sortedPoints();

        GraphView graph = findViewById(R.id.graph);
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        //graph.addSeries(series);

        graph.getViewport().setMinX(125);
        graph.getViewport().setMaxX(8000);

        graph.getViewport().setMinY(-20);
        graph.getViewport().setMaxY(125);


        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);


        PointsGraphSeries<DataPoint> series3 = new PointsGraphSeries<>(dataPoints);
        series3.setShape(PointsGraphSeries.Shape.POINT);
        //series3.setColor(Color.YELLOW);
        graph.addSeries(series3);

        GridLabelRenderer XLabel = graph.getGridLabelRenderer();
        XLabel.setHorizontalAxisTitle("Frequency (Hz)");

        GridLabelRenderer YLabel = graph.getGridLabelRenderer();
        YLabel.setVerticalAxisTitle("Volume (dB)");

        //graph.setPadding(32,16,32,16);
}
}
