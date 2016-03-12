package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Spiral extends AppCompatActivity implements  View.OnClickListener {

    public static Button redrawOpen,btnClr,btnSubmit;
    private CanvasSpiral customCanvas;
    public static TextView alert;
    private ParseFunctions customParse;
    private AlphaAnimation animationIn;
    private AlphaAnimation animationOut;
    private boolean DynamicFlag=false;

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

        animationIn = new AlphaAnimation(0.0f,1.0f);
        animationOut= new AlphaAnimation(1.0f,0.0f);
        btnClr.setOnClickListener(this);
        redrawOpen.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
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

                String json = new Gson().toJson(CanvasSpiral.spiralData);
                customParse.pushParseData(ParseUser.getCurrentUser(), "SpiralData", "ArrayList", json, "", "");
                /**demo PURPOSES **/
                customCanvas.cleardisp();
                if (DynamicFlag) {
                    MainActivity.sp = true; //test finished
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                DynamicFlag=true;

                customCanvas.startAnimation(animationIn);
                customCanvas.startAnimation(animationOut);
                animationIn.setDuration(2000);
                animationIn.setFillAfter(true);
                animationOut.setDuration(2000);
                animationOut.setFillAfter(true);
                animationOut.setStartOffset(4200+animationIn.getStartOffset());
//
//                Timer timerInvisible=new Timer ();
//                timerInvisible.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                     runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             customCanvas.setVisibility(View.INVISIBLE);
//                         }
//                     });
//                    }
//                },2000);
//
//                timerInvisible.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                customCanvas.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                },4000);
//                timerInvisible.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                customCanvas.setVisibility(View.INVISIBLE);
//                            }
//                        });
//                    }
//                }, 6000);
//                timerInvisible.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                customCanvas.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                }, 8000);


             //   customCanvas.setVisibility(View.VISIBLE);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_container, new DynamicSpiralFragment());
//                ft.commit();
                break;


        }
    }
}
