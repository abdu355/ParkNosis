package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;

import almadani.com.shared.AccelData;
import biz.source_code.dsp.math.Complex;
import biz.source_code.dsp.transform.Dft;

/**
 * Created by Kareem on 2/25/2016.
 */
public class AccelAnalysis {

    /*
    TODO
    create regular methods NOT Async
     */
    private Dft DFT;
    private Accelerometer Accel;
    private ArrayList<AccelData> AD;
    private ParseFunctions customParse;

    private float ax, ay, az, lastx, lasty, lastz;
    private float RMS;

    private double ArrZ_double[];
    private int ADSize;
    private Complex[] C_Arr;
    private double PeakFreq;

    //Constructor - Initializes all needed variables and calls all necessary functions
    public AccelAnalysis (){
        Accel = new Accelerometer();
        customParse = new ParseFunctions(Accel.getApplication());
        AD = getAccelData();
        ArrZ_double = ConvertToDoubleArr(AD, ADSize);
        FourierTransform();
        PeakFreq = GetPeakFreq(C_Arr);
    }

    public ArrayList<AccelData> getAccelData() {
        AD= new ArrayList<>();
        AD = customParse.getParseDataAccel(ParseUser.getCurrentUser(), 0, "AccelData", "createdAt", "ArrayList");
        ADSize = AD.size();
        Log.d("AccelTest", AD.toString()); //Testing
        Log.d("AccelArraySize", ADSize + ""); //Testing
        return AD;
    }

    //Converts an Arraylist<AccelData> to an array of doubles
    //Used to convert the sample data to a suitable form for processing and analysis
    public double[] ConvertToDoubleArr( ArrayList<AccelData> AD, int size) {
        double ArrZ_d[] = new double[size];
        for (int i = 0; i<size; i++){
            ArrZ_d[i] = AD.get(i).getZ();
        }
        Log.d("AccelArray", ArrZ_d[0] + " " + ArrZ_d[1] + " " + ArrZ_d[2]); //Testing
        return ArrZ_d;
    }

    //Computes the Fourier Transform of the array of doubles ArrZ_double
    //And stores it in the private data member Compex[]: C_Arr
    public void FourierTransform(){
        C_Arr = new Complex[ADSize];
        C_Arr = DFT.directDft(ArrZ_double);
    }

    //Get Peak Frequency of an array of Complex numbers
    public double GetPeakFreq(Complex C[]){
        double largest = C[0].abs();

        //Find largest frequency in the sample data
        for(int i=1; i< C.length; i++)
        {
            if(C[i].abs() > largest)
                largest = C[i].abs();
        }
        return largest;
    }




}
