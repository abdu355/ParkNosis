package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button quest_btn,acc_btn,spiral_btn,usrinfo_btn,tap_btn,results;
    private Spanned email_about= Html.fromHtml("<a href=\"hello.sah802@gmail.com\">hello.sah802@gmail.com</a>");
    ParseUser currentUser;
    public static boolean q,h,sp,t;  //completion indicator for each test
    //private boolean doneall;
    private RelativeLayout pdback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quest_btn=(Button)findViewById(R.id.quest_btn);
        acc_btn=(Button)findViewById(R.id.acc_btn);
        spiral_btn=(Button)findViewById(R.id.spiral_btn);
        usrinfo_btn=(Button)findViewById(R.id.btn_usrinfo_main);
        tap_btn=(Button)findViewById(R.id.btn_tap_main);
        results=(Button)findViewById(R.id.btn_resultsmain); // display results activity button

        quest_btn.setOnClickListener(this);
        acc_btn.setOnClickListener(this);
        spiral_btn.setOnClickListener(this);
        usrinfo_btn.setOnClickListener(this);
        tap_btn.setOnClickListener(this);
        results.setOnClickListener(this);

        pdback=(RelativeLayout)findViewById(R.id.pdback);
        Bitmap back = BitmapFactory.decodeResource(getResources(), R.drawable.rsz_1pdback2);
        Bitmap bm = NativeStackBlur.process(back, 250);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bm);
        //pdback.setBackground(ob);

        //try {
            currentUser = ParseUser.getCurrentUser();//check if user logged in
        //} catch (ParseException e) {
        //    e.printStackTrace();
        //}
        if (currentUser == null) {
            loadLoginView();
        }



    }
    public void loadLoginView() {
        Intent intent = new Intent(this, Login.class); //go to login activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*TODO
     user completion status
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (q && sp && t && h) {
            currentUser.put("Complete", true);
        }
        try {
            currentUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }catch  (NullPointerException e2){

            e2.printStackTrace();
        }
        try {
            results.setEnabled(currentUser.getBoolean("Complete"));
        }catch  (NullPointerException e2){

            e2.printStackTrace();
        }

    }//

//                if (q && sp && t && h) //done all tests
//                {
//    //            results.setEnabled(true);
//    //            ParseObject ob = new ParseObject("Status");
//    //            ob.put("doneall",true);
//    //            ob.put("createdBy",ParseUser.getCurrentUser());
//    //            ob.saveEventually();
//                    //ParseUser usr = new ParseUser();
//                    usr.put("Complete", true);
//                } else {
//    //            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");
//    //            query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
//    //            try {
//    //                ParseObject res = query.getFirst();
//    //                results.setEnabled(res.getBoolean("doneall"));
//    //            } catch (ParseException e) {
//    //                results.setEnabled(false);
//    //               Log.d("ParseMain", e.getMessage());
//    //            }
//                    results.setEnabled(usr.getBoolean("Complete"));
//                }

    @Override
    protected void onPause() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putBoolean("doneall",doneall);
       //editor.apply();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if(id==R.id.action_logout)
        {
                ParseUser.logOut();//update Parse current user
                loadLoginView();//load login activity
        }
        if(id==R.id.action_about)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("About");
            alertDialog.setMessage("Parkinson's Disease research & development application.\n\nAmerican University of Sharjah\n\nSenior Design Project created By:\nAbdulwahab Sahyoun\nKarim Chehab\nOsama Al Madani\n\nAdvisors:\nDr.Fadi Aloul\nDr.Assim Sagahyroon\n\nFor more details contact:\n" + email_about);
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.quest_btn:
                Intent questIntent = new Intent(this, Questionnaire.class);
                questIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(questIntent);
                break;
            case R.id.acc_btn:
                Intent accIntent = new Intent(this, Accelerometer.class);
                accIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(accIntent);
                break;
            case R.id.spiral_btn:
                Intent spiralIntent = new Intent(this, Spiral.class);
                spiralIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(spiralIntent);
                break;
            case R.id.btn_usrinfo_main:
                Intent userIntent = new Intent(this, UserInfo.class);
                userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(userIntent);
                break;
            case R.id.btn_tap_main:
                Intent tapIntent = new Intent(this, SimpleTapping.class);
                tapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(tapIntent);
                break;
            case R.id.btn_resultsmain: //display results in seperate activity
                Intent resultsIntent = new Intent(this, ResultsAnalysis.class);
                resultsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(resultsIntent);
                break;
        }
    }
}
