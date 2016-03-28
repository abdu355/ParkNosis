package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;


public class Consent extends AppCompatActivity {

    private Signup su;
    private FrameLayout pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        su = new Signup();
        pdf = (FrameLayout)findViewById(R.id.pdfframe);


        WebView mWebView=new WebView(Consent.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("https://drive.google.com/file/d/0B2zH4yXIkjArc3hhVWhEZ2dhTGM/view?usp=sharing");

        pdf.addView(mWebView);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.consent, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_pos) {

            //agree();
            Intent resp = new Intent();
            resp.putExtra("isEnabled", "enabled");
            setResult(1, resp);
            finish();
        }
        if(id == R.id.action_neg)
        {
            //disagree();
            Intent resp = new Intent();
            resp.putExtra("isEnabled", "disabled");
            setResult(0, resp);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
