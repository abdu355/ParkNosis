package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

import almadani.com.shared.AccelData;

/**
 * Created by Abdu Sah on 2/1/2016.
 */
public class ListenerServiceFromWear extends WearableListenerService {
    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";
    private static final String TAG = "fuck";
    Accelerometer c;
    private AccelData ACDATA;

    @Override
    public void onCreate() {
        super.onCreate();
        DataFromWearable= new ArrayList();
    }


    public ArrayList<AccelData> getDataFromWearable() {
        return DataFromWearable;
    }

    private ArrayList<AccelData> DataFromWearable;
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/Accel") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    long SystemTime=dataMap.getLong("Time");
                    ACDATA=new AccelData(dataMap.getLong("Time Stamp")
                            ,dataMap.getFloat("X value"),dataMap.getFloat("Y value"),dataMap.getFloat("Z value"));
                    Log.d(TAG, "onDataChanged: " + ACDATA.getZ());
                    DataFromWearable.add(ACDATA);

                        /*TODO
                               graph this
                               and stop continuous reading
                        */
                }
            }
            else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted

            }

        }
        // mGoogleApiClient.disconnect(); mGoogleApiClient.connect();


    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(HELLO_WORLD_WEAR_PATH)) {

            Intent startIntent = new Intent(this, Accelerometer.class);
            //c=new Accelerometer();
           // c.ReadForAWhile(Accelerometer.FROMWEAR);
            startIntent.putExtra("Read Data",1);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }

    }
}
