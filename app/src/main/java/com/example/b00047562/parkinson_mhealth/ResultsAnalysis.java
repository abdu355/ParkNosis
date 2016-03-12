package com.example.b00047562.parkinson_mhealth;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.ParseUser;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;

import java.util.ArrayList;

/*TODO
call data processing functions from this class and use AsyncTasks
 */

public class ResultsAnalysis extends AppCompatActivity {


    private static final int NORMAL=0,SLIGHT=1,MILD=2,MODERATE=3,SEVERE=4; /**FOR SPIRAL, NO CALCULATIONS ASSOCIATED WITH THESE NUMBERS*/

    ProgressDialog mProgressDialog;
    private boolean clicked=false;
    private TappingTestFunctions tapresults;
    private SpiralDataProcessing spiralDataProcessing;
    private AccelAnalysis accelresult;
    SpiralData sd;
    public static ArrayList<SpiralData> StaticSpiralData;
    public static ArrayList<SpiralData> DynamicSpiralData;




    ParseFunctions customParse;
    Double qscore; //questionnaire score - not overall score
    TextView extras,advice,extra1,title_tv,tapscore_tv,spiralscore_tv,handsore_tv;

    private FrameLayout primarygraph,secondarygraph;

    TableRow ad1,ad2,ad3,tapres,spiralres,handres,graphs,title;
    Button showhide;
    private View mChart1,mChart2;

    private int tapscore,handscore,spiralscore; //use these for final scale



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customParse = new ParseFunctions(getApplicationContext());
        tapresults= new TappingTestFunctions();
        accelresult = new AccelAnalysis(10.0);
        /*ToDo
        Karim: I made AccelAnalysis constructor require an Int parameter in order to pass Fs (sensor sampling rate) from accelerometer class
        So it broke this...
         */
        //accelresult= new AccelAnalysis();

        ad1=(TableRow)findViewById(R.id.tbrow_ad1);
        ad2=(TableRow)findViewById(R.id.tbrow_ad2);
        ad3=(TableRow)findViewById(R.id.tbrow_ad3);
        graphs=(TableRow)findViewById(R.id.tbrow_graphs);
        tapres= (TableRow)findViewById(R.id.tbrow_tap);
        spiralres=(TableRow)findViewById(R.id.tbrow_spiral);
        handres=(TableRow)findViewById(R.id.tbrow_hand);
        title=(TableRow)findViewById(R.id.tbrow_title);

        showhide=(Button)findViewById(R.id.btn_showadvice);

        extras = (TextView)findViewById(R.id.tv_question_extra); //questionnaire score here
        extra1=(TextView)findViewById(R.id.tv_extra1);
        advice= (TextView)findViewById(R.id.advice_tv_analysis); //detail advice and score details
        title_tv=(TextView)findViewById(R.id.tv_title);
        tapscore_tv=(TextView)findViewById(R.id.tapscore_tv);
        spiralscore_tv=(TextView)findViewById(R.id.tv_spiralres);
        handsore_tv=(TextView)findViewById(R.id.handres_tv);


        //test draw a bar chart
        primarygraph=(FrameLayout)findViewById(R.id.content_primary);
        secondarygraph=(FrameLayout)findViewById(R.id.content_secondary);

        StaticSpiralData= new ArrayList();
        DynamicSpiralData= new ArrayList();
        sd=new SpiralData(0,0,0,this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        new processDataTask().execute(); //Tapping data task
    }

//    private class processSpiralDataTask extends AsyncTask<Void,Void,Void>
//    {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(ResultsAnalysis.this);
//            // Set progressdialog title
//            mProgressDialog.setTitle("Fetching Results");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Hang on...");
//            mProgressDialog.setIcon(R.drawable.process);
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog
//            mProgressDialog.show();
//        }
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//
//        }
//
//    }

