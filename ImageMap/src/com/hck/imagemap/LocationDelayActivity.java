package com.hck.imagemap;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.TouchImageView1.OnSingleClickListener;
import net.yoojia.imagemap.core.CircleShape;
import net.yoojia.imagemap.core.RoundShape;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.hck.imagemap.utils.Loction;
import com.hck.imagemap.utils.UpLoad;
import com.hck.imagemap.view.CustomDialog;

public class LocationDelayActivity extends Activity
{
    private RequestQueue mRequestQueue;
    private UpLoad load;
    private ImageMap1 map;// ditu
    private RelativeLayout layoutTemp;
    private Bundle bn;
    private boolean isBack = false;
//    private Bitmap bitmap;
    private RelativeLayout layout;
    private Button delayClick;
    private Button cancleTest;
    private double[] oriLocation = new double[2];
    private Floor currFloor;
    private int postionNum = 0;
    private Boolean bAnimation;
    private SharedPreferences myPreferences;
    private boolean isRequestLocation = false;// 用于判断是否请求位置
    private boolean touchTag = true;
    private EditText touchPointX, touchPointY, range;
    private PointF pointF;
    private NumberFormat numberFormat;
    private Loction loction;
    private int testTag = 0;//
    private long DataDelay = 0;// 数据延时
    private long stopTime = 0;// 手动停止时间
    private long InTime = 0;// 进入范围时间
    private boolean bFirstLocation = false;
    private boolean bFirstIn = false;
    private float x, y;// 终点像素坐标
    private boolean userStop = false;
    private Handler handler = new Handler();
//10.125.253.90
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location_delay);
        initView();
//         showResult();
        requestLocationTask();
    }

    private void initView()
    {
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        bAnimation = myPreferences.getBoolean("bAnimation", true);
        delayClick = (Button) findViewById(R.id.delayClick);
        cancleTest = (Button) findViewById(R.id.cancleTest);
        cancleTest.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Cancle();
            }
        });
        load = new UpLoad(this);
        loction = new Loction();
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        layout = (RelativeLayout) findViewById(R.id.rl_delay_map);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutTemp = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.image_map_item, null);
        map = (ImageMap1) layoutTemp.findViewById(R.id.imagemap);
        layoutTemp.setLayoutParams(lp);
        layout.addView(layoutTemp);
        mRequestQueue = Volley.newRequestQueue(this);
        Intent in = this.getIntent();
        bn = in.getExtras();
