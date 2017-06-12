package com.hck.imagemap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.TouchImageView1.OnSingleClickListener;
import net.yoojia.imagemap.core.CircleShape;
import net.yoojia.imagemap.core.LineShape;
import net.yoojia.imagemap.core.NumCircleShape;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hck.imagemap.Constant.MapConstant;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.entity.Floor;
import com.hck.imagemap.entity.Location;
import com.hck.imagemap.utils.FileUtils;
import com.hck.imagemap.utils.Loction;
import com.hck.imagemap.utils.UpLoad;
import com.hck.imagemap.view.CustomDialog;

/**
 * 路径滤波（把定位时打偏的点通过算法拉近）
 * 
 * @author yipenga
 * 
 */
public class PathAcitivity extends Activity
{

    private ImageMap1 map;
    private Button pathDebug;
    private RelativeLayout layout;
    private RelativeLayout layoutTemp;
    private RelativeLayout rl_path_debug;
    private Bundle bn;
    private NumberFormat numberFormat;
    private LinearLayout ll_more;
    private RequestQueue mRequestQueue;
    private Boolean bAnimation;
    private SharedPreferences myPreferences;
//    private Bitmap bitmap;
    private Button deleteDebug;
    private ImageView iv_path;
    private Floor currFloor;
    private UpLoad load;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_path_acitivity);
        initViews();
        requestLocationTask();
    }

    /**
     * 初始化
     */
    private void initViews()
    {
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        bAnimation = myPreferences.getBoolean("bAnimation", true);
        pathDebug = (Button) findViewById(R.id.pathDebug);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);
        mRequestQueue = Volley.newRequestQueue(this);
        layout = (RelativeLayout) findViewById(R.id.rl_pathmap);
        rl_path_debug = (RelativeLayout) findViewById(R.id.rl_path_debug);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutTemp = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.image_map_item, null);
        layoutTemp.setLayoutParams(lp);
        map = (ImageMap1) layoutTemp.findViewById(R.id.imagemap);
        iv_path = (ImageView) findViewById(R.id.iv_path);
        deleteDebug = (Button) findViewById(R.id.deleteDebug);
        layout.addView(layoutTemp);
        Intent in = this.getIntent();
        bn = in.getExtras();
        load = new UpLoad(this);