    private class processDataTask extends AsyncTask<Void, Void, Void> {
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
            processQuestionnaire();
            processTappingData();
            processAccelData();//accel data (karim)
            try{processSpiralData();}catch (Exception e){
                Log.d("TAG", "doInBackground: Spiral analysis not working "+ e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mProgressDialog.dismiss();
            //tapresults.displayResults();
            extras.setText("Questionnaire Score: " + qscore);
            tapscore_tv.setText(tapscore+"");
            spiralscore_tv.setText(""+spiralscore);
            handsore_tv.setText(""+handscore);

            displayscoreAdvice();   // calls all display functions

            //display graphs for  results
            viewgraphs();

            //call next AsyncTask
            //--place functions for other Tests ex: HandTremorAsyncTask or SpiralDataAsyncTask

            // Vibrator vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // vibe.vibrate(50); // 50 is time in ms
        }
    }

    private void processSpiralData() {
        spiralscore=0;

        /**TODO
         * Initialize the following:- */
        StaticSpiralData =sd.getAS();
        DynamicSpiralData=sd.getDAS();
        spiralDataProcessing= new SpiralDataProcessing(StaticSpiralData,DynamicSpiralData);
        final float DAH=spiralDataProcessing.getDAH();
        Log.d("DAH", "processSpiralData: "+DAH);

       if(0.04f >= DAH)
          spiralscore=NORMAL;
        else if (0.05f>=DAH&&DAH<0.075)
           spiralscore=SLIGHT;
        else if (0.075f>=DAH&&DAH<0.1f)
           spiralscore=MILD;
       else if (0.1f>=DAH&&DAH<0.15f)
           spiralscore=MODERATE;
        else if (DAH>=0.15f)
           spiralscore=SEVERE;
    }

    private void processAccelData()
    {

        //accelresult.getAccelData();
        handscore = accelresult.PerformAnalysis1();
    }

     private void processTappingData()
    {
        //tapresults.runAlgorithm(tapresults.fetchData());   //run algo on arraylist retreived from parse
        tapresults.fetchData();
        //run tempalgo here
        tapscore=tapresults.runTempAlgorithm();
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

    //questionnaire score thresholds  59+/108 = severe -   33-58/108 moderate - 32 and below/108 mild
    private void displayscoreAdvice()
    {
        if(qscore <20)//normal
        {
            advice.setText("Your Questionnaire score shows almost no symptoms\n Nothing to worry about for now");
        }
       else if(qscore<=32) //mild
     {
            advice.setText("Your Questionnaire score shows mild symptoms\nRepeat the tests every month to track your symptoms");
     }
        else if(qscore>=33 && qscore<=58) //moderate
     {
            advice.setText("Your Questionnaire score shows moderate symptoms\nconsider visiting your docotor ");
     }
        else if(qscore>=59)//severe
     {
            advice.setText("Your Questionnaire score shows severe symptoms\nconsider visiting your docotor ");
     }

        extra1.setText("Keep in mind that Questionnaire scores may not reflect all symptoms.\nConsider scores for other tests aswell.");
    }

    private void viewgraphs()
    {

        mChart1 = ChartFactory.getLineChartView(getBaseContext(), tapresults.getDataSet1(),
                tapresults.getRenderer1());
        mChart2 =ChartFactory.getBarChartView(getBaseContext(), tapresults.getDataSet2(),
                tapresults.getRenderer2(), BarChart.Type.STACKED);
        // Adding the Line Chart to the FrameLayout
        primarygraph.addView(mChart1);
        secondarygraph.addView(mChart2);
    }

    public void displayadvicebtn(View v) //btn trigger
    {
        if(clicked==false) {

            graphs.setVisibility(View.GONE);
            ad1.setVisibility(View.VISIBLE);
            ad2.setVisibility(View.VISIBLE);
            ad3.setVisibility(View.VISIBLE);
            tapres.setVisibility(View.VISIBLE);
            spiralres.setVisibility(View.VISIBLE);
            handres.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            title_tv.setVisibility(View.GONE);
            clicked=true;
            showhide.setText("Show Graphs");
        }
        else
        {
            graphs.setVisibility(View.VISIBLE);
            ad1.setVisibility(View.GONE);
            ad2.setVisibility(View.GONE);
            ad3.setVisibility(View.GONE);
            tapres.setVisibility(View.GONE);
            spiralres.setVisibility(View.GONE);
            handres.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            title_tv.setVisibility(View.VISIBLE);
            clicked=false;
            showhide.setText("Show Advice");
        }
    }
}
