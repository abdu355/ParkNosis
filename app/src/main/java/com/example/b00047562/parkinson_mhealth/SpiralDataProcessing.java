package com.example.b00047562.parkinson_mhealth;

        import java.util.ArrayList;

/**
 * Created by Os on 1/30/2016.
 */

/*
TODO
create regular methods NOT Async
 */
public class SpiralDataProcessing {

    //User inputed spiral
    public static ArrayList<SpiralData> spiralData;
    float [] velocity=new float[2500];
    float [] acceleration=new float[2500];
    //Original Spiral Points


    SpiralDataProcessing(ArrayList<SpiralData> InputedData){



        for (int i=1;i<InputedData.size();i++)
        {
            try{
        velocity[i]= (float) Math.sqrt(((float) Math.pow((double) (InputedData.get(i).getX() - InputedData.get(i-1).getX()), 2) +
                Math.pow((double) (InputedData.get(i).getX() - InputedData.get(i-1).getX()), 2)));}
            catch (Exception e){}
        }
        for (int i=1;i<velocity.length;i++)
        {
            acceleration[i]=velocity[i]-velocity[i-1];
        }
    }

    /**
     *  TODO
     *  **********************************************************
     *  Idea to process the two different arrays:
     *  **********************************************************
     * 1-  find the distances between thee original nodes
     * 2-  Add them to the array to be compared with the user data
     * 3-  Compare
     * */


    public void Proccessing(){

        float averageError;

    }

}