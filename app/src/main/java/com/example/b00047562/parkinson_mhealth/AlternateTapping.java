package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AlternateTapping extends AppCompatActivity implements View.OnClickListener {

    private long previousClickTime,previousClickTime2;
    private Button btn1,btn2,start,next;
    private int tapcounter1,tapcounter2,outsidetaps;
    private CountDownTimer countDownTimer;
    private ParseFunctions customParse;
    private RelativeLayout altrellayout;


    Runnable mRunnable;
    Handler mHandler=new Handler();
    private ProgressBar barTimer;

    private TextView time1,time2;

    private ArrayList<Long> delaylist1,delaylist2;
    private ArrayList<AltTapData> locationarr1,locationarr2,locationarr3,locationarr4;
    private ArrayList<Integer> originalbtnloc;
    private AltTapData altdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_tapping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        btn1=(Button)findViewById(R.id.alttap_btn1);
        btn2=(Button)findViewById(R.id.alttap_btn2);
        next=(Button)findViewById(R.id.btn_next2);
        start=(Button)findViewById(R.id.start_btn);
        barTimer=(ProgressBar)findViewById(R.id.progressBar_alttap);
        time1=(TextView)findViewById(R.id.tv_time1);
        time2=(TextView)findViewById(R.id.tv_time2);
        altrellayout=(RelativeLayout)findViewById(R.id.altrellayout);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        start.setOnClickListener(this);
        next.setOnClickListener(this);
        //customParse= new ParseFunctions(getApplicationContext());
        customParse = new ParseFunctions();

        //----------------------------------------------
        originalbtnloc = new ArrayList<>();

//        int[] values1 = new int[2];
//        btn1.getLocationOnScreen(values1);
//        int[] values2 = new int[2];
//        btn2.getLocationOnScreen(values2);
//
//        originalbtnloc.add(values1[0]);
//        originalbtnloc.add(values1[1]);
//        originalbtnloc.add(values2[0]);
//        originalbtnloc.add(values2[1]);

        //Log.d("OriginalX&Y", originalbtnloc.toString());
        //----------------------------------------------

        //barTimer.getProgressDrawable().setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.SRC_IN);
        showHelpDialog();

        /*
        TODO
        To measure the overall precision while tapping the two fields over the
test trial, the mean distance from the centers of the fields (MDCF) was calculated. For the taps that
were tapped within the area of the fields, the distance was preset to zero. The second parameter
measures the regularity of precision over the test trial and is defined as the CV of distances from the
center fields (CVDCF). The higher the CVDCF, the higher irregularity of tapping precision is. In order
to quantify the overall distribution of the taps (ODT) over the two fields, initially the variation (ratio
between summed distance and total number of taps) for each field was calculated followed by a
calculation of mean variation of the two fields. Finally, the overall tapping precision (OTP) was
defined as the mean distance from center fields irrespective of whether the taps were inside or outside
the field areas, corrected for total number of taps. (page 7 , Automatic and Objective Assessment of Alternating Tapping Performance in Parkinson’s Disease,Sensors 2013, 13, 16965-16984; doi:10.3390/s131216965)
         */
        btn1.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    altdata = new AltTapData(System.currentTimeMillis(),event.getRawX(),event.getRawY());
                    locationarr1.add(altdata);
                    //Log.d("X&Ybtn1", locationarr1.toString());
                    //Log.d("X&Ybtn1","X:"+values1[0]+"Y:"+values1[1]);
                }
                return false;
            }
        });

        btn2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    altdata = new AltTapData(System.currentTimeMillis(),event.getRawX(),event.getRawY());
                    locationarr2.add(altdata);
                    // Log.d("X&Ybtn2", locationarr2.toString());
                    // Log.d("X&Ybtn2","X:"+values1[0]+"Y:"+values1[1]);

                }
                return false;
            }
        });
        altrellayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    outsidetaps++;
                    altdata = new AltTapData(System.currentTimeMillis(), event.getRawX(), event.getRawY());
                    if(event.getRawX()>=550) {
                        locationarr4.add(altdata);//right taps outside field
                        Log.d("X&YrellayoutRIGHT", locationarr4.toString());
                    }
                    else if(event.getRawX()<550) {
                        locationarr3.add(altdata); //left taps outside fields
                        Log.d("X&YrellayoutLEFT", locationarr3.toString());
                    }

                }
                return false;//always return true to consume event
            }
        });

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
                altrellayout.setFocusableInTouchMode(false);
                altrellayout.setClickable(false);
                altrellayout.setFocusable(false);

                btn1.setEnabled(false);
                btn2.setEnabled(false);
                //next test enable btn (add here)
                next.setEnabled(true);
                //display avg times
                time1.setText("Avg: "+String.format("%.1f", average(delaylist1))+" ms");
                time2.setText("Avg: "+String.format("%.1f", average(delaylist2))+" ms");

                String json1 = new Gson().toJson(delaylist1);
                String json2 = new Gson().toJson(delaylist2);

                String json3 = new Gson().toJson(locationarr1);
                String json4 = new Gson().toJson(locationarr2);

                String json5 = new Gson().toJson(locationarr3);
                String json6 = new Gson().toJson(locationarr4);

                customParse.pushParseList(ParseUser.getCurrentUser(),2,"TappingData","ArrayList",json1,json2,"Left","Right",Integer.toString(tapcounter1),Integer.toString(tapcounter2));
                customParse.pushParseList(ParseUser.getCurrentUser(), 2, "TappingData", "ArrayList",json3,json4,"LeftXY","RightXY",Integer.toString(tapcounter1),Integer.toString(tapcounter2) );
                customParse.pushParseList(ParseUser.getCurrentUser(), 2, "TappingData", "ArrayList",json5,json6,"outL","outR",Integer.toString(outsidetaps),Integer.toString(outsidetaps));
            }
        }.start();

    }//http://stackoverflow.com/questions/20010997/circular-progress-bar-for-a-countdown-timer

    @Override
    protected void onResume() {
        super.onResume();
        tapcounter1 = 0;
        tapcounter2 = 0;
        outsidetaps=0;
        delaylist1 = new ArrayList<>();
        delaylist2= new ArrayList<>();
        locationarr1 = new ArrayList<>();
        locationarr2 = new ArrayList<>();
        locationarr3 = new ArrayList<>();
        locationarr4 = new ArrayList<>();

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        next.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.start_btn:
                startTimer();
                altrellayout.setFocusableInTouchMode(true);
                altrellayout.setClickable(true);
                altrellayout.setFocusable(true);

                btn1.setEnabled(true);
                btn2.setEnabled(true);
                start.setEnabled(false);

                break;

            case R.id.alttap_btn1:

//                int[] values1 = new int[2];
//                btn1.getLocationOnScreen(values1);

                btn2.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                btn1.setBackgroundColor(Color.parseColor("#FFCFD8DC"));

                btn1.setEnabled(false);
                btn2.setEnabled(true);
                long temp = System.currentTimeMillis();
                if (previousClickTime != 0)
                {
                    //Log.i("MyView", "Time Between Clicks=" + (temp - previousClickTime));
                    delaylist1.add(tapcounter1++, temp - previousClickTime);
                }
                else {
                    //Log.i("MyView", "First Click");
                    //btn1.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                }
                previousClickTime = temp;
                break;

            case R.id.alttap_btn2:

//                int[] values2 = new int[2];
//                btn2.getLocationOnScreen(values2);



                btn1.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                btn2.setBackgroundColor(Color.parseColor("#FFCFD8DC"));

                btn1.setEnabled(true);
                btn2.setEnabled(false);
                long temp2 = System.currentTimeMillis();
                if (previousClickTime2 != 0)
                {
                    //Log.i("MyView", "Time Between Clicks=" + (temp2 - previousClickTime2));
                    delaylist2.add(tapcounter2++, temp2 - previousClickTime2);
                }
                else {
                    //Log.i("MyView", "First Click");
                    //btn2.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                }
                previousClickTime2 = temp2;
                break;
            case R.id.btn_next2:
                startActivity(new Intent(getApplicationContext(),InvolMovement.class));
                break;
        }

    }
    public static double average(List<Long> list) {
        // 'average' is undefined if there are no elements in the list.
        if (list == null || list.isEmpty())
            return 0.0;
        // Calculate the summation of the elements in the list
        long sum = 0;
        int n = list.size();
        // Iterating manually is faster than using an enhanced for loop.
        for (int i = 0; i < n; i++)
            sum += list.get(i);
        // We don't want to perform an integer division, so the cast is mandatory.
        return ((double) sum) / n;
    }
    public void showHelpDialog()
    {
        WebView view = new WebView(AlternateTapping.this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        AlertDialog alertDialog = new AlertDialog.Builder(AlternateTapping.this).create();
        alertDialog.setView(view);
        alertDialog.setTitle("What to do ?");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Tap the squares on screen as fast as possible within 20 seconds\nAlternate between index and middle finger\nInstructor will guide you through\n");
        //alertDialog.setIcon(R.drawable.tapping5);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        view.loadUrl("file:///android_asset/tapping3.png");

    }
}
