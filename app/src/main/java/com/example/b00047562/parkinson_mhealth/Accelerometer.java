package com.example.b00047562.parkinson_mhealth;

import android.content.Context;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Accelerometer extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private TextView txtXValue, txtYValue, txtZValue, tv_shakeAlert;
    private SensorManager MySensorManager;
    private Sensor MyAclmeter;
    private float ax, ay, az, lastx, lasty, lastz;
    private long lastUpdate;
    private static final int SHAKE_THRESHOLD = 1700;
    private static boolean output_upToDate = true;

    private LinearLayout SensorGraph;
    private ArrayList<AccelData> sensorData;
    private View mChart;
    private Button BtnShowGraph, BtnReadAccel;
    //private int i=0;

    /* Handles the refresh */
    private Handler outputUpdater = new Handler();

    /* Adjust this value for your purpose */
    public static final long REFRESH_INTERVAL = 100;      // in milliseconds

    /* This object is used as a lock to avoid data loss in the last refresh */
    private static final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtXValue = (TextView) findViewById(R.id.txtXValue);
        txtYValue = (TextView) findViewById(R.id.txtYValue);
        txtZValue = (TextView) findViewById(R.id.txtZValue);
        tv_shakeAlert = (TextView) findViewById(R.id.tv_shake);


        SensorGraph = (LinearLayout) findViewById(R.id.Layout_Graph_Container);
        sensorData = new ArrayList();
        BtnShowGraph = (Button) findViewById(R.id.BtnReadGraph);
        BtnReadAccel = (Button) findViewById(R.id.show_btn);
        BtnShowGraph.setOnClickListener(this);
        BtnReadAccel.setOnClickListener(this);

        BtnReadAccel.setEnabled(false);

        //Get SensorManager and accelerometer
        MySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            MyAclmeter = MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            MySensorManager.registerListener(this, MyAclmeter, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
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
                    float speed = Math.abs(ax + ay + az - lastx - lasty - lastz) / diffTime * 10000;

                    //record accelerometer values and store in arraylist of data

                    AccelData data = new AccelData(curTime, ax, ay, az);
                    sensorData.add(data);


                    if (speed > SHAKE_THRESHOLD) {
                        //Log.d("sensor", "shake detected w/ speed: " + speed);
                        //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                        tv_shakeAlert.setText("Hold Steady !");
                        tv_shakeAlert.setTextColor(Color.RED);
                    }

                    lastx = ax;
                    lasty = ay;
                    lastz = az;
                }
                String x = String.format("%2f", ax);
                String y = String.format("%2f", ay);
                String z = String.format("%2f", az);
                //change display values
                txtXValue.setText(x);
                txtYValue.setText(y);
                txtZValue.setText(z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something
    }

    private void updateTextView() {
        tv_shakeAlert.setText("Steady");
        tv_shakeAlert.setTextColor(Color.GREEN);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnReadGraph:
                sensorData = new ArrayList();
                BtnShowGraph.setEnabled(false);
                BtnReadAccel.setEnabled(true);
                //openChart();
                break;
            case R.id.show_btn:
                BtnShowGraph.setEnabled(true);
                BtnReadAccel.setEnabled(false);
                SensorGraph.removeAllViews(); //reset graph
                //push accel data to Parse
                String json = new Gson().toJson(sensorData);
                ParseObject acc = new ParseObject("AccelData");
                acc.put("ArrayList",json);
                acc.put("username", ParseUser.getCurrentUser().getUsername());
                acc.saveInBackground();
                openChart();
                break;
        }
    }

    private void openChart() {
        if (sensorData != null || sensorData.size() > 0) {
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
}
