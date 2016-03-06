package com.example.b00047562.parkinson_mhealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.parse.ParseUser;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

import almadani.com.shared.AccelData;

import static com.google.android.gms.wearable.DataApi.DataListener;


public class Accelerometer extends AppCompatActivity implements SensorEventListener, View.OnClickListener
  {

    private static final String TAG = "TAG ";
    private TextView txtXValue, txtYValue, txtZValue, tv_shakeAlert;
    private SensorManager MySensorManager;
    private Sensor MyAclmeter;
    private AccelData ACDATA;
    private AccelAnalysis A;
    private float ax, ay, az, lastx, lasty, lastz;
    private long lastUpdate;
    private static final int SHAKE_THRESHOLD = 1700;
    private static final int FROMAPP=0;
    public static final int FROMWEAR=1;
    private static boolean output_upToDate = true;
    private double Min_Delay;
    private Intent intent;
    private int l=0;

    private SharedPreferences prefs;
    private LinearLayout SensorGraph;
    private ArrayList<AccelData> DataFromPhone,DataFromWearable;
    private View mChart;
    private Button BtnShowGraph, BtnReadAccel, BtnShowAnalysis;
    private ParseFunctions customParse; //for custom parse functions from ParseFunctions class
    //private int i=0;

    /* Handles the refresh */
    private Handler outputUpdater = new Handler();

    /* Adjust this value for your purpose */
    public static final long REFRESH_INTERVAL = 100;      // in milliseconds
    private GoogleApiClient mGoogleApiClient;
    /* This object is used as a lock to avoid data loss in the last refresh */
    private static final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();


        txtXValue = (TextView) findViewById(R.id.txtXValue);
        txtYValue = (TextView) findViewById(R.id.txtYValue);
        txtZValue = (TextView) findViewById(R.id.txtZValue);
        tv_shakeAlert = (TextView) findViewById(R.id.tv_shake);

        //intent=new Intent();


        SensorGraph = (LinearLayout) findViewById(R.id.Layout_Graph_Container);
        DataFromPhone = new ArrayList();
        DataFromWearable= new ArrayList();

        BtnReadAccel = (Button) findViewById(R.id.read_btn);
        BtnShowGraph = (Button) findViewById(R.id.show_btn);
        BtnShowAnalysis = (Button) findViewById(R.id.analysis_btn);

        BtnReadAccel.setOnClickListener(this);
        BtnShowGraph.setOnClickListener(this);
        BtnShowAnalysis.setOnClickListener(this);

        BtnShowGraph.setEnabled(false);
        BtnShowAnalysis.setEnabled(false);

        //Get SensorManager and accelerometer
        MySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            MyAclmeter = MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            MySensorManager.registerListener(this, MyAclmeter, SensorManager.SENSOR_DELAY_FASTEST);

            //HARDCODE ACCELEROMETER SAMPLE RATE GALAXY S5 = 70 HZ
            Min_Delay = 10.0; //get accelerometer sampling rate

        } else {
            Min_Delay = 0.0;
            Log.d("Accelerometer not found", "Accelerometer not found");
        }
        Thread t = new Thread() {//http://stackoverflow.com/questions/14814714/update-textview-every-second

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        customParse = new ParseFunctions(getApplicationContext()); //initialized
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // outputUpdater.post(outputUpdaterTask);
                long curTime = System.currentTimeMillis();
                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    //Get accelerometer values
                    ax = event.values[0];
                    ay = event.values[1];
                    az = event.values[2];

                    //float speed = Math.abs(ax + ay + az - lastx - lasty - lastz) / diffTime * 10000;
                    //record accelerometer values and store in arraylist of data

                    AccelData data = new AccelData(curTime, ax, ay, az);
                    DataFromPhone.add(data);


                    /*if (speed > SHAKE_THRESHOLD) {
                        //Log.d("sensor", "shake detected w/ speed: " + speed);
                        //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                        tv_shakeAlert.setText("Hold Steady !");
                        tv_shakeAlert.setTextColor(Color.RED);
                    }*/

                    lastx = ax;
                    lasty = ay;
                    lastz = az;
                }
