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
import android.widget.Toast;

import com.hck.imagemap.R;
import com.hck.imagemap.utils.Dijkstra;
//import net.yoojia.imagemap.core.DottedLine;
//import net.yoojia.imagemap.core.DottedLine;

public class InitData
{
    private ArrayList<PointF> points = new ArrayList<PointF>();
    private ArrayList<Integer> pathPoit = new ArrayList<Integer>();
    private ArrayList<PointF> startEndPoints = new ArrayList<PointF>();
    private int [][]BZ1;
    private int [][]BZ2; 
    private int touchId = 0;
    private int N=-1;
    private ImageMap1 map; // lib库里面自定义试图对象
    private int start, end;
    private Context context;
    
    public InitData(Context context,ImageMap1 map){
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
    	if((startEndPoints.get(0).x>Constant.LEFT_X && startEndPoints.get(0).x<Constant.RIGHT_X
    			&& startEndPoints.get(0).y>Constant.TOP_Y && startEndPoints.get(0).y<Constant.BOTTOM_Y)
    			||(startEndPoints.get(1).x>Constant.LEFT_X && startEndPoints.get(1).x<Constant.RIGHT_X
    			&& startEndPoints.get(1).y>Constant.TOP_Y && startEndPoints.get(1).y<Constant.BOTTOM_Y)){
    		Toast.makeText(context, R.string.noway, Toast.LENGTH_SHORT).show();
    		removremovePath();
    		return;
    	}
        pathPoit.clear();
        if(start <= 11 && end <=11){
            ArrayList<String> dijkstra = Dijkstra
                    .dijkstra(BZ1, start, end);
            String path = dijkstra.get(0);
            String[] zz = path.split(">");
            for (int q = 0; q < zz.length; q++)
            {
                pathPoit.add(Integer
                        .parseInt(zz[q]));
            }
        }else if(start>11 && end >11){
        ArrayList<String> dijkstra = Dijkstra
                .dijkstra(BZ2, start-12, end-12);
        String path = dijkstra.get(0);
        String[] zz = path.split(">");
        for (int q = 0; q < zz.length; q++)
        {
            pathPoit.add(Integer
                    .parseInt(zz[q])+12);
        }
        }else if(start <=11 && end >11){
            ArrayList<String> dijkstra = Dijkstra
                    .dijkstra(BZ1, start, 8);
            String path = dijkstra.get(0);
            String[] zz = path.split(">");
            for (int q = 0; q < zz.length; q++)
            {
                pathPoit.add(Integer
                        .parseInt(zz[q]));
            }
            
            ArrayList<String> dijkstra2 = Dijkstra
                    .dijkstra(BZ2, 0, end-12);
            String path2 = dijkstra2.get(0);
            String[] zz2 = path2.split(">");
            for (int q = 0; q < zz2.length; q++)
            {
                pathPoit.add(Integer
                        .parseInt(zz2[q])+12);
            }
        }else{
            ArrayList<String> dijkstra = Dijkstra
                    .dijkstra(BZ2, start-12, 0);
            String path = dijkstra.get(0);
            String[] zz = path.split(">");
            for (int q = 0; q < zz.length; q++)
            {
                pathPoit.add(Integer
                        .parseInt(zz[q])+12);
            }
            
            ArrayList<String> dijkstra2 = Dijkstra
                    .dijkstra(BZ1, 8, end);
            String path2 = dijkstra2.get(0);
            String[] zz2 = path2.split(">");
            for (int q = 0; q < zz2.length; q++)
            {
                pathPoit.add(Integer
                        .parseInt(zz2[q]));
            }
        }
        drawLine();
    }
    
