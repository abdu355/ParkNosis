package com.example.b00047562.parkinson_mhealth;

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

    public ArrayList<SpiralData> getAS() {
        return AS;
    }

    private ArrayList<SpiralData> AS;
    private ParseFunctions customParse;
    private int ASSize;

    public SpiralData(long timestamp, float x, float y) {
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
        AS= new ArrayList<>();
        AS = (customParse.getParseDataSpiral(ParseUser.getCurrentUser(), 0, "SpiralData", "createdAt", "ArrayList"));
        ASSize = AS.size();
        Log.d("AccelTest", AS.toString());
        return AS;
    }



}
