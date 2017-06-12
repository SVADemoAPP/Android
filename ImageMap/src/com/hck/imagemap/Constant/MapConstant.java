package com.hck.imagemap.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.graphics.Bitmap;

public class MapConstant
{
    public static final int ON = 1;

    public static final int OFF = 0;
    
    public static boolean  startedApp = false;
    
    public static Bitmap bitmap;

    public static Locale local;
    
    public static Vector<double[]> locationForFilter = new Vector<double[]>(0);

    public static int filter2Flag = 0;
    
    public static Vector<double[]> locationForFilter_global=new Vector<double[]>(0);
    
    public static int distanceCnt=0;
    
    public static boolean start = true;
    public static boolean wifiSwitch;
    public static String content;
    public static String sned_time;
    
    public static Vector<double[]> FingureprintXY=new Vector<double[]>(0);
    
    //public static List<float[]> dataMemsList=new ArrayList<float[]>();
    
    public static List<float[]> dataMemsList=new ArrayList<float[]>();
    
//    public static void addDataMems(float[] data)
//    {
//    	synchronized (dataMemsList) 
//    	{
//    		if(dataMemsList.size() == 50)
//    		{
//    			dataMemsList.remove(0);
//    			dataMemsList.add(data);
//    		}
//    		else
//    		{
//    			dataMemsList.add(data);
//    		}
//		}
//    }
//    
//    public static float[][] getDataMems()
//    {
//    	synchronized (dataMemsList) 
//    	{
//    		float[][] dataMem = new float[10][50];
//    		for (int i = 0; i < MapConstant.dataMemsList.size(); i++) {
//        		float[] dataTemp = MapConstant.dataMemsList.get(i);
//				for (int k = 0; k < dataTemp.length; k++) 
//				{
//					dataMem[k][i] = dataTemp[k];
//				}
//			}
//    		return dataMem;
//		}
//    }

}
