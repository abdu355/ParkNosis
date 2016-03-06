package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;

import almadani.com.shared.AccelData;
import biz.source_code.dsp.math.Complex;
import biz.source_code.dsp.transform.Dft;
import flanagan.analysis.Stat;
import flanagan.math.Fmath;
import flanagan.math.FourierTransform;

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

        AD = getAccelData(); //get data from parse
        ArrZ_double = ConvertToDoubleArr(AD, ADSize); //convert to double array
        FourierTransform();//send to DFT
        IndexOfPeakFreq = GetIndexOfPeakFreq(C_Arr);// get i of largest mag

        C_largest_magnitude = C_Arr[IndexOfPeakFreq];
        SizeOfC_Arr = C_Arr.length;
        Fs = fs;
        EquivalentFreq = GetEquivalentFrequency(C_largest_magnitude, IndexOfPeakFreq, SizeOfC_Arr);
        Log.d("PeakFreq: ", EquivalentFreq + ""); //Testing
    }

    public ArrayList<AccelData> getAccelData() {
        AD= new ArrayList<>();
        AD = customParse.getParseDataAccel(ParseUser.getCurrentUser(), 0, "AccelData", "createdAt", "ArrayList");
        ADSize = AD.size();

        Log.d("AccelTest", AD.toString()); //Testing
        Log.d("AccelArraySize", ADSize + ""); //Testing
        return AD; //get time series of
    }

    //Converts an Arraylist<AccelData> to an array of doubles
    //Used to convert the sample data to a suitable form for processing and analysis
    public double[] ConvertToDoubleArr(ArrayList<AccelData> AD, int size) {
        double ArrY_d[] = new double[size];
        for (int i = 0; i<size; i++){
            ArrY_d[i] = AD.get(i).getZ(); //get Z-axis only
        }
        Log.d("AccelArray", ArrY_d[0] + " " + ArrY_d[1] + " " + ArrY_d[2]); //Testing
        return ArrY_d;
        //return AD.toArray(new Double[AD.size()]);
    }

    //Computes the Fourier Transform of the array of doubles ArrZ_double
    //And stores it in the private data member Compex[]: C_Arr
    public void FourierTransform(){
        C_Arr = new Complex[ADSize];
        C_Arr = DFT.directDft(ArrZ_double);
    }

    //Get Peak Frequency of an array of Complex numbers
    public int GetIndexOfPeakFreq(Complex C[]){
        int index = 10; //skip values
        double largest = GetMagnitude(C[index]);


        //Find largest frequency in the sample data
        for(int i=index+1; i< ((C.length/2)+1); i++) { //take half of array to eliminate conjugates
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



/*
TODO
Paper_120201_Parkinson_CameraReady
Apply on time series

avg normalized acc (10 trials) = 0.0028 g
std dev = 0.002108
var coeff = 0.74725


tremor:
avg normalized acc(10 trials)= 0.02354
std dev= 0.03019
var coeff = 1.2825

8.35 times higher
14.3 times higher
1.7  times higher

http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3548411/pdf/fnhum-06-00357.pdf
demean time series
get RMS of time series
get mean of time series
get SD of time series

Apply FFT/DFT

1 to 20 Hz peak freq indicate tremors
200 millig and above indicate tremors
 */

    private double geVarCoeff() //returns coefficient of variation
    {
        return Stat.coefficientOfVariation(ArrZ_double);

    }
    private double getSTD()
    {
        return Stat.standardDeviation(ArrZ_double);
    }
    private double getAvg()
    {
        return Stat.mean(ArrZ_double);
    }




}
