package com.hck.imagemap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.TouchImageView1.OnRotateListener;
import net.yoojia.imagemap.core.Bubble.RenderDelegate;
import net.yoojia.imagemap.core.CircleShape;
import net.yoojia.imagemap.core.CollectPointShape;
import net.yoojia.imagemap.core.ImageShape;
import net.yoojia.imagemap.core.MoniPointShape;
import net.yoojia.imagemap.core.PushMessageShape;
import net.yoojia.imagemap.core.RequestShape;
import net.yoojia.imagemap.core.Shape;
import net.yoojia.imagemap.core.ShapeExtension.OnShapeActionListener;
import net.yoojia.imagemap.core.SpecialShape;
import net.yoojia.imagemap.support.MResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.caverock.androidsvg.SVG;
import com.hck.imagemap.Constant.MapConstant;
import com.hck.imagemap.adapter.listAdapter;
import com.hck.imagemap.adapter.marketAdapter;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.config.MapData;
import com.hck.imagemap.entity.Book;
import com.hck.imagemap.entity.Floor;
import com.hck.imagemap.entity.Location;
import com.hck.imagemap.entity.MessageMng;
import com.hck.imagemap.entity.SellerInfo;
import com.hck.imagemap.utils.AppException;
import com.hck.imagemap.utils.AppManager;
import com.hck.imagemap.utils.FileUtils;
import com.hck.imagemap.utils.HttpDownloader;
import com.hck.imagemap.utils.Loction;
import com.hck.imagemap.utils.LogcatHelper;
import com.hck.imagemap.utils.PullBookParser;
import com.hck.imagemap.utils.UpLoad;
import com.hck.imagemap.utils.Utils;
import com.hck.imagemap.view.CompassView;
import com.hck.imagemap.view.CustomDialog;

public class MainActivity extends Activity {
	private String TAG = "MainActivity";
	private Context mContext;
	private int width, height;
	private ImageMap1 map; // lib库里面自定义试图对象
	private RelativeLayout rl_title;
	private PopupWindow pw;
	private ImageButton button3;
	private RequestQueue mRequestQueue;
	// 获取wifi服务
	private ConnectivityManager mConnectivityManager;
	private JSONArray messageList;
	private JSONArray ElectricMsgList;
	private RelativeLayout layout;
	private RelativeLayout layoutTemp;
	private ProgressDialog progressDialog;
	private boolean mapLoadfinish = false;
	private NumberFormat numberFormat;
	private Bitmap bitmap;
	private Bitmap pushInfoBitmap, defaultBitmap, vipBigBitmap;
	private ImageView iv_biaochi;
	private TextView markun;
	private int numZ;
	private Button btn_market;
	private double scaleB;
	private int startLangrage = 0;
	private int bmpWidth;
	private int bmpHeight;
	private RelativeLayout rl_biaochi;
	private SharedPreferences myPreferences;
	private Boolean autoLogin;
	private Boolean bAnimation;
	private String offset_distance;
	// private Boolean bIns;// 惯导辅助
	private Boolean biaochi;
	private Boolean bDownLoad;
	private long updateTime;
	private Boolean growslocSwitch;// 增强定位
	private Boolean bGLFloorSwitch;// 定位增强指定楼层
	private String ip, port;
	private String upload;
	// private String insapi;
	private String threshold;
	private String maxDeviate;
	private String moreMaxDeviate;
	private String direction;
	private NetworkInfo info;
	private boolean autoSwitch;
	private boolean vipShow = false;
	private boolean bIsEnable = false;
	private View bubble;
	private View bubble1;
	private View bubble2;
	private boolean isConnect = true;
	private boolean wait = true;
	private ImageView iv_button;
	private View vi_special;
	private TextView tex, tv_sva_title;
	private PopupWindow pop;
	private CompassView cv;
	private ImageView imgPlay;
	private LinearLayout btn_s;
	private LinearLayout btn_e;
	private MapData mapPathInfo;
	private RelativeLayout relativeVideo;
	private MediaController mController;
	private VideoView viv;
	private ImageView ivvip;
	private ImageView compass;
	private TextView tv_title_push1;
	private Button tobig, tosmall;
	private UpLoad load;
	private ListView list_floor;
	private ListView list_market;
	private View show_market;
	private List<String> listFloor = new ArrayList<String>();
	private List<String> listMarket = new ArrayList<String>();
	private listAdapter floorAdapter;
	private marketAdapter marketAdapter;
	private List<String> valList = new ArrayList<String>();
	private RelativeLayout.LayoutParams lps;
	private boolean bFollow;
	private LinearLayout linear_image;
	private RelativeLayout rl_push;
	private int t;// 特定位置点击标识
	private float linTouchY;// 特定位置消息按下坐标
	private TextView tv_video;
	private TextView tvpop_video;
	private ImageView logo2, logo4;
	private LinearLayout ll_list;
	private long updataTime;
	private ImageView iv_push_small1;
	private LinearLayout linPushMessage;
	private ImageView iv_image_center;
	private Map<String, Long> messageMap = new HashMap<String, Long>();
	private float PI = 3.1415926f;
	private Intent intent;
	private long lastShowTime;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapConstant.startedApp = true;
		myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
		editor = myPreferences.edit();
		ip = myPreferences.getString("serverIp", "223.202.102.66:8086");
		upload = myPreferences.getString("upload", "IP");
		threshold = myPreferences.getString("threshold", "0.2");
		// weights = myPreferences.getString("weights", "32");
		maxDeviate = myPreferences.getString("maxDeviate", "8");
		moreMaxDeviate = myPreferences.getString("moreMaxDeviate", "5");
		direction = myPreferences.getString("direction", "0");
		autoLogin = myPreferences.getBoolean("autoPush", true);
		bAnimation = myPreferences.getBoolean("bAnimation", true);
		biaochi = myPreferences.getBoolean("biaochi", true);
		autoSwitch = myPreferences.getBoolean("autoSwitch", true);
		vipShow = myPreferences.getBoolean("vipShow", true);
		bFollow = myPreferences.getBoolean("follow", false);
		bIsEnable = myPreferences.getBoolean("bIsEnable", true);
		GlobalConfig.setAutoPush(autoLogin);
		GlobalConfig.setAutoPush(biaochi);
		GlobalConfig.setServerIp(ip);
		GlobalConfig.setPort(port);
		GlobalConfig.setIdentification(upload);
		// GlobalConfig.setInsapi(insapi);
		GlobalConfig.setThreshold(threshold);
		// GlobalConfig.setWeights(weights);
		GlobalConfig.setMaxDeviate(maxDeviate);
		GlobalConfig.setMoreMaxDeviate(moreMaxDeviate);
		GlobalConfig.setDirection(direction);
		GlobalConfig.setAnimation(bAnimation);
		GlobalConfig.setEnable(bIsEnable);
		setContentView(MResource
				.getIdByName(getApplication(), "layout", "main"));
		// setContentView(R.layout.main);
		LogcatHelper.getInstance(MainActivity.this).start();
		Thread.setDefaultUncaughtExceptionHandler(AppException
				.getAppExceptionHandler());
		AppManager.getAppManager().addActivity(this);
		intent = new Intent();
		intent.setAction("com.hck.imagemap.myService");

