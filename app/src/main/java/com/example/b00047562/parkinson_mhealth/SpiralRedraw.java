package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.parse.ParseUser;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class SpiralRedraw extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout SpiralGraph;
    private View sChart;
    private Button BtnShowSpiral;
    private ParseFunctions customParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_redraw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SpiralGraph = (LinearLayout) findViewById(R.id.spirallinearlayout);
        BtnShowSpiral= (Button)findViewById(R.id.btn_drawspiral);


        BtnShowSpiral.setOnClickListener(this);

        customParse = new ParseFunctions(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_drawspiral:
                openChart(DrawView.spiralData);
                break;
        }
    }
    private void openChart(ArrayList<SpiralData> spiralData) {
        if (spiralData != null || spiralData.size() > 0) {
            long t = 0;
            XYMultipleSeriesDataset dataset = null;
            XYSeries xSeries = null;
            XYSeries ySeries = null;

            t = spiralData.get(0).getTimestamp();
            dataset = new XYMultipleSeriesDataset();

            xSeries = new XYSeries("X");
            ySeries = new XYSeries("Y");


            for (SpiralData data : spiralData) {
                xSeries.add(data.getTimestamp() - t, data.getX());
                ySeries.add(data.getTimestamp() - t, data.getY());
            }

            dataset.addSeries(xSeries);
            dataset.addSeries(ySeries);

            XYSeriesRenderer xRenderer = new XYSeriesRenderer();
            xRenderer.setColor(Color.RED);
            xRenderer.setPointStyle(PointStyle.CIRCLE);
            xRenderer.setFillPoints(true);
            xRenderer.setLineWidth(1);
            xRenderer.setDisplayChartValues(false);

            XYSeriesRenderer yRenderer = new XYSeriesRenderer();
            yRenderer.setColor(Color.GREEN);
            yRenderer.setPointStyle(PointStyle.CIRCLE);
            yRenderer.setFillPoints(true);
            yRenderer.setLineWidth(1);
            yRenderer.setDisplayChartValues(false);


            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(0);
            multiRenderer.setLabelsColor(Color.RED);
            multiRenderer.setChartTitle("t vs (x,y)");
            multiRenderer.setXTitle("Spiral Data");
            multiRenderer.setYTitle("Values of Spiral");
            multiRenderer.setZoomButtonsVisible(true);
            for (int i = 0; i < spiralData.size(); i++) {

                multiRenderer.addXTextLabel(i + 1, ""
                        + (spiralData.get(i).getTimestamp() - t));
            }
            for (int i = 0; i < 12; i++) {
                multiRenderer.addYTextLabel(i + 1, ""+i);
            }

            multiRenderer.addSeriesRenderer(xRenderer);
            multiRenderer.addSeriesRenderer(yRenderer);

            // Getting a reference to LinearLayout of the MainActivity Layout

            // Creating a Line Chart
            sChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                    multiRenderer);


            // Adding the Line Chart to the LinearLayout
            SpiralGraph.addView(sChart);

        }
    }
}
