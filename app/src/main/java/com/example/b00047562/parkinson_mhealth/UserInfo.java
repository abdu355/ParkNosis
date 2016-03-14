package com.example.b00047562.parkinson_mhealth;

import android.os.Bundle;
import android.os.ParcelFormatException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.ParseException;

public class UserInfo extends AppCompatActivity {

    private TextView name,email,gender,dob,domhand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name=(TextView)findViewById(R.id.tv_nameusrinfo);
        email=(TextView)findViewById(R.id.tv_emailusrinfo);
        dob=(TextView)findViewById(R.id.tv_dobusrinfo);
        gender=(TextView)findViewById(R.id.tv_genusrinfo);
        domhand=(TextView)findViewById(R.id.tv_domhand);

        ParseUser currentUser = null;
        try {
            currentUser = ParseUser.getCurrentUser().fetch();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        if (currentUser != null) {
            try {
                //Toast.makeText(this,"Updated Info",Toast.LENGTH_SHORT).show();
                name.setText(currentUser.getUsername());
                email.setText(currentUser.getEmail());
                dob.setText(currentUser.getString("DOB"));
                gender.setText(currentUser.getString("Gender"));
                domhand.setText(currentUser.getString("DomHand"));
            } catch (ParcelFormatException e) {
                Log.d("ParseError", e.getMessage());
            }
        } else {


        }

    }

}
