package com.example.b00047562.parkinson_mhealth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class Questionnaire extends AppCompatActivity implements View.OnClickListener {


    //private RadioGroup g1,g2,g3,g4;
    private int numofquestions=7; //define number of questions
    int radioButtonID ;
    View radioButton ;
    Button submit;
    int idx;
    private ArrayList<RadioGroup> groupid;
    private ArrayList<Integer> ansarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groupid=new ArrayList<>();


        groupid.add((RadioGroup)findViewById(R.id.g1));//index 0
        groupid.add((RadioGroup)findViewById(R.id.g2));
        groupid.add((RadioGroup)findViewById(R.id.g3));
        groupid.add((RadioGroup)findViewById(R.id.g4));
        groupid.add((RadioGroup)findViewById(R.id.g5));
        groupid.add((RadioGroup)findViewById(R.id.g6));
        groupid.add((RadioGroup)findViewById(R.id.g7));//index 6

        submit=(Button)findViewById(R.id.btn_submitquest);

        submit.setOnClickListener(this);
    }

    private void submit()
    {
        for(int i=0;i<numofquestions;i++)
        {
            //Before submit, do this for all radio groups
            //1-get index of each button
            radioButtonID=groupid.get(i).getCheckedRadioButtonId();
            radioButton=groupid.get(i).findViewById(radioButtonID);
            idx=groupid.get(i).indexOfChild(radioButton);
            //2-add it to the arraylist of answers
            ansarr.add(idx);
        }

        //upload to parse here ...

        //go to next test...
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_submitquest:
                ansarr=new ArrayList<>();
                submit();
                Log.d("Answers",ansarr.toString());
                break;

        }
    }
}
