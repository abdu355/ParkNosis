package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Abdu Sah on 1/28/2016.
 */
public class TappingTestFunctions {

    private ParseFunctions customParse;
    private SimpleTapping tap = new SimpleTapping();
    //shared vars
    private String hand;
    //private String array;
    private long delay;

    //alternate tapping vars
    private long timegiven = 20*1000;
    private ArrayList<Long> leftfingerarr;
    private ArrayList<Long> rightfingerarr;
    private ArrayList<Long> simpletaps;
    private long delayleftfinger;
    private long delayrightfinger;

    public ArrayList fetchData() {

        customParse = new ParseFunctions(tap.getApplication());
        rightfingerarr = new ArrayList<>();
        leftfingerarr = new ArrayList<>();
        simpletaps= new ArrayList<>();

        rightfingerarr =  customParse.getParseData(ParseUser.getCurrentUser(),0,"TappingData","createdAt","ArrayList");
        leftfingerarr = customParse.getParseData(ParseUser.getCurrentUser(), 1, "TappingData", "createdAt", "ArrayList");
        simpletaps = customParse.getParseData(ParseUser.getCurrentUser(), 2, "TappingData", "createdAt", "ArrayList");
        //0: right finger , 1:left finger, 2:simple tapping results



        Log.d("TappingTestR",rightfingerarr.toString());
        Log.d("TappingTestL",leftfingerarr.toString());
        Log.d("TappingTestS",simpletaps.toString());

        return new ArrayList();
    }

    public void runAlgorithm(ArrayList arrlist) {



    }
    public void displayResults() {


    }
}
