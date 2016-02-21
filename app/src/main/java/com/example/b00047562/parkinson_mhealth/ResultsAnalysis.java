package com.example.b00047562.parkinson_mhealth;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

/*TODO
call data processing functions from this class and use AsyncTasks
 */

public class ResultsAnalysis extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    private TappingTestFunctions tapresults;
    ParseFunctions customParse;
    Double qscore; //questionnaire score - not overall score
    TextView extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customParse = new ParseFunctions(getApplicationContext());
        tapresults= new TappingTestFunctions();

        extras = (TextView)findViewById(R.id.tv_question_extra);

    }


    @Override
    protected void onResume() {
        super.onResume();
        new processTappingDataTask().execute(); //Tapping data task
    }

    private class processTappingDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ResultsAnalysis.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Fetching Results");
            // Set progressdialog message
            mProgressDialog.setMessage("Hang on...");
            mProgressDialog.setIcon(R.drawable.process);
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            processTappingData();
            processQuestionnaire();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mProgressDialog.dismiss();
            tapresults.displayResults();
            extras.setText("Questionnaire Score: "+qscore);

            //call next AsyncTask
            //--place functions for other Tests ex: HandTremorAsyncTask or SpiralDataAsyncTask

            // Vibrator vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // vibe.vibrate(50); // 50 is time in ms
        }
    }

     private void processTappingData()
    {
        tapresults.runAlgorithm(tapresults.fetchData());   //run algo on arraylist retreived from parse
        //tapresults.displayResults();
    }
    private void processQuestionnaire()//questionairre data fetch result
    {
        qscore = sumArr(customParse.getParseData(ParseUser.getCurrentUser(), 0, "Questionnaire", "createdAt", "Answers"));
    }

    public double sumArr(ArrayList<Long> arr)
    {
        double sum = 0;
        for(int i = 0; i < arr.size(); i++)
        {
            sum += arr.get(i);
        }
        return sum;
    }
}
