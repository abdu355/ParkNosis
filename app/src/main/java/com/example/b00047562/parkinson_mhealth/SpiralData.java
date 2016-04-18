package com.example.b00047562.parkinson_mhealth;

import android.content.Context;
import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Abdu Sah on 12/6/2015.
 */
public class SpiralData {
    private long timestamp;
    private float x;
    private float y;
    private ArrayList<SpiralData> AS;
    private ArrayList<SpiralData> DAS;
    private ParseFunctions customParse;

    public SpiralData(long timestamp, float x, float y) {

        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
    }
    public SpiralData(long timestamp, float x, float y,Context context) {
        customParse = new ParseFunctions();
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y;
    }

    public ArrayList<SpiralData> getSpiralData() {
//        AS = new ArrayList<>();
//        AS = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 3, "SpiralData", "createdAt", "ArrayList"));
//
//        //Log.d("SpiralTest", AS.toString());
//        AS2 = new ArrayList<>();
//        AS2 = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 2, "SpiralData", "createdAt", "ArrayList"));
//
//        //Log.d("SpiralTest", "\n"+AS2.toString());
//        AS.addAll(AS2);
//        //Log.d("SpiralTest[AS combined]", "\n AS combined" + AS.toString());
        AS= new ArrayList<>();
        AS = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 1, "SpiralData", "createdAt", "ArrayList"));
        return AS;
    }
    public ArrayList<SpiralData> getDynamicSpiralData() {
//        DAS= new ArrayList<>();
//        DAS = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 1, "SpiralData", "createdAt", "ArrayList"));
//
//        //Log.d("SpiralTest", DAS.toString());
//
//        DAS2= new ArrayList<>();
//        DAS2 = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 0, "SpiralData", "createdAt", "ArrayList"));
//
//        //Log.d("SpiralTest", DAS2.toString());
//
//        DAS.addAll(DAS2);
//        //Log.d("SpiralTest[AS combined]", "\n AS combined" + DAS.toString());

        DAS= new ArrayList<>();
        DAS = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 0, "SpiralData", "createdAt", "ArrayList"));
        return DAS;
    }

    public ArrayList<SpiralData> getAS() {
        getSpiralData();
        return AS;
    }
    public ArrayList<SpiralData> getDAS() {
        getDynamicSpiralData();
        return DAS;
    }


}
