package com.example.ploie.hearingtest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_graph);

        createGraph();
    }

    private void createGraph() {

        DataPoint dataPoints[] = {
                new DataPoint(0,0),
                new DataPoint(1, 25),
                new DataPoint(2, 15),
                new DataPoint(3, 15),
                new DataPoint(4, 30),
                new DataPoint(5,15),
                new DataPoint(6,20),
                new DataPoint(7,15)
        };

        String[] frequencies = {"125", "250", "500", "1000", "2000", "4000", "8000"};

        GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);

        PointsGraphSeries<DataPoint> series3 = new PointsGraphSeries<>(dataPoints);
        graph.addSeries(series3);
        series3.setShape(PointsGraphSeries.Shape.TRIANGLE);
        series3.setColor(Color.GREEN);

        GridLabelRenderer XLabel = graph.getGridLabelRenderer();
        XLabel.setHorizontalAxisTitle("Frequency (Hz)");

        GridLabelRenderer YLabel = graph.getGridLabelRenderer();
        YLabel.setVerticalAxisTitle("Volume (dB)");

        StaticLabelsFormatter labels = new StaticLabelsFormatter(graph);
        labels.setHorizontalLabels(frequencies);

        

        //graph.setPadding(32,16,32,16);
}
}
