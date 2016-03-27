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
    //private SimpleTapping tapclass= new SimpleTapping();


    //shared vars
    private String hand;
    private double avgdelay;
    private double avgdelayleftfinger;
    private double avgdelayrightfinger;
    private ArrayList<String> numoftaps;


    //alternate tapping vars
    private long timegiven = 20*1000;
    private ArrayList<Long> leftfingerarr;
    private ArrayList<Long> rightfingerarr;
    private ArrayList<Long> simpletaps;
    private ArrayList<Integer> intList;
    private ArrayList<AltTapData> leftfingerxy,rightfingerxy;
    //Accuracy and precision variables
    private float D; //distance
    private float vectorx,vectory; //x2-x1,y2-y1

    //used for X,Y calculations (RAW values)
    private int Rorgx = 561;
    private int Lorgx = 159;
    private int orgy = 1041;





    private int indicator , indicator2, indicator3,indicator4;// for tapping scale 0:normal , <=2:hesistant ,  <=4:mild, >4  <=9:moderate , >9:severe


    public void fetchData() {

        indicator=0;
        indicator2=0;
        indicator3=0;

        customParse = new ParseFunctions();
        intList= new ArrayList<>();
        rightfingerarr = new ArrayList<>();
        leftfingerarr = new ArrayList<>();
        simpletaps= new ArrayList<>();
        numoftaps = new ArrayList<>(); //0: invol left taps - 1:invol right taps - 2:right finger taps - 3:left finger taps - 4:simple tap count

        rightfingerarr =  customParse.getParseData(ParseUser.getCurrentUser(),6,"TappingData","createdAt","ArrayList");
        leftfingerarr = customParse.getParseData(ParseUser.getCurrentUser(), 7, "TappingData", "createdAt", "ArrayList");
        simpletaps = customParse.getParseData(ParseUser.getCurrentUser(), 8, "TappingData", "createdAt", "ArrayList");
        //2: right finger , 3:left finger, 4:simple tapping results

        hand = customParse.getParseSingleColData(ParseUser.getCurrentUser(), 8,"TappingData", "createdAt", "hand");
        numoftaps = customParse.getParseDataTappingCount(ParseUser.getCurrentUser(),"TappingData","createdAt","numoftaps");

        for(String s : numoftaps) intList.add(Integer.valueOf(s)); //convert string counts to int

        Log.d("TappingTestR",rightfingerarr.toString());
        Log.d("TappingTestL",leftfingerarr.toString());
        Log.d("TappingTestS",simpletaps.toString());
        Log.d("TappingTest",hand+"");
        Log.d("TappingTest",intList.toString());


        avgdelay = average(simpletaps);
        avgdelayrightfinger = average(rightfingerarr);
        avgdelayleftfinger=average(leftfingerarr);

        Log.d("TappingTestAVG",""+avgdelay);
        Log.d("TappingTestAVGR",""+avgdelayrightfinger);
        Log.d("TappingTestAVGL",""+avgdelayleftfinger);

        //return new ArrayList();
    }
    public static double average(List<Long> list) {
        // 'average' is undefined if there are no elements in the list.
        if (list == null || list.isEmpty())
            return 0.0;
        // Calculate the summation of the elements in the list
        long sum = 0;
        int n = list.size();
        // Iterating manually is faster than using an enhanced for loop.
        for (int i = 0; i < n; i++)
            sum += list.get(i);
        // We don't want to perform an integer division, so the cast is mandatory.
        return ((double) sum) / n;
    }

     public void runAlgorithm() {
         // D = sqrt(xi+1 - xi)^2 +(yi+1-yi)^2




     }

    public int runTempAlgorithm() {

        //1- check avg delay on simple taps if >200 hesitant else >300 <400 mild  else >400 <500 moderate else >500 severe
        if(avgdelay<=200)
        {
            indicator = 0;
        } else if(avgdelay>200)
        {
            indicator = 1;
        }else if (avgdelay>300 && avgdelay<=400) {

            indicator =  2;
        }else if(avgdelay>400 && avgdelay<=500)
        {
            indicator =  3;

        }else if(avgdelay>500)
        {
            indicator =  4;
        }

        //2- check number of taps on alternate test >20 normal   <10 taps hesitant  <5 taps moderate to severe
        if(intList.get(2)<=5 || intList.get(3)<=5)
        {
            indicator2 = 4;
        }else if((intList.get(2)>5 && intList.get(2)<=10) || (intList.get(3)>5 && intList.get(3)<=10))
        {
            indicator2 = 2;
        }else if((intList.get(2)>10 && intList.get(2)<=20)|| (intList.get(3)>10 && intList.get(3)<=20))
        {
            indicator2 = 1;
        }else if (intList.get(2)>20 || intList.get(3)>20)
        {
            indicator2 = 0;
        }

        //3 - check avg delay on alt left and right taps if >200 <300 hesitant/mild  else if >300 <500 moderate  else if >500 severe
        if((avgdelayrightfinger<=450) || (avgdelayleftfinger<=450))
        {
            indicator3 =0;
        }else if ((avgdelayrightfinger>450 && avgdelayrightfinger<=500) || (avgdelayleftfinger>450 && avgdelayleftfinger<=500))
        {
            indicator3=1;
        }else if ((avgdelayrightfinger>500 && avgdelayrightfinger<=550) || (avgdelayleftfinger>500 && avgdelayleftfinger<=550))
        {
            indicator3=2;

        }else if ((avgdelayrightfinger>650) || (avgdelayleftfinger>650))
        {
            indicator3=4;
        }

        if(intList.get(0)>2 || intList.get(1)>2)
        {
            indicator4=1;
        }
        else if (intList.get(0)>4 || intList.get(1)>4)
        {
            indicator4=3;
        }

        int indic = indicator+indicator2+indicator3+indicator4;
        if(indic>0 && indic<=2)
            return 1;
        else if(indic>2 && indic<=4)
            return 2;
        else if(indic>4 && indic<=7)
            return 3;
        else if (indic >7 && indic<=9)
            return 4;
        else if (indic >9 )
            return 5;
        else return 0;
    }
    //public void displayResults() {}

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
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(16);
        renderer.setLabelsTextSize(16);
        renderer.setLegendTextSize(16);
        // The minimum and maximum number of digital set the X axis
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(5.5);

        // The minimum and maximum number of digital set the Y axis
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(300);
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
        XYSeriesRenderer sr = new XYSeriesRenderer();
        sr.setColor(Color.parseColor("#f06292"));
        sr.setLineWidth(5f);
        sr.setChartValuesTextSize(25);

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
        barSeries.add(avgdelay);
        barSeries.add(avgdelayrightfinger);
        barSeries.add(avgdelayleftfinger);
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
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(16);
        renderer.setLabelsTextSize(16);
        renderer.setLegendTextSize(16);
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

        sr.setChartValuesTextSize(25);

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
