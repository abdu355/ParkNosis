package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import almadani.com.shared.AccelData;

/**
 * Created by Abdu Sah on 2/1/2016.
 */
public class ListenerServiceFromWear extends WearableListenerService {
    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";
    private static final String ACCELMESS = "/Accel";
    private static final String TAG = "fuck";
    Accelerometer c;
    private AccelData ACDATA;
    private String resultString;
    private Accelerometer acc;

    private ArrayList<AccelData> DataFromWearable;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("oncreate!", "oncreate called");
        DataFromWearable= new ArrayList();
        acc = new Accelerometer();
    }


    public ArrayList<AccelData> getDataFromWearable() {
        return DataFromWearable;
    }


//    @Override
//    public void onDataChanged(DataEventBuffer dataEventBuffer) {
//        Log.d("DataCHANGED!","data changed called");
////        for (DataEvent event : dataEventBuffer) {
////            if (event.getType() == DataEvent.TYPE_CHANGED) {
////                // DataItem changed
////                DataItem item = event.getDataItem();
////                if (item.getUri().getPath().compareTo("/Accel/") == 0) {
////                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
////                    long SystemTime=dataMap.getLong("Time");
////                    ACDATA=new AccelData(dataMap.getLong("Time Stamp")
////                            ,dataMap.getFloat("X value"),dataMap.getFloat("Y value"),dataMap.getFloat("Z value"));
////                    Log.d(TAG, "onDataChanged: " + ACDATA.getZ());
////                    DataFromWearable.add(ACDATA);
////
////                        /*TODO
////                               graph this
////                               and stop continuous reading
////                        */
////                }
////            }
////            else if (event.getType() == DataEvent.TYPE_DELETED) {
////                // DataItem deleted
////
////            }
////
////        }
////        // mGoogleApiClient.disconnect(); mGoogleApiClient.connect();
//
//        for (DataEvent dataEvent : dataEventBuffer) {
//            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
//                DataItem dataItem = dataEvent.getDataItem();
//                Uri uri = dataItem.getUri();
//                String path = uri.getPath();
//                Log.d("PATH",path);
//                if (path.compareTo("/Accel/")==0)
//                {
//                    unpackSensorData(
//                            Integer.parseInt(uri.getLastPathSegment()),
//                            DataMapItem.fromDataItem(dataItem).getDataMap()
//                    );
//                }
//            }
//        }
//
//    }
//    private void unpackSensorData(int sensorType, DataMap dataMap) {
//        Log.d("unpack","unpack");
//        resultString =new  String (dataMap.getString("axisdata"));
//        Type type = new TypeToken<ArrayList<AccelData>>() {}.getType();
//
//        //ACDATA=new AccelData(dataMap.getLong("Time Stamp"),dataMap.getString("axisdata"));
//       // Log.d(TAG, "onDataChanged: " + ACDATA.getZ());
//        //Log.d(TAG, "Received sensor data " + sensorType + " = " + Arrays.toString(values));
//
//        DataFromWearable =  new Gson().fromJson(resultString, type);
//
//        Log.d("DataArray",DataFromWearable.toString());
//    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(HELLO_WORLD_WEAR_PATH)) {

            Intent startIntent = new Intent(this, Accelerometer.class);
            //c=new Accelerometer();
           // c.ReadForAWhile(Accelerometer.FROMWEAR);
           // startIntent.putExtra("Read Data",1);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }

        //Receive data message from wear
        if (messageEvent.getPath().equals(ACCELMESS)) {
            resultString = new String(messageEvent.getData());
            Log.d("resString",resultString);

//            Type type = new TypeToken<ArrayList<AccelData>>() {}.getType();
//            DataFromWearable =  new Gson().fromJson(resultString, type);

            //Log.d("DataArray",DataFromWearable.toString());

            //start accel and graph
            Intent startIntent = new Intent(this, Accelerometer.class);
            //c=new Accelerometer();
            // c.ReadForAWhile(Accelerometer.FROMWEAR);

            startIntent.putExtra("Read Data",1);
            startIntent.putExtra("DataFromWearable",resultString);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);


           // acc.Show_WearData();//graph and upload to Parse

        }


    }


}
