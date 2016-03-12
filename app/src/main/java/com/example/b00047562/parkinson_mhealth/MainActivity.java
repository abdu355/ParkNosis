package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button quest_btn,acc_btn,spiral_btn,usrinfo_btn,tap_btn,results;
    private Spanned email_about= Html.fromHtml("<a href=\"hello.sah802@gmail.com\">hello.sah802@gmail.com</a>");

    public static boolean q,h,sp,t;  //completion indicator for each test
    //private boolean doneall;

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


        ParseUser currentUser = ParseUser.getCurrentUser();//check if user logged in
        if (currentUser == null) {
            loadLoginView();
        }

    }
    public void loadLoginView() {
        Intent intent = new Intent(this, Login.class); //go to login activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(q && sp && t && h) //done all tests
        {
            results.setEnabled(true);
            ParseObject ob = new ParseObject("Status");
            ob.put("doneall",true);
            ob.put("createdBy",ParseUser.getCurrentUser());
            ob.saveEventually();
        }
        else
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");
            query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
            try {
                ParseObject res = query.getFirst();
                results.setEnabled(res.getBoolean("doneall"));
            } catch (ParseException e) {
                results.setEnabled(false);
               Log.d("ParseMain", e.getMessage());
            }
        }
    }

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
            alertDialog.setIcon(R.mipmap.brain48);
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
                this.startActivity(  new Intent(this,Questionnaire.class));
                break;
            case R.id.acc_btn:
                this.startActivity(  new Intent(this,Accelerometer.class));
                break;
            case R.id.spiral_btn:
                this.startActivity(  new Intent(this,Spiral.class));
                break;
            case R.id.btn_usrinfo_main:
                this.startActivity(new Intent(this,UserInfo.class));
                break;
            case R.id.btn_tap_main:
                this.startActivity(new Intent(this,SimpleTapping.class));
                break;
            case R.id.btn_resultsmain: //display results in seperate activity
                this.startActivity(new Intent(this,ResultsAnalysis.class));
                break;
        }
    }
}
