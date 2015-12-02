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
    private int ax , ay, az;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        txtXValue = (TextView) findViewById(R.id.txtXValue);
        txtYValue = (TextView) findViewById(R.id.txtYValue);
        txtZValue = (TextView) findViewById(R.id.txtZValue);

        //Get SensorManager and accelerometer
        MySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            MyAclmeter = MySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            MySensorManager.registerListener(this, MyAclmeter, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //Get accelerometer values
            ax = (int)event.values[0];
            ay = (int)event.values[1];
            az = (int)event.values[2];
            //change display values
            txtXValue.setText(ax + "");
            txtYValue.setText(ay + "");
            txtZValue.setText(az + "");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something
    }
}
