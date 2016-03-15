package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
        //customParse = new ParseFunctions(getApplicationContext());
        customParse = new ParseFunctions();
        btnSubmit= (Button) findViewById(R.id.btnSubmit);


        animationOut= new AlphaAnimation(1.0f,-1.0f);
        btnClr.setOnClickListener(this);
        redrawOpen.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        showHelpDialog();
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



                customCanvas.cleardisp();
                if (DynamicFlag) {
                    MainActivity.sp = true; //test finished
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                DynamicFlag=true;

                customCanvas.startAnimation(animationOut);
                animationOut.setDuration(3000);
                animationOut.setFillAfter(true);
                animationOut.startNow();
                animationOut.setRepeatCount(Animation.INFINITE);

                break;


        }
    }
    public void showHelpDialog()
    {
        WebView view = new WebView(Spiral.this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        AlertDialog alertDialog = new AlertDialog.Builder(Spiral.this).create();
        alertDialog.setView(view);
        alertDialog.setTitle("What to do ?");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Place the phone on a flat surface and draw a complete spiral using your index finger\nThe spiral may dissapear and appear after a while\nInstructor will guide you through\n");
        alertDialog.setIcon(R.drawable.spiralicon3);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        view.loadUrl("file:///android_asset/spiraldraw.png");
    }
}
