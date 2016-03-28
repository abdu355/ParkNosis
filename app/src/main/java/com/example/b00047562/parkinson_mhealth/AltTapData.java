package com.example.b00047562.parkinson_mhealth;

/**
 * Created by Administrator on 3/25/2016.
 */
public class AltTapData {
    private long timestamp;
    private float x;
    private float y;

    public AltTapData(long timestamp, float x, float y) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;

    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y;
    }
}
