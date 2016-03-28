package com.example.b00047562.parkinson_mhealth;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.math.BigInteger;
import java.security.SecureRandom;

@SuppressWarnings("deprecation")
public class Signup extends ActionBarActivity {

    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected EditText dobEditText;
    protected RadioGroup gender, domhand;
    protected RadioButton male,female,lh,rh;
    protected Button signUpButton,consentbtn;
    private String genderselection,domhandselection;
    private SessionIdentifierGenerator gen;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        usernameEditText = (EditText)findViewById(R.id.et_name);
        passwordEditText = (EditText)findViewById(R.id.et_pass);
        emailEditText = (EditText)findViewById(R.id.et_email);
        signUpButton = (Button)findViewById(R.id.btn_sign);
        dobEditText = (EditText)findViewById(R.id.et_dob);
        gender = (RadioGroup)findViewById(R.id.gender);
        domhand=(RadioGroup)findViewById(R.id.domhand);
        male = (RadioButton)findViewById(R.id.radioButton_male);
        female = (RadioButton)findViewById(R.id.radioButton_female);
        rh=(RadioButton)findViewById(R.id.radioButton_rh);
        lh= (RadioButton)findViewById(R.id.radioButton_lh);
        consentbtn = (Button)findViewById(R.id.consent_btn);
        genderselection="Male";
        domhandselection="Right Handed";

        gen= new SessionIdentifierGenerator();
        String genid = gen.nextSessionId();

        usernameEditText.setText(genid);
        passwordEditText.setText(genid);
        emailEditText.setText(genid+"@genid.com");


        consentbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showconsent();
            }
        });


        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton_male) {
                    genderselection = male.getText().toString();
                }
                else{
                    genderselection = female.getText().toString();
                }
            }

        });
        domhand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton_rh)
                {
                    domhandselection=rh.getText().toString();
                }
                else{
                    domhandselection=lh.getText().toString();
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String dob = dobEditText.getText().toString();


                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || dob.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //setProgressBarIndeterminateVisibility(true);
                    mProgressDialog = new ProgressDialog(Signup.this);
                    // Set progressdialog title
                    mProgressDialog.setTitle("Creating User");
                    // Set progressdialog message
                    mProgressDialog.setMessage("Hang on...");
                    mProgressDialog.setIcon(R.drawable.process);
                    mProgressDialog.setIndeterminate(false);
                    // Show progressdialog
                    mProgressDialog.show();

                    ParseUser newUser = new ParseUser();//create new user data
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.put("DOB", dobEditText.getText().toString());
                    newUser.put("Gender", genderselection);
                    newUser.put("DomHand" ,domhandselection);
                    newUser.put("Complete" , false);

                    //newUser.pinInBackground();
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            //setProgressBarIndeterminateVisibility(false);

                            if (e == null) {
                                // Success!
                                mProgressDialog.dismiss();
                                Intent intent = new Intent(Signup.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                try {
                                    mProgressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                    builder.setMessage(e.getMessage())
                                            .setTitle(R.string.signup_error_title)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public void showconsent()
    {
        startActivityForResult(new Intent(this,Consent.class),1);

    }
    @Override
    protected void onActivityResult(int reqCode, int respCode, Intent i) {
        super.onActivityResult(reqCode, respCode, i);
            switch (respCode) {
                case 0:
                    signUpButton.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    signUpButton.setVisibility(View.VISIBLE);
                    break;
                default:
                    signUpButton.setVisibility(View.INVISIBLE);
                    break;
        }
    }
    public final class SessionIdentifierGenerator {
        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(30, random).toString(32);
        }
    }
}
