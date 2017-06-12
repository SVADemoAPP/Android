package com.hck.imagemap.entity;

public class MessageMng
{
    private String place;
    private double xSpot;
    private double ySpot;

    public double getRangeSpot()
    {
        return this.rangeSpot;
    }

    public void setRangeSpot(double rangeSpot)
    {
        this.rangeSpot = rangeSpot;
    }

    private String floor;
    private double rangeSpot;

    public String getPlace()
    {
        return this.place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public double getxSpot()
    {
        return this.xSpot;
    }

    public void setxSpot(double xSpot)
    {
        this.xSpot = xSpot;
    }

    public double getySpot()
    {
        return this.ySpot;
    }

    public void setySpot(double ySpot)
    {
        this.ySpot = ySpot;
    }

    public String getFloor()
    {
        return this.floor;
    }

    public void setFloor(String floor)
    {
        this.floor = floor;
    }

    private String message;

    public String getMessage()
    {
        return this.message;
    }

    private String isEnable;

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getIsEnable()
    {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable)
    {
        this.isEnable = isEnable;
    }
}
