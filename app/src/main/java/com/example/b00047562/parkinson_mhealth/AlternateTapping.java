package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AlternateTapping extends AppCompatActivity implements View.OnClickListener {

    private long previousClickTime,previousClickTime2;
    private Button btn1,btn2,start,next;
    private int tapcounter1,tapcounter2;
    private CountDownTimer countDownTimer;
    private ParseFunctions customParse;


    Runnable mRunnable;
    Handler mHandler=new Handler();
    private ProgressBar barTimer;

    private TextView time1,time2;

    private ArrayList<Long> delaylist1,delaylist2;
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


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        start.setOnClickListener(this);
        next.setOnClickListener(this);
        customParse= new ParseFunctions(getApplicationContext());

        barTimer.getProgressDrawable().setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.SRC_IN);

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
                //next test enable btn (add here)
                next.setEnabled(true);
                //display avg times
                time1.setText("Avg: "+String.format("%.1f", average(delaylist1))+" ms");
                time2.setText("Avg: "+String.format("%.1f", average(delaylist2))+" ms");

                String json1 = new Gson().toJson(delaylist1);
                String json2 = new Gson().toJson(delaylist2);

                customParse.pushParseList(ParseUser.getCurrentUser(),2,"TappingData","ArrayList",json1,json2,"Left","Right",Integer.toString(tapcounter1),Integer.toString(tapcounter2));

            }
        }.start();

    }//http://stackoverflow.com/questions/20010997/circular-progress-bar-for-a-countdown-timer

    @Override
    protected void onResume() {
        super.onResume();
        tapcounter1 = 0;
        tapcounter2 = 0;
        delaylist1 = new ArrayList<>();
        delaylist2= new ArrayList<>();

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
                btn1.setEnabled(true);
                btn2.setEnabled(true);
                start.setEnabled(false);

                break;

            case R.id.alttap_btn1:

                btn2.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                btn1.setBackgroundColor(Color.parseColor("#FFCFD8DC"));

                btn1.setEnabled(false);
                btn2.setEnabled(true);
                long temp = System.currentTimeMillis();
                if (previousClickTime != 0)
                {
                    Log.i("MyView", "Time Between Clicks=" + (temp - previousClickTime));
                    delaylist1.add(tapcounter1++, temp - previousClickTime);
                }
                else {
                    Log.i("MyView", "First Click");
                    //btn1.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                }
                previousClickTime = temp;
                break;

            case R.id.alttap_btn2:

                btn1.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                btn2.setBackgroundColor(Color.parseColor("#FFCFD8DC"));

                btn1.setEnabled(true);
                btn2.setEnabled(false);
                long temp2 = System.currentTimeMillis();
                if (previousClickTime2 != 0)
                {
                    Log.i("MyView", "Time Between Clicks=" + (temp2 - previousClickTime2));
                    delaylist2.add(tapcounter2++, temp2 - previousClickTime2);
                }
                else {
                    Log.i("MyView", "First Click");
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
}
