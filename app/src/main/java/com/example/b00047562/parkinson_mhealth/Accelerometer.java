package com.example.b00047562.parkinson_mhealth;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00047562.parkinson_mhealth.R;

public class Accelerometer extends AppCompatActivity implements SensorEventListener{

    private TextView txtXValue, txtYValue, txtZValue,tv_shakeAlert;
    private SensorManager MySensorManager;
    private Sensor MyAclmeter;
    private float ax , ay, az,lastx,lasty,lastz;
    private long lastUpdate;
    private static final int SHAKE_THRESHOLD = 1700;
    private static boolean output_upToDate = true;

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
        tv_shakeAlert=(TextView)findViewById(R.id.tv_shake);

        //Get SensorManager and accelerometer
        MySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            MyAclmeter = MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            MySensorManager.registerListener(this, MyAclmeter, 1000000);
        }
        else {
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
                    ax = (float) event.values[0];
                    ay = (float) event.values[1];
                    az = (float) event.values[2];
                    float speed = Math.abs(ax + ay + az - lastx - lasty - lastz) / diffTime * 10000;

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


}
