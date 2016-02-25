package com.example.b00047562.parkinson_mhealth;

        import java.util.ArrayList;

/**
 * Created by Os on 1/30/2016.
 */
public class SpiralDataProcessing {

    //User inputed spiral
    public static ArrayList<SpiralData> spiralData;
    float []spiralInputedData=new float[170];
    //Original Spiral Points
    float []OriginalSpiralPoints = new float[168];

    SpiralDataProcessing(ArrayList<SpiralData> InputedData,float []OriginalPoints){

        spiralData=InputedData;
        OriginalSpiralPoints=OriginalPoints;
        for (int i=0,j=0;j<170;i++,j+=2)
        {
            spiralInputedData[j]=spiralData.get(i).getX();
            spiralInputedData[j+1]=spiralData.get(i).getY();
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