package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseUser;

public class Spiral extends AppCompatActivity implements  View.OnClickListener {

    public static Button redrawOpen,btnClr,btnSubmit;
    private CanvasSpiral customCanvas;
    public static TextView alert;
    private ParseFunctions customParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customCanvas=(CanvasSpiral)findViewById(R.id.spiral_canvas);
        redrawOpen =(Button)findViewById(R.id.btn_redraw);
        btnClr= (Button) findViewById(R.id.btn_clear);
        customParse = new ParseFunctions(getApplicationContext());
        btnSubmit= (Button) findViewById(R.id.btnSubmit);

        btnClr.setOnClickListener(this);
        redrawOpen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_clear:
                //Toast.makeText(this,"clear",Toast.LENGTH_SHORT).show();
                customCanvas.cleardisp();
                break;
            case R.id.btn_redraw:
                this.startActivity(new Intent(this, SpiralRedraw.class));
                break;
            case R.id.btnSubmit:

            case R.id.btn_upload:
                String json = new Gson().toJson(DrawView.spiralData);
                customParse.pushParseData(ParseUser.getCurrentUser(),"SpiralData","ArrayList",json,"","");
                MainActivity.sp=true; //test finished
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;


        }
    }
}
