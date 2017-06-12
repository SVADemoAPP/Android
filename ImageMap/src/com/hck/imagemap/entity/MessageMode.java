package com.hck.imagemap.entity;

import android.graphics.PointF;

public class MessageMode
{

    private PointF pointF;
    private double radius;
    private double distance;
    private int ID;

    
    public int getID()
    {
        return ID;
    }

    public void setID(int iD)
    {
        ID = iD;
    }

    public PointF getPointF()
    {
        return pointF;
    }

    public void setPointF(PointF pointF)
    {
        this.pointF = pointF;
    }

    public double getRadius()
    {
        return radius;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

}
