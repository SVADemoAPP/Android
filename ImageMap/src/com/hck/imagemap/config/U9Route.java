package com.hck.imagemap.config;
import java.util.ArrayList;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.core.DottedLine;
import net.yoojia.imagemap.core.LineShape;
import net.yoojia.imagemap.core.StartEndShape;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

import com.hck.imagemap.utils.Dijkstra;
//import net.yoojia.imagemap.core.DottedLine;
//import net.yoojia.imagemap.core.DottedLine;

public class U9Route
{
    private ArrayList<PointF> points = new ArrayList<PointF>();
    private ArrayList<Integer> pathPoit = new ArrayList<Integer>();
    private ArrayList<PointF> startEndPoints = new ArrayList<PointF>();
    private int [][]mapData; 
    private int touchId = 0;
    private int N=-1;
    private ImageMap1 map; // lib库里面自定义试图对象
    private int start, end;
    private Context context;
    
    public U9Route(Context context,ImageMap1 map){
        this.context = context;
        this.map = map;
        startEndPoints.clear();
        startEndPoints.add(new PointF(0, 0));
        startEndPoints.add(new PointF(0, 0));
        initData();
    }
    
    /**
     * 输出路径  
     */
    public void findPathAndDraw(){
        pathPoit.clear();
            ArrayList<String> dijkstra = Dijkstra
                    .dijkstra(mapData, start, end);
            String path = dijkstra.get(0);
            String[] zz = path.split(">");
            for (int q = 0; q < zz.length; q++)
            {
                pathPoit.add(Integer
                        .parseInt(zz[q]));
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
     * 清楚路径
     */
    public void removremovePath(){
        pathPoit.clear();
        map.removeShape("Lin");
        map.removeShape("startEnd1");
        map.removeShape("startEnd2");
    }
    /**
     * 绘制路径
     */

    public void drawLine()
    {
         map.removeShape("Lin");
         Path path=new Path();
         LineShape ls = null;
         path.moveTo(points.get(pathPoit.get(0)).x, points.get(pathPoit.get(0)).y);
         ls = new LineShape("Lin", Color.BLACK);
         for (int i = 1; i < (pathPoit.size()); i++)
         {
             path.lineTo(points.get(pathPoit.get(i)).x, points.get(pathPoit.get(i)).y);
         }
         ls.setPath(path);
         map.addShape(ls, false);
        drawNoRoad();
        
    }
    /**
     * 寻找最近点
     * @param pointf
     */
    public void findNearstPoint(PointF pointf,int length){
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
            findNearstPoint(pointf,length + 60);
        }
    }
    /**
     * 设置起点
     */
    public void setStart(PointF pointf,View viewStart){
        findNearstPoint(pointf,40);
        start=touchId;
        drawStart(pointf, viewStart);
        startEndPoints.get(0).x = pointf.x;
        startEndPoints.get(0).y = pointf.y;
    }
    
    /**
     * 设置终点
     */
    public void setEnd(PointF pointf,View viewEnd){
        findNearstPoint(pointf,40);
        end=touchId;
        drawEnd(pointf, viewEnd);
        startEndPoints.get(1).x = pointf.x;
        startEndPoints.get(1).y = pointf.y;
    }
    /**
     * 初始化地图数据
     */
    public void initData(){
        points.clear();
        points.add(new PointF(180, 90));// 0
        points.add(new PointF(260, 90));// 1
        points.add(new PointF(260, 180));// 2
        points.add(new PointF(360, 90));// 3
        points.add(new PointF(360, 180));// 4
        points.add(new PointF(480, 90));// 5
        points.add(new PointF(480, 180));// 06
        points.add(new PointF(560, 90));// 07
        points.add(new PointF(560, 180));// 08
        points.add(new PointF(680, 90));// 09
        points.add(new PointF(680, 180));// 10
        points.add(new PointF(780, 90));// 11
        points.add(new PointF(780, 180));// 12
        points.add(new PointF(860, 90));// 13
        points.add(new PointF(860, 340));// 14
        points.add(new PointF(780, 340));// 15
        points.add(new PointF(780, 260));// 16
        points.add(new PointF(680, 340));// 17
        points.add(new PointF(680, 260));// 18
        points.add(new PointF(560, 340));// 19
        points.add(new PointF(560, 260));// 20
        points.add(new PointF(480, 340));// 21
        points.add(new PointF(480, 260));// 22
        points.add(new PointF(360, 340));// 23
        points.add(new PointF(360, 260));// 24
        points.add(new PointF(260, 340));// 25
        points.add(new PointF(260, 260));// 26
        points.add(new PointF(180, 340));// 27
        
        
        
        mapData = new int[][]{
                // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 
                 { 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1 },// 0
                 { 1, 0, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N},// 1
                 { N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 2
                 { N, 1, N, 0, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 3
                 { N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N},// 4
                 { N, N, N, 1, N, 0, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 5
                 { N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 6
                 { N, N, N, N, N, 1, N, 0, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 7
                 { N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N},// 8
                 { N, N, N, N, N, N, N, 1, N, 0, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 9
                 { N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N},// 10
                 { N, N, N, N, N, N, N, N, N, 1, N, 0, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 11
                 { N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 12
                 { N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N },// 13
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N},// 14
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, 1, 1, N, N, N, N, N, N, N, N, N, N},// 15
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N},// 16
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, 1, 1, N, N, N, N, N, N, N, N },// 17
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N},// 18
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, 1, 1, N, N, N, N, N, N },// 19
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N},// 20
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, 1, 1, N, N, N, N},// 21
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N},// 22
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, 1, 2, N, N },// 23
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N },// 24
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, 1, 1 },// 25
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N },// 26
                 { 2, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0 },// 27
                 };  
        
    }
   /**
    * 绘制起点
    */
    public  void drawStart(PointF pointf,View viewStart)
    {
        StartEndShape black = new StartEndShape("start", Color.RED, viewStart,
                context);
        black.setValues(String.format("%.5f,%.5f,50", pointf.x,
                pointf.y));
        map.addShape(black, false);
    }
