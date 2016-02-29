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
    private Complex[] C_Arr;
    private Complex C_largest_magnitude;
    private int IndexOfPeakFreq, SizeOfC_Arr, ADSize;
    private double EquivalentFreq, Fs;

    //Constructor - Initializes all needed variables and calls all necessary functions
    public AccelAnalysis (double fs){
        Accel = new Accelerometer();
        customParse = new ParseFunctions(Accel.getApplication());
        AD = getAccelData();
        ArrZ_double = ConvertToDoubleArr(AD, ADSize);
        FourierTransform();
        IndexOfPeakFreq = GetIndexOfPeakFreq(C_Arr);
        C_largest_magnitude = C_Arr[IndexOfPeakFreq];
        SizeOfC_Arr = C_Arr.length;
        Fs = fs;
        EquivalentFreq = GetEquivalentFrequency(C_largest_magnitude, IndexOfPeakFreq, SizeOfC_Arr);
        Log.d("EquivalentFreq", EquivalentFreq + ""); //Testing
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
            ArrZ_d[i] = AD.get(i).getY();
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
    public int GetIndexOfPeakFreq(Complex C[]){
        int index = 10;
        double largest = GetMagnitude(C[index]);


        //Find largest frequency in the sample data
        for(int i=index+1; i< ((C.length/2)+1); i++) {
            Log.d("Magnitude of C[" + i + "]", GetMagnitude(C[i]) + ""); //Testing
            if (GetMagnitude(C[i]) > largest){
                largest = GetMagnitude(C[i]);
                index = i;
                }
        }
        Log.d("Largest Value", (GetMagnitude(C[index])) + ""); //Testing
        return index;
    }

    public double GetMagnitude(Complex C){
        double mag = Math.sqrt(Math.pow(C.re(), 2) + (Math.pow(C.im(), 2)));
        return mag;
    }

    //Calculate the equivalent frequency: freq = i_max * Fs / N
    //Here Fs = sample rate (Hz) and N = no of points in DFT
    public double GetEquivalentFrequency (Complex C, int i_max, int N) {
        double freq = i_max;
        Log.d("I_max", i_max + ""); //Testing
        freq *= Fs;
        Log.d("FS", Fs + ""); //Testing
        freq /= N;
        Log.d("N", N + ""); //Testing
        return freq;
    }





}
