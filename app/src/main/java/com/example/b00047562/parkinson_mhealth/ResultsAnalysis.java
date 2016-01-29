package com.example.b00047562.parkinson_mhealth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class ResultsAnalysis extends AppCompatActivity {

    private TappingTestFunctions tapresults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tapresults= new TappingTestFunctions();


    }


    @Override
    protected void onResume() {
        super.onResume();
        displayData();

    }

     private void displayData()
    {
        //tapresults.runAlgorithm(tapresults.fetchData());   //run algo on arraylist retreived from parse
        //tapresults.displayResults();
    }
}
