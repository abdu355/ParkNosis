package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.gson.Gson;
import com.parse.ParseUser;

/**
 * Created by Os on 3/6/2016.
 */

/**
 * To be tested
 * */

public class DynamicSpiralFragment extends Fragment implements View.OnClickListener
{
    public static Button redrawOpen,btnClr,btnSubmit;
    private CanvasSpiral customCanvas;
    private ParseFunctions customParse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_spiral, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        customCanvas=(CanvasSpiral)view.findViewById(R.id.spiral_canvas);
        customParse = new ParseFunctions(getActivity().getApplicationContext());
        btnClr= (Button)  view.findViewById(R.id.btn_clear);

        btnSubmit= (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnClr.setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {public void run(){} }, 2000);

        customCanvas.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_clear:
                //Toast.makeText(this,"clear",Toast.LENGTH_SHORT).show();
                customCanvas.cleardisp();
                break;
//            case R.id.btn_redraw:
//                this.startActivity(new Intent(this, SpiralRedraw.class));
//                break;
            case R.id.btnSubmit:

                String json = new Gson().toJson(CanvasSpiral.spiralData);
                customParse.pushParseData(ParseUser.getCurrentUser(), "SpiralData", "ArrayList", json, "", "Dynamic");
                MainActivity.sp=true; //test finished
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                break;


        }
    }


    //
}
