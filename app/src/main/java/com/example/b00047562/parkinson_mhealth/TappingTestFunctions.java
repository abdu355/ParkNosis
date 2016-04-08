package com.example.b00047562.parkinson_mhealth;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.parse.ParseUser;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.lang.reflect.Array;
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
    private ArrayList<AltTapData> outleftxy,outrightxy, combR,combL;

    //Accuracy and precision variables
    private float D; //distance
    private float vectorx,vectory,timevec, vectorxvarR, vectoryvarR,vectorxvarL,vectoryvarL,vectorrightX,vectorleftX,vectorrightY,vectorleftY; //x2-x1,y2-y1
    ArrayList<Float> distarr,distarr2, distOTPR, distOTPL,distCVarrR,distCVarrL;


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
        outleftxy = new ArrayList<>();
        outrightxy = new ArrayList<>();
        combR= new ArrayList<>();
        combL = new ArrayList<>();
        distarr = new ArrayList<>();
        distarr2 = new ArrayList<>();
        distOTPR = new ArrayList<>();
        distOTPL= new ArrayList<>();
        distCVarrR= new ArrayList<>();
        distCVarrL= new ArrayList<>();
        leftfingerxy= new ArrayList<>();
        rightfingerxy= new ArrayList<>();

        numoftaps = new ArrayList<>(); //0: invol left taps - 1:invol right taps - 2:right finger taps - 3:left finger taps - 4:simple tap count

        //"Left","Right","LeftXY","RightXY","outL","outR","combL","combR"

        outleftxy = customParse.getParseDataAltTap(ParseUser.getCurrentUser(), "outL", "TappingData", "createdAt", "ArrayList"); //taps outside the buttons
        outrightxy = customParse.getParseDataAltTap(ParseUser.getCurrentUser(), "outR", "TappingData","createdAt","ArrayList");

        combR = customParse.getParseDataAltTap(ParseUser.getCurrentUser(), "combR", "TappingData","createdAt","ArrayList");
        combL = customParse.getParseDataAltTap(ParseUser.getCurrentUser(), "combL", "TappingData","createdAt","ArrayList");

        rightfingerxy = customParse.getParseDataAltTap(ParseUser.getCurrentUser(), "RightXY", "TappingData","createdAt","ArrayList"); //taps inside the buttons
        leftfingerxy = customParse.getParseDataAltTap(ParseUser.getCurrentUser(), "LeftXY", "TappingData","createdAt","ArrayList");

        rightfingerarr =  customParse.getParseData(ParseUser.getCurrentUser(), "Right", "TappingData", "createdAt", "ArrayList");
        leftfingerarr = customParse.getParseData(ParseUser.getCurrentUser(), "Left", "TappingData", "createdAt", "ArrayList");
        simpletaps = customParse.getParseData(ParseUser.getCurrentUser(),"simple" , "TappingData", "createdAt", "ArrayList");
        //2: right finger , 3:left finger, 4:simple tapping results

        hand = customParse.getParseSingleColData(ParseUser.getCurrentUser(), 10, "TappingData", "createdAt", "hand");

        numoftaps = customParse.getParseDataTappingCount(ParseUser.getCurrentUser(),"TappingData","createdAt","numoftaps");

        for(String s : numoftaps) intList.add(Integer.valueOf(s)); //convert string counts to int

       // Log.d("TappingTestR",rightfingerarr.toString());
       // Log.d("TappingTestL",leftfingerarr.toString());
       //Log.d("TappingTestS",simpletaps.toString());
       // Log.d("TappingTest",hand+"");
        Log.d("TappingTest",intList.toString());


        avgdelay = average(simpletaps);
        avgdelayrightfinger = average(rightfingerarr);
        avgdelayleftfinger=average(leftfingerarr);

        //Log.d("TappingTestAVG",""+avgdelay);
       // Log.d("TappingTestAVGR",""+avgdelayrightfinger);
       // Log.d("TappingTestAVGL",""+avgdelayleftfinger);

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

         //GET MTS - SPEED FACTOR--------------------------------------------------------------------------------
         // D = sqrt(xi+1 - xi)^2 +(yi+1-yi)^2

        //taps will start from right button
         int size;
         //inside the buttons ONLY !
         if(combR.size()>combL.size())
             size = combL.size();
         else
             size = combR.size();

         for(int i=1;i<size;i++) {
             vectorx = combL.get(i).getX() - combR.get(i-1).getX();//xi+1-xi
             vectory = combL.get(i).getY() - combR.get(i-1).getY();//yi+1-yi
             timevec= combL.get(i).getTimestamp()-combR.get(i-1).getTimestamp();//ti+1-ti

             float vectorx2 = vectorx * vectorx;
             float vectory2 = vectory * vectory;
             distarr2.add((float) Math.sqrt(vectorx2 + vectory2));
             distarr.add((float) Math.sqrt(vectorx2 + vectory2)/timevec);

         }
         float MTS = sum(distarr); //higher = better
         Log.d("MTS",""+MTS);

         //GET OTP - ACCURACY FACTOR--------------------------------------------------------------------------------
         float initialleftx = combL.get(0).getX(); //center left X
         float initiallefty = combL.get(0).getY(); //center left Y
         float initialrightx = combR.get(0).getX(); //center right X
         float initialrighty = combR.get(0).getY(); //center right Y

         //taps will start froom right button
         //xi-1 - initialrigght   xi - initialleftx


         for(int i=0 ;i<size;i++) {

             vectorxvarR = combR.get(i).getX() - initialrightx;
             vectoryvarR = combR.get(i).getY() - initialrighty;
             vectorxvarL = combL.get(i).getX() - initialleftx;
             vectoryvarL = combL.get(i).getY() - initiallefty;


             float vectoryotp2R = vectoryvarR * vectoryvarR;
             float vectorxotp2R = vectorxvarR * vectorxvarR;

             float vectoryotp2L = vectoryvarL * vectoryvarL;
             float vectorxotp2L = vectorxvarL * vectorxvarL;


             distOTPR.add((float) Math.sqrt(vectorxotp2R + vectoryotp2R)); //distances from center field to taps on right button
             distOTPL.add((float) Math.sqrt(vectoryotp2L + vectorxotp2L)); //distances from center field to taps on left button
         }
         float OTPL = sum(distOTPL)/combL.size(); //lower = better
         float OTPR = sum(distOTPR)/combR.size();
         Log.d("OTP",""+OTPL+" "+OTPR);

         //find CV - ACCURACY FACTOR --------------------------------------------------------------------------------
         float ODT = sum(distarr2)/((combL.size()+combR.size())/2);
         Log.d("ODT",""+ODT);

         int size2;
         //inside the buttons ONLY !
         if(rightfingerxy.size()>leftfingerxy.size())
            size2 = leftfingerxy.size();
         else
            size2 = rightfingerxy.size();

         for(int i=0;i<size2;i++) {

             vectorrightX = rightfingerxy.get(i).getX() - initialrightx;
             vectorrightY = rightfingerxy.get(i).getY() - initialrighty;
             vectorleftX = leftfingerxy.get(i).getX() - initialleftx;
             vectorleftY = leftfingerxy.get(i).getY() - initiallefty;

             float vectoryvar2R = vectorrightY * vectorrightY;
             float vectorxvar2R = vectorrightX * vectorrightX;

             float vectoryvar2L = vectorleftY * vectorleftY;
             float vectorxvar2L = vectorleftX * vectorleftX;

             distCVarrR.add((float) Math.sqrt(vectorxvar2R + vectoryvar2R));
             distCVarrL.add((float) Math.sqrt(vectorxvar2L + vectoryvar2L));

             Float cvR[] = new Float[distCVarrR.size()];
             Float cvL[] = new Float[distCVarrL.size()];

             distCVarrL.toArray(cvL);
             distCVarrR.toArray(cvR);

             double varR = getVariance(cvR,cvR.length); //CV of Right button taps
             double varL = getVariance(cvL,cvL.length); //CV of l;eft button taps

             Log.d("CV",varR+" "+varL);

         }

     }
    public double getPrecision()//for both right and left
    {
        //Log.d("TapPrec",""+intList.get(2).doubleValue()+" "+intList.get(4).doubleValue());
        return ((intList.get(9).doubleValue() / (intList.get(2).doubleValue())))*1.0;


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
        if(intList.get(8)<=5 || intList.get(9)<=5)
        {
            indicator2 = 4;
        }else if((intList.get(8)>5 && intList.get(8)<=10) || (intList.get(9)>5 && intList.get(9)<=10))
        {
            indicator2 = 2;
        }else if((intList.get(8)>10 && intList.get(8)<=20)|| (intList.get(9)>10 && intList.get(9)<=20))
        {
            indicator2 = 1;
        }else if (intList.get(8)>20 || intList.get(9)>20)
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

    public Float sum(List<Float> list) {
        Float sum = 0f;
        for (Float i:list)
            sum = sum + i;
        return sum;
    }
    double getMean(Float[] data,int size)
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    double getVariance(Float[] data,int size)
    {
        double mean = getMean(data,size);
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }
}
