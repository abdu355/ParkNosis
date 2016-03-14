package com.example.b00047562.parkinson_mhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseUser;

/**
 * Created by OS on 3/7/2016.
 */
public class StaticSpiralFragment extends Fragment implements View.OnClickListener{
    public static Button redrawOpen,btnClr,btnSubmit;
    private CanvasSpiral customCanvas;
    public static TextView alert;
    private ParseFunctions customParse;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        customCanvas=(CanvasSpiral)view.findViewById(R.id.spiral_canvas);
        redrawOpen =(Button)view.findViewById(R.id.btn_redraw);
        btnClr= (Button) view.findViewById(R.id.btn_clear);
        //customParse = new ParseFunctions(getActivity().getApplicationContext());
        customParse = new ParseFunctions();
        btnSubmit= (Button) view.findViewById(R.id.btnSubmit);

        btnClr.setOnClickListener(this);
        redrawOpen.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_clear:
                //Toast.makeText(this,"clear",Toast.LENGTH_SHORT).show();
                customCanvas.cleardisp();
                break;
            case R.id.btn_redraw:
//                this.startActivity(new Intent(this, SpiralRedraw.class));
//                break;
            case R.id.btnSubmit:

                String json = new Gson().toJson(CanvasSpiral.spiralData);
                customParse.pushParseData(ParseUser.getCurrentUser(), "SpiralData", "ArrayList", json, "", "");
          //      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_container, new DynamicSpiralFragment());
//                ft.commit();
                break;


        }
    }
}