//        try
//        {
//            bitmap = BitmapFactory.decodeFile(Environment
//                    .getExternalStorageDirectory() + bn.getString("path"));
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        if(bitmap == null){
//            finish();
//            return;
//        }
        map.setMapBitmap(MapConstant.bitmap);
        currFloor = (Floor) bn.getSerializable("currFloor");
        map.setOnSingleClickListener(new OnSingleClickListener()
        {

            @Override
            public void onSingle(PointF p)
            {
                if (touchCon && !isBack)
                {
                    if (num < 1)
                    {
                        start = p;
                        CircleShape cs = new CircleShape("P" + i, Color.BLUE);
                        cs.setValues(String.format("%.5f:%.5f:20", start.x,
                                start.y));
                        map.addShape(cs, false);
                        num++;
                        i++;
                        starts[0] = start.x / bn.getDouble("scale") * 10;
                        starts[1] = start.y / bn.getDouble("scale") * 10;
                        touchList.add(start);
                        // MapConstant.FingureprintXY.add(starts);
                    } else
                    {
                        num++;
                        end = p;
                        sumpoint += Math.sqrt((end.x - touchList.get(i - 1).x)
                                * (end.x - touchList.get(i - 1).x)
                                + (end.y - touchList.get(i - 1).y)
                                * (end.y - touchList.get(i - 1).y))
                                / bn.getDouble("scale");
                        NumCircleShape cs = new NumCircleShape("P" + i,
                                Color.BLUE, numberFormat.format(sumpoint));
                        cs.setValues(String
                                .format("%.5f:%.5f:20", end.x, end.y));
                        map.addShape(cs, false);
                        LineShape ls = new LineShape("L" + i, Color.BLUE);
                        // ls.setValues(String.format("%.5f:%.5f:%.5f,%.5f",
                        // touchList.get(i - 1).x,
                        // touchList.get(i - 1).y, end.x, end.y));
                        Path path = new Path();
                        // ls.setValues(String.format("%.5f:%.5f:%.5f,%.5f",
                        // dynicStart.x*1.0,
                        // dynicStart.y*1.0, dynicEnd.x*1.0, dynicEnd.y*1.0));
                        path.moveTo(touchList.get(i - 1).x,
                                touchList.get(i - 1).y);
                        path.lineTo(end.x, end.y);
                        ls.setPath(path);
                        map.addShape(ls, false);
                        start = end;
                        starts[0] = start.x / bn.getDouble("scale") * 10;
                        starts[1] = start.y / bn.getDouble("scale") * 10;
                        touchList.add(start);
                        // MapConstant.FingureprintXY.add(starts);

                        i++;
                    }
                }
            }
        });
    }

    /**
     * 均值滤波计算过程
     * 
     * @param result
     * @return
     */
    private double[] meanFilterProcess(double[] result)
    {
        double[] outVal = new double[2];
        double[] outVal_global = new double[2];

        int filterWindowLen = 2; // 滤波窗长
        double dis_th = 50; // 局部搜索算法距离门限，单位是分米
        int cnt_th = 3; // 局部搜索算法次数门限

        // 全局搜索用
        MapConstant.locationForFilter_global.add(result);
        if (MapConstant.locationForFilter_global.size() > filterWindowLen)
        {
            MapConstant.locationForFilter_global.remove(0);
        }
        MapConstant.locationForFilter.add(result);
        if (MapConstant.locationForFilter.size() > filterWindowLen)
        {
            MapConstant.locationForFilter.remove(0);
        }

        if (MapConstant.locationForFilter_global.size() < filterWindowLen)
        {
            return result;
        }

        // outVal = Filter.meanFilter(result, 5);
        float sumX = 0;
        float sumY = 0;
        for (int i = 0; i < filterWindowLen; i++)
        {
            sumX += MapConstant.locationForFilter_global.get(i)[0];
            sumY += MapConstant.locationForFilter_global.get(i)[1];
        }

        outVal_global[0] = sumX / filterWindowLen;
        outVal_global[1] = sumY / filterWindowLen;

        // 局部搜索用
        if (MapConstant.locationForFilter.size() < filterWindowLen)
        {
            return result;
        }

        // outVal = Filter.meanFilter(result, 5);
        sumX = 0;
        sumY = 0;
        for (int i = 0; i < filterWindowLen; i++)
        {
            sumX += MapConstant.locationForFilter.get(i)[0];
            sumY += MapConstant.locationForFilter.get(i)[1];
        }

        outVal[0] = sumX / filterWindowLen;
        outVal[1] = sumY / filterWindowLen;

        // outVal = result;
        // outVal = pathAdapt(outVal);

        // double disScope = Math.sqrt(Math.pow((pathScope(outVal)[0] -
        // MapConstant.locationForFilter.get(3)[0]), 2)
        // + Math.pow((pathScope(outVal)[1] -
        // MapConstant.locationForFilter.get(3)[1]), 2));
        //
        // double disAdapt = Math.sqrt(Math.pow((pathAdapt(outVal)[0] -
        // MapConstant.locationForFilter.get(3)[0]), 2)
        // + Math.pow((pathAdapt(outVal)[1] -
        // MapConstant.locationForFilter.get(3)[1]), 2));
        // if (disScope <= disAdapt) {
        // outVal = pathScope(outVal);
        // } else {
        // outVal = pathAdapt(outVal);
        // }
        outVal = pathScope(outVal, filterWindowLen);
        outVal_global = pathAdapt(outVal_global, filterWindowLen);

        MapConstant.locationForFilter_global.set(filterWindowLen - 1,
                outVal_global);

        double dis = Math.sqrt(Math.pow((outVal[0] - outVal_global[0]), 2)
                + Math.pow((outVal[1] - outVal_global[1]), 2));

        if (dis > dis_th)
        {
            MapConstant.distanceCnt++;
        } else
        {
            MapConstant.distanceCnt = 0;
        }

        if (MapConstant.distanceCnt >= cnt_th)
        {
            outVal = outVal_global;
            // 清掉历史值
        }

        if (MapConstant.filter2Flag == 1)
        {
            sumX = 0;
            sumY = 0;
            for (int i = 0; i < filterWindowLen; i++)
            {
                sumX += MapConstant.locationForFilter.get(i)[0];
                sumY += MapConstant.locationForFilter.get(i)[1];
            }
            outVal[0] = sumX / filterWindowLen;
            outVal[1] = sumY / filterWindowLen;
            MapConstant.locationForFilter.set(filterWindowLen - 1, outVal);
        }
        // Toast.makeText(MainActivity.this, "滤波后存在定位数据并创建文件成功！", 0).show();
        // double []est_err = new double[2];
        // est_err[0] = result[0] - outVal[0];
        // est_err[1] = result[1] - outVal[1];
        //
        // outVal[0] += est_err[0] * 0.1;
        // outVal[1] += est_err[1] * 0.1;
        MapConstant.locationForFilter.set(filterWindowLen - 1, outVal);
        System.out.println(outVal[0] + " + " + outVal[1]);
        return outVal;
    }

    private double[] pathScope(double[] result, int filterWindowLen)
    {
        double scope_th = 90; // 单位是分米
        if (MapConstant.FingureprintXY.size() == 0)
        {
            return result;
        }
        double[] lastPoint = MapConstant.locationForFilter
                .get(filterWindowLen - 2);
        double[] outVal = new double[2];
        int dbNum = MapConstant.FingureprintXY.size();
        List<double[]> distance = new ArrayList<double[]>();
        double[] dbData;
        for (int i = 0; i < dbNum; i++)
        {
            dbData = MapConstant.FingureprintXY.get(i);
            double distanceNum = Math.sqrt(Math.pow((lastPoint[0] - dbData[0]),
                    2) + Math.pow((lastPoint[1] - dbData[1]), 2));
            if (distanceNum <= scope_th)
            {
                distance.add(dbData);
            }
        }
        int scopeNum = distance.size();
        if (scopeNum == 0)
        {
            return result;
        }
        double distanceScope[] = new double[scopeNum];
        int[] index = new int[scopeNum];
        for (int i = 0; i < scopeNum; i++)
        {
            dbData = distance.get(i);
            System.out.println("db" + i + "=" + dbData[0] + " " + dbData[1]);
            distanceScope[i] = Math.sqrt(Math.pow((result[0] - dbData[0]), 2)
                    + Math.pow((result[1] - dbData[1]), 2));
            index[i] = i;
        }
        // 降序
        for (int ii = 0; ii < distanceScope.length - 1; ii++)
        {
            for (int jj = ii + 1; jj < distanceScope.length; jj++)
            {
                if (distanceScope[ii] > distanceScope[jj])
                {
                    double temp = 0;
                    temp = distanceScope[ii];
                    distanceScope[ii] = distanceScope[jj];
                    distanceScope[jj] = temp;
                    int tempIndex;
                    tempIndex = index[ii];
                    index[ii] = index[jj];
                    index[jj] = tempIndex;
                }
            }
        }
        int s_data_num = 10;
        if (distance.size() < s_data_num)
        {
            return result;
        }
        double[] preDataDis = new double[s_data_num];
        int[] preDataIndex = new int[s_data_num];
        double[] preData = MapConstant.locationForFilter
                .get(filterWindowLen - 2);
        for (int i = 0; i < s_data_num; i++)
        {
            dbData = distance.get(index[i]);
            System.out.println("db_pre" + index[i] + "=" + dbData[0] + " "
                    + dbData[1]);
            preDataDis[i] = (float) Math.sqrt(Math.pow(
                    (preData[0] - dbData[0]), 2)
                    + Math.pow((preData[1] - dbData[1]), 2));
            preDataIndex[i] = index[i];
        }
        for (int ii = 0; ii < preDataDis.length - 1; ii++)
        {
            for (int jj = ii + 1; jj < preDataDis.length; jj++)
            {
                if (preDataDis[ii] > preDataDis[jj])
                {
                    double temp = 0;
                    temp = preDataDis[ii];
                    preDataDis[ii] = preDataDis[jj];
                    preDataDis[jj] = temp;
                    int tempIndex;
                    tempIndex = (int) preDataIndex[ii];
                    preDataIndex[ii] = preDataIndex[jj];
                    preDataIndex[jj] = tempIndex;
                }
            }
        }
        // outVal[0] = (distance.get(index[0])[0] +
        // distance.get(preDataIndex[0])[0]) / 2;
        // outVal[1] = (distance.get(index[0])[1] +
        // distance.get(preDataIndex[0])[1]) / 2;
        outVal[0] = distance.get(index[0])[0];
        outVal[1] = distance.get(index[0])[1];
        return outVal;
    }

    private double[] pathAdapt(double[] result, int filterWindowLen)
    {
        double[] outVal = new double[2];
        int dbNum = MapConstant.FingureprintXY.size();
        double[] distance = new double[dbNum];
        int[] index = new int[dbNum];
        double[] dbData;
        for (int i = 0; i < dbNum; i++)
        {
            dbData = MapConstant.FingureprintXY.get(i);
            distance[i] = Math.sqrt(Math.pow((result[0] - dbData[0]), 2)
                    + Math.pow((result[1] - dbData[1]), 2));
            index[i] = i;
        }

        // 先排序
        for (int ii = 0; ii < distance.length - 1; ii++)
        {
            for (int jj = ii + 1; jj < distance.length; jj++)
            {
                if (distance[ii] > distance[jj])
                {
                    double temp = 0;

                    temp = distance[ii];
                    distance[ii] = distance[jj];
                    distance[jj] = temp;

                    int tempIndex;
                    tempIndex = index[ii];
                    index[ii] = index[jj];
                    index[jj] = tempIndex;
                }
            }
        }

        int s_data_num = 15;
        double[] preDataDis = new double[s_data_num];
        int[] preDataIndex = new int[s_data_num];
        double[] preData = MapConstant.locationForFilter
                .get(filterWindowLen - 2);
        // Toast.makeText(this, "this ok", 0).show();
        for (int i = 0; i < s_data_num; i++)
        {
            dbData = MapConstant.FingureprintXY.get(index[i]);
            preDataDis[i] = (float) Math.sqrt(Math.pow(
                    (preData[0] - dbData[0]), 2)
                    + Math.pow((preData[1] - dbData[1]), 2));
            preDataIndex[i] = index[i];
        }

        // 先排序
        for (int ii = 0; ii < preDataDis.length - 1; ii++)
        {
            for (int jj = ii + 1; jj < preDataDis.length; jj++)
            {
                if (preDataDis[ii] > preDataDis[jj])
                {
                    double temp = 0;

                    temp = preDataDis[ii];
                    preDataDis[ii] = preDataDis[jj];
                    preDataDis[jj] = temp;

                    int tempIndex;
                    tempIndex = preDataIndex[ii];
                    preDataIndex[ii] = preDataIndex[jj];
                    preDataIndex[jj] = tempIndex;
                }
            }
        }

        outVal[0] = (MapConstant.FingureprintXY.get(index[0])[0] + MapConstant.FingureprintXY
                .get(preDataIndex[0])[0]) / 2;
        outVal[1] = (MapConstant.FingureprintXY.get(index[0])[1] + MapConstant.FingureprintXY
                .get(preDataIndex[0])[1]) / 2;
        return outVal;
    }

    private List<double[]> pathList;
    private List<PointF> touchList = new ArrayList<PointF>(0);;
    private int i = 0;
    private String lineTxt = null;
    private BufferedReader bufferedReader;
    private boolean touchCon;
    private boolean isRequestLocation;// 用于判断是否请求位置
    private boolean booleanTouch = false;

    /**
     * 路径调试按钮监听
     * 
     * @param view
     */
    public void pathClick(View view)
    {
        pathList = new ArrayList<double[]>(0);
        Log.i("Tag", "pathList=1" + pathList);
        if (getString(R.string.pathadjust).equals(pathDebug.getText()))
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle(getString(R.string.pathadjust)).setMessage(
                    getString(R.string.addpath));
            // 手动选择路径
            builder.setPositiveButton(getString(R.string.pathconnect),
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            MapConstant.FingureprintXY.clear();
                            pathDebug.setText(getString(R.string.startadjust));
                            dialog.dismiss();
                            touchCon = true;
                            booleanTouch = true;
                            deleteDebug.setVisibility(View.VISIBLE);
                        }
                    });
            // 导入路径文件
            builder.setNegativeButton(getString(R.string.addfile),
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            num = 2;
                            MapConstant.FingureprintXY.clear();
                            booleanTouch = false;
                            File file = new File(/*
                                                  * Environment.
                                                  * getExternalStorageDirectory
                                                  * () +
                                                  * "/data/data/com.hck.imagemap/files/data.txt"
                                                  */
                            Environment.getExternalStorageDirectory() + "/SVA/"
                                    + String.valueOf(bn.getInt("floorNo"))
                                    + ".txt");
                            if (file.isFile() && file.exists())
                            {
                                try
                                {
                                    InputStreamReader read = new InputStreamReader(
                                            new FileInputStream(file), "UTF-8");
                                    bufferedReader = new BufferedReader(read);
                                    System.out.println("star");
                                    Loction loction = new Loction();
                                    Floor currFloor = (Floor) bn
                                            .getSerializable("currFloor");
                                    while ((lineTxt = bufferedReader.readLine()) != null)
                                    {
                                        String[] sourceStrArray = lineTxt
                                                .split(" ");
                                        double[] coordiate = new double[2];
                                        coordiate[0] = Double
                                                .parseDouble(sourceStrArray[0]);
                                        coordiate[1] = Double
                                                .parseDouble(sourceStrArray[1]);
                                        pathList.add(coordiate);
                                        MapConstant.FingureprintXY
                                                .add(coordiate);
                                        if (pathList.size() == 2)
                                        {
                                            LineShape lineShape = new LineShape(
                                                    "line" + i, Color.BLUE);
                                            double[] startF = loction.location(
                                                    pathList.get(0)[0],
                                                    pathList.get(0)[1],
                                                    currFloor);
                                            double[] endF = loction.location(
                                                    pathList.get(1)[0],
                                                    pathList.get(1)[1],
                                                    currFloor);
                                            // lineShape.setValues(String
                                            // .format("%.5f:%.5f:%.5f,%.5f",startF[0],startF[1],endF[0],endF[1]));
                                            Path path = new Path();
                                            path.moveTo((float) startF[0],
                                                    (float) startF[1]);
                                            path.lineTo((float) endF[0],
                                                    (float) endF[1]);
                                            lineShape.setPath(path);
                                            map.addShape(lineShape, false);
                                        }
                                        if (pathList.size() > 2)
                                        {
                                            pathList.remove(0);
                                            LineShape lineShape = new LineShape(
                                                    "line" + i, Color.BLUE);
                                            double[] startF = loction.location(
                                                    pathList.get(0)[0],
                                                    pathList.get(0)[1],
                                                    currFloor);
                                            double[] endF = loction.location(
                                                    pathList.get(1)[0],
                                                    pathList.get(1)[1],
                                                    currFloor);
                                            // lineShape.setValues(String
                                            // .format("%.5f:%.5f:%.5f,%.5f",startF[0],startF[1],endF[0],endF[1]));
                                            Path path = new Path();
                                            path.moveTo((float) startF[0],
                                                    (float) startF[1]);
                                            path.lineTo((float) endF[0],
                                                    (float) endF[1]);
                                            lineShape.setPath(path);
                                            map.addShape(lineShape, false);
                                        }
                                        i++;

                                    }
                                } catch (Exception e)
                                {
                                    Log.e("error", e + "");
                                }
                            }
                            pathDebug.setText(getString(R.string.startadjust));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        } else if (getString(R.string.startadjust).equals(pathDebug.getText()))
        {
            if (touchCon && i == 0)
            {
                Toast.makeText(this, getString(R.string.takePoint),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(num<2)
            {
                Toast.makeText(this, getString(R.string.takePoint),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            deleteDebug.setVisibility(View.INVISIBLE);
            touchCon = false;
            isRequestLocation = true;
            System.out.println("pathDebug i=" + i);
            pathDebug.setText(getString(R.string.closeadjust));
            if (booleanTouch)
            {
                FileUtils utils = new FileUtils();
                String filePath = "/sdcard/SVA";
                String fileName = "/" + String.valueOf(bn.getInt("floorNo"))
                        + ".txt";
                utils.deleteFile(filePath, fileName);
                MapConstant.FingureprintXY.clear();
                Floor currFloor = (Floor) bn.getSerializable("currFloor");
                for (int i = 0; i < touchList.size() - 1; i++)
                {
                    double[] first = new double[2];
                    first[0] = touchList.get(i).x / bn.getDouble("scale") * 10;
                    first[1] = touchList.get(i).y / bn.getDouble("scale") * 10;
                    double[] startF = Loction.convert(first[0], first[1],
                            currFloor);
                    MapConstant.FingureprintXY.add(startF);
                    creatPath(startF[0] + " " + startF[1]);
                    PointF start = new PointF((float) startF[0],
                            (float) startF[1]);

                    first[0] = touchList.get(i + 1).x / bn.getDouble("scale")
                            * 10;
                    first[1] = touchList.get(i + 1).y / bn.getDouble("scale")
                            * 10;
                    double[] endF = Loction.convert(first[0], first[1],
                            currFloor);
                    PointF end = new PointF((float) endF[0], (float) endF[1]);

                    List<PointF> points = FileUtils.Calculation(start, end);

                    for (int j = 0; j < points.size(); j++)
                    {
                        double[] p = new double[2];
                        p[0] = points.get(j).x;
                        p[1] = points.get(j).y;
                        MapConstant.FingureprintXY.add(p);
                        creatPath(p[0] + " " + p[1]);
                    }
                }
                if (touchList.size() > 0)
                {
                    double[] one = new double[2];
                    one[0] = touchList.get(touchList.size() - 1).x
                            / bn.getDouble("scale") * 10;
                    one[1] = touchList.get(touchList.size() - 1).y
                            / bn.getDouble("scale") * 10;
                    double[] startF = Loction
                            .convert(one[0], one[1], currFloor);
                    MapConstant.FingureprintXY.add(startF);
                    creatPath(startF[0] + " " + startF[1]);
                }
            }
            Log.i("touchList.size()", touchList.toString());
        } else if (getString(R.string.closeadjust).equals(pathDebug.getText()))
        {
            System.out.println("guanbi i=" + i);
            isRequestLocation = false;
            clearPath();
        }

    }

    private void creatPath(String content)
    {
        FileUtils utils = new FileUtils();
        String filePath = "/sdcard/SVA";
        String fileName = "/" + String.valueOf(bn.getInt("floorNo")) + ".txt";
        utils.writeTxtToFile(content, filePath, fileName);
    }

    private int num = 0;
    private PointF start, end;
    private int a = 0;
    private double sumpoint = 0;
    private double[] starts = new double[2];
    private boolean isOpenPath;

    /**
     * 返回按钮监听
     * 
     * @param view
     */
    public void staticClick(View view)
    {
        this.finish();
//        bitmap.recycle();
        isBack = true;
        MapConstant.FingureprintXY.clear();
    }

    @Override
    public void onBackPressed()
    {
        finish();
//        bitmap.recycle();
        isBack = true;
        MapConstant.FingureprintXY.clear();
        super.onBackPressed();
    }
    public void iv_pathClick(View view)
    {
        if (isOpenPath)
        {
            iv_path.setImageResource(R.drawable.closepath);
            isOpenPath = false;
        } else
        {
            iv_path.setImageResource(R.drawable.openpath);
            isOpenPath = true;
        }
    }

    /**
     * 撤销按钮监听
     * 
     * @param view
     */
    public void deleteClick(View view)
    {
        System.out.println("deleteButton");
        if (touchCon && i == 0)
        {
            Toast.makeText(this, getString(R.string.takePoint),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        i--;
        map.removeShape("P" + i);
        map.removeShape("L" + i);
        if (i == 0)
        {
            touchList.remove(i);
            num = 0;
            return;
        }
        sumpoint -= Math.sqrt((touchList.get(i).x - touchList.get(i - 1).x)
                * (touchList.get(i).x - touchList.get(i - 1).x)
                + (touchList.get(i).y - touchList.get(i - 1).y)
                * (touchList.get(i).y - touchList.get(i - 1).y))
                / bn.getDouble("scale");
        touchList.remove(i);
    }

    /**
     * 路径清除
     */
    private void clearPath()
    {
        for (int ii = 0; ii < i; ii++)
        {
            map.removeShape("line" + ii);
            map.removeShape("P" + ii);
            map.removeShape("L" + ii);
        }
        for (int i = 0; i < postionNum; i++)
        {
            map.removeShape("NO" + i);
        }
        pathList = null;
        touchCon = false;
        // map.setBoo(touchCon);
        start = null;
        end = null;
        i = 0;
        num = 0;
        sumpoint = 0;
        touchList.clear();
        MapConstant.FingureprintXY.clear();
        pathDebug.setText(getString(R.string.pathadjust));
    }

    private double[] oriLocation = new double[2];

    /**
     * 要求的位置
     */
    private void requestLocation()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ip", load.getLocaIpOrMac());
        params.put("isPush", "2");
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/getData", new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.d("ThisAcitivity",
                                "requestLocation:111" + jsonobj.toString());
                        Location loc = new Location();
                        try
                        {
                            final JSONObject o = jsonobj.getJSONObject("data");
                            oriLocation[0] = o.getDouble("x");
                            oriLocation[1] = o.getDouble("y");
                            if (currFloor.getFloorNo() != o.getInt("z"))
                            {
                                return;
                            }
                            System.out.println(MapConstant.FingureprintXY
                                    .size());
                            if (isOpenPath
                                    && MapConstant.FingureprintXY.size() > 0)
                            {
                                double[] adjLocation = meanFilterProcess(oriLocation);
                                loc.setX(adjLocation[0]);
                                loc.setY(adjLocation[1]);
                            } else
                            {
                                loc.setX(oriLocation[0]);
                                loc.setY(oriLocation[1]);
                            }
                            addPostion(loc);
                        } catch (Exception e)
                        {
                            Log.e("error22", e + "");
                        }
                        requestLocationTask();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("ThisAcitivity", "requestLocation:", error);
                        requestLocationTask();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders()
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        mRequestQueue.add(newMissRequest);
    }

    private boolean isBack = false;// 用于判断是否返回

    /**
     * 请求定位任务
     */
    private void requestLocationTask()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!isBack)
                {
                    if (isRequestLocation)
                    {
                        requestLocation();
                    } else
                    {
                        requestLocationTask();
                    }
                }
            }
        }, 1000);
    }

    private int postionNum = 0;

    /**
     * 增加的位置
     * 
     * @param loc
     */
    private void addPostion(Location loc)
    {
        Loction loction = new Loction();
        double xx = loction.location(loc.getX(), loc.getY(),
                (Floor) bn.getSerializable("currFloor"))[0];
        double yy = loction.location(loc.getX(), loc.getY(),
                (Floor) bn.getSerializable("currFloor"))[1];
        // double xx = loc.getX() / 10 * bn.getDouble("scale")
        // + bn.getDouble("xSport") * bn.getDouble("scale");
        // double yy = loc.getY() / 10 * bn.getDouble("scale")
        // + bn.getDouble("ySport") * bn.getDouble("scale");
        CircleShape postion = new CircleShape("NO" + postionNum, Color.RED);
        postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
        map.addShape(postion, bAnimation);
        postionNum++;
    }

    /**
     * 此方法一般用于初步处理事件，因为动作是由此分发，分发触摸事件
     */
    private float eventX = 0;
    private float eventY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            eventX = ev.getX();
            eventY = ev.getY();
            a = 0;
            break;
        case MotionEvent.ACTION_MOVE:
            if (eventX != ev.getX() || eventY != ev.getY())
            {
                a = 1;
            }
            break;
        case MotionEvent.ACTION_UP:
            int[] location = new int[2];
            rl_path_debug.getLocationOnScreen(location);
            if (ev.getX() > ll_more.getLeft()
                    && ev.getY() > ll_more.getTop() + 100)
            {
                System.out.println(ev.getX() + " " + ev.getY());
            } else if (ev.getY() < location[1] + rl_path_debug.getHeight())
            {

            } else
            {
                if (a == 0)
                {

                }
            }
            break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mRequestQueue.cancelAll(this);
        isBack = true;
//        if(bitmap != null)
//        {
//            bitmap.recycle(); 
//        }
       // bitmap.recycle();
    }

}
