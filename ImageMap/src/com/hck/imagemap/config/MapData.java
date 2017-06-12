package com.hck.imagemap.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.core.DottedLine;
import net.yoojia.imagemap.core.LineShape;
import net.yoojia.imagemap.core.StartEndShape;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.hck.imagemap.entity.Book;
import com.hck.imagemap.utils.Dijkstra;
import com.hck.imagemap.utils.PullBookParser;
//import net.yoojia.imagemap.core.DottedLine;
//import net.yoojia.imagemap.core.DottedLine;

public class MapData
{
    private ArrayList<PointF> points = new ArrayList<PointF>();
    private ArrayList<Integer> pathPoit = new ArrayList<Integer>();
    private ArrayList<PointF> startEndPoints = new ArrayList<PointF>();
    private int[][] mapData;
    private int touchId = 0;
    private ImageMap1 map; // lib库里面自定义试图对象
    private int start, end;
    private Context context;
    private String routePath;

    public MapData(Context context, ImageMap1 map, String routePath)
    {
        this.context = context;
        this.map = map;
        this.routePath = routePath;
        startEndPoints.clear();
        startEndPoints.add(new PointF(0, 0));
        startEndPoints.add(new PointF(0, 0));
        initData();
    }

    /**
     * 输出路径
     */
    public void findPathAndDraw()
    {
        pathPoit.clear();
        ArrayList<String> dijkstra = Dijkstra.dijkstra(mapData, start, end);
        String path = dijkstra.get(0);
        String[] zz = path.split(">");
        for (int q = 0; q < zz.length; q++)
        {
            pathPoit.add(Integer.parseInt(zz[q]));
        }
        drawLine();
    }

    /**
     * 清除路径及起始点
     */
    public void removePathPoint()
    {
        pathPoit.clear();
        map.removeShape("Lin");
        map.removeShape("end");
        map.removeShape("start");
        map.removeShape("startEnd1");
        map.removeShape("startEnd2");
    }

    /**
     * 绘制路径
     */

    public void drawLine()
    {
        map.removeShape("Lin");
        Path path = new Path();
        LineShape ls = null;
        path.moveTo(points.get(pathPoit.get(0)).x,
                points.get(pathPoit.get(0)).y);
        ls = new LineShape("Lin", Color.parseColor("#8bc34a"));
        for (int i = 1; i < pathPoit.size(); i++)
        {
            path.lineTo(points.get(pathPoit.get(i)).x,
                    points.get(pathPoit.get(i)).y);
        }
        ls.setPath(path);
        map.addShape(ls, false);
        drawNoRoad();

    }

    /**
     * 寻找最近点
     * 
     * @param pointf
     */
    public void findNearstPoint(PointF pointf, int length)
    {
        double before = 0;
        double now = 9999;
        boolean find = false;
        for (int i = 0; i < points.size(); i++)
        {
            if (Math.abs(pointf.x - points.get(i).x) < length
                    && Math.abs(pointf.y - points.get(i).y) < length)
            {
                before = Math.sqrt((pointf.x - points.get(i).x)
                        * (pointf.x - points.get(i).x)
                        + (pointf.y - points.get(i).y)
                        * (pointf.y - points.get(i).y));
                if (now > before)
                {
                    now = before;
                    touchId = i;
                    find = true;
                }
            }
        }
        if (!find)
        {
            findNearstPoint(pointf, length + 60);
        }
    }

    /**
     * 设置起点
     */
    public void setStart(PointF pointf, View viewStart)
    {
        findNearstPoint(pointf, 40);
        start = touchId;
        drawStart(pointf, viewStart);
        startEndPoints.get(0).x = pointf.x;
        startEndPoints.get(0).y = pointf.y;
    }

    /**
     * 设置终点
     */
    public void setEnd(PointF pointf, View viewEnd)
    {
        findNearstPoint(pointf, 40);
        end = touchId;
        drawEnd(pointf, viewEnd);
        startEndPoints.get(1).x = pointf.x;
        startEndPoints.get(1).y = pointf.y;
    }

    public boolean hasRouteData()
    {
        if (points.size() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 初始化地图数据
     */
    public void initData()
    {
        points.clear();
        try
        {
            InputStream inputStream = new FileInputStream(
                    Environment.getExternalStorageDirectory() + "/" + routePath);
//             InputStream inputStream =
//             context.getAssets().open("shenzheng_2d_path.xml");
            PullBookParser bookParser = new PullBookParser();
            List<Book> personsList = bookParser.parse(inputStream);
            // 获取特定点数量
            int lineSize = bookParser.getLineSize();
            mapData = new int[lineSize][lineSize];
            for (int i = 0; i < lineSize; i++)
            {
                for (int j = 0; j < lineSize; j++)
                {
                    if (i == j)
                    {
                        mapData[i][j] = 0;
                    } else
                    {
                        mapData[i][j] = -1;
                    }
                }
            }
            for (Book person2 : personsList)
            {
                if (person2.getX() != null)
                {
                    String[] ds = person2.getX().split(",");
                    for (int i = 0; i < ds.length; i++)
                    {
                        mapData[Integer.valueOf(person2.getY())][Integer
                                .valueOf(ds[i])] = 1;
                    }
                } else
                {
                    points.add(new PointF(
                            Float.parseFloat(person2.getPointX()), Float
                                    .parseFloat(person2.getPointY())));
                }

            }
        } catch (Exception e)
        {
            Log.e("pathlook", "" + e);
        }
    }

    /**
     * 绘制起点
     */
    public void drawStart(PointF pointf, View viewStart)
    {
        StartEndShape black = new StartEndShape("start", Color.RED, viewStart,
                context);
        black.setValues(String.format("%.5f:%.5f:50", pointf.x, pointf.y));
        map.addShape(black, false);
    }

    /**
     * 绘制终点
     * 
     * @param pointf
     * @param viewEnd
     */
    public void drawEnd(PointF pointf, View viewEnd)
    {
        StartEndShape black = new StartEndShape("end", Color.RED, viewEnd,
                context);
        black.setValues(String.format("%.5f:%.5f:50", pointf.x, pointf.y));
        map.addShape(black, false);
    }

    private void drawNoRoad()
    {
        DottedLine ls = new DottedLine("startEnd1", Color.GRAY);
        ls.setValues(String.format("%.5f:%.5f:%.5f:%.5f",
                startEndPoints.get(0).x, startEndPoints.get(0).y,
                points.get(pathPoit.get(0)).x, points.get(pathPoit.get(0)).y));
        map.addShape(ls, false);

        DottedLine ls1 = new DottedLine("startEnd2", Color.GRAY);
        ls1.setValues(String.format("%.5f:%.5f:%.5f:%.5f",
                startEndPoints.get(1).x, startEndPoints.get(1).y,
                points.get(pathPoit.get(pathPoit.size() - 1)).x,
                points.get(pathPoit.get(pathPoit.size() - 1)).y));
        map.addShape(ls1, false);
    }

}