//        try
//        {
//            bitmap = BitmapFactory.decodeFile(Environment
//                    .getExternalStorageDirectory() + bn.getString("path"));
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        if (bitmap == null)
//        {
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
                pointF = p;
                if (touchTag)
                {
                    CircleShape cs = new CircleShape("touchPoint", Color.BLUE);
                    cs.setValues(String.format("%.5f:%.5f:20", pointF.x,
                            pointF.y));
                    map.addShape(cs, false);
                    testTag = 1;
                }
            }

        });
    }

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

    private double lastTime;
    /**
     * 定位
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
                        if (bFirstLocation)
                        {
                            DataDelay = System.currentTimeMillis() - DataDelay;
                            bFirstLocation = false;
                        }
                        Log.d("locationdelay",
                                "requestLocation:222" + jsonobj.toString());
                        Location loc = new Location();
                        try
                        {
                            final JSONObject o = jsonobj.getJSONObject("data");
                            oriLocation[0] = o.getDouble("x");
                            oriLocation[1] = o.getDouble("y");
                            if (currFloor.getFloorNo() != o.getInt("z"))
                            {
                                requestLocationTask();
                                return;
                            }
                            
                            if (lastTime == o.getDouble("timestamp")) {
                                requestLocationTask();
                                return;
                            }
                            lastTime = o.getDouble("timestamp");
                            loc.setX(oriLocation[0]);
                            loc.setY(oriLocation[1]);

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

    /**
     * 根据定位结果打点
     * 
     * @param loc
     */
    private void addPostion(Location loc)
    {
        Loction loction = new Loction();
        double xx = loction.location(loc.getX(), loc.getY(), currFloor)[0];
        double yy = loction.location(loc.getX(), loc.getY(), currFloor)[1];
        CircleShape postion = new CircleShape("NO" + postionNum, Color.RED);
        postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
        map.addShape(postion, bAnimation);
        postionNum++;

        if (!bFirstIn)
        {
            double ranges = Double.parseDouble(range.getText().toString());
            ranges = (double) (ranges * currFloor.getScale());
            if (Math.sqrt(Math.abs(x - xx) * Math.abs(x - xx)
                    + Math.abs(y - yy) * Math.abs(y - yy)) < ranges)
            {
                InTime = System.currentTimeMillis();
                bFirstIn = true;
                if(userStop)
                {
                    isRequestLocation = false;
                    userStop = false;
                    showResult();
                }
            }
        }
    }

    private long LocationDelay;
    private void showResult()
    {
        // TODO Auto-generated method stub
        LocationDelay = (InTime - stopTime);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(getString(R.string.test_results));
        builder.setMessage(getString(R.string.DataDelay) + DataDelay / 1000.0
                + " s" + "\n" + getString(R.string.Locationdelay)
                + LocationDelay / 1000.0 + " s" + "\n\n"
                + getString(R.string.makesure));
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        saveTestData();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(getString(R.string.no),
                new android.content.DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Cancle();
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 设置终点按钮点击
     */
    public void LocationDelay(View view)
    {
        if (getString(R.string.endPoint).equals(
                delayClick.getText().toString()))
        {
            if (testTag == 0)
            {
                Toast.makeText(this, getString(R.string.choseend), Toast.LENGTH_SHORT).show();
                return;
            }
            cancleTest.setVisibility(View.VISIBLE);
            View views = View.inflate(LocationDelayActivity.this,
                    R.layout.location_delay, null);
            touchPointX = (EditText) views.findViewById(R.id.xPoint);
            touchPointY = (EditText) views.findViewById(R.id.yPoint);
            range = (EditText) views.findViewById(R.id.range);
            touchPointX.setText(numberFormat.format(loction.location(
                    pointF.x / bn.getDouble("scale") * 10
                            - bn.getDouble("xSport") * 10,
                    pointF.y / bn.getDouble("scale") * 10
                            - bn.getDouble("ySport") * 10,
                    (Floor) bn.getSerializable("currFloor"))[0]
                    / bn.getDouble("scale") - bn.getDouble("xSport"))
                    + "");
            touchPointY.setText(numberFormat.format(loction.location(
                    pointF.x / bn.getDouble("scale") * 10
                            - bn.getDouble("xSport") * 10,
                    pointF.y / bn.getDouble("scale") * 10
                            - bn.getDouble("ySport") * 10,
                    (Floor) bn.getSerializable("currFloor"))[1]
                    / bn.getDouble("scale") - bn.getDouble("xSport"))
                    + "");
            CustomDialog.Builder builder = new CustomDialog.Builder(
                    LocationDelayActivity.this);
            String lockPosition = getString(R.string.lockPosition).toString();
            String yes = getString(R.string.yes).toString();
            String no = getString(R.string.no).toString();
            builder.setTitle(lockPosition);
            builder.setPositiveButton(yes,
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            touchTag = false;
                            map.removeShape("touchPoint");
                            x = (float) loction.location(Double
                                    .parseDouble(touchPointX.getText()
                                            .toString()) * 10, Double
                                    .parseDouble(touchPointY.getText()
                                            .toString()) * 10, currFloor)[0];
                            y = (float) loction.location(Double
                                    .parseDouble(touchPointX.getText()
                                            .toString()) * 10, Double
                                    .parseDouble(touchPointY.getText()
                                            .toString()) * 10, currFloor)[1];
                            CircleShape cs = new CircleShape("endPoint",
                                    Color.GREEN);
                            cs.setValues(String.format("%.5f:%.5f:20", x, y));
                            map.addShape(cs, false);
                            int ranges = Integer.parseInt(range.getText()
                                    .toString());
                            ranges = (int) (ranges * currFloor.getScale());
                            RoundShape rs = new RoundShape("rs", Color.GREEN);
                            rs.setValues(String.format("%.5f:%.5f:" + ranges,
                                    x, y));
                            map.addShape(rs, false);
                            delayClick.setText(getString(R.string.dynicStart));
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton(no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            builder.setContentView(views);
            builder.create().show();

        } else if (getString(R.string.dynicStart).equals(
                delayClick.getText().toString()))
        {
            DataDelay = System.currentTimeMillis();
            isRequestLocation = true;
            bFirstLocation = true;
            InTime = 0;
            userStop = false;
            delayClick.setText(getString(R.string.dynicEnd));
        } else if (getString(R.string.dynicEnd).equals(
                delayClick.getText().toString()))
        {
            stopTime = System.currentTimeMillis();
            userStop = true;
            if (InTime == 0)
            {
                Toast.makeText(this, getString(R.string.notAtrange), Toast.LENGTH_SHORT).show();
                delayClick.setText("Waiting");
                requestLocationTask();
                return;
            }
            isRequestLocation = false;
            showResult();
        }
    }

    public void Cancle()
    {
        isRequestLocation = false;
        delayClick.setText(getString(R.string.endPoint));
        cancleTest.setVisibility(View.GONE);
        touchTag = true;
        bFirstIn = false;
        testTag = 0;
        userStop = false;
        handler.postDelayed(runnable, 1500);
    }
    
 Runnable runnable = new Runnable()
{
    
    @Override
    public void run()
    {
        map.removeShape("touchPoint");
        map.removeShape("endPoint");
        map.removeShape("rs");
        for (int i = 0; i < postionNum; i++)
        {
            map.removeShape("NO" + i);
        }
        postionNum = 0;
    }
};

    private void saveTestData()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("place", currFloor.getPlace());
        param.put("placeId", String.valueOf(currFloor.getPlaceId()));
        param.put("floorNo", String.valueOf(currFloor.getFloorNo()));
        param.put("floor",String.valueOf(currFloor.getFloor()));
        param.put("dataDelay",String.valueOf(DataDelay/ 1000.0));
        param.put("positionDelay",String.valueOf(LocationDelay/ 1000.0));

        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/savaLocationDelay", new JSONObject(param),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.d("locationDelay",
                                "saveTestData:" + jsonobj.toString());
                        Cancle();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("locationDelay", "saveTestData:", error);
                        Cancle();
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
//        bitmap.recycle();
    }

    public void setting_back(View v)
    {
        finish();
//        bitmap.recycle();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mRequestQueue.cancelAll(this);
        isBack = true;
//        if (bitmap != null)
//        {
//            bitmap.recycle();
//        }
        // bitmap.recycle();
    }

}