//                String x = String.format("%.1f", ax);
//                String y = String.format("%.1f", ay);
//                String z = String.format("%.1f", az);
//                //change display values
//                txtXValue.setText(x);
//                txtYValue.setText(y);
//                txtZValue.setText(z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onConnected(Bundle bundle) {
//        Wearable.DataApi.addListener(mGoogleApiClient, this);
//
//    }




    @Override
    protected void onPause() {
        super.onPause();
//        Wearable.DataApi.removeListener(mGoogleApiClient, this);
//        mGoogleApiClient.disconnect();
    }





    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something
    }


    @Override
    protected void onResume() {
        super.onResume();
        //mGoogleApiClient.connect();

        processExtraData();
    }
    /*TODO
    instead of connecting inside onResume
     */
    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }
    @Override
    protected void onNewIntent(Intent intent) { //this will look for new intent even if activity is already open
        super.onNewIntent(intent);
        setIntent(intent); //get new intent, else old intent will be used
        /*TODO
            use processExtraData() here instead of onResume()
         */
        processExtraData();
    }
    private void processExtraData(){
        //use the data received here
        intent= getIntent();
        ListenerServiceFromWear listner=new ListenerServiceFromWear();

        l=intent.getIntExtra("Read Data", 0);
        DataFromWearable=listner.getDataFromWearable();
        Log.d("ff","Intent value:"+ l);

        if(l==1)
        {
            // DataFromWearable.clear();
            ReadForAWhile(FROMWEAR);
        }
    }//ref: http://www.helloandroid.com/tutorials/communicating-between-running-activities



    private void updateTextView() {
        tv_shakeAlert.setText("Steady");
        tv_shakeAlert.setTextColor(Color.GREEN);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Starts reading data for a duration of 10 seconds
            //Stops reading data after 10 seconds
            //Shows accelerometer data graph
            case R.id.read_btn:

                ReadForAWhile(FROMAPP);

                break;

            case R.id.show_btn:

                break;
            case R.id.analysis_btn:
                /*TODO
                Intent and go to new activity
                */

                //Converting accelerometer sampling rate Min_Delay (us) to Fs (Hz)
                //This is done by converting the sampling rate from microseconds to seconds,
                //Then by getting the reciprocal of that value
                if(Min_Delay != 0) {
                    //double Fs = 1/(Min_Delay/1000000);
                    //A = new AccelAnalysis(Fs);
                    A = new AccelAnalysis(Min_Delay);
                }
                break;
        }
    }

    private void openChart(ArrayList<AccelData> sensorData) {
        if (sensorData != null && sensorData.size() > 0) {
            long t = 0;
            XYMultipleSeriesDataset dataset = null;
            XYSeries xSeries = null;
            XYSeries ySeries = null;
            XYSeries zSeries = null;

            t = sensorData.get(0).getTimestamp();
            dataset = new XYMultipleSeriesDataset();

            xSeries = new XYSeries("X");
            ySeries = new XYSeries("Y");
            zSeries = new XYSeries("Z");


            for (AccelData data : sensorData) {
                xSeries.add(data.getTimestamp() - t, data.getX());
                ySeries.add(data.getTimestamp() - t, data.getY());
                zSeries.add(data.getTimestamp() - t, data.getZ());
            }

            dataset.addSeries(xSeries);
            dataset.addSeries(ySeries);
            dataset.addSeries(zSeries);

            XYSeriesRenderer xRenderer = new XYSeriesRenderer();
            xRenderer.setColor(Color.RED);
            xRenderer.setPointStyle(PointStyle.CIRCLE);
            xRenderer.setFillPoints(true);
            xRenderer.setLineWidth(1);
            xRenderer.setDisplayChartValues(false);

            XYSeriesRenderer yRenderer = new XYSeriesRenderer();
            yRenderer.setColor(Color.GREEN);
            yRenderer.setPointStyle(PointStyle.CIRCLE);
            yRenderer.setFillPoints(true);
            yRenderer.setLineWidth(1);
            yRenderer.setDisplayChartValues(false);

            XYSeriesRenderer zRenderer = new XYSeriesRenderer();
            zRenderer.setColor(Color.BLUE);
            zRenderer.setPointStyle(PointStyle.CIRCLE);
            zRenderer.setFillPoints(true);
            zRenderer.setLineWidth(1);
            zRenderer.setDisplayChartValues(false);

            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(0);
            multiRenderer.setLabelsColor(Color.RED);
            multiRenderer.setChartTitle("t vs (x,y,z)");
            multiRenderer.setXTitle("Sensor Data");
            multiRenderer.setYTitle("Values of Acceleration");
            multiRenderer.setZoomButtonsVisible(true);
            for (int i = 0; i < sensorData.size(); i++) {

                multiRenderer.addXTextLabel(i + 1, ""
                        + (sensorData.get(i).getTimestamp() - t));
            }
            for (int i = 0; i < 12; i++) {
                multiRenderer.addYTextLabel(i + 1, "" + i);
            }

            multiRenderer.addSeriesRenderer(xRenderer);
            multiRenderer.addSeriesRenderer(yRenderer);
            multiRenderer.addSeriesRenderer(zRenderer);

            // Getting a reference to LinearLayout of the MainActivity Layout

            // Creating a Line Chart
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                    multiRenderer);


            // Adding the Line Chart to the LinearLayout
            SensorGraph.addView(mChart);

        }
    }

    public void Read_Button () {
        DataFromPhone = new ArrayList();
        BtnReadAccel.setEnabled(false);
        BtnShowGraph.setEnabled(true);
        BtnShowAnalysis.setEnabled(false);
    }

    public void Show_Data(){
        BtnReadAccel.setEnabled(true);
        BtnShowGraph.setEnabled(false);
        BtnShowAnalysis.setEnabled(true);

        SensorGraph.removeAllViews(); //reset graph
        //push accel data to Parse
        String json = new Gson().toJson(DataFromPhone);

        customParse.pushParseData(ParseUser.getCurrentUser(), "AccelData", "ArrayList", json, "", ""); //user pointer
        openChart(DataFromPhone);
        MainActivity.h=true; //test finished
    }
    public void Show_WearData(){
        BtnReadAccel.setEnabled(true);
        BtnShowGraph.setEnabled(false);
        BtnShowAnalysis.setEnabled(true);

        SensorGraph.removeAllViews(); //reset graph
        //push accel data to Parse
        String json = new Gson().toJson(DataFromWearable);

        customParse.pushParseData(ParseUser.getCurrentUser(),"AccelData","ArrayList",json,"",""); //user pointer
        openChart(DataFromWearable);
        MainActivity.h=true; //test finished
    }
    public void ReadForAWhile(final int WhichSensor){

        Handler handler = new Handler();
        Read_Button();
        handler.postDelayed(new Runnable() {
            public void run()
            {
                if (WhichSensor == 0)
                    Show_Data();
                else if (WhichSensor == 1) {
                    BtnReadAccel.setEnabled(false);
                    //BtnReadAccel.setText("Read From WATCH");
                    Show_WearData();
                }
            }
        }, 3000);
    }
}
