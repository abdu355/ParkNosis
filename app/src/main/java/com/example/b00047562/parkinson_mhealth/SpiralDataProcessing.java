package com.example.b00047562.parkinson_mhealth;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Os on 1/30/2016.
 */

/*
TODO
create regular methods NOT Async
 */
public class SpiralDataProcessing {

    //User inputed spiral

    float [] velocity;
    float [] acceleration1,acceleration2;

    public float getDAH() {
        return DAH;
    }

    float DAH=0;
    Map<String, Integer> StaticCount = new HashMap<String,Integer>();
    Map<String, Integer> DynamicCount = new HashMap<String,Integer>();



    SpiralDataProcessing(ArrayList<SpiralData> StaticInputtedData,ArrayList<SpiralData> DynamicInputtedData){


        acceleration1 = new float[StaticInputtedData.size()];
        acceleration2 = new float[DynamicInputtedData.size()];

        acceleration1=FindAcceleration(StaticInputtedData);
        acceleration2=FindAcceleration(DynamicInputtedData);

        StaticCount.putAll(FindDuplicates(acceleration1));
        DynamicCount.putAll(FindDuplicates(acceleration2));

        Processing(StaticCount, DynamicCount);


    }

    private float[] FindAcceleration(ArrayList<SpiralData> InputedData){
        float [] acceleration;
        acceleration=new float[InputedData.size()];
        velocity=new float[InputedData.size()];

        for (int i=1;i<InputedData.size();i++)
        {
            try{
                velocity[i]= (float) Math.sqrt(((float) Math.pow((double) (InputedData.get(i).getX() - InputedData.get(i-1).getX()), 2) +
                        Math.pow((double) (InputedData.get(i).getX() - InputedData.get(i-1).getX()), 2)));}
            catch (Exception e){}
        }
        /**
         * Decimal Part: Tested & working
         * */
        DecimalFormat df = new DecimalFormat("#.#####");

        for (int i=1;i<velocity.length;i++)
        {
            acceleration[i]=velocity[i]-velocity[i-1];
            acceleration[i]= Float.parseFloat(df.format(acceleration[i]));
        }
        return acceleration;
    }

    private Map<String,Integer> FindDuplicates(float [] acceleration)
    {
        /** Finds the frequency of a number in an array and returns a map of array's values
         * and how many times they replicated
         *
         * Tested And Working
         *
         * **/
        Map<String, Integer> Count = new HashMap<String,Integer>();
        for(float t: acceleration)
        {
            Integer i = Count.get((String.valueOf(t)));     //check array
            if (i ==  null)
            {
                i = 0;
            }
            Count.put(String.valueOf(t), i + 1);            // store (value, occurance count)

        }
        return Count;
    }


    private static Map<String, Integer> sortByValues(Map<String, Integer> map) {

        /**
         * Sorts a map descending-ly and return it
         *
         * Tested And Working
         *
         * **/
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue()); }});
        Collections.reverse(list);
        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();


        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }

    public float Processing(Map<String, Integer> StaticCount, Map<String, Integer> DynamicCount){
//
        Map<String, Integer> Static = sortByValues(StaticCount);
        Map<String, Integer> Dynamic =sortByValues(DynamicCount);

        int a = Math.min(Static.size(), Dynamic.size());
        ArrayList<Integer> staticArr=new ArrayList ();
        ArrayList<Integer> dynamicArr= new ArrayList ();
        ArrayList<Float> DAHarr=new ArrayList ();
//
        for (Map.Entry<String, Integer> Staticentry : Static.entrySet() )

        {
            String key = Staticentry.getKey();
            Integer value = Staticentry.getValue();

          staticArr.add(value);

        }
        for(Map.Entry<String, Integer> Dynamicentry : Dynamic.entrySet() )
        {
            String key = Dynamicentry.getKey();
            Integer value = Dynamicentry.getValue();

          dynamicArr.add(value);
        }

        /*TODO
        - loop only over first 10 keys
        - calculate difference between integer values (using arraylists or other)
*/
        for(int i =0 ; i<10;i++)
        {

            DAHarr.add((float)Math.pow((double)staticArr.get(i) - (double)dynamicArr.get(i),2.0));
            Log.d("DAH Arr Val", "Processing: "+ DAHarr.get(i));
        }

        for(int i=0;i<10;i++)
        {
        DAH+=DAHarr.get(i);
        }


        return DAH;
    }


}