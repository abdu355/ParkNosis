package com.example.b00047562.parkinson_mhealth;

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
    float DAH;
    Map<String, Integer> StaticCount = new HashMap<String,Integer>();
    Map<String, Integer> DynamicCount = new HashMap<String,Integer>();



    SpiralDataProcessing(ArrayList<SpiralData> StaticInputtedData,ArrayList<SpiralData> DynamicInputtedData){


        acceleration1 =new float[StaticInputtedData.size()];
        acceleration2=new float[StaticInputtedData.size()];

        acceleration1=FindAcceleration(StaticInputtedData);
        acceleration2=FindAcceleration(DynamicInputtedData);
        StaticCount.putAll(FindDuplicates(acceleration1));
        DynamicCount.putAll(FindDuplicates(acceleration2));

        //Processing(StaticCount, DynamicCount);



    }
//TODO/** modularize work &


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
        DecimalFormat df = new DecimalFormat("#.##");

        for (int i=1;i<velocity.length;i++)
        {
            acceleration[i]=velocity[i]-velocity[i-1];
            acceleration[i]= Float.parseFloat(df.format(acceleration[i]));
        }
        return acceleration;
    }

    private Map<String,Integer> FindDuplicates(float [] acceleration)
    {
        Map<String, Integer> Count = new HashMap<String,Integer>();
        for(float t: acceleration) {
            Integer i = Count.get(t);
            if (i ==  null) {
                i = 0;
            }
            Count.put(String.valueOf(t), i + 1);

        }
        return Count;
    }


    private static HashMap sortByValues(Map<String, Integer> map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue()); }});
        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

//    public float Processing(Map<String, Integer> StaticCount, Map<String, Integer> DynamicCount){
//
//        Map<String, Integer> Static = sortByValues(StaticCount);
//        Map<String, Integer> Dynamic =sortByValues(DynamicCount);
//
//        for (Map.Entry<String, Integer> entry : Math.min(Static.size(),Dynamic.size()))
//        {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            // ...
//        }
//return DAH;
//    }


}