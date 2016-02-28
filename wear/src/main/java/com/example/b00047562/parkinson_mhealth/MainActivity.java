package com.example.b00047562.parkinson_mhealth;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.mariux.teleport.lib.TeleportClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import almadani.com.shared.AccelData;


//WATCH ACTIVITY !
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,SensorEventListener  {

    private TextView tvCountDownTimer;
    private Button accelbtn;
    Node mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";
    private boolean mResolvingError=false;

    TeleportClient mTeleportClient;


    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;
    private ArrayList<AccelData> sensorData;

    private SensorManager mSensorManager;
    private SensorEvent SEvent;
    private Sensor mSensor;
    private int mSensorType;
    private long lastUpdate;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mTeleportClient = new TeleportClient(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvCountDownTimer = (TextView) stub.findViewById(R.id.tvCountDownTimer);
                accelbtn=(Button)stub.findViewById(R.id.acl_btn);
                accelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvCountDownTimer.setText("Shit Shat get banged");



                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

//                                new CountDownTimer(15000, 1000) {
//
//                                    public void onTick(long millisUntilFinished) {
//                                        tvCountDownTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
//                                    }
//
//                                    public void onFinish() {
//                                        tvCountDownTimer.setText("done!");
//                                    }
//                                }.start();
                        TimerTask task = new TimerTask() {

                            @Override
                            public void run() {


                                String TAG = " ";
                                if(SEvent!=null) {
                                   mTeleportClient.syncLong("timeStamp", System.currentTimeMillis());

                                    Log.d(TAG, "run: "+ SEvent.values[0]);
                                    mTeleportClient.syncString("x", String.valueOf((SEvent.values[0])));
                                    mTeleportClient.syncString("y", String.valueOf((SEvent.values[1])));
                                    mTeleportClient.syncString("z", String.valueOf((SEvent.values[2])));

                                }
                            }
                        };
                        Timer timer = new Timer(true);  // runs on a separate thread
                        timer.schedule(task, 0, 500);
                            }
                        }, 15000);
                            }


                });
            }

        });



    }
    /**
     * Send message to mobile handheld

     /*  private void sendMessage(float xReading) {

     if (mNode != null && mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {

     Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(),String.valueOf(xReading), null).setResultCallback(

     new ResultCallback<MessageApi.SendMessageResult>() {
    @Override
    public void onResult(MessageApi.SendMessageResult sendMessageResult) {

    if (!sendMessageResult.getStatus().isSuccess()) {
    Log.e("TAG", "Failed to send message with status code: "
    + sendMessageResult.getStatus().getStatusCode());
    }
    }
    }
     );
     }else{
     //Improve your code
     }

     }*/



    private void storeData(){

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;}

        AccelData data= new AccelData(curTime, SEvent.values[0], SEvent.values[1], SEvent.values[2]);
        sensorData.add(data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        SEvent=event;
        Float.toString(event.values[0]);

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        }
        else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            detectRotation(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /*
    * Resolve the node = the connected device to send the message to
    */
    private void resolveNode() {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    mNode = node;
                }
            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void detectShake(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            float gForce = (float)Math.sqrt(gX*gX + gY*gY + gZ*gZ);

            // Change background color if gForce exceeds threshold;
            // otherwise, reset the color
            if(gForce > SHAKE_THRESHOLD) {
                //       mView.setBackgroundColor(Color.rgb(0, 100, 0));
            }
            else {
                //       mView.setBackgroundColor(Color.BLACK);
            }
        }
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if(Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                //         mView.setBackgroundColor(Color.rgb(0, 100, 0));
            }
            else {

            }
        }
    }


}
//WATCH ACTIVITY !

//Ref: https://gist.github.com/gabrielemariotti/117b05aad4db251f7534