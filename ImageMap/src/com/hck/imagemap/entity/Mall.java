package com.hck.imagemap.entity;

import java.util.ArrayList;
import java.util.List;

public class Mall
{
    private String name;
    private List<Floor> list = new ArrayList<Floor>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Floor> getList()
    {
        return list;
    }

    public void setList(List<Floor> list)
    {
        this.list = list;
    }

}
