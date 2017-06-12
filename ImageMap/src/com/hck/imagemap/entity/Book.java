package com.hck.imagemap.entity;
public class Book {  
    private String x = null;  
    private String y = null;  
    private String pointX = null;
    private String pointY = null;
    
      
    
  
    public String getPointX()
    {
        return pointX;
    }


    public void setPointX(String pointX)
    {
        this.pointX = pointX;
    }


    public String getPointY()
    {
        return pointY;
    }


    public void setPointY(String pointY)
    {
        this.pointY = pointY;
    }


    public String getX()
    {
        return x;
    }


    public void setX(String string)
    {
        this.x = string;
    }


    public String getY()
    {
        return y;
    }


    public void setY(String string)
    {
        this.y = string;
    }


    @Override  
    public String toString() {  
        return "x:" + x + ", y:" + y ;  
    }  
}  