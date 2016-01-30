package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Abdu Sah on 1/28/2016.
 */
public class TappingTestFunctions {

    private ParseFunctions customParse;
    //shared vars
    private String hand;
    //private String array;
    private long delay;

    //alternate tapping vars
    private long timegiven = 5000;
    private ArrayList<Long> leftfingerarr;
    private ArrayList<Long> rightfingerarr;
    private long delayleftfinger;
    private long delayrightfinger;


    public ArrayList fetchData() {
        customParse = new ParseFunctions();
        rightfingerarr = new ArrayList<>();
        rightfingerarr =  customParse.getParseData(ParseUser.getCurrentUser(),0,"TappingData","createdAt","ArrayList");

        Log.d("TappingTest",rightfingerarr.toString());

        return new ArrayList();
    }

    public void runAlgorithm(ArrayList arrlist) {
    //Asynchronous tasks


    }
    public void displayResults() {
    //Asynchronous tasks

    }
}
