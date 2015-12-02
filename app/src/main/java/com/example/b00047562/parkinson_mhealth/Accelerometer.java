package com.example.b00047562.parkinson_mhealth;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.b00047562.parkinson_mhealth.R;

public class Accelerometer extends AppCompatActivity implements SensorEventListener{

    private TextView txtXValue, txtYValue, txtZValue;
    private SensorManager MySensorManager;
    private Sensor MyAclmeter;
    private float ax , ay, az;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtXValue = (TextView) findViewById(R.id.txtXValue);
        txtYValue = (TextView) findViewById(R.id.txtYValue);
        txtZValue = (TextView) findViewById(R.id.txtZValue);

        //Get SensorManager and accelerometer
        MySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            MyAclmeter = MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            MySensorManager.registerListener(this, MyAclmeter, 1000000);
        }
        else {
            Log.d("Accelerometer not found", "Accelerometer not found");
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                //Get accelerometer values
                ax = (float)event.values[0];
                ay = (float)event.values[1];
                az = (float)event.values[2];
                String x = String.format("%2f",ax);
                String y = String.format("%2f",ay);
                String z = String.format("%2f",az);
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
}
