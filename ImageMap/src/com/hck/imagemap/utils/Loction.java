package com.hck.imagemap.utils;

import com.hck.imagemap.entity.Floor;

/**
 * 判断定位的原点
 * 
 * @author yipenga
 * 
 */
public class Loction
{

    public double[] location(double x, double y, Floor currFloor)
    {
        double[] location = new double[2];
        if ("ul".equals(currFloor.getCoordinate()))// 左上为原点
        {
            location[0] = x * currFloor.getScale() / 10 + currFloor.getX()
                    * currFloor.getScale();
            location[1] = y * currFloor.getScale() / 10 + currFloor.getY()
                    * currFloor.getScale();
        } else if ("ll".equals(currFloor.getCoordinate()))// 左下
        {
            location[0] = x * currFloor.getScale() / 10 + currFloor.getX()
                    * currFloor.getScale();
            location[1] = currFloor.getImgHeight()
                    - (y * currFloor.getScale() / 10 + currFloor.getY()
                            * currFloor.getScale());
        } else if ("ur".equals(currFloor.getCoordinate()))// 右上
        {
            location[0] = currFloor.getImgWidth()
                    - (x * currFloor.getScale() / 10 + currFloor.getX()
                            * currFloor.getScale());
            location[1] = y * currFloor.getScale() / 10 + currFloor.getY()
                    * currFloor.getScale();
        } else if ("lr".equals(currFloor.getCoordinate()))// 右上
        {
            location[0] = currFloor.getImgWidth()
                    - (x * currFloor.getScale() / 10 + currFloor.getX()
                            * currFloor.getScale());
            location[1] = currFloor.getImgHeight()
                    - (y * currFloor.getScale() / 10 + currFloor.getY()
                            * currFloor.getScale());
        }
        return location;
    }
    
    public double[] locationll(double x, double y, Floor currFloor)
    {
        double[] location = new double[2];
        if ("ul".equals(currFloor.getCoordinate()))// 左上为原点
        {
            location[0] = x * currFloor.getScale() / 10 + currFloor.getX()
                    * currFloor.getScale();
            location[1] = currFloor.getImgHeight()-(y * currFloor.getScale() / 10 + currFloor.getY()
                    * currFloor.getScale());
        } else if ("ll".equals(currFloor.getCoordinate()))// 左下
        {
            location[0] = x * currFloor.getScale() / 10 + currFloor.getX()
                    * currFloor.getScale();
            location[1] =y * currFloor.getScale() / 10 + currFloor.getY()
                            * currFloor.getScale();
        } else if ("ur".equals(currFloor.getCoordinate()))// 右上
        {
            location[0] = currFloor.getImgWidth()
                    - (x * currFloor.getScale() / 10 + currFloor.getX()
                            * currFloor.getScale());
            location[1] = currFloor.getImgHeight()-(y * currFloor.getScale() / 10 + currFloor.getY()
                    * currFloor.getScale());
        } else if ("lr".equals(currFloor.getCoordinate()))// 右下
        {
            location[0] = currFloor.getImgWidth()
                    - (x * currFloor.getScale() / 10 + currFloor.getX()
                            * currFloor.getScale());
            location[1] = y * currFloor.getScale() / 10 + currFloor.getY()
                            * currFloor.getScale();
        }
        return location;
    }
    
    public static double[] convert(double x, double y, Floor currFloor)
    {
        double[] location = new double[2];
        if ("ul".equals(currFloor.getCoordinate()))// 左上为原点
        {
            location[0] = x - currFloor.getX()*10;
            location[1] = y - currFloor.getY()*10;
        } 
        else if ("ll".equals(currFloor.getCoordinate()))// 左下
        {
            location[0] = x - currFloor.getX()*10;
            location[1] = currFloor.getImgHeight()/currFloor.getScale()*10 - y - currFloor.getY()*10;
        } 
        else if ("ur".equals(currFloor.getCoordinate()))// 右上
        {
            location[0] = currFloor.getImgWidth()/currFloor.getScale()*10 - x - currFloor.getX()*10;
            location[1] = y - currFloor.getY()*10;
        } 
        else if ("lr".equals(currFloor.getCoordinate()))// 右上
        {
            location[0] = currFloor.getImgWidth()/currFloor.getScale()*10 - x - currFloor.getX()*10;
            location[1] = currFloor.getImgHeight()/currFloor.getScale()*10 - y - currFloor.getY()*10;
        }
        return location;
    }
}
