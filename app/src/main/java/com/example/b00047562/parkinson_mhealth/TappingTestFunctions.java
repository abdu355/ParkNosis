package com.example.b00047562.parkinson_mhealth;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.parse.ParseUser;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdu Sah on 1/28/2016.
 */
public class TappingTestFunctions {

    private ParseFunctions customParse;
    private SimpleTapping tapclass= new SimpleTapping();


    //shared vars
    private String hand;
    private long delay;
    private long delayleftfinger;
    private long delayrightfinger;
    private ArrayList<String> numoftaps;

    //alternate tapping vars
    private long timegiven = 20*1000;
    private ArrayList<Long> leftfingerarr;
    private ArrayList<Long> rightfingerarr;
    private ArrayList<Long> simpletaps;
    private ArrayList<Integer> intList;

    public LinearLayout SensorGraph;
    public View mChart;


    public ArrayList fetchData() {


        customParse = new ParseFunctions(tapclass.getApplication());
        intList= new ArrayList<>();
        rightfingerarr = new ArrayList<>();
        leftfingerarr = new ArrayList<>();
        simpletaps= new ArrayList<>();
        numoftaps = new ArrayList<>(); //0: invol left taps - 1:invol right taps - 2:right finger taps - 3:left finger taps - 4:simple tap count

        rightfingerarr =  customParse.getParseData(ParseUser.getCurrentUser(),2,"TappingData","createdAt","ArrayList");
        leftfingerarr = customParse.getParseData(ParseUser.getCurrentUser(), 3, "TappingData", "createdAt", "ArrayList");
        simpletaps = customParse.getParseData(ParseUser.getCurrentUser(), 4, "TappingData", "createdAt", "ArrayList");
        //2: right finger , 3:left finger, 4:simple tapping results

        hand = customParse.getParseSingleColData(ParseUser.getCurrentUser(), 4,"TappingData", "createdAt", "hand");
        numoftaps = customParse.getParseDataTappingCount(ParseUser.getCurrentUser(),"TappingData","createdAt","numoftaps");

        for(String s : numoftaps) intList.add(Integer.valueOf(s)); //convert string counts to int

        Log.d("TappingTestR",rightfingerarr.toString());
        Log.d("TappingTestL",leftfingerarr.toString());
        Log.d("TappingTestS",simpletaps.toString());
        Log.d("TappingTest",hand+"");
        Log.d("TappingTest",intList.toString());


        return new ArrayList();
    }

    public void runAlgorithm(ArrayList arrlist) {



    }
    public void displayResults() {


    }

    public XYMultipleSeriesDataset getDataSet1() {
        // The structure of data
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("Simple Tapping  Results ("+hand+" Hand)");
        for(int i=0;i<simpletaps.size();i++)
        {
            barSeries.add(simpletaps.get(i));
        }
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }
    public XYMultipleSeriesRenderer getRenderer1() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//      renderer.setChartTitle("Monthly billing");
//      // Set the title font size
//      renderer.setChartTitleTextSize(16);
        renderer.setXTitle("Tap Index");
        renderer.setYTitle("Delay");
        renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.WHITE);
        // The minimum and maximum number of digital set the X axis
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(5.5);
        // The minimum and maximum number of digital set the Y axis
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(500);
        renderer.addXTextLabel(1, "1-2");
        renderer.addXTextLabel(2, "2-3");
        renderer.addXTextLabel(3, "3-4");
        renderer.addXTextLabel(4, "4-5");
        renderer.addXTextLabel(5, "5-6");
        renderer.addXTextLabel(6, "6-7");
        renderer.addXTextLabel(7, "7-8");
        renderer.addXTextLabel(8, "8-9");
        renderer.addXTextLabel(9, "9-10");
        renderer.addXTextLabel(10,"last tap");
        renderer.setZoomButtonsVisible(true);
        // Set the renderer allows zooming
        renderer.setZoomEnabled(true);
        // Antialiasing
        renderer.setAntialiasing(true);
        // Set the background color
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.GRAY);
        // Each column is the color
        SimpleSeriesRenderer sr = new XYSeriesRenderer();
        sr.setColor(Color.parseColor("#f06292"));
        renderer.addSeriesRenderer(sr);
        // Set each post whether to display the numerical
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        // Approximate coordinate - axis X number (it does not display the abscissa)
        renderer.setXLabels(0);
        // Approximate coordinate - axis Y number
        renderer.setYLabels(6);
        // The scale line and the X axis left aligned text
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y axis and Y axis align = left
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // Allow about drag, but are not allowed to move up and down
        renderer.setPanEnabled(true, false);
        // Column width
        renderer.setBarSpacing(0.5f);
        // Set the X, Y axis unit font size
        renderer.setAxisTitleTextSize(20);
        return renderer;
    }

    public XYMultipleSeriesDataset getDataSet2() {
        // The structure of data
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("Average Delays ("+hand+" Hand)");
        barSeries.add(tapclass.average(simpletaps));
        barSeries.add(tapclass.average(rightfingerarr));
        barSeries.add(tapclass.average(leftfingerarr));
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }
    public XYMultipleSeriesRenderer getRenderer2() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//      renderer.setChartTitle("Monthly billing");
//      // Set the title font size
//      renderer.setChartTitleTextSize(16);
        renderer.setXTitle("Test Type");
        renderer.setYTitle("Delay");
        renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.WHITE);
        // The minimum and maximum number of digital set the X axis
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(5.5);
        // The minimum and maximum number of digital set the Y axis
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(1000);
        renderer.addXTextLabel(1, "Simple Taps");
        renderer.addXTextLabel(2, "Alt Taps(R)");
        renderer.addXTextLabel(3, "Alt Taps(L)");
        //renderer.addXTextLabel(4, "20");
        //renderer.addXTextLabel(5, "25");
        renderer.setZoomButtonsVisible(true);
        // Set the renderer allows zooming
        renderer.setZoomEnabled(true);
        // Antialiasing
        renderer.setAntialiasing(true);
        // Set the background color
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.GRAY);
        // Each column is the color
        SimpleSeriesRenderer sr = new XYSeriesRenderer();
        sr.setColor(Color.parseColor("#f06292"));
        renderer.addSeriesRenderer(sr);
        // Set each post whether to display the numerical
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        // Approximate coordinate - axis X number (it does not display the abscissa)
        renderer.setXLabels(0);
        // Approximate coordinate - axis Y number
        renderer.setYLabels(6);
        // The scale line and the X axis left aligned text
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y axis and Y axis align = left
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // Allow about drag, but are not allowed to move up and down
        renderer.setPanEnabled(true, false);
        // Column width
        renderer.setBarSpacing(0.5f);
        // Set the X, Y axis unit font size
        renderer.setAxisTitleTextSize(20);
        return renderer;
    }
}
