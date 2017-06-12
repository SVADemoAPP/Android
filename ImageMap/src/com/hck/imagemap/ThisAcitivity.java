package com.hck.imagemap;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.TouchImageView1.OnSingleClickListener;
import net.yoojia.imagemap.core.CircleShape;
import net.yoojia.imagemap.core.LineShape;
import net.yoojia.imagemap.core.PointShape;
import net.yoojia.imagemap.core.RoundShape;
import net.yoojia.imagemap.core.StartEndShape;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.hck.imagemap.view.CakeSurfaceView;
import com.hck.imagemap.view.CakeSurfaceView.RankType;
import com.hck.imagemap.view.CustomDialog;
import com.hck.imagemap.view.HorizontalProgressBar;

public class ThisAcitivity extends Activity
{

    private ImageMap1 map;
    private RelativeLayout layout;
    private RelativeLayout layoutTemp;
    private RequestQueue mRequestQueue;
    private List<Location> list;// 采样点集合
    private double[] vail_list;// 误差数组
    private double max_vail_list;// 最大误差
    private double static_accuracy;// 静态精度
    private double[] out_center_list;// 重心误差数组
    private double out_center;// 偏移重心
    private double offset;// 偏移量
    private double stability;// 稳定度
    private Boolean bAnimation;// 判断是否移动
    private SharedPreferences myPreferences;
    private SharedPreferences.Editor editor;
    private int progress;
    private Bundle bn;
    private Button testBtn;
    private int bmpWidth;
    private int bmpHeight;
//    private Bitmap bitmap;

    private Loction loction;
    // private Button debug;
    private LinearLayout btnStart;
    private LinearLayout btnEnd;
    private LinearLayout ll_tishi;
    private LinearLayout ll_tishi_down;
    private int startTag = 0;
    private int endTag = 0;
    private PointF dynicStart;
    private PointF dynicEnd;
    private Button btnStatic, btnDynamic;// 查看历史记录按钮
    private ImageView ivcenter;// 十字叉图标
    private int three = 0, tof = 0, fot = 0, ten = 0;
    private ArrayList<Float> arrayDistance = new ArrayList<Float>(0);
    private ArrayList<PointF> arrayStartEnd = new ArrayList<PointF>(0);
    private boolean boolStart = false;
    private float deviation = 0;
    private View viewStart;
    private View viewEnd;
    private boolean first = false;
    private TextView tv_position, this_title;
    private LinearLayout ll_dynic;
    private EditText et_start_x, et_start_y, et_end_x, et_end_y;
    private boolean isBack;
    private int debugPostionNum;
    private JSONObject jsonObject = null;
    private HorizontalProgressBar bar;
    private String jsb = null;
    private JSONArray array = new JSONArray();
    private EditText editTextx, editTexty;
    private TextView tv_locationX, tv_locationY;
    private NumberFormat numberFormat;
    private Floor currFloor;
    private UpLoad load;
    private Boolean growslocSwitch;
    private Boolean bGLFloorSwitch;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = myPreferences.edit();
        bAnimation = myPreferences.getBoolean("bAnimation", true);
        testNum = myPreferences.getInt("testNum", 10);
        setContentView(R.layout.activity_this_acitivity);
        first = true;
        loction = new Loction();
        load = new UpLoad(this);
        growslocSwitch = myPreferences.getBoolean("growslocSwitch", false);
        bGLFloorSwitch = myPreferences.getBoolean("growslocFloorSwitch", true);
        initViews();
        initOnclickListener();
        requestLocationTask();
//        staticSaveTestData("1", "1", "2015-01-01 22:33:44",
//                "2015-01-01 22:33:44", "1", "1", "1", "1", "1", "2", 1, 1, 1,
//                1, "2,3,4,3,2,3,2,3");
//
//          dynamicSaveTestData( "1", "1", "2015-01-01 22:33:44",
//          "2015-01-01 22:33:44", "1", "1", "1", 1, 1, 1, 1, "1,3,44,4,4,4");
    }

    /**
     * 初始化
     */
    private void initViews()
    {
        // dynicDB = new Dynic_DB(this);
        // dao = new MessageDao(this);
        viewStart = View.inflate(ThisAcitivity.this, R.layout.view_start, null);
        viewEnd = View.inflate(ThisAcitivity.this, R.layout.view_end, null);
        tv_position = (TextView) findViewById(R.id.tv_position);
        et_start_x = (EditText) findViewById(R.id.et_start_x);
        et_start_y = (EditText) findViewById(R.id.et_start_y);
        et_end_x = (EditText) findViewById(R.id.et_end_x);
        et_end_y = (EditText) findViewById(R.id.et_end_y);
        ll_dynic = (LinearLayout) findViewById(R.id.ll_dynic);
        layout = (RelativeLayout) findViewById(R.id.rl_map);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutTemp = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.image_map_item, null);
        layoutTemp.setLayoutParams(lp);
        list = new ArrayList<Location>();
        map = (ImageMap1) layoutTemp.findViewById(R.id.imagemap);
        bar = (HorizontalProgressBar) findViewById(R.id.progress);
        testBtn = (Button) findViewById(R.id.testing);
        tv_locationX = (TextView) findViewById(R.id.tvlocationX);
        tv_locationY = (TextView) findViewById(R.id.tvlocationY);
        // buttongetfloor = (Button) findViewById(R.id.tvfloor);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        mRequestQueue = Volley.newRequestQueue(this);
        layout.addView(layoutTemp);
        // debug = (Button) findViewById(R.id.debug);
        btnStart = (LinearLayout) findViewById(R.id.btnStart);
        btnEnd = (LinearLayout) findViewById(R.id.btnEnd);
        ll_tishi = (LinearLayout) findViewById(R.id.ll_tishi);
        ll_tishi_down = (LinearLayout) findViewById(R.id.ll_tishi_down);