/**
 * 绘制终点
 * @param pointf
 * @param viewEnd
 */
    public void drawEnd(PointF pointf,View viewEnd)
    {
        StartEndShape black = new StartEndShape("end", Color.RED, viewEnd,
                context);
        black.setValues(String.format("%.5f,%.5f,50", pointf.x,
                pointf.y));
        map.addShape(black, false);
    }

    private void drawNoRoad()
    {
       /* if(!drawFirst){
        DottedLine ls = new DottedLine("startEnd1", Color.GRAY);
        ls.setValues(String.format("%.5f,%.5f,%.5f,%.5f",
                startEndPoints.get(0).x, startEndPoints.get(0).y,
                points.get(pathPoit.get(0)).x, points.get(pathPoit.get(0)).y));
        map.addShape(ls, false);

        DottedLine ls1 = new DottedLine("startEnd2", Color.GRAY);
        ls1.setValues(String.format("%.5f,%.5f,%.5f,%.5f",
                startEndPoints.get(1).x, startEndPoints.get(1).y,
                points.get(pathPoit.get(pathPoit.size() - 1)).x,
                points.get(pathPoit.get(pathPoit.size() - 1)).y));
        map.addShape(ls1, false);
    
    }else{*/
        DottedLine ls = new DottedLine("startEnd1", Color.GRAY);
        ls.setValues(String.format("%.5f,%.5f,%.5f,%.5f",
                startEndPoints.get(0).x, startEndPoints.get(0).y,
                points.get(pathPoit.get(0)).x, points.get(pathPoit.get(0)).y));
        map.addShape(ls, false);

        DottedLine ls1 = new DottedLine("startEnd2", Color.GRAY);
        ls1.setValues(String.format("%.5f,%.5f,%.5f,%.5f",
                startEndPoints.get(1).x, startEndPoints.get(1).y,
                points.get(pathPoit.get(pathPoit.size() - 1)).x,
                points.get(pathPoit.get(pathPoit.size() - 1)).y));
        map.addShape(ls1, false);
   // }

    }
    
}
