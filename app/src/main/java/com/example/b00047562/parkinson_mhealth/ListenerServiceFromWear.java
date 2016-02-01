package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Abdu Sah on 2/1/2016.
 */
public class ListenerServiceFromWear extends WearableListenerService {
    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(HELLO_WORLD_WEAR_PATH)) {

            Intent startIntent = new Intent(this, Accelerometer.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }

    }
}
