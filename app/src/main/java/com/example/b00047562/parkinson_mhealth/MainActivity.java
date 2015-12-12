package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button acc_btn,spiral_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        acc_btn=(Button)findViewById(R.id.acc_btn);
        spiral_btn=(Button)findViewById(R.id.spiral_btn);

        acc_btn.setOnClickListener(this);
        spiral_btn.setOnClickListener(this);

        ParseUser currentUser = ParseUser.getCurrentUser();//check if user logged in
        if (currentUser == null) {
            loadLoginView();
        }
    }
    private void loadLoginView() {
        Intent intent = new Intent(this, Login.class); //go to login activity
        startActivity(intent);
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
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_logout)
        {
                ParseUser.logOut();//update Parse current user
                loadLoginView();//load login activity
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.acc_btn:
                this.startActivity(  new Intent(this,Accelerometer.class));
                break;
            case R.id.spiral_btn:
                this.startActivity(  new Intent(this,Spiral.class));
                break;
        }
    }
}
