package com.example.b00047562.parkinson_mhealth;

/**
 * Created by Kareem on 12/3/2015.
 */
public class AccelData {
    private long timestamp;
    private float x;
    private float y;
    private float z;

    public AccelData(long timestamp, float x, float y, float z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
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
    public float getZ() {
        return z;
    }
    public void setZ(float z) {
        this.z = z;
    }
    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }
}