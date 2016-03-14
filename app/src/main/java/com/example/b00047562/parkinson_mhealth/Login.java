package com.example.b00047562.parkinson_mhealth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@SuppressWarnings("deprecation")
public class Login extends ActionBarActivity {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected Button signUpButton;
    private Spanned email_about= Html.fromHtml("<a href=\"hello.sah802@gmail.com\">hello.sah802@gmail.com</a>");
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//progress bar circle
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signUpButton = (Button)findViewById(R.id.btn_signup);
        emailEditText = (EditText)findViewById(R.id.et_email);
        passwordEditText = (EditText)findViewById(R.id.et_pass);
        loginButton = (Button)findViewById(R.id.btn_login);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                email = email.trim();
                password = password.trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //setProgressBarIndeterminateVisibility(true);//enable
                    mProgressDialog = new ProgressDialog(Login.this);
                    // Set progressdialog title
                    mProgressDialog.setTitle("Authenticating");
                    // Set progressdialog message
                    mProgressDialog.setMessage("Hang on...");
                    mProgressDialog.setIcon(R.drawable.process);
                    mProgressDialog.setIndeterminate(false);
                    // Show progressdialog
                    mProgressDialog.show();
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", email);
                    final String finalPassword = password;
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            if (object != null) {

                                ParseUser.logInInBackground(object.getString("username"), finalPassword, new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (e == null) {
                                            // Success
                                            mProgressDialog.dismiss();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            // Fail
                                            mProgressDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                            builder.setMessage(e.getMessage())
                                                    .setTitle(R.string.login_error_title)
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }
                                });
                            } else {
                                //do nothing
                            }


                        }
                    });


                }
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
