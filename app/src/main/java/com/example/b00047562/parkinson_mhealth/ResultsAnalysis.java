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
import android.widget.Toast;

import com.parse.ParseUser;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*TODO
call data processing functions from this class and use AsyncTasks
 */

public class ResultsAnalysis extends AppCompatActivity {


    private static final int NORMAL=0,SLIGHT=1,MILD=2,MODERATE=3,SEVERE=4;
    private Map<Integer,String> ResultsMap;
    private ProgressDialog mProgressDialog;
    private boolean clicked=false;
    private TappingTestFunctions tapresults;
    private SpiralDataProcessing spiralDataProcessing;
    private AccelAnalysis accelresult;
    private SpiralData sd;
    public static ArrayList<SpiralData> StaticSpiralData;
    public static ArrayList<SpiralData> DynamicSpiralData;

    private ParseFunctions customParse;
    private  Double qscore; //questionnaire score - not overall score
    private TextView extras,advice,extra1,title_tv,tapscore_tv,spiralscore_tv,handsore_tv;

    private FrameLayout primarygraph,secondarygraph;

    private  TableRow ad1,ad2,ad3,tapres,spiralres,handres,graphs,title;
    private Button showhide;
    private View mChart1,mChart2;
    private double tapprecision;

    private int tapscore,handscore,spiralscore; //use these for final scale

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ResultsMap=new HashMap<>() ;
        ResultsMap.put(0, getString(R.string.Results_Normal));
        ResultsMap.put(1,getString(R.string.Results_Slight));
        ResultsMap.put(2,getString(R.string.Results_Mild));
        ResultsMap.put(3,getString(R.string.Results_Moderate));
        ResultsMap.put(4,getString(R.string.Results_Severe));

        //customParse = new ParseFunctions(getApplicationContext());
         customParse = new ParseFunctions();
         tapresults= new TappingTestFunctions();
         accelresult = new AccelAnalysis(10.0);
        initialize();

        //accelresult= new AccelAnalysis();

//        ad1=(TableRow)findViewById(R.id.tbrow_ad1);
//        ad2=(TableRow)findViewById(R.id.tbrow_ad2);
//        ad3=(TableRow)findViewById(R.id.tbrow_ad3);
//        graphs=(TableRow)findViewById(R.id.tbrow_graphs);
//        tapres= (TableRow)findViewById(R.id.tbrow_tap);
//        spiralres=(TableRow)findViewById(R.id.tbrow_spiral);
//        handres=(TableRow)findViewById(R.id.tbrow_hand);
//        title=(TableRow)findViewById(R.id.tbrow_title);
//
//        showhide=(Button)findViewById(R.id.btn_showadvice);
//
//        extras = (TextView)findViewById(R.id.tv_question_extra); //questionnaire score here
//        extra1=(TextView)findViewById(R.id.tv_extra1);
//        advice= (TextView)findViewById(R.id.advice_tv_analysis); //detail advice and score details
//        title_tv=(TextView)findViewById(R.id.tv_title);
//        tapscore_tv=(TextView)findViewById(R.id.tapscore_tv);
//        spiralscore_tv=(TextView)findViewById(R.id.tv_spiralres);
//        handsore_tv=(TextView)findViewById(R.id.handres_tv);
//
//
//        //test draw a bar chart
//        primarygraph=(FrameLayout)findViewById(R.id.content_primary);
//        secondarygraph=(FrameLayout)findViewById(R.id.content_secondary);

//        StaticSpiralData= new ArrayList();
//        DynamicSpiralData= new ArrayList();
//        sd=new SpiralData(0,0,0,this);
        new processDataTask().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Tapping data task
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
            mProgressDialog.setCancelable(false);
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
            StaticSpiralData= new ArrayList();
            DynamicSpiralData= new ArrayList();
            sd=new SpiralData(0,0,0,getApplicationContext());
            //initialize();
            try{
            processQuestionnaire();
            processTappingData();
            processAccelData();//accel data (karim)
            processSpiralData();
            }catch (Exception e){
                Log.d("TAG", "doInBackground: Error " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "ProcessingError - Some tests do not have complete data", Toast.LENGTH_SHORT).show();
                    }
                });

                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mProgressDialog.dismiss();

            displayscoreAdvice();
            //tapresults.displayResults();
            extras.setText("Questionnaire Score: " + qscore);

            tapscore_tv.setText(ResultsMap.get(tapscore)+" ("+ String.format("%.2f", tapprecision)+"%)");
            spiralscore_tv.setText(""+ResultsMap.get(spiralscore));
            handsore_tv.setText("" + ResultsMap.get(handscore));

              // calls all display functions

            //display graphs for  results
            try {
                viewgraphs();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"ViewError - Some tests do not have complete data",Toast.LENGTH_SHORT).show();
               //finish();
            }
            showhide.performClick();

            //call next AsyncTask
            //--place functions for other Tests ex: HandTremorAsyncTask or SpiralDataAsyncTask

            // Vibrator vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // vibe.vibrate(50); // 50 is time in ms
        }
    }

    private void processSpiralData() {
        spiralscore=0;

        StaticSpiralData =sd.getAS();
        DynamicSpiralData=sd.getDAS();
        spiralDataProcessing= new SpiralDataProcessing(StaticSpiralData,DynamicSpiralData);
        final float DAH=spiralDataProcessing.getDAH();

        Log.d("DAH", "processSpiralData: "+DAH);

       if(1.45E-05 <= DAH)
          spiralscore=NORMAL;
//        else if (0.05f>=DAH&&DAH<0.075f)
//           spiralscore=SLIGHT;
//        else if (0.075f>=DAH&&DAH<0.1f)
//           spiralscore=MILD;
//       else if (0.1f>=DAH&&DAH<0.15f)
//           spiralscore=MODERATE;
        else if (DAH>=7.70E-04)
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
        tapscore=tapresults.runTempAlgorithm();
        tapprecision=tapresults.getPrecision()*100;

        Log.d("TapPrec",tapprecision+"");
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
        if(qscore>=0 && qscore<20)//normal
        {
            advice.setText("Your Questionnaire score shows almost no symptoms\n Nothing to worry about for now");
        }
       else if(qscore>=20 && qscore<=32) //mild
     {
            advice.setText("Your Questionnaire score shows mild symptoms.Repeat the tests every month to track your progress");
     }
        else if(qscore>=33 && qscore<=58) //moderate
     {
            advice.setText("Your Questionnaire score shows moderate symptoms\nconsider visiting your doctor ");
     }
        else if(qscore>=59)//severe
     {
            advice.setText("Your Questionnaire score shows severe symptoms\nconsider visiting your doctor ");
     }
        else if(qscore<0) {
            qscore = 0.0;
            advice.setText("Your Questionnaire score is incomplete");
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

    private void initialize ()
    {

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        new processDataTask().cancel(true);
    }
}
