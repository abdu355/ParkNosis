package com.example.b00047562.parkinson_mhealth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Abdu Sah on 1/28/2016.
 */


public class ParseFunctions {
    public String result1; // add more results as necessary
    public ArrayList<String> resarr;
    private Type type;
    private Context context;
    public ParseFunctions(Context c)
    {
        context=c;
    }

    public ArrayList<Long> getParseData(ParseUser user , final int listPointer, final String ... params) //fetch from Parse
    //0: class name -- 1: orderby String  -- 2: column name
    // user: pointer to ParseUser (use ParseUser.getCurrentUser())
    // you may add additional String parameters as necessary
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(params[0]);
        query.whereEqualTo("createdBy", user);
        query.orderByDescending(params[1]); //typically order by date to get latest data string
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        try {
            List<ParseObject> results = query.find();
            result1 = results.get(listPointer).getString(params[2]);
            type = new TypeToken<ArrayList<Long>>() {}.getType();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(result1,type);
    }
    public void pushParseData(ParseUser user, String ... params) //upload to Parse single row
    //0: class name -- 1: column name -- 2: json String
    // user: pointer to ParseUser (use ParseUser.getCurrentUser())
    //you may add additional String parameters as necessary
    {
        try {

            ParseObject ob = new ParseObject(params[0]);
            ob.put(params[1],params[2]);
            ob.put("username", user.getUsername());
            ob.put("createdBy", user);
            ob.put("numoftaps",params[3]); //if not using taps just put "" [empty String]
            ob.put("hand",params[4]);
            //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
            ob.saveInBackground();
            Toast.makeText(context, "Data uploaded to Parse",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("ParseError", e.getMessage());
            Toast.makeText(context, "Data upload fail",Toast.LENGTH_SHORT).show();
        }

    }

    public void pushParseList(ParseUser user, int numofitems, String ... params) //upload to Parse multiple rows - used for tapping test currently
    {

        List<ParseObject> objectlist = new ArrayList<ParseObject>();
        ParseObject ob = new ParseObject(params[0]);
        int i=0;
        while(i<numofitems)//num of items can only be 2 at the moment - create other custom function for your needs
        {
            try {
                ob = new ParseObject(params[0]);
                ob.put(params[1],params[2+i]); //2+i to get next paramater of different data,  params[2] and params[3] will be the data containers
                ob.put("username", ParseUser.getCurrentUser().getUsername());
                ob.put("createdBy", ParseUser.getCurrentUser());
                ob.put("position",params[numofitems+(i+2)]); //get remaining parameters
                ob.put("numoftaps",params[numofitems+(i+4)]);
                objectlist.add(i++, ob);
                Toast.makeText(context, "Data uploaded to Parse",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d("ParseError",e.getMessage());
                Toast.makeText(context, "Data upload fail",Toast.LENGTH_SHORT).show();
            }
        }
        ob.saveAllInBackground(objectlist);

    }

    public ArrayList<AccelData> getParseDataAccel(ParseUser user , final int listPointer, final String ... params) //fetch from Parse
    //0: class name -- 1: orderby String  -- 2: column name
    // user: pointer to ParseUser (use ParseUser.getCurrentUser())
    // you may add additional String parameters as necessary
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(params[0]);
        query.whereEqualTo("createdBy", user);
        query.orderByDescending(params[1]); //typically order by date to get latest data string
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        try {
            List<ParseObject> results = query.find();
            result1 = results.get(listPointer).getString(params[2]);
            type = new TypeToken<ArrayList<AccelData>>() {}.getType();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(result1,type);
    }

    public String getParseSingleColData(ParseUser user , final int listPointer, final String ... params)
    {
        String hand="";

        ParseQuery<ParseObject> query = ParseQuery.getQuery(params[0]);
        query.whereEqualTo("createdBy", user);
        query.orderByDescending(params[1]); //typically order by date to get latest data string
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        try {
            List<ParseObject> results = query.find();
            hand = results.get(listPointer).getString(params[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hand;
    }
    public ArrayList<String> getParseDataTappingCount(ParseUser user ,  final String ... params)
    {
        resarr = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(params[0]);
        query.whereEqualTo("createdBy", user);
        query.orderByDescending(params[1]); //typically order by date to get latest data string
        query.setLimit(5);
        //add additional query paramters here if needed ( make sure to also include the params[index] for it ) ...
        try {
            List<ParseObject> results = query.find();
           for(int i=0;i<5;i++)
           {
               resarr.add(i, results.get(i).getString(params[2]));
           }
           // type = new TypeToken<Integer>(){}.getType();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resarr;
    }


}