    /**
     * 清除路径及起始点
     */
    public void removePathPoint()
    {
        pathPoit.clear();
        //startEndPoints.clear();
        /*for (int i = 0; i < lines; i++)
        {*/
            map.removeShape("Lin");
       // }
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
    	 /*for (int i = 0; i < lines; i++)
         {*/
             map.removeShape("Lin");
        // }
         Path path=new Path();
         LineShape ls = null;
         path.moveTo(points.get(pathPoit.get(0)).x, points.get(pathPoit.get(0)).y);
         ls = new LineShape("Lin", Color.parseColor("#8bc34a"));
         for (int i = 1; i < (pathPoit.size()); i++)
         {
             /*ls.setValues(String.format("%.5f,%.5f,%.5f,%.5f",
                     points.get(pathPoit.get(i)).x,
                     points.get(pathPoit.get(i)).y,
                     points.get(pathPoit.get(i + 1)).x,
                     points.get(pathPoit.get(i + 1)).y));*/
             path.lineTo(points.get(pathPoit.get(i)).x, points.get(pathPoit.get(i)).y);
           //  lines++;
         }
         ls.setPath(path);
         map.addShape(ls, false);
        /*for (int i = 0; i < (pathPoit.size() - 1); i++)
        {
            LineShape ls = new LineShape("Lin" + i, Color.parseColor("#8bc34a"));
            ls.setValues(String.format("%.5f,%.5f,%.5f,%.5f",
                    points.get(pathPoit.get(i)).x,
                    points.get(pathPoit.get(i)).y,
                    points.get(pathPoit.get(i + 1)).x,
                    points.get(pathPoit.get(i + 1)).y));
            map.addShape(ls, false);
            lines++;
        }*/
        /*for (int i = 0; i < (pathPoit.size() - 1); i++)
        {
            CircleShape black = new CircleShape("s"+i, Color.BLUE); //
            black.setValues(String.format("%.5f,%.5f,4",
                    points.get(pathPoit.get(i)).x*1.0, points.get(pathPoit.get(i)).y * 1.0)); // 设置圆点的位置和大小
            map.addShape(black, true);
        
        }*/
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
        //startEndPoints.add(pointf);
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
       // startEndPoints.add(pointf);
        startEndPoints.get(1).x = pointf.x;
        startEndPoints.get(1).y = pointf.y;
    }
    /**
     * 初始化地图数据
     */
    public void initData(){
        points.clear();
        points.add(new PointF(645, 400));// 0
        points.add(new PointF(645, 500));// 1
        points.add(new PointF(645, 600));// 2
        points.add(new PointF(530, 600));// 3
        points.add(new PointF(920, 500));// 4
        points.add(new PointF(920, 430));// 5
        points.add(new PointF(1010, 430));// 06
        points.add(new PointF(1010, 500));// 07
        points.add(new PointF(1280, 500));// 08
        points.add(new PointF(1280, 400));// 09
        points.add(new PointF(1430, 500));// 10
        points.add(new PointF(1430, 430));// 11
        points.add(new PointF(1280, 815));// 12
        points.add(new PointF(1140, 700));// 13
        points.add(new PointF(1140, 815));// 14
        points.add(new PointF(1010, 815));// 15
        points.add(new PointF(1280, 1040));// 16
        points.add(new PointF(960, 1040));// 17
        points.add(new PointF(960, 1160));// 18
        points.add(new PointF(620, 1450));// 19
        points.add(new PointF(960, 1460));// 20
        points.add(new PointF(940, 1620));// 21
        points.add(new PointF(1280, 1550));// 22
        points.add(new PointF(1280, 1380));// 23
        points.add(new PointF(1180, 1280));// 24
        points.add(new PointF(1280, 1280));// 25
        points.add(new PointF(1640, 1300));// 26
        points.add(new PointF(1550, 1140));// 27
        points.add(new PointF(1550, 1040));// 28
        points.add(new PointF(1550, 930));// 29
        points.add(new PointF(1860, 1040));// 30
        points.add(new PointF(2130, 1040));// 31
        points.add(new PointF(2130, 930));// 32
        points.add(new PointF(2130, 1130));// 33
        points.add(new PointF(2360, 1040));// 34
        points.add(new PointF(2360, 860));// 35
        points.add(new PointF(2360, 1230));// 36
        points.add(new PointF(2630, 1040));// 37
        points.add(new PointF(2730, 1040));// 38
        points.add(new PointF(2730, 1200));// 39
        points.add(new PointF(1460, 815));// 40
        points.add(new PointF(1460, 710));// 41
        points.add(new PointF(1720, 700));// 42
        points.add(new PointF(1720, 815));// 43
        points.add(new PointF(1860, 820));// 44
        points.add(new PointF(2120, 730));// 45
        points.add(new PointF(2120, 560));// 46
        points.add(new PointF(2630, 560));// 47
        points.add(new PointF(2630, 400));// 48
        points.add(new PointF(750, 1040));// 49
        points.add(new PointF(650, 830));// 50
        points.add(new PointF(750, 1200));// 51
        points.add(new PointF(1060, 1320));// 52
        points.add(new PointF(1830, 530));// 53
        points.add(new PointF(1860, 1200));// 54
        
        BZ1 = new int[][]{
               //0  1  2  3  4  5  6  7  8  9 10 11
                {0, 1, N, N, N, N, N, N, N, N, N, N},//0
                {1, 0, 1, N, 3, N, N, N, N, N, N, N},//1
                {N, 1, 0, 1, N, N, N, N, N, N, N, N},//2
                {N, N, 1, 0, N, N, N, N, N, N, N, N},//3
                {N, 3, N, N, 0, 1, N, 1, N, N, N, N},//4
                {N, N, N, N, 1, 0, N, N, N, N, N, N},//5
                {N, N, N, N, N, N, 0, 1, N, N, N, N},//6
                {N, N, N, N, 1, N, 1, 0, 3, N, N, N},//7
                {N, N, N, N, N, N, N, 3, 0, 1, 1, N},//8
                {N, N, N, N, N, N, N, N, 1, 0, N, N},//9
                {N, N, N, N, N, N, N, N, 1, N, 0, 1},//10
                {N, N, N, N, N, N, N, N, N, N, 1, 0},//11
                
        };
        
        BZ2 = new int[][]{
                // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42
                 { 0, N, 2, N, 3, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 0
                 { N, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 1
                 { 2, 1, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 2
                 { N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 3
                 { 3, N, N, N, 0, 2, N, N, N, N, N, N, N, 2, N, N, 3, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 4
                 { N, N, N, N, 2, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, N, N, N, N  },// 5
                 { N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 6
                 { N, N, N, N, N, N, N, 0, 3, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 3, N, N, N  },// 7
                 { N, N, N, N, N, N, N, 3, 0, 1, N, 2, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, N, N  },// 8
                 { N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 9
                 { N, N, N, N, N, N, N, N, N, N, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 10
                 { N, N, N, N, N, N, N, N, 2, N, 1, 0, N, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, N, N  },// 11
                 { N, N, N, N, N, N, N, N, N, N, N, N, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 12
                 { N, N, N, N, 2, N, N, N, N, N, N, 1, 1, 0, 3, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 13
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, 3, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 14
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 15
                 { N, N, N, N, 3, N, N, N, N, N, N, N, N, N, N, 1, 0, 1, 3, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 16
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 17
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 3, N, 0, 2, N, N, N, N, N, N, N, N, N, N, N, N, 3, N, N, N, N, N, N, N, N, N, 2 },// 18
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, 0, 1, 1, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 19
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 20
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 21
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, N, 0, 1, 1, 2, N, N, N, N, N, N, N, 4, N, N, N, N, N, N, N, N, N  },// 22
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 23
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, N, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N },// 24
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, N, N, 0, 1, N, N, N, N, N, N, N, N, 6, N, N, N, N, N, N, N  },// 25
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 26
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 27
                 { 2, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 0, 1, N, 3, N, N, N, N, N, N, N, N, N, N, N  },// 28
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N, N, N, N, N, N, N, N  },// 29
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 0, 1, N, N, N, N, N, N, N, N, N, N, N },// 30
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 3, N, 1, 0, 2, N, N, N, N, N, N, N, N, N, N  },// 31
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 3, N, N, N, N, N, N, N, N, N, N, N, N, 2, 0, 2, N, N, N, N, N, N, N, 4, N  },// 32
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 4, N, N, N, N, N, N, N, N, N, 2, 0, 2, N, N, N, N, N, N, N, N  },// 33
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, 0, 6, N, N, N, N, N, N, N  },// 34
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 6, N, N, N, N, N, N, N, N, 6, 0, 1, N, N, N, N, N, N  },// 35
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 1, 0, N, N, N, N, N, N  },// 36
                 { N, N, N, N, N, 1, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 0, 4, 2, N, N, N  },// 37
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 4, 0, N, N, N, N  },// 38
                 { N, N, N, N, N, N, N, 3, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, N, 0, 2, N, N  },// 39
                 { N, N, N, N, N, N, N, N, 2, N, N, 2, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, 0, N, N  },// 40
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 4, N, N, N, N, N, N, N, N, 0, N  },// 41
                 { N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 2, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, 0  },// 41
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
