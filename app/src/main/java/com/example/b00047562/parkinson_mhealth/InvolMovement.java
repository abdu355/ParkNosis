package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

public class InvolMovement extends AppCompatActivity {

    private ProgressBar barTimer;
    private CountDownTimer countDownTimer;
    private Button btn1,btn2,start,done;
    private TextView leftview,rightview;
    private int tapcount_left,tapcount_right;
    private ParseFunctions customParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invol_movement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barTimer=(ProgressBar)findViewById(R.id.progressBar_invol);

        btn1 =(Button)findViewById(R.id.lfinger_invol);
        btn2=(Button)findViewById(R.id.rfinger_invol);
        done=(Button)findViewById(R.id.btn_finish_involtap);
        start=(Button)findViewById(R.id.btn_start_invol);
        leftview=(TextView)findViewById(R.id.left_indic);
        rightview=(TextView)findViewById(R.id.right_indic);
        customParse= new ParseFunctions(getApplicationContext());

        //barTimer.getProgressDrawable().setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.SRC_IN);
        showHelpDialog();
    }


    public void leftTrigger(View v)
    {
        if(tapcount_left<1)
            btn1.setBackgroundColor(Color.parseColor("#ff8a80"));
        else if (tapcount_left>3)
            btn1.setBackgroundColor(Color.parseColor("#FFEF5350"));
        else if(tapcount_left>5)
            btn1.setBackgroundColor(Color.parseColor("#FFD50000"));
        tapcount_left++;

    }
    public void rightTrigger(View v)
    {
        if(tapcount_right<1)
            btn2.setBackgroundColor(Color.parseColor("#ff8a80"));
        else if (tapcount_right>3)
            btn2.setBackgroundColor(Color.parseColor("#FFEF5350"));
        else if(tapcount_right>5)
            btn2.setBackgroundColor(Color.parseColor("#FFD50000"));
        tapcount_right++;
    }
    public void startDetection(View v)
    {

        startTimer();
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        start.setEnabled(false);

    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(20*1000, 500) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                int progress = (int) (leftTimeInMilliseconds/100);
                barTimer.setProgress(progress);

            }
            @Override
            public void onFinish() {
                barTimer.setProgress(0);
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                done.setEnabled(true);
                //next test enable btn (add here)

                //display avg times
                leftview.setText("Taps: "+ tapcount_left);
                rightview.setText("Taps: "+ tapcount_right);

                customParse.pushParseList(ParseUser.getCurrentUser(), 2, "TappingData", "ArrayList", "Involuntary", "Involuntary", "Left", "Right", Integer.toString(tapcount_left), Integer.toString(tapcount_right));

                MainActivity.t=true; //test finished

            }
        }.start();

    }//http://stackoverflow.com/questions/20010997/circular-progress-bar-for-a-countdown-timer
    @Override
    protected void onResume() {
        super.onResume();
        tapcount_left = 0;
        tapcount_right = 0;

        btn1.setEnabled(false);
        btn2.setEnabled(false);
    }

    public void finishTest(View v)
    {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void showHelpDialog()
    {
        WebView view = new WebView(InvolMovement.this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        AlertDialog alertDialog = new AlertDialog.Builder(InvolMovement.this).create();
        alertDialog.setView(view);
        alertDialog.setTitle("What to do ?");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("rest your hand above the phone screen for 20 seconds\ntry not to press any of the buttons\nInstructor will guide you through\n");
        alertDialog.setIcon(R.drawable.tapping5);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        view.loadUrl("file:///android_asset/holdabove2.png");

    }
}
