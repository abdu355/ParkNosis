package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.parse.ParseUser;

import java.util.ArrayList;

public class Questionnaire extends AppCompatActivity implements View.OnClickListener {

    /*TODO
          this section is incomplete , but the data is correctly transferred to results page
           */
    //private RadioGroup g1,g2,g3,g4;
    private int numofquestions=27; //define number of questions
    int radioButtonID ;
    View radioButton ;
    Button submit;
    int idx;
    private ParseFunctions customParse;
    private ArrayList<RadioGroup> groupid;
    private ArrayList<Integer> ansarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groupid=new ArrayList<>();
        customParse = new ParseFunctions(getApplicationContext());

        /*TODO
        more to be added
         */
        groupid.add((RadioGroup)findViewById(R.id.g1));//index 0
        groupid.add((RadioGroup)findViewById(R.id.g2));
        groupid.add((RadioGroup)findViewById(R.id.g3));
        groupid.add((RadioGroup)findViewById(R.id.g3a));
        groupid.add((RadioGroup)findViewById(R.id.g3b));
        groupid.add((RadioGroup)findViewById(R.id.g3c));
        groupid.add((RadioGroup)findViewById(R.id.g3d));
        groupid.add((RadioGroup)findViewById(R.id.g4));
        groupid.add((RadioGroup)findViewById(R.id.g4a));
        groupid.add((RadioGroup)findViewById(R.id.g4b));
        groupid.add((RadioGroup)findViewById(R.id.g4c));
        groupid.add((RadioGroup)findViewById(R.id.g4d));
        groupid.add((RadioGroup)findViewById(R.id.g5));
        groupid.add((RadioGroup)findViewById(R.id.g5b));
        groupid.add((RadioGroup)findViewById(R.id.g6));
        groupid.add((RadioGroup)findViewById(R.id.g6b));
        groupid.add((RadioGroup)findViewById(R.id.g7));
        groupid.add((RadioGroup)findViewById(R.id.g7b));
        groupid.add((RadioGroup)findViewById(R.id.g8a));
        groupid.add((RadioGroup)findViewById(R.id.g8b));
        groupid.add((RadioGroup)findViewById(R.id.g9));
        groupid.add((RadioGroup)findViewById(R.id.g10));
        groupid.add((RadioGroup)findViewById(R.id.g11));
        groupid.add((RadioGroup)findViewById(R.id.g12));
        groupid.add((RadioGroup)findViewById(R.id.g13));
        groupid.add((RadioGroup)findViewById(R.id.g14a));
        groupid.add((RadioGroup)findViewById(R.id.g14b));//index 26

        submit=(Button)findViewById(R.id.btn_submitquest);

        submit.setOnClickListener(this);

        showHelpDialog();
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
        String json = new Gson().toJson(ansarr);
        customParse.pushParseData(ParseUser.getCurrentUser(),"Questionnaire","Answers",json,"",""); //user pointer

        //go to main
        finish();
        MainActivity.q = true;//test finished
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
    public void showHelpDialog()
    {


        AlertDialog alertDialog = new AlertDialog.Builder(Questionnaire.this).create();
        alertDialog.setTitle("What to do ?");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Instructor will guide you through the survey\nthe questionnaire will be filled by instructor\n");
        alertDialog.setIcon(R.drawable.questicon);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
