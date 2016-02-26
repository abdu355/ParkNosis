package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdu Sah on 1/28/2016.
 */
public class TappingTestFunctions {

    private ParseFunctions customParse;
    private SimpleTapping tap = new SimpleTapping();

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


    public ArrayList fetchData() {

        customParse = new ParseFunctions(tap.getApplication());
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
}
