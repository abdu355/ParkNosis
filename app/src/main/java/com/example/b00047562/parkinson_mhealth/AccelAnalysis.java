package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;

import almadani.com.shared.AccelData;

/**
 * Created by Kareem on 2/25/2016.
 */
public class AccelAnalysis {


    private Accelerometer Accel;
    private ArrayList<AccelData> AD;
    private ParseFunctions customParse;
    private float ArrZ[];
    private float ax, ay, az, lastx, lasty, lastz;
    private float RMS;
    private int ADSize;

    public AccelAnalysis (){
        Accel = new Accelerometer();
        customParse = new ParseFunctions(Accel.getApplication());

    }

    public ArrayList<AccelData> getAccelData() {
        AD= new ArrayList<>();
        AD = customParse.getParseDataAccel(ParseUser.getCurrentUser(), 0, "AccelData", "createdAt", "ArrayList");
        ADSize = AD.size();
        Log.d("AccelTest", AD.toString());
        return AD;
    }


}