		startService(intent);
		initView();
		initResources();
		initServices();
		sensorRegister();
		initButtonOnClickListener();
		initBigSmallSize();
		getFlootNO();
		getDataParam();
		initGravitySensor();
		// Thread collectThread = new Thread(collectObj);
		// collectThread.start();
		timer.scheduleAtFixedRate(task, 100, 20);
		Thread processThread = new Thread(processObj);
		processThread.start();
		requestLocationTask();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		mContext = this;
		defaultBitmap = BitmapFactory.decodeResource(getResources(),
				MResource.getIdByName(getApplication(), "drawable", "failed"));
		load = new UpLoad(this);
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mRequestQueue = Volley.newRequestQueue(this);
		layout = (RelativeLayout) findViewById(MResource.getIdByName(
				getApplication(), "id", "relativeLayout"));
		iv_biaochi = (ImageView) findViewById(MResource.getIdByName(
				getApplication(), "id", "iv_biaochi"));
		compass = (ImageView) findViewById(MResource.getIdByName(
				getApplication(), "id", "compass"));
		markun = (TextView) findViewById(MResource.getIdByName(
				getApplication(), "id", "tvChangeShow"));
		tv_sva_title = (TextView) findViewById(MResource.getIdByName(
				getApplication(), "id", "tv_sva_title"));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutTemp = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				R.layout.image_map_item, null);
		// layoutTemp = (RelativeLayout) LayoutInflater.from(mContext).inflate(
		// R.layout.image_map_item, null);
		layoutTemp.setLayoutParams(lp);
		map = (ImageMap1) layoutTemp.findViewById(MResource.getIdByName(
				getApplication(), "id", "imagemap"));
		layout.addView(layoutTemp);
		tv_title_push1 = (TextView) findViewById(MResource.getIdByName(
				getApplication(), "id", "tv_title_push1"));
		iv_push_small1 = (ImageView) findViewById(MResource.getIdByName(
				getApplication(), "id", "iv_push_small1"));

		// tv_svg = (TextView) findViewById(R.id.tv_svg);
		rl_push = (RelativeLayout) findViewById(MResource.getIdByName(
				getApplication(), "id", "rl_push"));
		// android.view.ViewGroup.LayoutParams layoutParams =
		// push_content.getLayoutParams();
		// layoutParams.height=height*3/4;
		// push_content.setLayoutParams(layoutParams);
		//
		// android.view.ViewGroup.LayoutParams layoutParams1 =
		// rl_height.getLayoutParams();
		// layoutParams.height=height/2;
		// rl_height.setLayoutParams(layoutParams1);
		//
		// android.view.ViewGroup.LayoutParams layoutParams2 =
		// push_hand.getLayoutParams();
		// layoutParams.height=height/4;
		// push_hand.setLayoutParams(layoutParams2);

		relativeVideo = (RelativeLayout) findViewById(MResource.getIdByName(
				getApplication(), "id", "linerVideo"));
		imgPlay = (ImageView) findViewById(MResource.getIdByName(
				getApplication(), "id", "imgPlay"));

		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(1);
		// button2 = (ImageButton) findViewById(R.id.button2);

		rl_title = (RelativeLayout) findViewById(R.id.title);
		button3 = (ImageButton) findViewById(MResource.getIdByName(
				getApplication(), "id", "button3"));
		tv_biaochi = (TextView) findViewById(MResource.getIdByName(
				getApplication(), "id", "tvbiaochi"));
		rl_biaochi = (RelativeLayout) findViewById(MResource.getIdByName(
				getApplication(), "id", "rl_biaochi"));
		tv_video = (TextView) findViewById(R.id.tv_video);
		btn_market = (Button) findViewById(MResource.getIdByName(
				getApplication(), "id", "btn_market"));
		mapData = new ArrayList<Floor>(0);
		// 用资源文件创建一个bitmap，地图
		bitmap = BitmapFactory.decodeResource(getResources(),
				MResource.getIdByName(getApplication(), "drawable", "imm_01"));
		bmpWidth = bitmap.getWidth();
		bmpHeight = bitmap.getHeight();
		// 把图加载到ImageMap上面去
		map.setMapBitmap(bitmap);
		cv = new CompassView(this);
		cv.setId(0);
		cv.setImageResource(MResource.getIdByName(getApplication(), "drawable",
				"daohang"));

		// 加载一个用来标注位置的视图view，这个view自己可以定义的
		vi_special = View.inflate(mContext, MResource.getIdByName(
				getApplication(), "layout", "special_message"), null);
		btn_s = (LinearLayout) vi_special.findViewById(MResource.getIdByName(
				getApplication(), "id", "btn_s"));
		btn_e = (LinearLayout) vi_special.findViewById(MResource.getIdByName(
				getApplication(), "id", "btn_e"));
		tvpop_video = (TextView) vi_special.findViewById(R.id.tvpop_video);
		linPushMessage = (LinearLayout) vi_special
				.findViewById(R.id.linPushMessage);
		viewStart = View
				.inflate(MainActivity.this, MResource.getIdByName(
						getApplication(), "layout", "view_start"), null);
		viewEnd = View.inflate(MainActivity.this,
				MResource.getIdByName(getApplication(), "layout", "view_end"),
				null);
		pop = new PopupWindow(vi_special, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setAnimationStyle(MResource.getIdByName(getApplication(), "style",
				"AnimBottom"));
		tex = (TextView) vi_special.findViewById(MResource.getIdByName(
				getApplication(), "id", "details1"));
		iv_special = (ImageView) vi_special.findViewById(MResource.getIdByName(
				getApplication(), "id", "logo3"));
		ll_list = (LinearLayout) findViewById(R.id.ll_list);
		logo4 = (ImageView) vi_special.findViewById(MResource.getIdByName(
				getApplication(), "id", "logo4"));
		logo4.setVisibility(View.GONE);

		tobig = (Button) findViewById(MResource.getIdByName(getApplication(),
				"id", "tobig"));
		tosmall = (Button) findViewById(MResource.getIdByName(getApplication(),
				"id", "tosmall"));
		list_floor = (ListView) findViewById(R.id.list_floor);
		list_floor.setVerticalScrollBarEnabled(false);
		show_market = View.inflate(mContext, MResource.getIdByName(
				getApplication(), "layout", "show_market"), null);
		list_market = (ListView) show_market.findViewById(MResource
				.getIdByName(getApplication(), "id", "listMarket"));
		lps = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.mSensor = ((SensorManager) getSystemService("sensor"));
		this.acc = this.mSensor.getDefaultSensor(1);
		this.gyroscope = this.mSensor.getDefaultSensor(4);
		this.magnetic = this.mSensor.getDefaultSensor(2);
		this.pressure = this.mSensor.getDefaultSensor(6);
		isBack = false;
	}

	private int count = 0;
	private Timer timer = new Timer();
	private volatile float[][] dataMem = new float[10][50];
	private float[][] senorData = new float[10][50];
	private boolean isStop = true;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			if (isStop) {
				Locale local1 = getResources().getConfiguration().locale;
				if (!MapConstant.local.equals(local1)) {
					handler.sendEmptyMessage(14);
					return;
				}
			}
			for (int j = 0; j < 10; j++) {
				// System.out.print(sensorTemp[j]+" ");
				senorData[j][count] = sensorTemp[j];
				// System.out.print(senorData[j][count]+" ");
			}
			// System.out.print("\n"+"original data end"+ count);
			count++;
			synchronized (lock) {
				if (count == 50) {
					// System.out.println("collect threat to notify process"+count);
					System.arraycopy(senorData, 0, dataMem, 0, senorData.length);
					// for (int i = 0; i <50; i++) {
					// for (int k = 0; k < 10; k++) {
					// dataMem[k][i]=senorData[k][i];
					// // System.out.print(dataMem[k][i]+" ");
					// }
					// // System.out.print("\n"+"copy data end"+ count);
					// }
					count = 0;
					lock.notifyAll();
				}
				// System.out.println("collect without notify "+count);
			}
		}
	};
	private boolean isOk = false;

	private boolean done = false;

	private void showFloor(String market) {
		listFloor.clear();
		messageMap.clear();
		valList.clear();
		for (int i = 0; i < mapData.size(); i++) {
			if (market.equals(mapData.get(i).getPlace())) {
				// 更新数据从下面显示
				listFloor.add(mapData.get(i).getFloor());
				valList.add(String.valueOf(i));
				// listFloor集合倒序排列
				// Collections.sort(listFloor, Collections.reverseOrder());
			}
		}
		floorAdapter = new listAdapter(mContext, listFloor);
		list_floor.setAdapter(floorAdapter);
		done = false;
		for (int i = 0; i < mapData.size(); i++) {
			if (mapData.get(i).getFloorNo() == numZ) {
				for (int j = 0; j < listFloor.size(); j++) {
					if (listFloor.get(j).equals(mapData.get(i).getFloor())) {
						done = true;
						floorAdapter.setSelectedPosition(j);
						floorAdapter.notifyDataSetChanged();
						val = i;
						numZ = mapData.get(val).getFloorNo();
						break;
					}
				}
				break;
			}
		}
		if (!done) {
			floorAdapter.setSelectedPosition(0);
			floorAdapter.notifyDataSetChanged();
			val = Integer.parseInt(valList.get(0));
			numZ = mapData.get(val).getFloorNo();
		}
		progressDialog.dismiss();
		downLoadMap();
	}

	private Floor currFloor;
	private TextView tv_biaochi;

	public void findClick(View view) {
		if (isDebugRequestLocation) {
			Toast.makeText(
					MainActivity.this,
					getString(MResource.getIdByName(getApplication(), "string",
							"warm")), Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
		}
	}

	private int p = 0;
	private List<Floor> mapData = null;
	private boolean isExist;

	private void getMapData() {
		String dowloadData = getString(
				MResource
						.getIdByName(getApplication(), "string", "loadMapdata"))
				.toString();
		progressDialog = ProgressDialog.show(MainActivity.this, "Loading",
				dowloadData, true, false);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getMapDataByIp", new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.d(TAG, "jsonobj:" + jsonobj.toString());
						System.out.println("jsonobj:" + jsonobj.toString());
						JSONArray array = null;
						try {
							array = jsonobj.getJSONArray("data");
						} catch (Exception e) {
							Log.e("error", e + "");
							return;
						}

						for (int i = 0; i < array.length(); i++) {
							// {"scale":"5","x":"4","y":"6","floor":"F1","path":"4444F1.jpg","floorNo":1,"place":"4444"}
							try {
								Floor f = new Floor();
								JSONObject o = array.getJSONObject(i);
								f.setFloor(o.getString("floor"));
								f.setFloorNo(o.getInt("floorNo"));
								f.setPath(o.getString("path"));
								f.setPlace(o.getString("place"));
								f.setPlaceId(o.getInt("placeId"));
								f.setScale(o.getDouble("scale"));
								f.setX(o.getDouble("xo"));
								if (o.has("route")) {
									f.setRoute(o.getString("route"));
								}
								if (o.has("updateTime")) {
									f.setUpdateTime(o.getLong("updateTime"));
								}
								f.setY(o.getDouble("yo"));
								f.setCoordinate(o.getString("coordinate"));
								f.setImgWidth(o.getInt("imgWidth"));
								f.setImgHeight(o.getInt("imgHeight"));
								f.setAngle(o.getDouble("angle"));
								if (o.has("svg")) {
									f.setSvg(o.getString("svg"));
								}
								f.setNavPath(o.getString("pathFile"));
								mapData.add(f);

							} catch (Exception e) {
								Log.e("error", e + "");
							}
						}
						if (z > 0) {
							numZ = z;
						} else {
							numZ = mapData.get(0).getFloorNo();
						}
						for (int j = 0; j < mapData.size(); j++) {
							if (numZ == mapData.get(j).getFloorNo()) {
								val = j;
								break;
							} else {
								val = 0;
							}
						}
						showMarket();
						// progressDialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "jsonobj:", error);
						String wrong = getString(
								MResource.getIdByName(getApplication(),
										"string", "wrong")).toString();
						Toast.makeText(MainActivity.this, wrong,
								Toast.LENGTH_LONG).show();
						progressDialog.dismiss();
					}
				});
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1.0f));
		mRequestQueue.add(newMissRequest);
	}

	private void getDataParam() {
		// getDataParam:{"error":null,"data":[{"id":1,"exceedMaxDeviation":5,"weight":32,
		// "updateTime":1452222338000,"banThreshold":0.7,"biasSpeed":1.33,"maxDeviation":8,
		// "spr":1.26}]}
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.GET, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getDataParam", new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						System.out.println("getDataParam:" + jsonobj.toString());
						JSONArray t;
						try {
							t = jsonobj.getJSONArray("data");
							for (int i = 0; i < t.length(); i++) {
								JSONObject o = t.getJSONObject(i);
								GlobalConfig.setMoreMaxDeviate(o
										.getString("exceedMaxDeviation"));
								GlobalConfig.setMaxDeviate(o
										.getString("maxDeviation"));
								GlobalConfig.setWeights(o
										.getString("horizontalWeight"));
								GlobalConfig.setThreshold(o
										.getString("banThreshold"));
								GlobalConfig.setSpr(o.getString("filterLength"));
								GlobalConfig.setCrossWeights(o
										.getString("ongitudinalWeight"));
								GlobalConfig.setErrorAngle(o
										.getDouble("restTimes"));
								GlobalConfig.setPositioningError(o
										.getDouble("filterPeakError"));
								GlobalConfig.setFilterNumber(o
										.getDouble("peakWidth"));
								GlobalConfig.setGaussVariance(o
										.getDouble("step"));
								GlobalConfig.setDirection(o
										.getString("correctMapDirection"));

								editor.putString("threshold",
										o.getString("banThreshold"));
								editor.putString("readius",
										o.getString("filterLength"));
								editor.putString("weights",
										o.getString("horizontalWeight"));
								editor.putString("crossWeights",
										o.getString("ongitudinalWeight"));
								editor.putString("maxDeviate",
										o.getString("maxDeviation"));
								editor.putString("moreMaxDeviate",
										o.getString("exceedMaxDeviation"));
								editor.putString("direction",
										o.getString("correctMapDirection"));
								editor.putString("errorAngle",
										o.getDouble("restTimes") + "");
								editor.putString("positioningError",
										o.getDouble("filterPeakError") + "");
								editor.putString("filterNumber",
										o.getDouble("peakWidth") + "");
								editor.putString("gaussVariance",
										o.getDouble("step") + "");
								editor.commit();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		mRequestQueue.add(newMissRequest);
	}

	private void showMarket() {
		String beforeMarket = null;
		listMarket.clear();
		if (mapData == null && mapData.isEmpty()) {
			return;
		}
		for (int i = 0; i < mapData.size(); i++) {
			beforeMarket = mapData.get(i).getPlace();
			if (listMarket.size() > 0) {
				for (int j = 0; j < listMarket.size(); j++) {
					if (beforeMarket.equals(listMarket.get(j))) {
						isExist = true;
						break;
					} else {
						isExist = false;
					}
				}
				if (!isExist) {
					listMarket.add(beforeMarket);
				}
			} else {
				listMarket.add(beforeMarket);
			}
		}
		marketAdapter = new marketAdapter(mContext, listMarket);
		list_market.setAdapter(marketAdapter);
		String mark = mapData.get(0).getPlace();
		AllMark = mark;
		for (int i = 0; i < mapData.size(); i++) {
			if (mapData.get(i).getFloorNo() == numZ) {
				mark = mapData.get(i).getPlace();
				AllMark = mark;
				break;
			}
		}
		ArcadeCollection();
		for (int i = 0; i < listMarket.size(); i++) {
			if (mark.equals(listMarket.get(i).toString())) {
				marketAdapter.setSelectedPosition(i);
				marketAdapter.notifyDataSetChanged();
				break;
			}
		}
		showFloor(mark);
	}

	/**
	 * 同一商场楼层集合
	 */
	private void ArcadeCollection() {
		place.clear();
		for (int i = 0; i < mapData.size(); i++) {
			if (mapData.get(i).getPlace().equals(AllMark)) {
				place.add(mapData.get(i));
			}
		}
	}

	private Picture picture = null;

	private void switchMap(String mapPath) {
		switchBool = true;
		handler.removeCallbacks(update);
		FileInputStream fis;
		isFrom = false;
		isTo = false;
		Log.d("map", "mapPath:" + mapPath);
		// toSubscription();
		// currFloor = mapData.get(val);
		map.clearShapes();
		GlobalConfig.setxSport(currFloor.getX());
		GlobalConfig.setySport(currFloor.getY());
		GlobalConfig.setProportion(currFloor.getScale());
		// data = new MapData(MainActivity.this, map,
		// /*currFloor.getFloorNo()*/currFloor.getKeyWord());
		SVG svg = null;
		try {
			fis = new FileInputStream(mapPath);
			svg = SVG.getFromInputStream(fis);
			picture = svg.renderToPicture();
			bmpWidth = currFloor.getImgWidth();
			bmpHeight = currFloor.getImgHeight();
		} catch (Exception e) {
			Log.e("switchMaperror", e + "");
			e.printStackTrace();
		}
		progressDialog.dismiss();
		if (picture != null) {
			map.setMapPicture(picture);
			if (bitmap != null) {
				bitmap.recycle();
			}
			mapLoadfinish = true;
			thisOne[0] = 0;
			thisOne[1] = 0;
			getSellerInfo();
			if (vipShow) {
				getVipSellerInfo();
			}

		}

	}

	private void switchMap1(String mapPath) {

		FileInputStream fis;
		map.clearShapes();
		isFrom = false;
		isTo = false;
		Log.d("map", "mapPath:" + mapPath);
		progressDialog.dismiss();
		try {
			if (MapConstant.bitmap != null) {
				MapConstant.bitmap.recycle();
				MapConstant.bitmap = null;
			}

			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}

			fis = new FileInputStream(mapPath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			MapConstant.bitmap = BitmapFactory.decodeStream(fis, null, options);
			map.setMapBitmap(MapConstant.bitmap);
			bmpWidth = currFloor.getImgWidth();
			bmpHeight = currFloor.getImgHeight();
			mapLoadfinish = true;
			thisOne[0] = 0;
			thisOne[1] = 0;
			getSellerInfo();
			if (vipShow) {
				getVipSellerInfo();
			}
			downLoadNavFile();
			Log.i("width_heigh", bmpWidth + " " + bmpHeight);

		} catch (Exception e) {
			Log.e("switchMaperror", e + "");
			e.printStackTrace();
		}

	}

	String navPath_url = null;

	private void downLoadNavFile() {
		try {
			if (currFloor.getNavPath() != null
					&& !currFloor.getNavPath().equals("null")) {
				navPath_url = "http://" + GlobalConfig.server_ip
						+ "/sva/upload/"
						+ URLEncoder.encode(currFloor.getNavPath(), "UTF-8");
			} else {
				return;
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpDownloader downloader = new HttpDownloader();

				int result = downloader.downFile(navPath_url, "SVA/",
						currFloor.getPlace() + "_" + currFloor.getFloor()
								+ "_nav_path.xml");
				if (result != -1) {
					mapPathInfo = new MapData(mContext, map, "SVA/"
							+ currFloor.getPlace() + "_" + currFloor.getFloor()
							+ "_nav_path.xml");
					return;
				} else {
					Toast.makeText(mContext, "downLoad nav failed",
							Toast.LENGTH_SHORT).show();
				}
			}
		}).start();

	}

	private String path_url;
	private long locateTime = 0;
	private long webTime = 1;

	/**
	 * 无更新/服务器未响应调用
	 */
	private void defaultPath() {
		FileUtils fu = new FileUtils();
		if (fu.isFileExist("SVA/" + currFloor.getPlace() + "_"
				+ currFloor.getFloor() + "_path.xml")) {
			handler.sendEmptyMessage(18);
			return;
		}
	}

	/**
	 * 读取路径
	 */
	public void readPath() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/SVA/" + currFloor.getPlace() + "_" + currFloor.getFloor()
				+ "_path.xml");
		if (file.isFile() && file.exists()) {
			try {

				InputStream inputStream = new FileInputStream(file);

				MapConstant.FingureprintXY.clear();
				PullBookParser bookParser = new PullBookParser();
				List<Book> personsList = bookParser.parse(inputStream);
				for (Book person2 : personsList) {
					String[] ds = person2.getX().split(",");
					for (int i = 0; i < ds.length; i++) {
						double[] coordiate = new double[2];
						double[] coordiates = new double[2];
						// coordiate[0] = Float.parseFloat(ds[i]);
						// coordiate[1] = currFloor.getImgHeight()
						// - Float.parseFloat(person2.getY());

						if ("ul".equals(currFloor.getCoordinate()))// 左上为原点
						{
							coordiate[0] = Float.parseFloat(ds[i]);
							coordiate[1] = currFloor.getImgHeight()
									- Float.parseFloat(person2.getY());
						} else if ("ll".equals(currFloor.getCoordinate()))// 左下
						{
							coordiate[0] = Float.parseFloat(ds[i]);
							coordiate[1] = Float.parseFloat(person2.getY());
						} else if ("ur".equals(currFloor.getCoordinate()))// 右上
						{
							coordiate[0] = currFloor.getImgWidth()
									- Float.parseFloat(ds[i]);
							coordiate[1] = currFloor.getImgHeight()
									- Float.parseFloat(person2.getY());
						} else if ("lr".equals(currFloor.getCoordinate()))// 右xia
						{
							coordiate[0] = currFloor.getImgWidth()
									- Float.parseFloat(ds[i]);
							coordiate[1] = Float.parseFloat(person2.getY());
						}

						coordiates[0] = (coordiate[0] / currFloor.getScale() - currFloor
								.getX()) * 10;
						coordiates[1] = (coordiate[1] / currFloor.getScale() - currFloor
								.getY()) * 10;

						MapConstant.FingureprintXY.add(coordiates);
					}
				}
			} catch (Exception e) {
				Log.e("pathlook", "" + e);
			}
		}

	}

	private boolean isRequestLocation = false;
	private boolean isDebugRequestLocation = false;

	private boolean checkConnect() {
		info = mConnectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			info.getType();
			isConnect = true;
		} else {
			isConnect = false;
		}
		return isConnect;
	}

	private boolean isBack = false;

	private void requestLocationTask() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isBack) {
					System.out.println("isok" + isOk);
					if (isDebugRequestLocation == false
							&& isRequestLocation == false) {
						clearRequestmessage();
					}
					if (biaochi) {
						handler.sendEmptyMessage(3);

					} else {
						handler.sendEmptyMessage(6);
					}
					if (!checkConnect()) {
						if (wait) {
							Toast.makeText(
									MainActivity.this,
									getString(MResource.getIdByName(
											getApplication(), "string",
											"netWrong")), Toast.LENGTH_SHORT)
									.show();
							wait = false;
						}
						requestLocationTask();
					} else {
						wait = true;
						if ((isRequestLocation)) {
							if (!growslocSwitch) {
								requestLocation();
							} else {
								requestLocationCollect();
							}
						} else {
							requestLocationTask();
						}
					}
				}
			}
		}, 1000);
	}

	private double lastTime = 0;
	// private double parmaTime = 0;
	private ArrayList<PointF> ap = new ArrayList<PointF>();
	private ArrayList<SellerInfo> vipListInfo = new ArrayList<SellerInfo>();
	private boolean isAlive3 = false;
	private ArrayList<SellerInfo> listInfo;
	private ImageView imageView;
	private TextView tv_vipContent;
	private boolean vipBoolean = true;// vip是否播放

	private void getVipSellerInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getVipSellerInfo?floorNo=" + numZ,
				new JSONObject(params), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonobj) {

						Log.d(TAG, "getVipSellerinfo:" + jsonobj.toString());
						System.out.println("getVipSellerinfo:"
								+ jsonobj.toString());
						JSONArray array = null;

						listInfo = new ArrayList<SellerInfo>();
						listInfo.clear();
						SellerInfo info;
						try {
							array = jsonobj.getJSONArray("data");
						} catch (Exception e) {
							Log.e("error", e + "");
						}
						for (int i = 0; i < array.length(); i++) {
							info = null;
							info = new SellerInfo();
							try {
								JSONObject o = array.getJSONObject(i);
								info.setPictureUrl(o.getString("pictruePath"));
								info.setMovieUrl(o.getString("moviePath"));
								info.setContent(o.getString("info"));
								listInfo.add(info);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if (listInfo.size() > 0) {
							CustomDialog.Builder builder = new CustomDialog.Builder(
									MainActivity.this);
							View v = View.inflate(MainActivity.this, MResource
									.getIdByName(getApplication(), "layout",
											"vipseller"), null);
							ivvip = (ImageView) v.findViewById(MResource
									.getIdByName(getApplication(), "id",
											"iv_vip"));
							// vip图片监听
							ivvip.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									CustomDialog.Builder builder1 = new CustomDialog.Builder(
											MainActivity.this);
									View v1 = View.inflate(MainActivity.this,
											MResource.getIdByName(
													getApplication(), "layout",
													"image_center"), null);
									iv_image_center = (ImageView) v1
											.findViewById(MResource
													.getIdByName(
															getApplication(),
															"id",
															"iv_image_center"));
									linear_image = (LinearLayout) v1
											.findViewById(MResource
													.getIdByName(
															getApplication(),
															"id",
															"linear_image"));
									getBitMap(listInfo.get(0).getPictureUrl(),
											11);
									final CustomDialog dia1 = builder1.create();
									dia1.setContentView(v1);
									dia1.show();
									// 设置dialog宽高
									WindowManager.LayoutParams params = dia1
											.getWindow().getAttributes();
									params.width = width;
									params.height = height;
									dia1.getWindow().setAttributes(params);
									linear_image
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View arg0) {
													dia1.dismiss();
												}
											});
								}
							});
							tv_vipContent = (TextView) v.findViewById(MResource
									.getIdByName(getApplication(), "id",
											"tv_vip"));
							tv_vipContent.setText(listInfo.get(0).getContent());
							final CustomDialog dia = builder.create();
							dia.setContentView(v);
							viv = (VideoView) v.findViewById(MResource
									.getIdByName(getApplication(), "id",
											"vv_vip"));
							imgPlay = (ImageView) v.findViewById(MResource
									.getIdByName(getApplication(), "id",
											"imgPlay"));
							relativeVideo = (RelativeLayout) v
									.findViewById(MResource.getIdByName(
											getApplication(), "id",
											"linerVideo"));
							mController = new MediaController(MainActivity.this);
							viv.setMediaController(mController);
							vipBoolean = true;
							relativeVideo
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {
											if (listInfo.get(0).getMovieUrl()
													.equals("null")
													|| listInfo.get(0)
															.getMovieUrl()
															.equals("")) {
												Toast.makeText(
														MainActivity.this,
														getString(MResource
																.getIdByName(
																		getApplication(),
																		"string",
																		"no_video")),
														Toast.LENGTH_SHORT)
														.show();
												return;
											} else {
												if (vipBoolean) {
													try {
														viv.setVideoURI(Uri.parse("http://"
																+ GlobalConfig.server_ip
																+ "/sva/upload/"
																+ URLEncoder
																		.encode(listInfo
																				.get(0)
																				.getMovieUrl(),
																				"UTF-8")));
														vipBoolean = false;
													} catch (UnsupportedEncodingException e) {
														e.printStackTrace();
													}
													viv.requestFocus();
													viv.seekTo(100);
												}
											}
											if (!viv.isPlaying()) {
												viv.start();
												imgPlay.setVisibility(View.GONE);
											} else {
												viv.pause();
												imgPlay.setVisibility(View.VISIBLE);
											}
										}
									});
							dia.show();
							imageView = (ImageView) v.findViewById(MResource
									.getIdByName(getApplication(), "id",
											"iv_close"));
							isAlive3 = true;
							new Thread() {
								@Override
								public void run() {
									try {
										// 创建一个url对象
										while (isAlive3) {
											isAlive3 = false;
											URL url = new URL(
													"http://"
															+ GlobalConfig.server_ip
															+ "/sva/upload/"
															+ URLEncoder
																	.encode(listInfo
																			.get(0)
																			.getPictureUrl(),
																			"UTF-8"));
											// 打开URL对应的资源输入流
											InputStream is = url.openStream();
											// 从InputStream流中解析出图片
											pushInfoBitmap = BitmapFactory
													.decodeStream(is);
											// imageview.setImageBitmap(bitmap);
											// 发送消息，通知UI组件显示图片
											handler.sendEmptyMessage(9);
											// 关闭输入流
											is.close();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}.start();
							imageView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									dia.dismiss();
								}
							});
							WindowManager.LayoutParams params = dia.getWindow()
									.getAttributes();
							params.width = new Float((width / 1.2)).intValue();
							params.height = 2 * height / 3;
							dia.getWindow().setAttributes(params);
						} else {
							// Toast.makeText(mContext, "error", 0).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "getVipSellerInfo:", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		mRequestQueue.add(newMissRequest);
	}

	public void getBitMap(final String picUrl, final int i) {
		isAlive3 = true;
		new Thread() {
			@Override
			public void run() {
				try {
					// 创建一个url对象
					while (isAlive3) {
						isAlive3 = false;
						URL url = new URL("http://" + GlobalConfig.server_ip
								+ "/sva/upload/"
								+ URLEncoder.encode(picUrl, "UTF-8"));
						// 打开URL对应的资源输入流
						InputStream is = url.openStream();
						// 从InputStream流中解析出图片
						vipBigBitmap = BitmapFactory.decodeStream(is);
						// imageview.setImageBitmap(bitmap);
						// 发送消息，通知UI组件显示图片
						handler.sendEmptyMessage(i);
						// 关闭输入流
						is.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void iv_click(View view) {
		viv.stopPlayback();
		imgPlay.setVisibility(View.VISIBLE);
	}

	private String pictureUrl;

	private void getSellerInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getSellerInfo?floorNo=" + numZ,
				new JSONObject(params), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {

						Log.d(TAG, "getsellerinfo:" + jsonobj.toString());
						System.out.println(jsonobj.toString());
						JSONArray array = null;
						System.out.println("numz" + numZ);
						try {
							array = jsonobj.getJSONArray("data");
							ap.clear();
							vipListInfo.clear();
						} catch (Exception e) {
							Log.e("error", e + "");
						}
						loction = new Loction();
						arraySpecial.clear();
						arrayPoint.clear();
						for (int i = 0; i < array.length(); i++) {
							try {
								JSONObject o = array.getJSONObject(i);
								int xSpot = o.getInt("xSpot");
								int ySpot = o.getInt("ySpot");
								PointF ppp = new PointF();
								ppp.x = (float) loction.location(xSpot * 10,
										ySpot * 10, currFloor)[0];
								ppp.y = (float) loction.location(xSpot * 10,
										ySpot * 10, currFloor)[1];

								ap.add(ppp);
								String url = null;
								String msg = o.getString("info");
								SpecialShape black = null; // Color.BLUE，圆点的颜色
								if (o.has("moviePath")) {
									if (!o.getString("moviePath")
											.equals("null")) {

										url = "http://"
												+ GlobalConfig.server_ip
												+ "/sva/upload/"
												+ URLEncoder.encode(
														o.getString("moviePath"),
														"UTF-8");
									} else {
										url = null;
									}
								}
								if (o.has("pictruePath")) {
									pictureUrl = "http://"
											+ GlobalConfig.server_ip
											+ "/sva/upload/"
											+ o.getString("pictruePath");
									System.out.println(pictureUrl);
								}

								black = new SpecialShape("no" + i, url,
										pictureUrl, Color.DKGRAY, null, msg,
										MainActivity.this, i); // Color.BLUE，圆点的颜色

								black.setValues(String.format("%.5f:%.5f:35",
										ppp.x, ppp.y)); // 设置圆点的位置和大小
								// map.addShape(black, true);
								arrayPoint.add(ppp);
								arraySpecial.add(black);
							} catch (Exception e) {
								Log.e("error", e + "");
							}
						}
						switchBool = false;
						handlers.postDelayed(update, 500);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "getSellerInfo:", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(500, 1, 1.0f));
		mRequestQueue.add(newMissRequest);
	}

	private int z = 0;

	private double[] thisOne = new double[2];
	private boolean isGetLocation = false;

	private void getFlootNO() {
		progressDialog = ProgressDialog.show(MainActivity.this, "Loading",
				"loding..", true, false);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getData", new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.d(TAG, "getFlootNO:" + jsonobj.toString());
						try {
							progressDialog.dismiss();
							JSONObject o = jsonobj.getJSONObject("data");
							thisOne[0] = o.getDouble("x");
							thisOne[1] = o.getDouble("y");
							z = o.getInt("z");
							isGetLocation = true;
							getMapData();
						} catch (Exception e) {
							progressDialog.dismiss();
							getMapData();
							Log.e("error", e + "");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "requestLocation:", error);
						progressDialog.dismiss();
						Toast.makeText(MainActivity.this,
								getString(R.string.noResponse),
								Toast.LENGTH_LONG).show();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, 1.0f));
		mRequestQueue.add(newMissRequest);
	}

	/**
	 * 
	 * 发起prru订阅
	 */
	private void getPrruSubscribe() {

		Map<String, String> params = new HashMap<String, String>();
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/subscribePrru?storeId="
						+ currFloor.getPlaceId() + "&ip="
						+ load.getLocaIpOrMac(), new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						System.out.println("subscribePrru:"
								+ jsonobj.toString());
						try {
						} catch (Exception e) {
							Log.e("error", e + "");
							return;
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String wrong = getString(R.string.prru_fail).toString();
						Toast.makeText(MainActivity.this, wrong,
								Toast.LENGTH_LONG).show();
					}
				});
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(500, 1, 1.0f));
		mRequestQueue.add(newMissRequest);
	}

	private void toSubscription() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/subscription?storeId="
						+ currFloor.getPlaceId() + "&ip="
						+ load.getLocaIpOrMac(), new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.i("subscription2", "subscription");
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "jsonobj:", error);
					}
				});
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(300, 1, 1.0f));
		mRequestQueue.add(newMissRequest);
	}

	private double[] asLocation = new double[2];
	private double[] oriLocation = new double[2];
	private double[] oldBeforeLocation = new double[2];

	private long atposiTime;

	String sss;

	private String lastMessage;// 最后的消息
	private String lastPictureUrl;// 最后的tupian url
	private boolean isUpdate = false;
	private String messageTag; // 消息标识
	private int timeStamp; // tuisong 时间间隔
	private String bubbleTag = "default";// bubble标识

	private void requestLocation() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac()/* "10.180.7.79" */);
		Log.i("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getData", new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						System.out.println("requestLocation:111"
								+ jsonobj.toString());
						String string = "uncollect";
						requestContant(jsonobj, string);
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "requestLocation:", error);
						Toast.makeText(MainActivity.this,
								getString(R.string.locnoresponse),
								Toast.LENGTH_SHORT).show();
						requestLocationTask();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		mRequestQueue.add(newMissRequest);
	}

	private String floorNo;

	private void requestLocationCollect() {
		if (bGLFloorSwitch) {
			floorNo = currFloor.getFloorNo() + "";
		} else {
			floorNo = null;
		}
		Map<String, String> params = new HashMap<String, String>();
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/getData?userId="
						+ load.getLocaIpOrMac() + "&x=" + oriLocation[0]
						+ "&y=" + oriLocation[1] + "&x1=" + asLocation[0]
						+ "&y1=" + asLocation[1] + "&floorNo=" + floorNo,
				new JSONObject(params), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						System.out.println("requestLocationCollect:111"
								+ jsonobj.toString());
						String string = "collect";
						requestContant(jsonobj, string);
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "requestLocationCollect:", error);
						Toast.makeText(MainActivity.this,
								getString(R.string.locnoresponse),
								Toast.LENGTH_SHORT).show();
						requestLocationTask();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		mRequestQueue.add(newMissRequest);
	}

	private String timestampPrru;
	// Map<String, Object> hashMap = new HashMap<String, Object>();

	private long paramUpdataTime;

	private void requestContant(JSONObject jsonobj, String string) {

		// getDataParam();
		Location loc = new Location();
		try {
			final JSONObject o = jsonobj.getJSONObject("data");

			// if (parmaTime != o.getDouble("paramUpdataTime"))
			// {
			// getDataParam();
			// }

			if (lastTime == o.getDouble("timestamp")) {
				requestLocationTask();
				return;
			}
			lastTime = o.getDouble("timestamp");

			// 获取空口结果
			oriLocation[0] = o.getDouble("x");
			oriLocation[1] = o.getDouble("y");

			// // 萧山临时修改版本
			// if (bGLFloorSwitch) {
			// if (String.valueOf(o.getInt("z")).endsWith("4")
			// || String.valueOf(o.getInt("z")).endsWith("5")) {
			// oriLocation[0] = o.getDouble("y") + 52 * 10;
			// oriLocation[1] = -o.getDouble("x") + 212 * 10;
			// }
			// }

			// // test
			// oriLocation[0] = 1120;
			// oriLocation[1] = 342;

			// test
			if (o.has("timestampPrru")) {
				timestampPrru = o.getString("timestampPrru");
			}
			loc.setX(oriLocation[0]);
			loc.setY(oriLocation[1]);
			lastz = o.getInt("z");
			if (mapLoadfinish && isMove) {
				if (!bGLFloorSwitch) {
					if (autoSwitch) {
						if (numZ != lastz) {
							confirmFloor(o);
							return;
						}
					} else {
						if (numZ != lastz) {
							requestLocationTask();
							return;
						}
					}
				}

				includeMessage(jsonobj);
				includeElectronicMsg(jsonobj);
				addPostion(loc);
			}
		} catch (Exception e) {
			Log.e("error22", e + "");
			Toast.makeText(this, getString(R.string.getdatafaild),
					Toast.LENGTH_SHORT).show();
		}
		requestLocationTask();
	}


	/** 定位数据包含推送消息的后续执行 */
	private void includeMessage(JSONObject jsonobj) throws JSONException {
		if (jsonobj.has("message")) {
			if (autoLogin)// 如果打开开关
			{
				messageList = jsonobj.getJSONArray("message");
				if (messageList.length() > 0) {
					isUpdate = false;
					pushInterval();
					if (isUpdate)// 判断如果推送消息有更新
					{
						rl_push.setVisibility(View.VISIBLE);
						pop.dismiss();
						if (bubble != null) {
							bubble.setVisibility(View.INVISIBLE);
						}
						iv_push_small1.setImageBitmap(defaultBitmap);
						tv_title_push1.setText(lastMessage);
						isAlive3 = true;
						new Thread() {
							@Override
							public void run() {
								try {
									// 创建一个url对象
									while (isAlive3) {
										isAlive3 = false;
										URL url = new URL(lastPictureUrl);
										// 打开URL对应的资源输入流
										InputStream is = url.openStream();
										// 从InputStream流中解析出图片
										pushInfoBitmap = BitmapFactory
												.decodeStream(is);
										// imageview.setImageBitmap(bitmap);
										// 发送消息，通知UI组件显示图片
										handler.sendEmptyMessage(10);
										// 关闭输入流
										is.close();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				} else {
					clearMessage();
				}
			} else {
				rl_push.setVisibility(View.GONE);
				clearMessage();
			}
		}
	}

	private void includeElectronicMsg(JSONObject jsonobj) throws JSONException {
		if (jsonobj.has("geofencing")) {
			if (autoLogin)// 如果打开开关
			{
				ElectricMsgList = jsonobj.getJSONArray("geofencing");
				if (ElectricMsgList.length() > 0) {
					map.removeShape("message");
					for (int i = 0; i < ElectricMsgList.length(); i++) {
						JSONObject t = (JSONObject) ElectricMsgList.get(i);
						timeStamp = 1 * 30 * 1000;
						String zoneid = t.getString("zoneid");
						Long showTime = messageMap.get(zoneid);
						atposiTime = System.currentTimeMillis();
						if (showTime != null) {
							if (atposiTime - showTime.longValue() < 10000) {
								continue;
							} else if (atposiTime - showTime.longValue() > 10000
									&& atposiTime - showTime.longValue() < timeStamp) {
								if (System.currentTimeMillis() - lastShowTime > 1200) {
									rl_push.setVisibility(View.GONE);
								}
								continue;
							}
						}
						lastShowTime = System.currentTimeMillis();
						messageMap.put(zoneid, atposiTime);
						rl_push.setVisibility(View.VISIBLE);
						pop.dismiss();
						tv_title_push1.setText("userid: "
								+ t.getString("userid") + " _"
								+ t.getString("entre") + "_zoneid: " + zoneid);
					}
				}
			}
		}
	}

	/** 设置消息推送的时间间隔并对每一个消息声明一个对象 */
	private void pushInterval() throws JSONException {
		for (int i = 0; i < messageList.length(); i++) {
			JSONObject t = (JSONObject) messageList.get(i);

			if (t.has("timeInterval")) {
				if (t.getString("timeInterval") != null
						|| !t.getString("timeInterval").equals("")) {
					timeStamp = Integer.valueOf(t.getString("timeInterval")) * 60 * 1000;
				} else {
					timeStamp = 3 * 60 * 1000;// 三分钟转毫秒
				}
			} else {
				timeStamp = 1 * 60 * 1000;// 三分钟转毫秒
			}
			String message = t.getString("message");
			Long showTime = messageMap.get(message);
			atposiTime = System.currentTimeMillis();
			if (showTime != null && atposiTime - showTime.longValue() < 12000) {
				continue;
			} else if (showTime != null
					&& atposiTime - showTime.longValue() > 12000
					&& atposiTime - showTime.longValue() < timeStamp) {
				map.removeShape("pushShape" + i);
				if (("pushShape" + i).equals(messageTag)) {
					map.removeShape("message");
				}
				if (bubbleTag.equals("pushShape" + i)) {
					bubble.setVisibility(View.INVISIBLE);
				}
				if (System.currentTimeMillis() - lastShowTime > 1200) {
					rl_push.setVisibility(View.GONE);
				}
				continue;
			}
			lastShowTime = System.currentTimeMillis();
			messageMap.put(message, atposiTime);
			double xx = loction.location(t.getDouble("xSpot") * 10,
					t.getDouble("ySpot") * 10, currFloor)[0];
			double yy = loction.location(t.getDouble("xSpot") * 10,
					t.getDouble("ySpot") * 10, currFloor)[1];
			MessageMng m = new MessageMng();
			m.setMessage(message);
			m.setFloor(t.getString("floor"));
			// m.setIsEnable(t.getString("isEnable"));
			m.setxSpot(t.getDouble("xSpot"));
			m.setySpot(t.getDouble("ySpot"));

			View vi = View.inflate(MainActivity.this, MResource.getIdByName(
					getApplication(), "layout", "push_message_shape"), null);
			final String picUrl = "http://" + GlobalConfig.server_ip
					+ "/sva/upload/" + t.getString("pictruePath");
			final String videoUrl;
			if (!t.getString("moviePath").equals("null")
					&& !t.getString("moviePath").equals("")) {
				videoUrl = "http://" + GlobalConfig.server_ip + "/sva/upload/"
						+ t.getString("moviePath");
			} else {
				videoUrl = null;
			}
			System.out.println(">>7");
			ImageShape black = new ImageShape("message", Color.RED, vi,
					mContext, videoUrl, picUrl, message,
					t.getString("shopName"));
			black.setValues(String.format("%.5f:%.5f:0", xx - 3, yy));
			map.addShape(black, false);
			System.out.println(">>8");
			PushMessageShape ss = new PushMessageShape("pushShape" + i,
					videoUrl, picUrl, Color.RED, null, message,
					MainActivity.this); // Color.BLUE，圆点的颜色
			ss.setValues(String.format("%.5f:%.5f:35", xx, yy)); // 设置圆点的位置和大小
			map.addShape(ss, true);
			messageTag = "pushShape" + i;
			p++;
			isUpdate = true;
			lastMessage = message;
			lastPictureUrl = picUrl;
			tv_video.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (videoUrl != null) {
						isStop = false;
						Intent intent = new Intent(MainActivity.this,
								VideoPlayerAcitivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("url", videoUrl);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						Toast.makeText(mContext, getString(R.string.no_video),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	/**
	 * 楼层变化弹框
	 * 
	 * @param o
	 */
	private void confirmFloor(final JSONObject o) {
		isRequestLocation = false;
		CustomDialog.Builder builder = new CustomDialog.Builder(
				MainActivity.this);
		builder.setTitle(
				getString(MResource.getIdByName(getApplication(), "string",
						"prompt"))).setMessage(
				getString(MResource.getIdByName(getApplication(), "string",
						"changeFloor")));
		builder.setPositiveButton(getString(MResource.getIdByName(
				getApplication(), "string", "yes")),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							isRequestLocation = true;
							numZ = o.getInt("z");
						} catch (Exception e) {
							Log.e("error", e + "");
						}
						setVal();
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(getString(MResource.getIdByName(
				getApplication(), "string", "no")),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							isRequestLocation = true;
						} catch (Exception e) {
							Log.e("error", e + "");
						}
						isRequestLocation = false;
						button3.setBackgroundResource(MResource.getIdByName(
								getApplication(), "drawable", "location2"));
						clearRequestmessage();
						dialog.dismiss();

					}
				});
		builder.create().show();
		requestLocationTask();
	}

	private int lastz = 0;

	private void setVal() {
		for (int index = 0; index < mapData.size(); index++) {
			if (lastz == mapData.get(index).getFloorNo()) {
				val = index;
				numZ = lastz;
				String mark = null;
				for (int i = 0; i < mapData.size(); i++) {
					if (numZ == mapData.get(i).getFloorNo()) {
						mark = mapData.get(i).getPlace();
						AllMark = mark;
						break;
					}
				}
				for (int i = 0; i < listMarket.size(); i++) {
					if (listMarket.get(i).toString().equals(mark)) {
						marketAdapter.setSelectedPosition(i);
						marketAdapter.notifyDataSetChanged();
						beforeMark = mark;
						break;
					}
				}
				ArcadeCollection();
				showFloor(mark);
				break;
			}
		}
	}

	private int postionNum = 0;
	private Loction loction = new Loction();
	private double lastXX = 99999, lastYY = 99999;
	private double distance;

	private void addPostion(Location loc) {
		Floor f = mapData.get(val);
		loction = new Loction();

		double xx = loction.location(loc.getX(), loc.getY(), currFloor)[0];
		double yy = loction.location(loc.getX(), loc.getY(), currFloor)[1];

		if (growslocSwitch) {
			distance = Math.sqrt((xx - lastXX) * (xx - lastXX) + (yy - lastYY)
					* (yy - lastYY));
			if (distance > Float.parseFloat(offset_distance)
					* currFloor.getScale()) {
				lastXX = xx;
				lastYY = yy;
			} else {
				xx = lastXX;
				yy = lastYY;
			}
		}

		// if (xx > mapData.get(val).getX() * f.getScale()
		// && xx < bmpWidth - mapData.get(val).getX() * f.getScale()
		// && yy > mapData.get(val).getY() * f.getScale()
		// && yy < bmpHeight - mapData.get(val).getY() * f.getScale())
		// {
		thisOne[0] = xx;
		thisOne[1] = yy;
		if (bFollow == true)// 跟随打开，开始打点
		{
			CircleShape postion = new CircleShape("NO" + postionNum, Color.RED);
			postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
			map.addShape(postion, bAnimation);
			RequestShape requestShape = new RequestShape("s", Color.BLUE, cv,
					MainActivity.this);
			requestShape.setValues(String.format("%.5f:%.5f:120", xx, yy));
			map.addShape(requestShape, bAnimation);
			postionNum++;
		} else {
			if (postionNum > 0) {
				for (int i = 0; i < postionNum; i++) {
					map.removeShape("NO" + i);
					map.removeShape("NOpath" + i);
				}
			}
			RequestShape requestShape = new RequestShape("s", Color.BLUE, cv,
					MainActivity.this);
			requestShape.setValues(String.format("%.5f:%.5f:120", xx, yy));
			map.addShape(requestShape, bAnimation);
			postionNum = 0;
		}
		// }
		handler.sendEmptyMessage(20);
	}

	private String AllMark = null;
	private String beforeMark = null;
	private ArrayList<Floor> place = new ArrayList<Floor>();

	public void initButtonOnClickListener() {
		click();
		iv_special.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					t = 0;
					linTouchY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					if (Math.abs(event.getY() - linTouchY) > 5) {
						t = 1;
					}
					break;
				case MotionEvent.ACTION_UP:
					if (t == 0) {
						if (logo4.getVisibility() == View.GONE) {
							logo4.setVisibility(View.VISIBLE);
						} else {
							logo4.setVisibility(View.GONE);
						}
					} else {
						if ((linTouchY - event.getY()) > 8
								&& logo4.getVisibility() == View.GONE) {
							logo4.setVisibility(View.VISIBLE);
						} else if ((event.getY() - linTouchY) > 8
								&& logo4.getVisibility() == View.VISIBLE) {
							logo4.setVisibility(View.GONE);
							pop.dismiss();
						}
					}
					break;
				default:
					break;
				}

				return true;
			}
		});
		rl_biaochi.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指第一次触摸到屏幕
					this.startX = (int) event.getRawX();
					this.startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:// 手指移动
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - this.startX;
					int dy = newY - this.startY;
					// 计算出控件原来的位置
					int l = MainActivity.this.rl_biaochi.getLeft();
					int r = MainActivity.this.rl_biaochi.getRight();
					int t = MainActivity.this.rl_biaochi.getTop();
					int b = MainActivity.this.rl_biaochi.getBottom();
					int newt = t + dy;
					int newb = b + dy;
					int newl = l + dx;
					int newr = r + dx;
					if ((newl < 0) || (newt < 0)
							|| (newr > MainActivity.this.width)
							|| (newb > MainActivity.this.height)) {
						break;
					}
					// 更新iv在屏幕的位置.
					MainActivity.this.rl_biaochi.layout(newl, newt, newr, newb);
					this.startX = (int) event.getRawX();
					this.startY = (int) event.getRawY();
					lps.leftMargin = MainActivity.this.rl_biaochi.getLeft();
					lps.topMargin = MainActivity.this.rl_biaochi.getTop();
					rl_biaochi.setLayoutParams(lps);
					break;
				case MotionEvent.ACTION_UP: // 手指离开屏幕的一瞬间
					break;
				}
				return true;
			}
		});
		btn_market.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mapData.size() == 0) {
					getMapData();
					return;
				}
				pw = new PopupWindow(show_market, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, true);
				pw.setBackgroundDrawable(getResources().getDrawable(
						MResource.getIdByName(getApplication(), "drawable",
								"market_item_bg")));
				pw.showAsDropDown(v, 0, 3);
			}
		});
		list_market.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AllMark = listMarket.get(position).toString();
				System.out.println("AllMark = " + AllMark);
				ArcadeCollection();
				if (!AllMark.equals(beforeMark)) {
					beforeMark = AllMark;
					marketAdapter.setSelectedPosition(position);
					marketAdapter.notifyDataSetChanged();
					for (int i = 0; i < mapData.size(); i++) {
						if (beforeMark.equals(mapData.get(i).getPlace())) {
							numZ = mapData.get(i).getFloorNo();
							break;
						}
					}
					pw.dismiss();
					showFloor(listMarket.get(position).toString());
					tv_sva_title.setText(beforeMark);
				}
				pw.dismiss();
			}
		});
		list_floor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (place.size() > 0) {
					for (int i = 0; i < place.size(); i++) {
						if (listFloor.get(position).equals(
								place.get(i).getFloor())) {
							numZ = place.get(i).getFloorNo();
							floorAdapter.setSelectedPosition(position);
							floorAdapter.notifyDataSetChanged();
							for (int j = 0; j < mapData.size(); j++) {
								if (mapData.get(j).getPlace().equals(AllMark)
										&& mapData
												.get(j)
												.getFloor()
												.equals(listFloor.get(position))) {
									val = j;
									downLoadMap();
								}
							}
							break;
						}
					}
				}
			}
		});
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (checkConnect()) {
					if (isRequestLocation) {
						creatPath("————————定位结束");
						view.setBackgroundResource(MResource.getIdByName(
								getApplication(), "drawable", "location2"));
						isRequestLocation = false;
						clearRequestmessage();
						markun.setVisibility(View.GONE);
					} else {
						if (mapData.size() == 0) {
							getMapData();
							return;
						}
						creatPath("————————定位开始");
						oriLocation[0] = 0;
						oriLocation[1] = 0;
						view.setBackgroundResource(MResource.getIdByName(
								getApplication(), "drawable", "location1"));
						isRequestLocation = true;
						updataTime = System.currentTimeMillis();
					}
				} else {
					if (isRequestLocation) {
						view.setBackgroundResource(MResource.getIdByName(
								getApplication(), "drawable", "location2"));
						isRequestLocation = false;

						clearRequestmessage();
					} else {
						Toast.makeText(
								MainActivity.this,
								getString(MResource.getIdByName(
										getApplication(), "string", "noNet")),
								Toast.LENGTH_SHORT).show();
					}

				}
			}
		});

	}

	/** 清除展示的所有推送消息 */
	private void clearMessage() {
		if (p != 0) {
			for (int i = 0; i < p; i++) {
				map.removeShape("pushShape" + i);
			}
			map.removeShape("message");
			if (bubble != null) {
				bubble.setVisibility(View.INVISIBLE);
			}
		}
	}

	/** 清除地图上所有的Shape */
	private void clearRequestmessage() {

		for (int i = 0; i < postionNum; i++) {
			map.removeShape("NO" + i);
			map.removeShape("NOpath" + i);
		}
		clearMessage();
		p = 0;
		postionNum = 0;
		map.removeShape("message");
		map.removeShape("NO");
		map.removeShape("NOpath");
		messageMap.clear();
	}

	public void clickMenu(View view) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingActivity.class);
		Bundle bundle = new Bundle();
		if (currFloor == null) {
			bundle.putInt("startLangrage", 0);
			bundle.putString("path", "");
			bundle.putDouble("xSport", 0);
			bundle.putDouble("ySport", 0);
			bundle.putDouble("scale", 0);
			bundle.putString("place", "");
			bundle.putInt("floorNo", 0);
			bundle.putString("floor", "");
			bundle.putSerializable("currFloor", "");
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
			Log.i("TAG", "currFloor为空时跳。。。。。。。");
			return;
		}
		bundle.putInt("startLangrage", startLangrage);
		bundle.putString("path", path);
		bundle.putDouble("xSport", currFloor.getX());
		bundle.putDouble("ySport", currFloor.getY());
		bundle.putDouble("scale", currFloor.getScale());
		bundle.putString("place", currFloor.getPlace());
		bundle.putInt("placeId", currFloor.getPlaceId());
		bundle.putInt("floorNo", currFloor.getFloorNo());
		bundle.putString("floor", currFloor.getFloor());
		bundle.putSerializable("currFloor", currFloor);
		intent.putExtras(bundle);
		startActivityForResult(intent, 0);
		Log.i("palceId", String.valueOf(currFloor.getPlaceId()));
		Log.i("TAG", "main跳转set成功");
	}

	private String path = null;

	private void downLoadMap() {
		switchBool = true;
		map.clearShapes();
		pop.dismiss();
		rl_push.setVisibility(View.GONE);
		currFloor = mapData.get(val);
		tv_sva_title.setText(currFloor.getPlace());
		load.setPlaceId(currFloor.getPlaceId());
		toSubscription();
		getPrruSubscribe();
		final Floor f = mapData.get(val);
		GlobalConfig.setxSport(f.getX());
		GlobalConfig.setySport(f.getY());
		GlobalConfig.setProportion(f.getScale());

		/**
		 * if (fu.isFileExist("SVA/" + f.getPlace() + "_" + f.getFloorNo() +
		 * ".jpg")) { path = "/SVA/" + f.getPlace() + "_" + f.getFloorNo() +
		 * ".jpg"; handler.sendEmptyMessage(0); } else if (fu.isFileExist("SVA/"
		 * + f.getPlace() + "_" + f.getFloorNo() + ".png")) { path = "/SVA/" +
		 * f.getPlace() + "_" + f.getFloorNo() + ".png";
		 * handler.sendEmptyMessage(1); } else {
		 */
		mapLoadfinish = false;
		mapPathInfo = new MapData(mContext, map, "test");
		String dowload = getString(
				MResource.getIdByName(getApplication(), "string", "loadMap"))
				.toString();
		progressDialog = ProgressDialog.show(this, "Loading", dowload, true,
				false);

		new Thread() {
			@Override
			public void run() {
				// 执行完毕后给handler发送一个空消息
				HttpDownloader downloader = new HttpDownloader();
				String url = "";
				try {
					if (!f.getSvg().equals("null")) {
						url = "http://" + GlobalConfig.server_ip
								+ "/sva/upload/"
								+ URLEncoder.encode(f.getSvg(), "UTF-8");

					} else {

						url = "http://" + GlobalConfig.server_ip
								+ "/sva/upload/"
								+ URLEncoder.encode(f.getPath(), "UTF-8");
					}

				} catch (Exception e) {
					Log.e("error", e + "");
				}
				// 判断是否下载
				bDownLoad = myPreferences.getBoolean(
						f.getPlace() + "_" + f.getFloorNo(), false);
				updateTime = myPreferences.getLong(
						f.getPlace() + "-" + f.getFloorNo(), 1);
				Log.e(bDownLoad + ">>>>", "" + updateTime);
				if (bDownLoad && updateTime == currFloor.getUpdateTime()) {
					File file = new File(
							Environment.getExternalStorageDirectory() + "/SVA/"
									+ f.getPlace() + "_" + f.getFloorNo()
									+ ".png");
					if (file.exists()) {
						path = "/SVA/" + f.getPlace() + "_" + f.getFloorNo()
								+ ".png";
						handler.sendEmptyMessage(1);
					} else {
						path = "/SVA/" + f.getPlace() + "_" + f.getFloorNo()
								+ ".jpg";
						handler.sendEmptyMessage(1);
					}
					return;
				}

				int result = 0;
				if (url.lastIndexOf("jpg") > -1) {
					result = downloader.downFile(url, "SVA/", f.getPlace()
							+ "_" + f.getFloorNo() + ".jpg");
					if (result == -1) {
						handler.sendEmptyMessage(-1);
						return;
					} else {
						path = "/SVA/" + f.getPlace() + "_" + f.getFloorNo()
								+ ".jpg";
						editor.putBoolean(f.getPlace() + "_" + f.getFloorNo(),
								true);
						editor.putLong(f.getPlace() + "-" + f.getFloorNo(),
								currFloor.getUpdateTime());
						editor.commit();
						handler.sendEmptyMessage(1);
					}

				} else if (url.lastIndexOf("png") > -1) {
					result = downloader.downFile(url, "SVA/", f.getPlace()
							+ "_" + f.getFloorNo() + ".png");
					if (result == -1) {
						handler.sendEmptyMessage(-1);
					} else {
						path = "/SVA/" + f.getPlace() + "_" + f.getFloorNo()
								+ ".png";
						editor.putBoolean(f.getPlace() + "_" + f.getFloorNo(),
								true);
						editor.putLong(f.getPlace() + "-" + f.getFloorNo(),
								currFloor.getUpdateTime());
						editor.commit();
						handler.sendEmptyMessage(1);
					}
				} else {
					result = downloader.downFile(url, "SVA/", f.getPlace()
							+ "_" + f.getFloorNo() + ".svg");
					if (result == -1) {
						try {
							url = "http://" + GlobalConfig.server_ip
									+ "/sva/upload/"
									+ URLEncoder.encode(f.getPath(), "UTF-8");
							if (url.lastIndexOf("jpg") > -1) {
								result = downloader.downFile(url, "SVA/",
										f.getPlace() + "_" + f.getFloorNo()
												+ ".jpg");
								if (result == -1) {
									handler.sendEmptyMessage(-1);
								} else {
									path = "/SVA/" + f.getPlace() + "_"
											+ f.getFloorNo() + ".jpg";
									editor.putBoolean(
											f.getPlace() + "_" + f.getFloorNo(),
											true);
									editor.putLong(
											f.getPlace() + "-" + f.getFloorNo(),
											currFloor.getUpdateTime());
									editor.commit();
									handler.sendEmptyMessage(1);
								}
							} else if (url.lastIndexOf("png") > -1) {
								result = downloader.downFile(url, "SVA/",
										f.getPlace() + "_" + f.getFloorNo()
												+ ".png");
								if (result == -1) {
									handler.sendEmptyMessage(-1);
								} else {
									path = "/SVA/" + f.getPlace() + "_"
											+ f.getFloorNo() + ".png";
									editor.putBoolean(
											f.getPlace() + "_" + f.getFloorNo(),
											true);
									editor.putLong(
											f.getPlace() + "-" + f.getFloorNo(),
											currFloor.getUpdateTime());
									editor.commit();
									handler.sendEmptyMessage(1);
								}
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						progressDialog.dismiss();
						return;
					}
					path = "/SVA/" + f.getPlace() + "_" + f.getFloorNo()
							+ ".svg";
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	// 定义Handler对象
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				String wrong = getString(
						MResource.getIdByName(getApplication(), "string",
								"wrongMap")).toString();
				Toast.makeText(MainActivity.this, wrong, Toast.LENGTH_LONG)
						.show();
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				bitmap = BitmapFactory.decodeResource(getResources(), MResource
						.getIdByName(getApplication(), "drawable", "imm_01"));
				map.setMapBitmap(bitmap);
				numZ = 0;
				progressDialog.dismiss();
				break;
			case 0:
				switchMap(Environment.getExternalStorageDirectory() + path);
				break;
			case 1:
				switchMap1(Environment.getExternalStorageDirectory() + path);
				break;
			case 2:
				Toast.makeText(
						MainActivity.this,
						getString(MResource.getIdByName(getApplication(),
								"string", "checkNet")), Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				iv_biaochi.setVisibility(View.VISIBLE);
				tv_biaochi.setVisibility(View.VISIBLE);
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				iv_biaochi.setVisibility(View.INVISIBLE);
				tv_biaochi.setVisibility(View.INVISIBLE);
				break;

			case 7:
				// 得到可用的图片
				iv_special.setImageBitmap(pushInfoBitmap);
				logo4.setImageBitmap(pushInfoBitmap);
				break;
			case 8:
				// 得到可用的图片
				logo2.setImageBitmap(pushInfoBitmap);
				break;
			case 9:
				ivvip.setImageBitmap(pushInfoBitmap);
				break;
			case 10:
				iv_push_small1.setImageBitmap(pushInfoBitmap);
				iv_push_small1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						CustomDialog.Builder builder = new CustomDialog.Builder(
								MainActivity.this);
						View v = View.inflate(MainActivity.this, MResource
								.getIdByName(getApplication(), "layout",
										"image_center"), null);
						iv_image_center = (ImageView) v.findViewById(MResource
								.getIdByName(getApplication(), "id",
										"iv_image_center"));
						linear_image = (LinearLayout) v.findViewById(MResource
								.getIdByName(getApplication(), "id",
										"linear_image"));
						iv_image_center.setImageBitmap(pushInfoBitmap);
						final CustomDialog dia = builder.create();
						dia.setContentView(v);
						dia.show();
						WindowManager.LayoutParams params = dia.getWindow()
								.getAttributes();
						params.width = width;
						params.height = height;
						dia.getWindow().setAttributes(params);
						linear_image.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dia.dismiss();
							}
						});
					}
				});

				break;
			case 11:
				iv_image_center.setImageBitmap(vipBigBitmap);
				break;
			case 12:
				iv_special.setImageBitmap(defaultBitmap);
				logo4.setImageBitmap(defaultBitmap);
				break;
			case 13:
				iv_push_small1.setImageBitmap(defaultBitmap);
				iv_push_small1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						CustomDialog.Builder builder = new CustomDialog.Builder(
								MainActivity.this);
						View v = View.inflate(MainActivity.this, MResource
								.getIdByName(getApplication(), "layout",
										"image_center"), null);
						iv_image_center = (ImageView) v.findViewById(MResource
								.getIdByName(getApplication(), "id",
										"iv_image_center"));
						linear_image = (LinearLayout) v.findViewById(MResource
								.getIdByName(getApplication(), "id",
										"linear_image"));
						iv_image_center.setImageBitmap(defaultBitmap);
						final CustomDialog dia = builder.create();
						dia.setContentView(v);
						dia.show();
						WindowManager.LayoutParams params = dia.getWindow()
								.getAttributes();
						params.width = width;
						params.height = height;
						dia.getWindow().setAttributes(params);
						linear_image.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dia.dismiss();
							}
						});
					}
				});
				break;
			case 14:
				LogcatHelper.getInstance(MainActivity.this).stop();
				finish();
				System.exit(0);
				break;
			case 15:
				unSubscribePrru();
			case 18:
				readPath();
				break;
			case 20:
				markun.setVisibility(View.VISIBLE);
				markun.setText(timestampPrru);
				break;
			default:
				break;
			}
			// 处理UI
		}

	};

	public void readBitmapViaVolley(String imgUrl, final ImageView imageView) {
		ImageRequest imgRequest = new ImageRequest(imgUrl,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {

						map.reset();
						Utils.saveBitmap(arg0, "bb.jpg");
						FileInputStream fis;
						Bitmap bitmap = null;
						try {
							fis = new FileInputStream("/sdcard/SVA/bb.jpg");
							bitmap = BitmapFactory.decodeStream(fis);
						} catch (Exception e) {
							Log.e("error", e + "");
						}
						map.setMapBitmap(bitmap);
						String yes = getString(
								MResource.getIdByName(getApplication(), "yes",
										"yes")).toString();
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("标题")
								.setMessage(
										map.getLayoutParams().height + "--"
												+ map.getLayoutParams().width)
								.setPositiveButton(yes, null).show();

					}
				}, 300, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		mRequestQueue.add(imgRequest);
	}

	private FileUtils utils;

	private void creatPath(String content) {
		utils = new FileUtils();
		String filePath = "/sdcard/SVA";
		String fileName = "/point.txt";
		utils.writeTxtToFile(content, filePath, fileName);
		System.out.println();
	}

	private void unSubscribePrru() {
		// getDataParam:{"error":null,"data":[{"id":1,"exceedMaxDeviation":5,"weight":32,
		// "updateTime":1452222338000,"banThreshold":0.7,"biasSpeed":1.33,"maxDeviation":8,
		// "spr":1.26}]}
		Map<String, String> params = new HashMap<String, String>();
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/unSubscribePrru?storeId="
						+ currFloor.getPlaceId() + "&ip="
						+ load.getLocaIpOrMac(), new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.d(TAG, "unSubscribePrru:" + jsonobj.toString());
						MapConstant.startedApp = false;
						MapConstant.start = false;
						System.exit(0);
						try {
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "unSubscribePrru:", error);
						System.out.println("unSubscribePrru:"
								+ error.toString());

						// String wrong = getString(
						// MResource.getIdByName(getApplication(), "string",
						// "wrong")).toString();
						// Toast.makeText(MainActivity.this, wrong,
						// Toast.LENGTH_LONG).show();
						MapConstant.startedApp = false;
						MapConstant.start = false;
						System.exit(0);

					}
				}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(300, 1, 1.0f));
		mRequestQueue.add(newMissRequest);
	}

	@Override
	public void onBackPressed() {

		if (isFrom || isTo || pop.isShowing()
				|| (bubble != null && bubble.getVisibility() == View.VISIBLE)) {
			isFrom = false;
			isTo = false;
			pop.dismiss();
			bubble.setVisibility(View.INVISIBLE);
			return;
		}
		String no = getString(
				MResource.getIdByName(getApplication(), "string", "no"))
				.toString();
		String yes = getString(
				MResource.getIdByName(getApplication(), "string", "yes"))
				.toString();
		String exit = getString(
				MResource.getIdByName(getApplication(), "string", "exit"))
				.toString();
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle(
				getString(MResource.getIdByName(getApplication(), "string",
						"prompt"))).setMessage(exit);
		builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				handler.sendEmptyMessage(15);

			}
		});
		builder.setNegativeButton(no,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// mRequestQueue.cancelAll(this);
	}

	// Scrolling flag
	@SuppressWarnings("unused")
	private boolean scrolling = false;
	private int val = 0;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		isBack = false;
		// isRequestLocation = false;// 点击定位后，终止requsetLocction的线程
		requestLocationTask();
		switch (resultCode) {
		case RESULT_OK:
			// isRun=false;
			if (data.getExtras().getString("server").equals("change")) {
				isBack = true;
				timer.cancel();
				isRun = false;
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				if (MapConstant.bitmap != null) {
					MapConstant.bitmap.recycle();
					MapConstant.bitmap = null;
				}
				String saveInfo = getString(
						MResource.getIdByName(getApplication(), "string",
								"saveInfo")).toString();
				Toast.makeText(MainActivity.this, saveInfo, Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);
				startActivity(intent);
				LogcatHelper.getInstance(MainActivity.this).stop();
				MainActivity.this.finish();
			}
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}

	@Override
	protected void onPause() {
		isBack = true;
		// 在暂停的生命周期里注销传感器服务和位置更新服务
		sensorMag.unregisterListener(sensorLis);
		super.onPause();
		mStopDrawing = true;
		if (mOrientationSensor != null) {
			mSensorManager.unregisterListener(mOrientationSensorEventListener);
		}
		sensorMag.unregisterListener(sensorLis);
		handler.removeCallbacks(mCompassViewUpdater);
	}

	private String send_time;
	private String content;

	@Override
	protected void onResume() {
		boolean last = isBack;
		isBack = false;
		// 设置语言
		String language = myPreferences.getString("Language", "English");
		if (language.equals("English")) {
			setLang(Locale.ENGLISH);
		} else {
			setLang(Locale.SIMPLIFIED_CHINESE);
		}
		// 是否判断语言环境变化的标志
		isStop = true;
		if (last) {
			requestLocationTask();
		}
		ip = myPreferences.getString("serverIp", "223.202.102.66:8086");
		Log.i("ippp", ip);
		upload = myPreferences.getString("upload", "IP");
		send_time = myPreferences.getString("send_time", "25");
		content = myPreferences.getString("content", "send");
		autoLogin = myPreferences.getBoolean("autoPush", true);
		biaochi = myPreferences.getBoolean("biaochi", true);
		bAnimation = myPreferences.getBoolean("bAnimation", true);
		offset_distance = myPreferences.getString("distance", "5");
		map.setAllowRequestTranslate(bAnimation);
		map.setAllowTranslate(bAnimation);
		MapConstant.wifiSwitch = myPreferences.getBoolean("wifiSwitch", false);
		growslocSwitch = myPreferences.getBoolean("growslocSwitch", false);
		bGLFloorSwitch = myPreferences.getBoolean("growslocFloorSwitch", true);
		autoSwitch = myPreferences.getBoolean("autoSwitch", true);
		vipShow = myPreferences.getBoolean("vipShow", true);
		bFollow = myPreferences.getBoolean("follow", false);
		bIsEnable = myPreferences.getBoolean("bIsEnable", true);
		GlobalConfig.setEnable(bIsEnable);
		GlobalConfig.setServerIp(ip);
		GlobalConfig.setSend_time(Integer.parseInt(send_time));
		GlobalConfig.setContent(content);
		GlobalConfig.setIdentification(upload);

		sensorMag.registerListener(sensorLis, gravitySensor,
				SensorManager.SENSOR_DELAY_UI);

		super.onResume();

		if (mOrientationSensor != null) {
			mSensorManager.registerListener(mOrientationSensorEventListener,
					mOrientationSensor, SensorManager.SENSOR_DELAY_GAME);
		}
		mStopDrawing = false;
		handlers.postDelayed(mCompassViewUpdater, 20);// 20毫秒执行一次更新指南针图片旋转
		MapConstant.local = getResources().getConfiguration().locale;
		sensorMag.registerListener(sensorLis, gravitySensor,
				SensorManager.SENSOR_DELAY_UI);

	}

	protected void setLang(Locale l) {

		Configuration cfg = getResources().getConfiguration();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		cfg.locale = l;
		getResources().updateConfiguration(cfg, dm);
	}

	private int a = 0;
	private ImageView iv_special;
	private boolean isAlive = false;
	private boolean isAlive2 = false;
	private boolean isAlive4 = false;
	private boolean isAlive5 = false;
	private boolean isFrom = false;
	private boolean isTo = false;
	private float evX = 0;
	private float evY = 0;
	int[][] W1;
	int[][] W2;
	PointF pointf = null;

	private int[] touch = new int[2];

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			a = 0;
			evX = ev.getX();
			evY = ev.getY();
			map.removeShape("touch");
			// handler.postDelayed(runnable, 600);
			break;
		case MotionEvent.ACTION_MOVE:
			if (Math.abs(evX - ev.getX()) > 5 || Math.abs(evY - ev.getY()) > 5) {
				a = 1;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (a == 0) {
				if (rl_push.getVisibility() == View.VISIBLE
						&& ev.getY() < height - rl_push.getHeight()
						&& ev.getY() > rl_title.getBottom() + 50) {
					rl_push.setVisibility(View.GONE);
					map.removeShape("message");
				}

				linPushMessage.getLocationOnScreen(touch);
				if (pop.isShowing() && ev.getY() < touch[1]
						&& ev.getY() > rl_title.getBottom() + 50) {
					logo4.setVisibility(View.GONE);
					pop.dismiss();
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private PointF touchPoint = new PointF();
	private float rotate;

	/**
	 * 地图缩放
	 */
	private void click() {
		tex.setText("");
		map.setOnRotateListener(new OnRotateListener() {
			@Override
			public void onRotate(float rotate) {
				MainActivity.this.rotate = rotate;
			}
		});
		compass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				map.releaseImageShow();
			}
		});
		tobig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				map.pullScale();
			}
		});
		tosmall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				map.zoomScale();
			}
		});
		map.setOnShapeClickListener(new OnShapeActionListener() {
			@Override
			public void onPushMessageShapeClick(PushMessageShape shape,
					float xOnImage, float yOnImage) {
				map.removeShape("message");
				touchPoint.set(xOnImage, yOnImage);
				if (bubble == null) {
					bubble2 = getLayoutInflater().inflate(
							MResource.getIdByName(getApplication(), "layout",
									"push_message_shape"), null);
					bubble = bubble2;
				} else {
					if (bubble == bubble2) {
						isAlive5 = true;
						return;
					} else {
						bubble.setVisibility(View.INVISIBLE);
						bubble = null;
						bubble2 = getLayoutInflater().inflate(
								MResource.getIdByName(getApplication(),
										"layout", "push_message_shape"), null);
						bubble = bubble2;
					}
				}
				isAlive2 = true;
				setMsgBubbleview();
			}

			@Override
			public void onSpecialShapeClick(SpecialShape shape, float xOnImage,
					float yOnImage) {
				touchPoint.set(xOnImage, yOnImage);
				handler.sendEmptyMessage(12);
				if (bubble == null) {
					bubble1 = getLayoutInflater().inflate(
							MResource.getIdByName(getApplication(), "layout",
									"popupview"), null);
					bubble = bubble1;
				} else {
					if (bubble == bubble1) {
						isAlive4 = true;
						return;
					} else {
						isAlive4 = false;
						bubble.setVisibility(View.INVISIBLE);
						bubble = null;
						bubble1 = getLayoutInflater().inflate(
								MResource.getIdByName(getApplication(),
										"layout", "popupview"), null);
						bubble = bubble1;
					}
				}
				isAlive = true;
				setSpecialBubbleView();
			}

			@Override
			public void onCollectShapeClick(CollectPointShape shape,
					float xOnImage, float yOnImage) {

			}

			@Override
			public void outShapeClick(float xOnImage, float yOnImage) {

			}

			@Override
			public void onMoniShapeClick(MoniPointShape shape, float xOnImage,
					float yOnImage) {
				// TODO Auto-generated method stub

			}
		});
	}

	/** 消息推送的点击消息 */
	private void setMsgBubbleview() {
		map.setBubbleView(bubble2, new RenderDelegate() {
			@Override
			public void onDisplay(final Shape shape, View bubbleView) {
				bubbleTag = (String) shape.tag;
				if (isAlive5) {
					isAlive2 = true;
				}

				if (isAlive2) {
					rl_push.setVisibility(View.VISIBLE);
					if (tv_title_push1.getText().toString()
							.equals(shape.getTitle())) {
						Log.i("go", ">>>>>>>>");
						isAlive2 = false;
						isAlive5 = false;
						return;
					}
					Log.i("go", "go>>>>>>>>>>>>>>>");
					tv_title_push1.setText(shape.getContent());
					handler.sendEmptyMessage(13);
					Thread thread = new Thread() {
						@Override
						public void run() {
							try {
								// 创建一个url对象
								System.out.println("++++++");
								isAlive5 = false;
								isAlive2 = false;
								System.out.println(shape.getPictureUrl());
								URL url = new URL(shape.getPictureUrl());
								// 打开URL对应的资源输入流
								HttpURLConnection urlConn = null;

								urlConn = (HttpURLConnection) url
										.openConnection();
								urlConn.setConnectTimeout(5000);
								InputStream is = urlConn.getInputStream();
								// 从InputStream流中解析出图片
								pushInfoBitmap = BitmapFactory.decodeStream(is);
								handler.sendEmptyMessage(10);
								// 关闭输入流
								is.close();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					thread.start();
					tv_video.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (shape.getUrl() != null) {
								isStop = false;
								Intent intent = new Intent(MainActivity.this,
										VideoPlayerAcitivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("url", shape.getUrl());
								intent.putExtras(bundle);
								startActivity(intent);
							} else {
								Toast.makeText(mContext,
										getString(R.string.no_video),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
	}

	/** 特定商户的点击信息显示 */
	private void setSpecialBubbleView() {
		map.setBubbleView(bubble1, new RenderDelegate() {
			@Override
			public void onDisplay(final Shape shape, View bubbleView) {
				bubbleTag = (String) shape.tag;
				if (isAlive4) {
					isAlive = true;
				}

				if (isAlive) {
					tv_title_push1.setText("");
					iv_button = (ImageView) bubble1.findViewById(MResource
							.getIdByName(getApplication(), "id", "iv_button"));
					Log.i("run", "run");
					Thread thread = new Thread() {
						@Override
						public void run() {
							try {
								// 创建一个url对象
								isAlive4 = false;
								isAlive = false;
								URL url = new URL(shape.getPictureUrl());
								// 打开URL对应的资源输入流
								HttpURLConnection urlConn = null;

								urlConn = (HttpURLConnection) url
										.openConnection();
								urlConn.setConnectTimeout(5000);
								InputStream is = urlConn.getInputStream();
								// 从InputStream流中解析出图片
								pushInfoBitmap = BitmapFactory.decodeStream(is);
								handler.sendEmptyMessage(7);
								// 关闭输入流
								is.close();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					thread.start();
					tex.setText(shape.getContent() + "");
					pop.showAtLocation(vi_special, Gravity.BOTTOM, 0, 0);
					// 从这里出发
					btn_s.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if (mapPathInfo.hasRouteData()) {
								mapPathInfo.setStart(touchPoint, viewStart);
								bubble1.setVisibility(View.GONE);
								map.removeShape("touch");
								isFrom = true;
								if (isFrom && isTo) {
									mapPathInfo.findPathAndDraw();
									pop.dismiss();
								}
							}

						}
					});
					// 到这里去
					btn_e.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if (mapPathInfo.hasRouteData()) {
								mapPathInfo.setEnd(touchPoint, viewEnd);
								bubble1.setVisibility(View.GONE);
								isTo = true;
								map.removeShape("touch");
								if (isFrom && isTo) {
									mapPathInfo.findPathAndDraw();
									pop.dismiss();
								}
							}

						}
					});
					iv_button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if (pop.isShowing()) {
								pop.dismiss();
								return;
							}
						}
					});
					tvpop_video.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (shape.getUrl() != null) {
								isStop = false;
								Intent intent = new Intent(MainActivity.this,
										VideoPlayerAcitivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("url", shape.getUrl());
								intent.putExtras(bundle);
								startActivity(intent);
							} else {
								Toast.makeText(mContext,
										getString(R.string.no_video),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
	}

	/**
	 * 根据屏幕大小初始化放大缩小按钮大小
	 * 
	 * @param mapPath
	 */
	public void initBigSmallSize() {
		android.view.ViewGroup.LayoutParams layoutParams1 = tobig
				.getLayoutParams();
		layoutParams1.width = width / 9;
		layoutParams1.height = (int) (width / 8.5);
		tobig.setLayoutParams(layoutParams1);

		android.view.ViewGroup.LayoutParams layoutParams2 = tosmall
				.getLayoutParams();
		layoutParams2.width = width / 9;
		layoutParams2.height = (int) (width / 8.5);
		tosmall.setLayoutParams(layoutParams2);

		android.view.ViewGroup.LayoutParams layoutParams3 = button3
				.getLayoutParams();
		layoutParams3.width = width / 9;
		layoutParams3.height = width / 9;
		button3.setLayoutParams(layoutParams3);

		android.view.ViewGroup.LayoutParams layoutParams4 = ll_list
				.getLayoutParams();
		layoutParams4.width = width / 9;
		layoutParams4.height = (int) (width / 2.6);
		ll_list.setLayoutParams(layoutParams4);
	}

	/**
	 * 更新特殊位置商家
	 */
	private ArrayList<SpecialShape> arraySpecial = new ArrayList<SpecialShape>();
	private ArrayList<PointF> arrayPoint = new ArrayList<PointF>();
	private boolean switchBool = false;
	protected Runnable update = new Runnable() {
		@Override
		public void run() {
			if (bubble != null) {
				if (bubble.getVisibility() == View.INVISIBLE) {
					pop.dismiss();
					logo4.setVisibility(View.GONE);
				}
			}
			if (arraySpecial.size() > 0 && !switchBool) {
				if (scaleB >= 1) {
					Log.i("222222222222222222", "11");
					for (int i = 0; i < arraySpecial.size(); i++) {
						if (!map.getShape(arraySpecial.get(i).getTag())) {
							Log.i("222222222222222222", "22");
							arraySpecial.get(i).setValues(
									String.format("%.5f:%.5f:35",
											arrayPoint.get(i).x,
											arrayPoint.get(i).y));
							map.addShape(arraySpecial.get(i), false);
						}
					}
				} else if (scaleB < 1 && scaleB > 0.7) {
					for (int i = 0; i < arraySpecial.size(); i++) {
						if (arraySpecial.get(i).getLevel() % 5 == 0) {
							if (map.getShape(arraySpecial.get(i).getTag())) {
								map.removeShape(arraySpecial.get(i).getTag());

								if (map.getBubblePosition().equals(
										arraySpecial.get(i).getCenterPoint().x,
										arraySpecial.get(i).getCenterPoint().y)) {
									bubble.setVisibility(View.INVISIBLE);
								}
							}
						} else {
							if (!map.getShape(arraySpecial.get(i).getTag())) {
								arraySpecial.get(i).setValues(
										String.format("%.5f:%.5f:35",
												arrayPoint.get(i).x,
												arrayPoint.get(i).y));
								map.addShape(arraySpecial.get(i), false);
							}
						}
					}
				} else if (scaleB <= 0.7 && scaleB > 0.4) {
					for (int i = 0; i < arraySpecial.size(); i++) {
						if (arraySpecial.get(i).getLevel() % 3 == 0) {
							if (map.getShape(arraySpecial.get(i).getTag())) {
								map.removeShape(arraySpecial.get(i).getTag());
								if (map.getBubblePosition().equals(
										arraySpecial.get(i).getCenterPoint().x,
										arraySpecial.get(i).getCenterPoint().y)) {
									bubble.setVisibility(View.INVISIBLE);
								}
							}
						} else {
							if (!map.getShape(arraySpecial.get(i).getTag())) {
								arraySpecial.get(i).setValues(
										String.format("%.5f:%.5f:35",
												arrayPoint.get(i).x,
												arrayPoint.get(i).y));
								map.addShape(arraySpecial.get(i), false);
							}
						}
					}
				} else if (scaleB <= 0.4 && scaleB > 0.1) {
					for (int i = 0; i < arraySpecial.size(); i++) {
						if (arraySpecial.get(i).getLevel() % 2 == 0) {
							if (map.getShape(arraySpecial.get(i).getTag())) {
								Log.i("1111111111111111", "11");
								map.removeShape(arraySpecial.get(i).getTag());
								if (map.getBubblePosition().equals(
										arraySpecial.get(i).getCenterPoint().x,
										arraySpecial.get(i).getCenterPoint().y)) {
									bubble.setVisibility(View.INVISIBLE);
								}
							}
						} else {
							if (!map.getShape(arraySpecial.get(i).getTag())) {
								arraySpecial.get(i).setValues(
										String.format("%.5f:%.5f:35",
												arrayPoint.get(i).x,
												arrayPoint.get(i).y));
								map.addShape(arraySpecial.get(i), false);
							}
						}
					}
				}
				handlers.postDelayed(update, 500);
			}
		}

	};
	private final float MAX_ROATE_DEGREE = 1.0f;// 最多旋转一周，即360°
	private SensorManager mSensorManager;// 传感器管理对象
	private Sensor mOrientationSensor;// 传感器对象
	private float mDirection;// 当前浮点方向
	private float mTargetDirection;// 目标浮点方向
	private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
	protected final Handler handlers = new Handler();
	private boolean mStopDrawing;// 是否停止指南针旋转的标志位
	int stepSamplingCount = 0;
	private Object lock = new Object();

	// 这个是更新指南针旋转的线程，handler的灵活使用，每20毫秒检测方向变化值，对应更新指南针旋转
	protected Runnable mCompassViewUpdater = new Runnable() {
		@Override
		public void run() {
			scaleB = map.getZoom();
			if (currFloor != null) {
				setStaff();
			} else {
				int change = (int) Math.rint((double) 120 / (double) 22
						/ map.getZoom());
				tv_biaochi.setText(change
						+ getString(MResource.getIdByName(getApplication(),
								"string", "mi")));
				android.view.ViewGroup.LayoutParams layoutParams = iv_biaochi
						.getLayoutParams();
				layoutParams.width = (int) (change * 22 * map.getZoom());
				iv_biaochi.setLayoutParams(layoutParams);

			}
			if (cv != null && !mStopDrawing) {
				setRoutine();
				float[] sensorTempToAdd = new float[10];
				for (int i = 0; i < sensorTemp.length; i++) {
					sensorTempToAdd[i] = sensorTemp[i];
				}
				handlers.postDelayed(mCompassViewUpdater, 20);// 20毫秒后重新执行自己
			}
		}

	};

	private void setRoutine() {
		if (mDirection != mTargetDirection) {
			// calculate the short routine
			float to = mTargetDirection;
			if (to - mDirection > 180) {
				to -= 360;
			} else if (to - mDirection < -180) {
				to += 360;
			}
			// limit the max speed to MAX_ROTATE_DEGREE
			float distance = to - mDirection;
			if (Math.abs(distance) > MAX_ROATE_DEGREE) {
				distance = distance > 0 ? MAX_ROATE_DEGREE
						: (-1.0f * MAX_ROATE_DEGREE);
			}
			// need to slow down if the distance is shortl
			mDirection = normalizeDegree(mDirection
					+ ((to - mDirection) * mInterpolator.getInterpolation(Math
							.abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));// 用了一个加速动画去旋转图片
			cv.updateDirection(mDirection);// 更新指南针旋转
			if (currFloor != null) {
				if (mapLoadfinish) {
					if (isGetLocation) {
						RequestShape requestShape = new RequestShape("s",
								Color.BLUE, cv, MainActivity.this);
						requestShape.setValues(String.format("%.5f:%.5f:120",
								loction.location(thisOne[0], thisOne[1],
										currFloor)[0], loction.location(
										thisOne[0], thisOne[1], currFloor)[1]));
						map.addShape(requestShape, false);
						isGetLocation = false;
					} else {
						RequestShape requestShape = new RequestShape("s",
								Color.BLUE, cv, MainActivity.this);
						requestShape.setValues(String.format("%.5f:%.5f:120",
								thisOne[0], thisOne[1]));
						map.addShape(requestShape, false);
					}
				}
			}
		}
	}

	private boolean isRun = true;
	protected Runnable processObj = new Runnable() {
		@Override
		public void run() {
			float[][] senorData = new float[10][50];
			while (isRun) {
				synchronized (lock) {
					System.out.println("wait to process");
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.arraycopy(dataMem, 0, senorData, 0, dataMem.length);
					System.out.println("process copy 50 array " + count);
				}
				System.out.println("process start");
				String sss = "";
				for (int i = 0; i < 50; i++) {
					for (int j = 0; j < 10; j++) {
						sss = sss + " " + senorData[j][i];
					}
				}
				long timeStart = System.currentTimeMillis();
				long timeEnd = System.currentTimeMillis();
				if (timeEnd - timeStart > 1000) {
					creatPath((timeEnd - timeStart) + " 计算超时");
				}
			}
		}
	};

	private void setStaff() {
		int change = (int) Math.rint((double) 120 / currFloor.getScale()
				/ map.getZoom());
		if (change == 0) {
			change = 1;
		}
		tv_biaochi.setText(change
				+ getString(MResource.getIdByName(getApplication(), "string",
						"mi")));
		android.view.ViewGroup.LayoutParams layoutParams = iv_biaochi
				.getLayoutParams();
		layoutParams.width = (int) (change * currFloor.getScale() * map
				.getZoom());
		iv_biaochi.setLayoutParams(layoutParams);
	}

	// 初始化view
	private void initResources() {
		mDirection = 0.0f;// 初始化起始方向
		mTargetDirection = 0.0f;// 初始化目标方向
		mInterpolator = new AccelerateInterpolator();// 实例化加速动画对象
		mStopDrawing = true;
	}

	// 初始化传感器和位置服务
	@SuppressWarnings("deprecation")
	private void initServices() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mOrientationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		Criteria criteria = new Criteria();// 条件对象，即指定条件过滤获得LocationProvider
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 较高精度
		criteria.setAltitudeRequired(false);// 是否需要高度信息
		criteria.setBearingRequired(false);// 是否需要方向信息
		criteria.setCostAllowed(false);// 是否产生费用
		criteria.setPowerRequirement(Criteria.POWER_LOW);// 设置低电耗
	}

	float TargetDirection;
	float change;
	// 方向传感器变化监听
	private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			float direction = event.values[0] * -1.0f;
			if (currFloor != null) {
				change = -direction - (float) currFloor.getAngle()
						- Float.parseFloat(GlobalConfig.getDirection());
				mTargetDirection = normalizeDegree(change - rotate);// 赋值给全局变量，让指南针旋转
				TargetDirection = normalizeDegree(change);// 定位方向
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	// 调整方向传感器获取的值
	private float normalizeDegree(float degree) {
		return (degree + 720) % 360;
	}

	// 摇晃速度临界值
	private static final int SPEED_SHRESHOLD = 50;
	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 200;
	// 上次检测时间
	private long lastUpdateTime;
	private SensorManager sensorMag;
	private Sensor gravitySensor;
	private boolean isMove;
	// 保存上一次记录
	float lastX = 0;
	float lastY = 0;
	float lastZ = 0;

	/**
	 * 初始化传感器
	 */
	private void initGravitySensor() {
		sensorMag = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		gravitySensor = sensorMag.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		System.out.println("initGravitySensor");
	}

	float tMax = 1.0f;
	double speed;
	private SensorEventListener sensorLis = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@SuppressWarnings("deprecation")
		@Override
		public void onSensorChanged(SensorEvent event) {

			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
				return;
			}
			// 现在检测时间
			long currentUpdateTime = System.currentTimeMillis();
			// 两次检测的时间间隔
			long timeInterval = currentUpdateTime - lastUpdateTime;
			// 判断是否达到了检测时间间隔
			if (timeInterval < UPTATE_INTERVAL_TIME)
				return;
			// 现在的时间变成last时间
			lastUpdateTime = currentUpdateTime;
			// 获取加速度数值，以下三个值为重力分量在设备坐标的分量大小
			float x = event.values[SensorManager.DATA_X];

			float y = event.values[SensorManager.DATA_Y];

			float z = event.values[SensorManager.DATA_Z];

			// 获得x,y,z的变化值
			float deltaX = x - lastX;
			float deltaY = y - lastY;
			float deltaZ = z - lastZ;
			// 备份本次坐标
			lastX = x;
			lastY = y;
			lastZ = z;
			// 计算移动速度
			speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
					* deltaZ)
					/ timeInterval * 10000;
			isMove = true;

		}
	};

	SensorManager mSensor;
	private Sensor acc;
	private Sensor gyroscope;
	private Sensor magnetic;
	private Sensor pressure;
	volatile float[][] sensorValue = new float[10][50];
	volatile float[][] sensorLast = new float[10][50];

	volatile float[] sensorTemp = new float[10];

	private void sensorRegister() {
		this.mSensor.registerListener(this.accListener, this.acc,
				SensorManager.SENSOR_DELAY_FASTEST);
		this.mSensor.registerListener(this.gyrListener, this.gyroscope,
				SensorManager.SENSOR_DELAY_FASTEST);
		this.mSensor.registerListener(this.magListener, this.magnetic,
				SensorManager.SENSOR_DELAY_FASTEST);
		this.mSensor.registerListener(this.preListener, this.pressure,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	SensorEventListener accListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			sensorTemp[0] = event.values[0];
			sensorTemp[1] = event.values[1];
			sensorTemp[2] = event.values[2];
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	SensorEventListener gyrListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			sensorTemp[3] = event.values[0];
			sensorTemp[4] = event.values[1];
			sensorTemp[5] = event.values[2];
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	SensorEventListener magListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			sensorTemp[6] = event.values[0];
			sensorTemp[7] = event.values[1];
			sensorTemp[8] = event.values[2];
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	SensorEventListener preListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			sensorTemp[9] = event.values[0];
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	private View viewStart;
	private View viewEnd;

	int startEnd = 0;

}