//        System.out.println("跳啦跳啦");
        ivcenter = (ImageView) findViewById(R.id.ivcenter);
        isBack = false;
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
        bmpWidth = MapConstant.bitmap.getWidth();
        bmpHeight = MapConstant.bitmap.getHeight();
        map.setMapBitmap(MapConstant.bitmap);
        btnStatic = (Button) findViewById(R.id.static_debug);
        btnDynamic = (Button) findViewById(R.id.dynamic_debug);
        this_title = (TextView) findViewById(R.id.this_title);
        currFloor = (Floor) bn.getSerializable("currFloor");
    }

    /**
     * 事件监听
     */
    private void initOnclickListener()

    {
        map.setOnSingleClickListener(new OnSingleClickListener()
        {

            @Override
            public void onSingle(PointF p)
            {
                if (dynamicDebug)
                {
                    start = p;
                    PointShape dy = new PointShape("touch", Color.RED);
                    dy.setValues(String.format("%.5f:%.5f:16", p.x, p.y));
                    map.addShape(dy, false);
                }
            }
        });
        // 起点选择
        btnStart.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (start != null)
                {
                    startTag = 1;
                    map.removeShape("touch");
                    dynicStart = start;
                    // 画起点
                    PointShape cs = new PointShape("start", Color.GREEN);
                    cs.setValues(String.format("%.5f:%.5f:16", dynicStart.x,
                            dynicStart.y));
                    map.addShape(cs, false);
                    // 画终点
                    StartEndShape black = new StartEndShape("start1",
                            Color.RED, viewStart, ThisAcitivity.this);
                    black.setValues(String.format("%.5f:%.5f:50", dynicStart.x,
                            dynicStart.y));
                    map.addShape(black, false);
                    // 动态起点关联原点
                    et_start_x.setText(numberFormat.format(loction.location(
                            dynicStart.x / bn.getDouble("scale") * 10
                                    - bn.getDouble("xSport") * 10,
                            dynicStart.y / bn.getDouble("scale") * 10
                                    - bn.getDouble("ySport") * 10,
                            (Floor) bn.getSerializable("currFloor"))[0]
                            / bn.getDouble("scale") - bn.getDouble("xSport"))
                            + "");
                    et_start_y.setText(numberFormat.format(loction.location(
                            dynicStart.x / bn.getDouble("scale") * 10
                                    - bn.getDouble("xSport") * 10,
                            dynicStart.y / bn.getDouble("scale") * 10
                                    - bn.getDouble("ySport") * 10,
                            (Floor) bn.getSerializable("currFloor"))[1]
                            / bn.getDouble("scale") - bn.getDouble("ySport"))
                            + "");
                    if (startTag + endTag == 2)
                    {
                        startToEnd();
                    }
                } else
                {
                    Toast.makeText(ThisAcitivity.this,
                            getString(R.string.chosestart), Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // 终点选择
        btnEnd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (start != null)
                {
                    endTag = 1;
                    map.removeShape("touch");
                    dynicEnd = start;
                    PointShape dye = new PointShape("end", Color.RED);
                    dye.setValues(String.format("%.5f:%.5f:16", dynicEnd.x,
                            dynicEnd.y));
                    map.addShape(dye, false);
                    StartEndShape black = new StartEndShape("end1", Color.RED,
                            viewEnd, ThisAcitivity.this);
                    black.setValues(String.format("%.5f:%.5f:50", dynicEnd.x,
                            dynicEnd.y));
                    map.addShape(black, false);
                    et_end_x.setText(numberFormat.format(loction.location(
                            dynicEnd.x / bn.getDouble("scale") * 10
                                    - bn.getDouble("xSport") * 10,
                            dynicEnd.y / bn.getDouble("scale") * 10
                                    - bn.getDouble("ySport") * 10,
                            (Floor) bn.getSerializable("currFloor"))[0]
                            / bn.getDouble("scale") - bn.getDouble("xSport"))
                            + "");
                    et_end_y.setText(numberFormat.format(loction.location(
                            dynicEnd.x / bn.getDouble("scale") * 10
                                    - bn.getDouble("xSport") * 10,
                            dynicEnd.y / bn.getDouble("scale") * 10
                                    - bn.getDouble("ySport") * 10,
                            (Floor) bn.getSerializable("currFloor"))[1]
                            / bn.getDouble("scale") - bn.getDouble("ySport"))
                            + "");
                    if (startTag + endTag == 2)
                    {

                        startToEnd();
                    }
                } else
                {
                    Toast.makeText(ThisAcitivity.this,
                            getString(R.string.choseend), Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }

    private void cleanText()
    {
        et_start_x.setText("");
        et_start_y.setText("");
        et_end_x.setText("");
        et_end_y.setText("");
    }

    /**
     * 起始点直线绘制
     */
    private void startToEnd()
    {
        LineShape ls = new LineShape("Lin", Color.BLUE);
        Path path = new Path();
        // ls.setValues(String.format("%.5f:%.5f:%.5f,%.5f", dynicStart.x*1.0,
        // dynicStart.y*1.0, dynicEnd.x*1.0, dynicEnd.y*1.0));
        path.moveTo(dynicStart.x, dynicStart.y);
        path.lineTo(dynicEnd.x, dynicEnd.y);
        ls.setPath(path);
        map.addShape(ls, false);
    }

    /**
     * 线程循环
     */
    private void requestLocationTask()
    { // 延迟1s运行
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!isBack)
                {
                    mHandler.sendEmptyMessage(1);
                    if (isDebugRequestLocation)// 判断是否开启定位
                    {
                    	if (!growslocSwitch)
                        {
                            requestLocation();
                        } else
                        {
                            requestLocationCollect();
                        }
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
        params.put("ip", load.getLocaIpOrMac()/*"10.180.7.79"*/);
        params.put("isPush", "2");
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/getData", new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    { // 接口响应输出日支
                        Log.d("ThisAcitivity",
                                "requestLocation:111" + jsonobj.toString());
                        System.out.println("requestLocation:111"
                                + jsonobj.toString());
//                        Location l = new Location();
//                        l.setX(200);
//                        l.setY(500);
//                        addPostion(l, 10000);
                        requestContent(jsonobj,"");
                        
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
    private double[] asLocation = new double[2];
    private double[] oriLocation = new double[2];
    private double[] oldBeforeLocation = new double[2];
    private String floorNo;
    private void requestLocationCollect()
    {
        if (bGLFloorSwitch)
        {
            floorNo = currFloor.getFloorNo() + "";
        } else
        {
            floorNo = null;
        }
        Map<String, String> params = new HashMap<String, String>();
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/app/getData?userId="
                        + load.getLocaIpOrMac() + "&x=" + oriLocation[0]
                        + "&y=" + oriLocation[1] +"&x1="+asLocation[0]+"&y1="+asLocation[1]+ "&floorNo=" + floorNo,
                new JSONObject(params), new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        System.out.println("requestLocationCollect:111"
                                + jsonobj.toString());
                        String string = "collect";
                        requestContent(jsonobj, string);
                    }

                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("ThisAcitivity", "requestLocationCollect:", error);
                        Toast.makeText(ThisAcitivity.this, getString(R.string.locnoresponse), Toast.LENGTH_SHORT).show();
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
    private void requestContent(JSONObject jsonobj,String string) {
		Location loc = new Location();
        try
        {
            final JSONObject o = jsonobj.getJSONObject("data");
            loc.setX(o.getDouble("x"));
            loc.setY(o.getDouble("y"));
//            if (currFloor.getFloorNo() != o.getInt("z"))
//            {
//                requestLocationTask();
//                return;
//            }

            if (lastTime == o.getDouble("timestamp"))
            {
                requestLocationTask();
                return;
            }
            lastTime = o.getDouble("timestamp");
            
            if("collect".equals(string)){
            	if(o.getDouble("x") == oriLocation[0]&&o.getDouble("y") == oriLocation[1]){
            		asLocation[0] = oldBeforeLocation[0];
            		asLocation[1] = oldBeforeLocation[1];
//            		requestLocationTask();
            	}else{
            		asLocation[0] = 0;
            		asLocation[1] = 0;
            		oldBeforeLocation[0] = oriLocation[0];
            		oldBeforeLocation[1] = oriLocation[1];
            		oriLocation[0] = o.getDouble("x");
            		oriLocation[1] = o.getDouble("y");
            	}
            }else{
            	String jj = jsonobj.getString("paramUpdateTime");
             // 获取空口结果
                oriLocation[0] = o.getDouble("x");
                oriLocation[1] = o.getDouble("y");
            }
            addPostion(loc, lastTime);
        } catch (Exception e)
        {
            Log.e("error22", e + "");
        }
        requestLocationTask();
	}
    // private Button buttongetfloor;
    private double sumx = 0;
    private double sumy = 0;
    private String tR;
    private String tofR;
    private String fotR;
    private String tenR;
    private String tv_location_X;
    private String tv_location_Y;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
            case 0:
                break;
            case 1:
                PointF p = map.getCenterByImagePoint();
                double d[] = new double[2];
                d[0] = loction.location(
                        p.x / bn.getDouble("scale") * 10
                                - bn.getDouble("xSport") * 10,
                        p.y / bn.getDouble("scale") * 10
                                - bn.getDouble("ySport") * 10,
                        (Floor) bn.getSerializable("currFloor"))[0];
                d[1] = loction.location(
                        p.x / bn.getDouble("scale") * 10
                                - bn.getDouble("xSport") * 10,
                        p.y / bn.getDouble("scale") * 10
                                - bn.getDouble("ySport") * 10,
                        (Floor) bn.getSerializable("currFloor"))[1];
                tv_location_X = numberFormat.format(d[0]
                        / bn.getDouble("scale") - bn.getDouble("xSport"));
                tv_location_Y = numberFormat.format(d[1]
                        / bn.getDouble("scale") - bn.getDouble("ySport"));
                tv_locationX.setText("X:" + tv_location_X);
                tv_locationY.setText("Y:" + tv_location_Y);
                // System.out.println(p.x + "  " + bn.getDouble("xSport") + "  "
                // + bn.getDouble("scale"));
                // System.out.println(p.x / bn.getDouble("scale") * 10);
                /*
                 * tv_location.setText("x:" + numberFormat.format((p.x -
                 * bn.getDouble("xSport") bn.getDouble("scale")) /
                 * bn.getDouble("scale")) + "\n" + "y:" +
                 * numberFormat.format((p.y - bn.getDouble("ySport")
                 * bn.getDouble("scale")) / bn.getDouble("scale")));
                 */
                // buttongetfloor.setText(bn.getString("floor"));
                break;
            case 0x111:
                progress = bar.getProgress();
                if (debugPostionNum == testNum)
                {
                    bar.setProgress(100);
                    getEstimate();
                }
                break;
            default:
                break;
            }
        }

    };

    private void getEstimate()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("place", bn.getString("place"));
        params.put("floorNo", bn.getInt("floorNo") + "");
        JsonObjectRequest newMissRequest = null;
        try
        {
            newMissRequest = new JsonObjectRequest(Request.Method.POST,
                    "http://" + GlobalConfig.server_ip
                            + "/sva/api/getEstimate?place="
                            + URLEncoder.encode(bn.getString("place"), "UTF-8")
                            + "&floorNo=" + bn.getInt("floorNo"),
                    new JSONObject(params), new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject jsonobj)
                        {
                            Log.d("ThisAcitivity",
                                    "getEstimate:" + jsonobj.toString());
                            System.out.println("getEstimate:"
                                    + jsonobj.toString());
                            System.out.println("dynic=:" + dynic);
                            System.out.println("statics=:" + statics);
                            try
                            {
                                jsb = jsonobj.getString("data");
                            } catch (Exception e)
                            {
                                Log.e("error", e + "");
                            }
                            if (jsb != null && !"".equals(jsb))
                            {
                                if (dynic)
                                {
                                    CaculateTestData();
                                }
                                if (statics)
                                {
                                    getEstimateFinish();
                                }
                            }
                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.e("ThisAcitivity", "getEstimate:", error);
                            jsb = "5";
                            if (dynic)
                            {
                                CaculateTestData();
                            }
                            if (statics)
                            {
                                getEstimateFinish();
                            }
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders()
                {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type",
                            "application/json; charset=UTF-8");
                    return headers;
                }
            };
        } catch (UnsupportedEncodingException e)
        {
            Log.e("ThisAcitivity", "getEstimate error:", e);
        }
        mRequestQueue.add(newMissRequest);
    }

    private void clearMessage()
    {
        if (debugPostionNum != 0)
        {
            for (int i = 0; i < debugPostionNum; i++)
            {
                map.removeShape("debugNO" + i);
            }
            debugPostionNum = 0;
        }

        if (iNum != 0)
        {
            for (int i = 0; i < iNum; i++)
            {
                map.removeShape("debugNO" + i);
            }
            iNum = 0;
        }
        map.removeShape("ownLoad");
        map.removeShape("rs_accuracy");
        map.removeShape("rs_stability");
        map.removeShape("cs");
    }

    private double nums;
    private float aver;

//    private void textDemo()
//    {
//        for (int i = 0; i < 10; i++)
//        {
//            double xx = Math.random() * 300 + 300;
//            double yy = Math.random() * 300 + 400;
//            Location loc = new Location();
//            loc.setX(xx);
//            loc.setY(yy);
//            addPostion(loc, 123456789);
//        }
//    }

    /**
     * 计算静态
     */
    private void getEstimateFinish()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss",Locale.getDefault());
        end_date = dateFormat.format(new Date());
        // end_date = Utils.getCurrentDateStr();
        mHandler.removeMessages(0x111);
        progress = 0;
        bar.setProgress(0);
        isDebugRequestLocation = false;
        isBack = false;
        // testBtn.setTextColor(Color.WHITE);
        testBtn.setText(getString(R.string.configPosition));
        bar.setVisibility(View.INVISIBLE);
        tv_position.setVisibility(View.INVISIBLE);
        // clearMessage();
        sumx = 0;
        sumy = 0;
        nums = 0;
        double result = 0;
        Loction loction = new Loction();
        out_center_list = new double[list.size()];
        vail_list = new double[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            Location location = list.get(i);
            sumx = sumx
                    + loction.location(location.getX(), location.getY(),
                            (Floor) bn.getSerializable("currFloor"))[0]
                    / bn.getDouble("scale");
            sumy = sumy
                    + loction.location(location.getX(), location.getY(),
                            (Floor) bn.getSerializable("currFloor"))[1]
                    / bn.getDouble("scale");
            result = Math
                    .sqrt(((loction.location(location.getX(), location.getY(),
                            (Floor) bn.getSerializable("currFloor"))[0]
                            / bn.getDouble("scale") - loction.location(
                            Float.parseFloat(getStrx) * 10,
                            Float.parseFloat(getStry) * 10,
                            (Floor) bn.getSerializable("currFloor"))[0]
                            / bn.getDouble("scale"))
                            * (loction.location(location.getX(),
                                    location.getY(),
                                    (Floor) bn.getSerializable("currFloor"))[0]
                                    / bn.getDouble("scale") - loction.location(
                                    Float.parseFloat(getStrx) * 10,
                                    Float.parseFloat(getStry) * 10,
                                    (Floor) bn.getSerializable("currFloor"))[0]
                                    / bn.getDouble("scale")) + (loction
                            .location(location.getX(), location.getY(),
                                    (Floor) bn.getSerializable("currFloor"))[1]
                            / bn.getDouble("scale") - loction.location(
                            Float.parseFloat(getStrx) * 10,
                            Float.parseFloat(getStry) * 10,
                            (Floor) bn.getSerializable("currFloor"))[1]
                            / bn.getDouble("scale"))
                            * (loction.location(location.getX(),
                                    location.getY(),
                                    (Floor) bn.getSerializable("currFloor"))[1]
                                    / bn.getDouble("scale") - loction.location(
                                    Float.parseFloat(getStrx) * 10,
                                    Float.parseFloat(getStry) * 10,
                                    (Floor) bn.getSerializable("currFloor"))[1]
                                    / bn.getDouble("scale"))));
            vail_list[i] = result;

            if (result < 3)
            {
                T++;
            } else if (result >= 3 && result < 5)
            {
                TOF++;
            } else if (result >= 5 && result < 10)
            {
                FOT++;
            } else
            {
                Ten++;
            }
            nums = nums + result;
        }
        Arrays.sort(vail_list);
        max_vail_list = vail_list[list.size() - 1];
        int index = list.size() * 2 / 3;
        static_accuracy = vail_list[index - 1];
        aver = Float.parseFloat(numberFormat.format(nums / list.size()));// 平均误差
        for (int j = 0; j < list.size(); j++)
        {
            Location location = list.get(j);
            double distance = Math
                    .sqrt(((loction.location(location.getX(), location.getY(),
                            (Floor) bn.getSerializable("currFloor"))[0]
                            / bn.getDouble("scale") - sumx / list.size())
                            * (loction.location(location.getX(),
                                    location.getY(),
                                    (Floor) bn.getSerializable("currFloor"))[0]
                                    / bn.getDouble("scale") - sumx
                                    / list.size()) + (loction.location(
                            location.getX(), location.getY(),
                            (Floor) bn.getSerializable("currFloor"))[1]
                            / bn.getDouble("scale") - sumy / list.size())
                            * (loction.location(location.getX(),
                                    location.getY(),
                                    (Floor) bn.getSerializable("currFloor"))[1]
                                    / bn.getDouble("scale") - sumy
                                    / list.size())));
            out_center_list[j] = distance;
        }
        Arrays.sort(out_center_list);
        offset = out_center_list[list.size() - 1];
        int center_index = list.size() * 9 / 10;
        stability = out_center_list[center_index - 1];
        out_center = Math
                .sqrt(((sumx / list.size() - loction.location(
                        Float.parseFloat(getStrx) * 10,
                        Float.parseFloat(getStry) * 10,
                        (Floor) bn.getSerializable("currFloor"))[0]
                        / bn.getDouble("scale")) * (sumx / list.size() - loction
                        .location(Float.parseFloat(getStrx) * 10,
                                Float.parseFloat(getStry) * 10,
                                (Floor) bn.getSerializable("currFloor"))[0]
                        / bn.getDouble("scale")))
                        + ((sumy / list.size() - loction.location(
                                Float.parseFloat(getStrx) * 10,
                                Float.parseFloat(getStry) * 10,
                                (Floor) bn.getSerializable("currFloor"))[1]
                                / bn.getDouble("scale")) * (sumy / list.size() - loction.location(
                                Float.parseFloat(getStrx) * 10,
                                Float.parseFloat(getStry) * 10,
                                (Floor) bn.getSerializable("currFloor"))[1]
                                / bn.getDouble("scale"))));
        // Intent intent = new Intent(this, ResultAcitivity.class);
        // Bundle bundle = new Bundle();
        // tR = numberFormat.format((double) T / (double) list.size());
        // tofR = numberFormat.format((double) TOF / (double) list.size());
        // fotR = numberFormat.format((double) FOT / (double) list.size());
        // tenR = numberFormat.format((double) Ten / (double) list.size());
        // if (list.size() > 0)
        // {
        // bundle.putString("type", "0");
        // bundle.putString("ygpc", jsb);
        // bundle.putString("wucha", "" + numberFormat.format(wucha));
        // bundle.putString("num", "" + list.size());
        // bundle.putString("t", Double.parseDouble(tR) * 100 + "");
        // bundle.putString("tof", Double.parseDouble(tofR) * 100 + "");
        // bundle.putString("fot", Double.parseDouble(fotR) * 100 + "");
        // bundle.putString("ten", Double.parseDouble(tenR) * 100 + "");
        // bundle.putString("aver", aver + "");
        // }
        // bundle.putString("yesNo", "yes");
        // intent.putExtras(bundle);
        // startActivityForResult(intent, 1);
        // SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss",
        // Locale.getDefault());
        // time = sdf.format(new Date());
        // 判断为24小时还是12小时
        // ContentResolver cv = this.getContentResolver();
        // String strTimeFormat = android.provider.Settings.System.getString(cv,
        // android.provider.Settings.System.TIME_12_24);
        // if (strTimeFormat.equals("24"))
        // {
        // Log.i("tag", "当前时间是24小时制");
        // }
        CustomDialog.Builder builder = new CustomDialog.Builder(
                ThisAcitivity.this);
        View v = View.inflate(ThisAcitivity.this, R.layout.aci_text, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_aci_text);
        builder.setTitle(getString(R.string.test_results));
        textView.setText(getString(R.string.ygpc) + ":  " + jsb + " "
                + getString(R.string.mi) + "\n\n" + getString(R.string.results)
                + ":  " + aver + " " + getString(R.string.mi) + "\n\n"
                + getString(R.string.max_vail_list) + ":  "
                + numberFormat.format(max_vail_list) + " "
                + getString(R.string.mi) + "\n\n"
                + getString(R.string.static_accuracy) + ":  "
                + numberFormat.format(static_accuracy) + " "
                + getString(R.string.mi) + "\n\n"
                + getString(R.string.out_center) + ":  "
                + numberFormat.format(out_center) + " "
                + getString(R.string.mi) + "\n\n" + getString(R.string.offset)
                + ":  " + numberFormat.format(offset) + " "
                + getString(R.string.mi) + "\n\n"
                + getString(R.string.stability) + ":  "
                + numberFormat.format(stability) + " " + getString(R.string.mi));
        builder.setContentView(v);
        builder.setPositiveButton(getString(R.string.saves),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        staticSaveTestData("(" + getStrx + "," + getStry + ")",
                                "", start_date, end_date, String.valueOf(aver),
                                numberFormat.format(max_vail_list),
                                numberFormat.format(static_accuracy),
                                numberFormat.format(out_center),
                                numberFormat.format(offset),
                                numberFormat.format(stability), T, TOF, FOT,
                                Ten, array.toString());
                        // saveTestData("(" + getStrx + "," + getStry + ")", "",
                        // String.valueOf(aver), String.valueOf(aver),
                        // String.valueOf(T), String.valueOf(TOF),
                        // String.valueOf(FOT), String.valueOf(Ten),
                        // array.toString(), String.valueOf(aver), "0");
                        array = null;
                        array = new JSONArray();
                        arg0.dismiss();
                    }
                });
        builder.setNegativeButton(getString(R.string.out),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.dismiss();
                    }
                });
        builder.create().show();
        double xx = loction.location(Double.parseDouble(getStrx) * 10,
                Double.parseDouble(getStry) * 10,
                (Floor) bn.getSerializable("currFloor"))[0];
        double yy = loction.location(Double.parseDouble(getStrx) * 10,
                Double.parseDouble(getStry) * 10,
                (Floor) bn.getSerializable("currFloor"))[1];
        RoundShape roundShape_accuracy = new RoundShape("rs_accuracy",
                Color.BLUE);
        roundShape_accuracy.setValues(String.format("%.5f:%.5f:"
                + static_accuracy * bn.getDouble("scale"), xx, yy));
        map.addShape(roundShape_accuracy, false);

        CircleShape postion = new CircleShape("cs", Color.BLACK);
        postion.setValues(String.format("%.5f:%.5f:15",
                sumx / list.size() * bn.getDouble("scale"), sumy / list.size()
                        * bn.getDouble("scale")));
        map.addShape(postion, false);

        RoundShape roundShape_stability = new RoundShape("rs_stability",
                Color.BLACK);
        roundShape_stability.setValues(String.format("%.5f:%.5f:" + stability
                * bn.getDouble("scale"),
                sumx / list.size() * bn.getDouble("scale"), sumy / list.size()
                        * bn.getDouble("scale")));
        map.addShape(roundShape_stability, false);
        String configPosition = getString(R.string.configPosition).toString();
        testBtn.setText(configPosition);
    }

    /**
     * 测试点，用于测试
     */
    // private void showPosition()
    // {
    // for (int i = 0; i < 5; i++)
    // {
    // double xx = Math.random() * 200 + 300;
    // double yy = Math.random() * 200 + 300;
    // CircleShape postion = new CircleShape("o" + i, Color.RED);
    // postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
    // map.addShape(postion, bAnimation);
    // // 像素转成米
    // xx = xx / bn.getDouble("scale");
    // yy = yy / bn.getDouble("scale");
    // startPoint = new PointF((float) xx, (float) yy);
    // arrayStartEnd.add(startPoint);
    // clearMessage();
    // }
    // }

    private int iNum;
    private boolean staticMode = false;
    private PointF startPoint;

    private void addPostion(Location loc, double timestamp)
    {
        Log.i("getx", loc.getX() + "");
        Log.i("gety", loc.getY() + "");
        Loction loction = new Loction();
        double xx = loction.location(loc.getX(), loc.getY(),
                (Floor) bn.getSerializable("currFloor"))[0];
        double yy = loction.location(loc.getX(), loc.getY(),
                (Floor) bn.getSerializable("currFloor"))[1];
        // xx = loc.getX() / 10 * bn.getDouble("scale");
        // yy = loc.getY() / 10 * bn.getDouble("scale");
        tv_position.setVisibility(View.VISIBLE);
        tv_position.setText(numberFormat.format(loc.getX() / 10) + " , "
                + numberFormat.format(loc.getY() / 10));
        if (boolStart)
        { // 当为动态时
            startPoint = new PointF((float) xx, (float) yy);
            arrayStartEnd.add(startPoint);
            CircleShape postion = new CircleShape("debugNO" + iNum, Color.RED);
            postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
            map.addShape(postion, bAnimation);
            jsonObject = new JSONObject();
            try
            {
                jsonObject.put("y", loc.getY() / 10);
                jsonObject.put("x", loc.getX() / 10);
                jsonObject.put("timestamp", timestamp);
            } catch (Exception e)
            {
                Log.e("13error", e + "");
            }
            array.put(jsonObject);
            jsonObject = null;
            iNum++;
        }
        if (staticMode)
        {// 当为静态时
            if (debugPostionNum < testNum)
            {
                list.add(loc);
                CircleShape postion = new CircleShape("debugNO"
                        + debugPostionNum, Color.RED);
                postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
                map.addShape(postion, bAnimation);
                jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("y", loc.getY() / 10);
                    jsonObject.put("x", loc.getX() / 10);
                    jsonObject.put("timestamp", timestamp);
                } catch (Exception e)
                {
                    Log.e("error", e + "");
                }
                array.put(jsonObject);
                jsonObject = null;
                debugPostionNum++;
                bar.setProgress(100 / testNum + progress);
                mHandler.sendEmptyMessage(0x111);
            }
        }
    }

    public String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress
                                    .getHostAddress()))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex)
        {
            Log.e("ThisAcitivity", ex.toString());
        }
        return null;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mRequestQueue.cancelAll(this);
        first = false;
        isBack = true;
    }

    @Override
    protected void onResume()
    {
        Log.i("first", first + "");
        super.onResume();
        if (!first)
        {
            isBack = false;
            requestLocationTask();
        }
    }

    private String getStrx;
    private String getStry;
    private String start_date;
    private String end_date;
    private int T = 0, TOF = 0, FOT = 0, Ten = 0;
    // private double wucha = 0;
    private boolean isDebugRequestLocation = false;// 用于判断是否打开定位开关

    // public void findClick(View view)
    // {
    // if (isDebugRequestLocation)
    // {
    // Toast.makeText(this, getString(R.string.warm), Toast.LENGTH_SHORT)
    // .show();
    // return;
    // } else
    // {
    // String tag = myPreferences.getString("testTag", "静态");
    // if (tag.equals("静态"))
    // {
    // Intent intent = new Intent(this, HistoryActivity.class);
    // startActivity(intent);
    // } else
    // {
    // Intent intent = new Intent(this, DynicHistory.class);
    // startActivity(intent);
    // }
    // }
    // }

    private boolean statics;
    private boolean dynic;
    private int testNum;

    /**
     * 静态测试
     * 
     * @param v
     */
    public void static_Click(View v)
    {
        if (getString(R.string.set_static).equals(btnStatic.getText()))
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(
                    ThisAcitivity.this);
            View vv = View.inflate(ThisAcitivity.this, R.layout.edit_text_aci,
                    null);
            final EditText editText = (EditText) vv
                    .findViewById(R.id.edit_text);
            editText.setText(testNum + "");
            builder.setTitle(getString(R.string.samples));
            builder.setContentView(vv);
            builder.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            editor.putInt(
                                    "testNum",
                                    Integer.parseInt(editText.getText()
                                            .toString().trim()));
                            testNum = Integer.parseInt(editText.getText()
                                    .toString().trim());
                            arg0.dismiss();
                        }
                    });
            builder.setNegativeButton(getString(R.string.no),
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            arg0.dismiss();
                        }
                    });
            builder.create().show();
            ll_tishi.setVisibility(View.VISIBLE);
            ll_tishi_down.setVisibility(View.VISIBLE);
            if (!isDebugRequestLocation)// 当定位开关打开的时候
            {
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putString("testTag", "静态");
                editor.commit();
                statics = true;
                successDebug();
            } else
            {
                String closePositionInfo = getString(R.string.closePositionInfo)
                        .toString();
                Toast.makeText(ThisAcitivity.this, closePositionInfo,
                        Toast.LENGTH_SHORT).show();
            }
        } else
        {
            closeDebug();
        }
    }

    /**
     * 动态测试
     * 
     * @param v
     */
    public void dynamic_Click(View v)
    {
        if (getString(R.string.set_dynamic).equals(btnDynamic.getText()))
        {
            if (!isDebugRequestLocation)
            {
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putString("testTag", "动态");
                editor.commit();
                dynamicDebug();
                dynic = true;
            } else
            {
                String closePositionInfo = getString(R.string.closePositionInfo)
                        .toString();
                Toast.makeText(ThisAcitivity.this, closePositionInfo,
                        Toast.LENGTH_SHORT).show();
            }
        } else
        {
            closeDebug();
        }
    }

    /**
     * 开始测试
     * 
     * @param view
     */
    // private List<Double[]> dblist=new ArrayList<Double[]>();
    // private double[] dbnum=new double[2];
    public void testClick(View view)
    {
        if (getString(R.string.dynicStart).equals(testBtn.getText()))
        {
            if (startTag + endTag == 2)
            {
                // showPosition();
                if (dynicStart.x == dynicEnd.x && dynicEnd.y == dynicStart.y)
                {
                    Toast.makeText(this, getString(R.string.wrongPoint),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                map.removeShape("touch");
                three = 0;
                tof = 0;
                fot = 0;
                ten = 0;
                // wucha = 0;
                sum = 0;
                isDebugRequestLocation = true;
                arrayDistance.clear();
                testBtn.setText(getString(R.string.dynicEnd));
                dynamicDebug = false;
                arrayStartEnd.clear();
                // ll_dynic.setVisibility(View.GONE);
                boolStart = true;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yy-MM-dd HH-mm-ss",Locale.getDefault());
                start_date = dateFormat.format(new Date());
                // start_date = Utils.getCurrentDateStr();
                // showPosition();
            } else
            {
                Toast.makeText(this, getString(R.string.choseposition),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (getString(R.string.dynicEnd).equals(testBtn.getText()))
        {
            tv_position.setVisibility(View.INVISIBLE);
            if (arrayStartEnd.size() > 0)
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yy-MM-dd HH-mm-ss",Locale.getDefault());
                end_date = dateFormat.format(new Date());
                // end_date = Utils.getCurrentDateStr();
                getEstimate();
                Log.i("array:", array.toString());
                closeTest();
//                cleanText();
                testBtn.setText(getString(R.string.dynicStart));
            } else
            {
                closeTest();
                cleanText();
                testBtn.setText(getString(R.string.dynicStart));
                Toast.makeText(this, getString(R.string.faild),
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (getString(R.string.configPosition).equals(testBtn.getText()))
        {
            View views = View.inflate(ThisAcitivity.this,
                    R.layout.positive_view, null);
            editTextx = (EditText) views.findViewById(R.id.etcomx);
            editTexty = (EditText) views.findViewById(R.id.etcomy);
            TextWatcher text = new TextWatcher()
            {
                public void afterTextChanged(Editable edt)
                {
                    String temp = edt.toString();
                    int posDot = temp.indexOf(".");
                    if (posDot <= 0)
                        return;
                    if (temp.length() - posDot - 1 > 2)
                    {
                        edt.delete(posDot + 3, posDot + 4);
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1,
                        int arg2, int arg3)
                {
                }

                public void onTextChanged(CharSequence arg0, int arg1,
                        int arg2, int arg3)
                {
                }
            };
            editTexty.addTextChangedListener(text);
            editTextx.addTextChangedListener(text);
            editTextx.setText(tv_location_X);
            editTexty.setText(tv_location_Y);
            CustomDialog.Builder builder = new CustomDialog.Builder(
                    ThisAcitivity.this);
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
                            clearMessage();
                            getStrx = editTextx.getText().toString().trim();
                            getStry = editTexty.getText().toString().trim();
                            if ("".equals(getStrx) || "".equals(getStry))
                            {
                                System.out.println("isin1");
                                mHandler.sendEmptyMessage(0);
                            } else if (Float.parseFloat(getStrx)
                                    * bn.getDouble("scale")
                                    + bn.getDouble("xSport")
                                    * bn.getDouble("scale") > bmpWidth
                                    || Float.parseFloat(getStry)
                                            * bn.getDouble("scale")
                                            + bn.getDouble("ySport")
                                            * bn.getDouble("scale") > bmpHeight
                                    || Float.parseFloat(getStry)
                                            * bn.getDouble("scale")
                                            + bn.getDouble("ySport")
                                            * bn.getDouble("scale") < 0
                                    || Float.parseFloat(getStrx)
                                            * bn.getDouble("scale")
                                            + bn.getDouble("xSport")
                                            * bn.getDouble("scale") < 0)
                            {
                                System.out.println("isin2");
                                Toast.makeText(ThisAcitivity.this,
                                        getString(R.string.notAt),
                                        Toast.LENGTH_SHORT).show();
                            } else
                            {
                                System.out.println("isin3");
                                Loction loction = new Loction();
                                String startTest = getString(R.string.startTest)
                                        .toString();
                                testBtn.setText(startTest);
                                CircleShape circleShape = new CircleShape(
                                        "ownLoad", Color.BLUE);
                                circleShape.setValues(String.format(
                                        "%.5f:%.5f:20",
                                        loction.location(
                                                Float.parseFloat(getStrx) * 10,
                                                Float.parseFloat(getStry) * 10,
                                                (Floor) bn
                                                        .getSerializable("currFloor"))[0],
                                        loction.location(
                                                Float.parseFloat(getStrx) * 10,
                                                Float.parseFloat(getStry) * 10,
                                                (Floor) bn
                                                        .getSerializable("currFloor"))[1]));
                                System.out.println("isout");
                                map.addShape(circleShape, true); // 加到地图上

                            }
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
        } else if (getString(R.string.startTest).equals(testBtn.getText()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yy-MM-dd HH-mm-ss",Locale.getDefault());
            start_date = dateFormat.format(new Date());
            // start_date = Utils.getCurrentDateStr();
            T = 0;
            TOF = 0;
            FOT = 0;
            Ten = 0;
            // wucha = 0;
            sum = 0;
            list.clear();
            String endTest = getString(R.string.endTest).toString();
            testBtn.setText(endTest);
            // testBtn.setTextColor(Color.GRAY);
            isDebugRequestLocation = true;
            staticMode = true;
            bar.setVisibility(View.VISIBLE);
            array = new JSONArray();
            mHandler.sendEmptyMessage(0x111);
        }
    }

    private boolean dynamicDebug = false;

    /**
     * 动态测试显示
     */
    private void dynamicDebug()
    {
        dynamicDebug = true;
        btnStatic.setText(getString(R.string.closeDebug));
        ll_dynic.setVisibility(View.VISIBLE);
        testBtn.setVisibility(View.VISIBLE);
        testBtn.setText(getString(R.string.dynicStart));
        btnDynamic.setVisibility(View.GONE);
        this_title.setText(getString(R.string.set_dynamic));
    }

    /**
     * 静态测试时显示
     */
    private void successDebug()
    {
        String configPosition = getString(R.string.configPosition).toString();
        this_title.setText(getString(R.string.set_static));
        testBtn.setVisibility(View.VISIBLE);
        testBtn.setText(configPosition);
        btnStatic.setText(getString(R.string.closeDebug));
        tv_locationX.setVisibility(View.VISIBLE);
        tv_locationY.setVisibility(View.VISIBLE);
        // buttongetfloor.setVisibility(View.VISIBLE);
        ivcenter.setVisibility(View.VISIBLE);
        btnDynamic.setVisibility(View.GONE);
    }

    /**
     * 关闭测试
     */
    public void closeDebug()
    {
        testBtn.setVisibility(View.INVISIBLE);
        btnDynamic.setText(getString(R.string.set_dynamic));
        this_title.setText(getString(R.string.test));
        btnDynamic.setVisibility(View.VISIBLE);
        btnStatic.setText(R.string.set_static);
        ll_dynic.setVisibility(View.GONE);
        statics = false;
        dynic = false;
        closeTest();
        cleanText();
        dynamicDebug = false;
    }

    /**
     * 结束测试
     */
    private void closeTest()
    {
        staticMode = false;
        isDebugRequestLocation = false;
        startTag = 0;
        start = null;
        dynamicDebug = true;
        ivcenter.setVisibility(View.GONE);
        ll_tishi.setVisibility(View.GONE);
        ll_tishi_down.setVisibility(View.GONE);
        tv_position.setVisibility(View.INVISIBLE);
        endTag = 0;
        boolStart = false;
        // testBtn.setTextColor(Color.WHITE);
        bar.setVisibility(View.INVISIBLE);
        tv_locationX.setVisibility(View.INVISIBLE);
        tv_locationY.setVisibility(View.INVISIBLE);
        // buttongetfloor.setVisibility(View.INVISIBLE);
        mHandler.removeMessages(0x111);
        bar.setProgress(0);
        clearMessage();
        map.removeShape("start");
        map.removeShape("start1");
        map.removeShape("end");
        map.removeShape("end1");
        map.removeShape("touch");
        map.removeShape("Lin");
        map.removeShape("ownLoad");
    }

    private float s = 0;
    private float sum = 0;
    private float startEnd = 0;
    private float distance = 0;

    /**
     * 已知三点坐标算点到直接距离
     */
    private void CaculateTestData()
    {
        /*
         * dynicStart = new
         * PointF(Float.parseFloat(String.valueOf(loction.location(dynicStart.x
         * / bn.getDouble("scale") * 10, dynicStart.y / bn.getDouble("scale") *
         * 10, (Floor) bn.getSerializable("currFloor"))[0]) ),
         * Float.parseFloat(String.valueOf(loction.location( dynicStart.x /
         * bn.getDouble("scale") * 10, dynicStart.y / bn.getDouble("scale") *
         * 10, (Floor) bn.getSerializable("currFloor"))[1]) ));
         * Log.i("dynicStart", dynicStart + ""); dynicEnd = new
         * PointF(Float.parseFloat(String.valueOf(loction.location(dynicEnd.x /
         * bn.getDouble("scale") * 10, dynicEnd.y / bn.getDouble("scale") * 10,
         * (Floor) bn.getSerializable("currFloor"))[0]) ),
         * Float.parseFloat(String.valueOf(loction.location( dynicEnd.x /
         * bn.getDouble("scale") * 10, dynicEnd.y / bn.getDouble("scale") * 10,
         * (Floor) bn.getSerializable("currFloor"))[1]) ));
         */
        startEnd = (float) Math.sqrt((dynicStart.x - dynicEnd.x)
                * (dynicStart.x - dynicEnd.x) + (dynicStart.y - dynicEnd.y)
                * (dynicStart.y - dynicEnd.y));
        for (int i = 0; i < arrayStartEnd.size(); i++)
        {
            s = (float) CaculateArea(arrayStartEnd.get(i));// 三角形面积
            distance = (float) ((float) 2 * s / startEnd / bn// 定位点到线的距离
                    .getDouble("scale"));
            arrayDistance.add(distance);
            if (distance < 3)
            {
                three++;
            } else if (distance >= 3 && distance <= 5)
            {
                tof++;
            } else if (distance > 5 && distance <= 10)
            {
                fot++;
            } else if (distance > 10)
            {
                ten++;
            }
        }
        Log.i("three", three + "--" + tof + "--" + fot + "--" + ten);
        double[] ds = new double[arrayDistance.size()];
        for (int i2 = 0; i2 < arrayDistance.size(); i2++)
        {
            sum += arrayDistance.get(i2);
            ds[i2] = arrayDistance.get(i2);
        }
        Arrays.sort(ds);
        final double max_distance = ds[arrayDistance.size() - 1];
        Log.i("sum", "" + sum);
        deviation = sum / arrayDistance.size();
        deviation = Float.parseFloat(numberFormat.format(deviation));
        int index = arrayDistance.size() * 2 / 3;
        final double dy_error = ds[index];
        CustomDialog.Builder builder = new CustomDialog.Builder(
                ThisAcitivity.this);
        View v = View.inflate(ThisAcitivity.this, R.layout.jingdu_result, null);
        TextView textView = (TextView) v.findViewById(R.id.tvresult);
        CakeSurfaceView cakeSurfaceView = (CakeSurfaceView) v
                .findViewById(R.id.cake);
        builder.setTitle(getString(R.string.test_results));
        textView.setText(getString(R.string.ygpc) + ":  " + jsb + " "
                + getString(R.string.mi) + "\n\n" + getString(R.string.results)
                + ":  " + deviation + " " + getString(R.string.mi) + "\n\n"
                + getString(R.string.max_vail_list) + ":  "
                + numberFormat.format(max_distance) + " "
                + getString(R.string.mi) + "\n\n"
                + getString(R.string.dy_error) + ":  "
                + numberFormat.format(dy_error) + " " + getString(R.string.mi)
                + "\n\n" + getString(R.string.num) + ":  "
                + arrayDistance.size());
        String meters = getString(R.string.mi).toString();
        String totalNum = getString(R.string.totalNum).toString();
        List<CakeSurfaceView.CakeValue> cakeValues2 = new ArrayList<CakeSurfaceView.CakeValue>();
        cakeValues2
                .add(new CakeSurfaceView.CakeValue("0-3" + meters,
                        (float) three / arrayDistance.size() * 100, "0-3"
                                + totalNum
                                + ": "
                                + new BigDecimal((three / 100.0 * arrayDistance
                                        .size())).setScale(0,
                                        BigDecimal.ROUND_HALF_UP)));
        cakeValues2.add(new CakeSurfaceView.CakeValue("3-5" + meters,
                (float) tof / arrayDistance.size() * 100, "3-5"
                        + totalNum
                        + ": "
                        + new BigDecimal((tof / 100.0 * arrayDistance.size()))
                                .setScale(0, BigDecimal.ROUND_HALF_UP)));
        cakeValues2.add(new CakeSurfaceView.CakeValue("5-10" + meters,
                (float) fot / arrayDistance.size() * 100, "5-10"
                        + totalNum
                        + ": "
                        + new BigDecimal((fot / 100.0 * arrayDistance.size()))
                                .setScale(0, BigDecimal.ROUND_HALF_UP)));
        cakeValues2.add(new CakeSurfaceView.CakeValue("10" + meters,
                (float) ten / arrayDistance.size() * 100, "10"
                        + totalNum
                        + ": "
                        + new BigDecimal((ten / 100.0 * arrayDistance.size()))
                                .setScale(0, BigDecimal.ROUND_HALF_UP)));
        cakeSurfaceView.setData(cakeValues2);
        cakeSurfaceView.setDetailTopSpacing(15);
        cakeSurfaceView.setDetailLeftSpacing(6);
        cakeSurfaceView.setRankType(RankType.RANK_BY_ROW);
        builder.setContentView(v);
        builder.setPositiveButton(getString(R.string.saves),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        dynamicSaveTestData(
                                "("
                                        + et_start_x.getText()
                                        + ","
                                        + et_start_y.getText() + ")",
                                "("
                                        + et_end_x.getText()
                                        + ","
                                        + et_end_y.getText() + ")",
                                start_date, end_date,
                                String.valueOf(deviation),
                                numberFormat.format(max_distance),
                                numberFormat.format(dy_error), three, tof, fot,
                                ten, array.toString());
                        // saveTestData("(" + getStrx + "," + getStry + ")", "",
                        // String.valueOf(aver), String.valueOf(aver),
                        // String.valueOf(T), String.valueOf(TOF),
                        // String.valueOf(FOT), String.valueOf(Ten),
                        // array.toString(), String.valueOf(aver), "0");
                        array = null;
                        array = new JSONArray();
                        arg0.dismiss();
                    }
                });
        builder.setNegativeButton(getString(R.string.out),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.dismiss();
                    }
                });
        builder.create().show();
        // JumpToShow();
    }

    /**
     * 任意三点面积计算
     * 
     * @param point
     * @return
     */
    public double CaculateArea(PointF point)
    {
        PointF p1;
        PointF p2;
        PointF p3;
        p1 = dynicStart;
        p2 = dynicEnd;
        p3 = point;
        Log.i("p1", p1 + "");
        Log.i("p2", p2 + "");
        Log.i("p3", p3 + "");
        double x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y, x3 = p3.x, y3 = p3.y;
        double s1 = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        double s2 = Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3));
        double s3 = Math.sqrt((x2 - x3) * (x2 - x3) + (y2 - y3) * (y2 - y3));
        double p = (s1 + s2 + s3) / 2;
        double a = Math.sqrt(p * (p - s1) * (p - s2) * (p - s3));
        BigDecimal big = new BigDecimal(a);
        double area = big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return area;
    }

    /**
     * 跳转结果页面
     */
    @SuppressWarnings("unused")
    private void JumpToShow()
    {
        Intent intent = new Intent(ThisAcitivity.this, ResultAcitivity.class);
        Bundle bundle = new Bundle();
        tR = numberFormat
                .format((double) three / (double) arrayStartEnd.size());
        tofR = numberFormat
                .format((double) tof / (double) arrayStartEnd.size());
        fotR = numberFormat
                .format((double) fot / (double) arrayStartEnd.size());
        tenR = numberFormat
                .format((double) ten / (double) arrayStartEnd.size());
        if (arrayStartEnd.size() > 0)
        {
            bundle.putString("type", "1");
            bundle.putString("ygpc", jsb);
            bundle.putString("wucha", "" + deviation);
            bundle.putString("num", "" + arrayStartEnd.size());
            bundle.putString("t", Double.parseDouble(tR) * 100 + "");
            bundle.putString("tof", Double.parseDouble(tofR) * 100 + "");
            bundle.putString("fot", Double.parseDouble(fotR) * 100 + "");
            bundle.putString("ten", Double.parseDouble(tenR) * 100 + "");
        }
        bundle.putString("yesNo", "yes");
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
        // SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss",
        // Locale.getDefault());
        // time = sdf.format(new Date());
        // // 判断为24小时还是12小时
        // ContentResolver cv = this.getContentResolver();
        // String strTimeFormat = android.provider.Settings.System.getString(cv,
        // android.provider.Settings.System.TIME_12_24);
        // if (strTimeFormat.equals("24"))
        // {
        // Log.i("tag", "当前时间是24小时制");
        // }
    }

    /**
     * 保存测试数据
     * 
     * @param x
     * @param y
     * @param offset
     * @param variance
     * @param count_3
     * @param count_5
     * @param count_10
     * @param count_10p
     * @param detail
     * @param aver
     * @param type
     */
    @SuppressWarnings("unused")
    private void saveTestData(String x, String y, String offset,
            String variance, String count_3, String count_5, String count_10,
            String count_10p, String detail, String aver, String type)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("placeId", String.valueOf(bn.getInt("placeId")));
        param.put("floorNo", String.valueOf(bn.getInt("floorNo")));
        param.put("origin", x);
        param.put("destination", y);
        param.put("startdate", start_date);
        param.put("enddate", end_date);
        param.put("triggerIp", load.getLocaIpOrMac());
        param.put("offset", offset);
        param.put("variance", variance);
        param.put("count3", count_3);
        param.put("count5", count_5);
        param.put("count10", count_10);
        param.put("count10p", count_10p);
        param.put("detail", detail);
        param.put("type", type);
        param.put("averDevi", aver);

        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/saveTestData", new JSONObject(param),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.d("this", "saveTestData:" + jsonobj.toString());
                        System.out.println("saveTestData:" + jsonobj.toString());
                        // System.out.println(getStrx+"  "+getStry+"  "+String.valueOf(wucha)+"  "+
                        // String.valueOf(wucha)+"  "+ String.valueOf(ts)+"  "+
                        // String.valueOf(tofs)+"  "+ String.valueOf(fots)+"  "+
                        // String.valueOf(tens)+"  "+array.toString()+"  "+"0");
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("this", "saveTestData:", error);
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

    private PointF start;

    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev)
    // {
    // switch (ev.getAction())
    // {
    //
    // case MotionEvent.ACTION_DOWN:
    // eventX = ev.getX();
    // eventY = ev.getY();
    // a = 0;
    // break;
    // case MotionEvent.ACTION_MOVE:
    // if (eventX != ev.getX() || eventY != ev.getY())
    // {
    // a = 1;
    // }
    //
    // break;
    //
    // case MotionEvent.ACTION_UP:
    // if (a == 0)
    // {
    //
    // }
    // }
    // return super.dispatchTouchEvent(ev);
    // }

    @Override
    public void onBackPressed()
    {
        finish();
//        bitmap.recycle();
        super.onBackPressed();
    }

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data)
    // {
    // /*
    // * isBack = false; requestLocationTask();
    // */
    // switch (resultCode)
    // {
    // case 2:
    // SharedPreferences preferences = getSharedPreferences("setting",
    // Activity.MODE_PRIVATE);
    // String type = preferences.getString("testTag", "静态");
    // if (type.equals("静态"))
    // {
    // saveTestData("(" + getStrx + "," + getStry + ")", "",
    // String.valueOf(wucha), String.valueOf(wucha),
    // String.valueOf(T), String.valueOf(TOF),
    // String.valueOf(FOT), String.valueOf(Ten),
    // array.toString(), String.valueOf(aver), "0");
    // System.out.println(getStrx + "  " + getStry + "  "
    // + String.valueOf(wucha) + "  " + String.valueOf(wucha)
    // + "  " + String.valueOf(T) + "  " + String.valueOf(TOF)
    // + "  " + String.valueOf(FOT) + "  "
    // + String.valueOf(Ten) + "  " + array.toString() + "  "
    // + String.valueOf(aver) + "  " + "0");
    // if (dao.add(list.size(), numberFormat.format(wucha),
    // Double.parseDouble(tR) * 100 + "",
    // Double.parseDouble(tofR) * 100 + "",
    // Double.parseDouble(fotR) * 100 + "",
    // Double.parseDouble(tenR) * 100 + "", time))
    // {
    // }
    //
    // } else
    // {
    // saveTestData(
    // "("
    // + numberFormat.format(dynicStart.x
    // / bn.getDouble("scale"))
    // + ","
    // + numberFormat.format(dynicStart.y
    // / bn.getDouble("scale")) + ")",
    // "("
    // + numberFormat.format(dynicEnd.x
    // / bn.getDouble("scale"))
    // + ","
    // + numberFormat.format(dynicEnd.y
    // / bn.getDouble("scale")) + ")",
    // String.valueOf(deviation), String.valueOf(deviation),
    // String.valueOf(three), String.valueOf(tof),
    // String.valueOf(fot), String.valueOf(ten),
    // array.toString(), "0", "1");
    // Dynic d = new Dynic(arrayStartEnd.size(),
    // numberFormat.format(deviation),
    // Double.parseDouble(numberFormat.format((double) three
    // / arrayStartEnd.size()))
    // * 100 + "", Double.parseDouble(numberFormat
    // .format((double) tof / arrayStartEnd.size()))
    // * 100 + "", Double.parseDouble(numberFormat
    // .format((double) fot / arrayStartEnd.size()))
    // * 100 + "", Double.parseDouble(numberFormat
    // .format((double) ten / arrayStartEnd.size()))
    // * 100 + "", time);
    // dynicDB.saveDynicResult(d);
    // }
    // array = null;
    // array = new JSONArray();
    // break;
    // case 3:
    // SharedPreferences preferences1 = getSharedPreferences("setting",
    // Activity.MODE_PRIVATE);
    // String type1 = preferences1.getString("testTag", "静态");
    // if (type1.equals("静态"))
    // {
    // if (dao.add(list.size(), numberFormat.format(wucha),
    // Double.parseDouble(tR) * 100 + "",
    // Double.parseDouble(tofR) * 100 + "",
    // Double.parseDouble(fotR) * 100 + "",
    // Double.parseDouble(tenR) * 100 + "", time))
    // {
    // System.out.println("isIntoDB");
    // }
    //
    // } else
    // {
    // Dynic d = new Dynic(arrayStartEnd.size(),
    // numberFormat.format(deviation),
    // Double.parseDouble(numberFormat.format((double) three
    // / arrayStartEnd.size()))
    // * 100 + "", Double.parseDouble(numberFormat
    // .format((double) tof / arrayStartEnd.size()))
    // * 100 + "", Double.parseDouble(numberFormat
    // .format((double) fot / arrayStartEnd.size()))
    // * 100 + "", Double.parseDouble(numberFormat
    // .format((double) ten / arrayStartEnd.size()))
    // * 100 + "", time);
    // dynicDB.saveDynicResult(d);
    // }
    // array = null;
    // array = new JSONArray();
    // break;
    // case 4:
    // array = null;
    // array = new JSONArray();
    // break;
    // }
    // super.onActivityResult(requestCode, resultCode, data);
    // }

    /**
     * 返回上一页
     * 
     * @param v
     */
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

    private void staticSaveTestData(String origin, String destination,
            String startDate, String endDate, String avgeOffset,
            String maxOffset, String staticAccuracy, String offsetCenter,
            String offsetNumber, String stability, int count3, int count5,
            int count10, int count10p, String detail)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("triggerIp", load.setIpPassword());
        param.put("placeId", "" + bn.getInt("placeId"));
        param.put("floorNo", bn.getInt("floorNo") + "");
        param.put("origin", origin);
        param.put("destination", destination);
        param.put("startdate", startDate);
        param.put("enddate", endDate);
        param.put("avgeOffset", avgeOffset);
        param.put("maxOffset", maxOffset);
        param.put("staicAccuracy", staticAccuracy);
        param.put("offsetCenter", offsetCenter);
        param.put("offsetNumber", offsetNumber);
        param.put("stability", stability);
        param.put("count3", count3 + "");
        param.put("count5", count5 + "");
        param.put("count10", count10 + "");
        param.put("count10p", count10p + "");
        param.put("detail", detail);
        // System.out.println(">>>>>>>>>>>>>");

        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/staticSaveTestData", new JSONObject(param),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.d("this",
                                "staticSaveTestData:" + jsonobj.toString());
                        System.out.println("staticSaveTestData:"
                                + jsonobj.toString());
                        // System.out.println(getStrx+"  "+getStry+"  "+String.valueOf(wucha)+"  "+
                        // String.valueOf(wucha)+"  "+ String.valueOf(ts)+"  "+
                        // String.valueOf(tofs)+"  "+ String.valueOf(fots)+"  "+
                        // String.valueOf(tens)+"  "+array.toString()+"  "+"0");
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("this", "staticSaveTestData:", error);
                        creatPath(error.toString());
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

    private void creatPath(String content)
    {
        FileUtils utils = new FileUtils();
        String filePath = "/sdcard/SVA";
        String fileName = "/Test.txt";
        utils.writeTxtToFile(content, filePath, fileName);
        System.out.println();
    }

    private void dynamicSaveTestData(String origin, String destination,
            String startDate, String endDate, String avgeOffset,
            String maxOffset, String offset, int count3, int count5,
            int count10, int count10p, String detail)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("triggerIp", load.setIpPassword());
        param.put("placeId", String.valueOf(bn.getInt("placeId")));
        param.put("floorNo", String.valueOf(bn.getInt("floorNo")));
        param.put("origin", origin);
        param.put("destination", destination);
        param.put("startdate", startDate);
        param.put("enddate", endDate);
        param.put("avgeOffset", avgeOffset);
        param.put("maxOffset", maxOffset);
        param.put("offset", offset);
        param.put("count3", count3 + "");
        param.put("count5", count5 + "");
        param.put("count10", count10 + "");
        param.put("count10p", count10p + "");
        param.put("detail", detail);

        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/dynamicSaveTestData",
                new JSONObject(param), new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.d("this",
                                "dynamicSaveTestData:" + jsonobj.toString());
                        System.out.println("dynamicSaveTestData:"
                                + jsonobj.toString());
                        // System.out.println(getStrx+"  "+getStry+"  "+String.valueOf(wucha)+"  "+
                        // String.valueOf(wucha)+"  "+ String.valueOf(ts)+"  "+
                        // String.valueOf(tofs)+"  "+ String.valueOf(fots)+"  "+
                        // String.valueOf(tens)+"  "+array.toString()+"  "+"0");
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("this", "dynamicSaveTestData:", error);
                        creatPath(error.toString());
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
}
