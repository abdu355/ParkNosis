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
                    Log.d("ParseError", e.getMessage());
                }

            }

        });
        return new Gson().fromJson(result1,type);

    }
    public void pushParseData(ParseUser user, String ... params) //upload to Parse single row
    //0: class name -- 1: column name -- 2: json String
    // user: pointer to ParseUser (use ParseUser.getCurrentUser())
    //you may add additional String parameters as necessary
    {

        ParseObject ob = new ParseObject(params[0]);
        ob.put(params[1],params[2]);
        ob.put("username", user.getUsername());
        ob.put("createdBy", user);
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        ob.saveInBackground();

    }

    public void pushParseList(ParseUser user, int numofitems, String ... params) //upload to Parse multiple rows
    {
        List<ParseObject> objectlist = new ArrayList<ParseObject>();
        ParseObject ob = new ParseObject(params[0]);
        int i=0;
        while(i<numofitems)
        {
            try {
                ob = new ParseObject(params[0]);
                ob.put(params[1],params[2+i]); //2+i to get next paramater of different data,  params[2] and params[3] will be the data containers
                ob.put("username", ParseUser.getCurrentUser().getUsername());
                ob.put("createdBy", ParseUser.getCurrentUser());
                ob.put("position",params[4+i]);
                objectlist.add(i++, ob);
            } catch (Exception e) {
                Log.d("ParseError",e.getMessage());
            }
        }
        ob.saveAllInBackground(objectlist);

    }

}
