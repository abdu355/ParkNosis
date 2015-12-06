package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Spiral extends AppCompatActivity implements  View.OnClickListener {

    private Button redrawopen;
    private DrawView myview;
    public static TextView alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        redrawopen=(Button)findViewById(R.id.btn_clear);
        myview=(DrawView)findViewById(R.id.drawing);
        alert=(TextView)findViewById(R.id.tv_alert);
        redrawopen.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.btn_clear:
                //Toast.makeText(this,"clear",Toast.LENGTH_SHORT).show();
                //myview.cleardisp();
                this.startActivity(new Intent(this, SpiralRedraw.class));
                break;
        }
    }
}
