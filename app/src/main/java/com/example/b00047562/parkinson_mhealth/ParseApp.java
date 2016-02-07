package com.example.b00047562.parkinson_mhealth;

import android.app.Application;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Abdu Sah on 12/11/2015.
 */
public class ParseApp extends Application {
    public static final String YOUR_APPLICATION_ID = "VQiDfFq3nK7WByBY6Gr08X9C6rceopcksCB04kSu";
    public static final String YOUR_CLIENT_KEY = "DyCN4DSCNj9prKFf79De6TgRP9Sd0tPFOkIPCv7U";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        // ParseObject testObject = new ParseObject("TestObject");
        // testObject.put("object1","Spiral");
        // testObject.saveInBackground();
        startService(new Intent(getApplicationContext(),ListenerServiceFromWear.class));
    }
}
