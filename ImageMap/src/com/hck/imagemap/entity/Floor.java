package com.hck.imagemap.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Floor implements Serializable
{
    /**
     * {"scale":"26","floorNo":1,"floor":"1F","coordinate":"ul","path":
     * "U6食堂_1F.jpg"
     * ,"imgWidth":0,"place":"U6食堂","y":"2.12","imgHeight":0,"x":"0.92"}
     */
    private String place;
    private int floorNo;
    private String floor;
    private String path;
    private double angle;
    private int placeId;
    private String svg;
    private long updateTime;
    private String route;
    private String pathFile;
    
    public String getNavPath()
    {
        return pathFile;
    }

    public void setNavPath(String pathFile)
    {
        this.pathFile = pathFile;
    }
    
    public String getRoute()
    {
        return route;
    }

    public void setRoute(String route)
    {
        this.route = route;
    }

    public long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getSvg()
    {
        return svg;
    }

    public void setSvg(String svg)
    {
        this.svg = svg;
    }

    public int getPlaceId()
    {
        return placeId;
    }

    public void setPlaceId(int placeId)
    {
        this.placeId = placeId;
    }

    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    private double scale;
    private double x;
    private double y;
    private String coordinate;
    private int imgWidth;
    private int imgHeight;

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public int getFloorNo()
    {
        return floorNo;
    }

    public void setFloorNo(int floorNo)
    {
        this.floorNo = floorNo;
    }

    public String getFloor()
    {
        return floor;
    }

    public void setFloor(String floor)
    {
        this.floor = floor;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public double getScale()
    {
        return scale;
    }

    public void setScale(double scale)
    {
        this.scale = scale;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public String getCoordinate()
    {
        return coordinate;
    }

    public void setCoordinate(String coordinate)
    {
        this.coordinate = coordinate;
    }

    public int getImgWidth()
    {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth)
    {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight()
    {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight)
    {
        this.imgHeight = imgHeight;
    }

}
