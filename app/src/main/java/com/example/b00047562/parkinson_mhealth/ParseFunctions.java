package com.example.b00047562.parkinson_mhealth;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdu Sah on 1/28/2016.
 */

  /*TODO
        Test all functions make sure they fetch properly
        if any issues exist add them to the list here:
        1-
        2-
        3-
        ...
  */
public class ParseFunctions {
    private String result1; // add more results as necessary
    private Type type;



    public ArrayList getParseData(ParseUser user , final int listPointer, final String ... params) //fetch from Parse
    //0: class name -- 1: orderby String  -- 2: column name
    // user: pointer to ParseUser (use ParseUser.getCurrentUser())
    // you may add additional String parameters as necessary
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(params[0]);
        query.whereEqualTo("createdBy", user);
        query.orderByDescending(params[1]); //typically order by date to get latest data string
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {

                     result1 = results.get(listPointer).getString(params[2]); // column name goes here ( e.g: "ArrayList") ; listPointer = 0 means get latest row
                     type = new TypeToken<List<String[]>>() {}.getType();

                } else {
                    Log.d("ParseError", "Error fetching from Parse");
                }

            }

        });
        return new Gson().fromJson(result1,type);

    }
    public void pushParseData(ParseUser user, String ... params) //upload to Parse
    //0: class name -- 1: column name -- 2: json String
    // user: pointer to ParseUser (use ParseUser.getCurrentUser())
    //you may add additional String parameters as necessary
    {

        ParseObject acc = new ParseObject(params[0]);
        acc.put(params[1],params[2]);
        acc.put("username", user.getUsername());
        acc.put("createdBy",user);
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        acc.saveInBackground();

    }

}
