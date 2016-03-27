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

    private double ArrZ_double[],ArrZ_Normalized[];
    private Complex[] C_Arr;
    private Complex C_largest_magnitude;
    private int IndexOfPeakFreq, SizeOfC_Arr, ADSize;
    private double EquivalentFreq, Fs;

    //Constructor - Initializes all needed variables and calls all necessary functions
    public AccelAnalysis (double fs){
        Fs = fs;
        //Accel = new Accelerometer();
        // customParse = new ParseFunctions(Accel.getApplication());
        customParse = new ParseFunctions();
        //PerformAnalysis0();
        PerformAnalysis1();
    }
    /*TODO

               add AD= customParse.getParseDataAccel(ParseUser.getCurrentUser(), 0, "AccelData", "createdAt", "ArrayList"); //left hand
               seperate results in results analysis
               run AD through the same algorithms and display results
               */

       public ArrayList<AccelData> getAccelData2() {
        try {
            AD = new ArrayList<>();
            AD = customParse.getParseDataAccel(ParseUser.getCurrentUser(), 0, "AccelData", "createdAt", "ArrayList"); // left hand

            ADSize = AD.size();

            Log.d("AccelTest", AD.toString()); //Testing
            Log.d("AccelArraySize", ADSize + ""); //Testing}
        }catch (Exception e){
            Log.d("ACCel analysis", "getAccelData: problem");
        }
        return AD; //get time series of
    }

    public ArrayList<AccelData> getAccelData() {
        try {
            AD = new ArrayList<>();
            AD = customParse.getParseDataAccel(ParseUser.getCurrentUser(), 1, "AccelData", "createdAt", "ArrayList"); //  right hand

            ADSize = AD.size();

            Log.d("AccelTest", AD.toString()); //Testing
            Log.d("AccelArraySize", ADSize + ""); //Testing}
        }catch (Exception e){
            Log.d("ACCel analysis", "getAccelData: problem");
        }
        return AD; //get time series of
    }

    //Converts an Arraylist<AccelData> to an array of doubles
    //Used to convert the sample data to a suitable form for processing and analysis
    public double[] ConvertToDoubleArr(ArrayList<AccelData> AD, int size) {
        double ArrZ_d[] = new double[size];
        for (int i = 0; i<size; i++){
            ArrZ_d[i] = AD.get(i).getY(); //get Y-axis only
        }
        return ArrZ_d;
        //return AD.toArray(new Double[AD.size()]);
    }

    //ANALYSIS 0 ----------------------------------------------

    public void PerformAnalysis0 (){
        FourierTransform();//send to DFT
        IndexOfPeakFreq = GetIndexOfPeakFreq(C_Arr);// get i of largest mag

        C_largest_magnitude = C_Arr[IndexOfPeakFreq];
        SizeOfC_Arr = C_Arr.length;
        EquivalentFreq = GetEquivalentFrequency(C_largest_magnitude, IndexOfPeakFreq, SizeOfC_Arr);
        Log.d("PeakFreq: ", EquivalentFreq + ""); //Testing
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


    //ANALYSIS 1 -----------------------------------------------

    /*TODO
    -Set Benchmarks

    Info:
    Paper_120201_Parkinson_CameraReady
    Apply on time series

    normal:
    Mean normalized acc (10 trials) = 0.0028 g
    std dev = 0.002108
    var coeff = 0.74725


    tremor:
    Mean normalized acc(10 trials)= 0.02354  (8.35 times higher)
    std dev= 0.03019                        (14.3 times higher)
    var coeff = 1.2825                      (1.7  times higher)
    */

    public int PerformAnalysis1() {  //rescale
        int resscale =0 ;
        AD = getAccelData(); //get data from parse
        ArrZ_double = ConvertToDoubleArr(AD, ADSize); //convert to double array
        double Mean = getMean();
        ArrZ_Normalized = NormalizeArrayZ(ArrZ_double, Mean); //normalize the accelerometer values with respect to the mean
        double VC = getVarCoeff();
        Log.d("Variation Coefficient", VC + ""); //1
        if (VC>=1.58E-04 && VC<3.26E-04)
        {
            resscale=1;
            Log.d("VC", "Slight Tremor Detected"); //2
        }
        //else if (VC >=3.26E-04 && VC<4.16E-03) {
        else if (VC >=3.26E-04 && VC<8.16E-04) {
            resscale=2;
            Log.d("VC", "Mild Tremor Detected"); //3
        }
        //else if(VC>=4.16E-03 && VC<1.31E-02) {
        else if(VC>=8.16E-04 && VC<5.31E-03) {
            resscale=3;
            Log.d("VC", "Moderate Tremor Detected");//4
        }
        //else if(VC>=1.31E-02) {
        else if(VC>=5.31E-03) {
            resscale=4;
            Log.d("VC", "Severe Tremor Detected");//5
        }
        else
            resscale =0;

        return  resscale;
    }

    private double getVarCoeff() //returns coefficient of variation
    {
        Statistics s = new Statistics((ArrZ_Normalized));
        return s.getVariance();

    }

    //Our STD benchmark for normal = 0.01185
    //Our STD benchmark for tremor = 14.3*normal = 0.1695
    private double getSTD()
    {
        Statistics s = new Statistics((ArrZ_Normalized));
        return s.getStdDev();
    }

    private double getMean()
    {
        Statistics s = new Statistics((ArrZ_double));
        return s.getMean();
    }

    //normalizes the values of a double array of accelerometer data to gravity "g"
    private double[] NormalizeArrayZ(double[] Arr, double M){
        double ArrZ_d[] = new double[Arr.length];
        for (int i = 0; i<Arr.length; i++){
            ArrZ_d[i] = (Arr[i])/M; //get Z-axis only
        }
        return ArrZ_d;
    }


    //ANALYSIS 2 --------------------------------------

    /*
    TODO

    http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3548411/pdf/fnhum-06-00357.pdf
    DONE:
    -demean time series
    -get RMS of time series
    -get mean of time series
    -get SD of time series
    -Apply FFT/DFT

    TODO:
    -1 to 20 Hz peak freq indicate tremors
    -200 millig and above indicate tremors
    */

    public void PerformAnalysis2() {
        double Mean = getMean();
        double STD = getSTD();
        double RMS = getRMS();
        FourierTransform();

    }

    private double getRMS(){
        double avg = 0;
        for (int i=0; i<ArrZ_Normalized.length; i++){
            avg += Math.pow(ArrZ_Normalized[i],2);
        }
        avg /= ArrZ_Normalized.length;
        avg = Math.sqrt(avg);
        double RMS = avg;
        return RMS;
    }

}
