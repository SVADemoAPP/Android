package com.hck.imagemap.Dao;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dynic implements Serializable
{

    private int num;
    private String wucha;
    private String three;
    private String tof;
    private String fot;
    private String ten;
    private String time;

    public Dynic(int num, String wucha, String t, String tOF, String fOT,
            String ten, String time)
    {
        super();
        this.num = num;
        this.wucha = wucha;
        this.three = t;
        this.tof = tOF;
        this.fot = fOT;
        this.ten = ten;
        this.time = time;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public String getWucha()
    {
        return wucha;
    }

    public void setWucha(String wucha)
    {
        this.wucha = wucha;
    }

    public String getT()
    {
        return three;
    }

    public void setT(String t)
    {
        three = t;
    }

    public String getTOF()
    {
        return tof;
    }

    public void setTOF(String tOF)
    {
        tof = tOF;
    }

    public String getFOT()
    {
        return fot;
    }

    public void setFOT(String fOT)
    {
        fot = fOT;
    }

    public String getTen()
    {
        return ten;
    }

    public void setTen(String ten)
    {
        this.ten = ten;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

}
