package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SimpleTapping extends AppCompatActivity implements View.OnClickListener {

    private Button tap,next,left,right;
    private int tapcounter;
    private long previousClickTime;
    private AlphaAnimation alphaDown,alphaUp;
    private TextView avgtimedisp;
    private ArrayList<Long> delaylist;
    private ParseFunctions customParse;
    private String RL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tap=(Button)findViewById(R.id.btn_tap);
        next=(Button)findViewById(R.id.btn_next1);
        left=(Button)findViewById(R.id.btn_lefthand);
        right=(Button)findViewById(R.id.btn_righthand);
        avgtimedisp=(TextView)findViewById(R.id.tv_avgtime);

        tap.setOnClickListener(this);
        next.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);


        delaylist = new ArrayList<>();
        customParse= new ParseFunctions();

        showHelpDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tapcounter = 0;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_tap:
                alphaDown = new AlphaAnimation(1.0f, 0.3f);
                alphaUp = new AlphaAnimation(0.3f, 1.0f);
                alphaDown.setDuration(1000);
                alphaUp.setDuration(500);
                alphaDown.setFillAfter(true);
                alphaUp.setFillAfter(true);
                tap.startAnimation(alphaUp);


                long temp = System.currentTimeMillis();
                if (previousClickTime != 0)
                {
                    Log.i("MyView", "Time Between Clicks=" + (temp - previousClickTime));
                    delaylist.add(tapcounter++, temp - previousClickTime);
                }
                else
                {
                    Log.i("MyView", "First Click");
                    tap.setBackgroundColor(Color.parseColor("#FFDCEDC8"));
                }
                previousClickTime = temp;

                if(tapcounter==10)
                {
                    tap.setBackgroundColor(Color.parseColor("#FFE1BEE7"));
                    tap.setText("DONE!");
                    tap.setEnabled(false);
                    avgtimedisp.setText("Average: " + average(delaylist) + " ms");
                    next.setEnabled(true);
                    String json = new Gson().toJson(delaylist);
                    customParse.pushParseData(ParseUser.getCurrentUser(),"TappingData","ArrayList",json,Integer.toString(tapcounter),RL);


                }
                break;
            case R.id.btn_next1:
                startActivity(new Intent(getApplicationContext(),AlternateTapping.class));
                break;
            case R.id.btn_lefthand:
                right.setEnabled(false);
                tap.setEnabled(true);
                RL = "left";
                break;
            case R.id.btn_righthand:
                left.setEnabled(false);
                tap.setEnabled(true);
                RL = "right";
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
        WebView view = new WebView(SimpleTapping.this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        AlertDialog alertDialog = new AlertDialog.Builder(SimpleTapping.this).create();
        alertDialog.setView(view);
        alertDialog.setTitle("What to do ?");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Tap the square on screen as fast as possible 10 times\nInstructor will guide you through\n");
        //alertDialog.setIcon(R.drawable.tapping5);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        view.loadUrl("file:///android_asset/simpletap.png");

    }
}
