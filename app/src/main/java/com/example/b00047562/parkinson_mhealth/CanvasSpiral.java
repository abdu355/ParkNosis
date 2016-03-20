package com.example.b00047562.parkinson_mhealth;

/**
 * Created by Os on 2/22/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class CanvasSpiral extends View {

    private static final String TAG = "Showing size";
    String TAG2="Listing x&y";
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
  //  private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;
    private Paint mPaint,mPaint2,mPaint3;
    int maxX,maxY;
    float []OriginalSpiralPoints = new float[168];
    float []pointsArray = new float[2160];

    public static ArrayList<SpiralData> getSpiralData() {
        return spiralData;
    }

    //User inputed spiral
    public static ArrayList<SpiralData> spiralData;

    public CanvasSpiral(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10f);
        mPaint.setAlpha(0x80);

        mPaint2= new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.GREEN);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeWidth(10f);
        mPaint3= new Paint();
        mPaint3.setAntiAlias(true);
        mPaint3.setColor(Color.parseColor("#f06292"));
        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setStrokeJoin(Paint.Join.ROUND);
        mPaint3.setStrokeWidth(10f);

        Display mdisp =((Activity) context).getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;

        setupDrawing();
    }


    private void setupDrawing(){
//get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();

      //  drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setColor(Color.GREEN);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        //start new list
        spiralData = new ArrayList<>();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

    }


    public float[] getOriginalSpiralPoints() {
        return OriginalSpiralPoints;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        canvas.setDensity(metrics.densityDpi);
        float angle,x ,y ;


        int j=0;
        for (int i=0; i< 1080; i++,j=j+2) {
            angle = 0.50502f * i;

            x = maxX / 2 -10+ (5 + angle) * (float) Math.cos(angle);
            y = (maxY / 2)-250 + (5 + angle) * (float) Math.sin(angle);


            pointsArray[j]=x;
            pointsArray[j+1]=y;
        }
        int k=0;
        for (int m=0;m<2160;m++,k=k+2)
        {

            OriginalSpiralPoints[k]=pointsArray[m];
            OriginalSpiralPoints[k+1]=pointsArray[m+1];
            m+=25;

        }
        for (int i=0;i<166;i=i+2)
        {
            canvas.drawLine(OriginalSpiralPoints[i], OriginalSpiralPoints[i + 1], OriginalSpiralPoints[i + 2], OriginalSpiralPoints[i + 3], mPaint);

           // Log.d(TAG2, "onDraw: "+(i/2)+ " x&y: " + OriginalSpiralPoints[i] + " " + OriginalSpiralPoints[i + 1]);
        }

        canvas.drawPoints(OriginalSpiralPoints,mPaint3);

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        long curTime = System.currentTimeMillis();
        float touchX = event.getX();
        float touchY = event.getY();

        //store spiral draw points
        SpiralData data = new SpiralData(curTime,touchX,touchY);
        spiralData.add(data);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
//                Spiral.alert.setTextColor(Color.parseColor("#00e676"));
//                Spiral.alert.setText("OK!");
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
//                Spiral.alert.setTextColor(Color.RED);
//                Spiral.alert.setText("KEEP TOUCHING!");
                drawPath.reset();
                Spiral.redrawOpen.setEnabled(true); // to prevent crashing or redrawing when no data is available - this will updated soon
                Spiral.btnSubmit.setEnabled(true);
             //   Log.d(TAG, "onTouchEvent: " + spiralData.size());
                break;
            default:
                return false;
        }
        invalidate();

        return true;
    }
    public void cleardisp()
    {
        spiralData.clear();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        invalidate();
    }
}
