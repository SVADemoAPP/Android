package com.hck.imagemap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.core.PrruShape;
import net.yoojia.imagemap.core.RequestShape;
import net.yoojia.imagemap.support.MResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
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
import com.hck.imagemap.adapter.PrruInfoAdapter;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.entity.Floor;
import com.hck.imagemap.entity.PrruInfo;
import com.hck.imagemap.entity.PrruInfoO;
import com.hck.imagemap.utils.Loction;
import com.hck.imagemap.utils.UpLoad;
import com.hck.imagemap.view.CompassViewtran;

public class PrruInfoAcitivity extends Activity {

	private ImageMap1 map;
	private RelativeLayout layout;
	private RelativeLayout layoutTemp;
	private Bundle bn;
//	private Bitmap bitmap;
	private boolean isBack = false;
	private TextView tv_prruNum;
	private RequestQueue mRequestQueue;
	private UpLoad load;
	private ListView lv_prru;
	private PrruInfoAdapter adapter;

	private int num;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 1:
				tv_prruNum.setText(getString(R.string.prruNum) + num);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_prru_info_acitivity);
		isBack = false;
		initView();
		getPrruInfo();
//		requestLocationTask();
		getCurrentPrruWithRsrpTask();
	}
	private void initView() {
		load = new UpLoad(this);
		layout = (RelativeLayout) findViewById(R.id.rl_prru_map);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutTemp = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.image_map_item, null);
		map = (ImageMap1) layoutTemp.findViewById(R.id.imagemap);
		layoutTemp.setLayoutParams(lp);
		layout.addView(layoutTemp);
		tv_prruNum = (TextView) findViewById(R.id.tv_prruNum);
		RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
	                10,10);
		cv = new CompassViewtran(this);
        cv.setId(1);
        cv.setImageResource(MResource.getIdByName(getApplication(), "drawable",
                "saomiao"));
        cv.setLayoutParams(lps);
		lv_prru = (ListView) findViewById(R.id.lv_prru);
		adapter = new PrruInfoAdapter(this, prruInfos);
		lv_prru.setAdapter(adapter);
		mRequestQueue = Volley.newRequestQueue(this);
		Intent in = this.getIntent();
		bn = in.getExtras();
//		try {
//			bitmap = BitmapFactory.decodeFile(Environment
//					.getExternalStorageDirectory() + bn.getString("path"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (bitmap == null) {
//			finish();
//			return;
//		}
		map.setMapBitmap(MapConstant.bitmap);
	}

	List<double[]> list = new ArrayList<double[]>();
	private ArrayList<PrruInfo> prruInfos = new ArrayList<PrruInfo>();
	private ArrayList<String> neCodeArray = new ArrayList<String>();
	private ArrayList<PrruInfoO> locationArray = new ArrayList<PrruInfoO>();

	private void getPrruInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getPrruInfo?floorNo="
						+ bn.getInt("floorNo"), new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.d("getPrruInfo",
								"getPrruInfo:" + jsonobj.toString());
						System.out.println("getPrruInfo:" + jsonobj.toString());
						JSONArray array = null;
						try {
							array = jsonobj.getJSONArray("data");
						} catch (Exception e) {
							Log.e("error", e + "");
						}
						locationArray.clear();
						// tv_prruNum.setText(getString(R.string.prruNum)
						// + array.length());
						for (int i = 0; i < array.length(); i++) {
							try {
								JSONObject o = array.getJSONObject(i);
								PrruInfoO pinf = new PrruInfoO();
								double xSpot = o.getDouble("x");
								double ySpot = o.getDouble("y");
								Loction loction = new Loction();
								double xx = loction
										.location(xSpot, ySpot, (Floor) bn
												.getSerializable("currFloor"))[0];
								double yy = loction
										.location(xSpot, ySpot, (Floor) bn
												.getSerializable("currFloor"))[1];
								pinf.setF(new PointF((float) xx,
										(float) yy));
								pinf.setNe_code(o.getString("neCode"));
								locationArray.add(pinf);
								PrruShape prruShape = new PrruShape("prru" + i,
										Color.BLACK, null,
										PrruInfoAcitivity.this);
								prruShape.setValues(String.format(
										"%.5f:%.5f:30", xx, yy));
								map.addShape(prruShape, false);
//								RequestShape  requestShape = new RequestShape("fdfd", Color.RED, seismicWaveView, PrruInfoAcitivity.this);
//								requestShape.setValues(String.format(
//										"%.5f:%.5f:30", xx, yy));
//								map.addShape(requestShape, false);
								
							} catch (Exception e) {
								Log.e("error", e + "");
							}
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("getPrruInfo", "getPrruInfo:" + error);
						Toast.makeText(PrruInfoAcitivity.this,
								getString(R.string.prruinfo_norespond),
								Toast.LENGTH_SHORT).show();
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
	private CompassViewtran cv;
	protected final Handler handlers = new Handler();
	private int wrongTag = 0;
	private int mDirection;
	protected Runnable mCompassViewUpdater = new Runnable()
    {
        @Override
        public void run()
        {
        	if(cv!=null&&f!=null){
        		mDirection+=4;
        		cv.updateDirection(mDirection);
        		RequestShape requestShape = new RequestShape("sdas",
        				Color.BLUE, cv, PrruInfoAcitivity.this);
        		requestShape.setValues(String.format("%.5f:%.5f:50",f.x,f.y));
        		map.addShape(requestShape, false);
        	}
        	handlers.postDelayed(mCompassViewUpdater, 20);// 20毫秒后重新执行自己
        }

    };
	private ArrayList<PrruInfo> removeToLengthIsTen(ArrayList<PrruInfo> list) {
		Collections.sort(list, new Comparator<PrruInfo>() {

			@Override
			public int compare(PrruInfo arg0, PrruInfo arg1) {
				if (Double.parseDouble(arg0.getRsrp()) < Double
						.parseDouble(arg1.getRsrp())) {
					return 1;
				} else if (Double.parseDouble(arg0.getRsrp()) == Double
						.parseDouble(arg1.getRsrp())) {
					return 0;
				} else {
					return -1;
				}
			}

		});// 降序
		if (list.size() > 10) {
			for (int i = 10; i < list.size(); i++) {
				list.remove(10);
			}
		}
		return list;
	}

	private void getCurrentPrruWithRsrpTask() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getCurrentPrruWithRsrp();
			}
		}, 1000);
	}

	// 192.168.0.59:8080
	// {"data":[{"timestamp":1476147836900,"id":216096,"gpp":"0_2_2","rsrp":-1100,"userId":"C0A80A6E","enbid":"509146"},
	// {"timestamp":1476147836900,"id":216097,"gpp":"0_2_3","rsrp":-900,"userId":"C0A80A6E","enbid":"509146"},
	// {"timestamp":1476147836900,"id":216098,"gpp":"0_2_4","rsrp":-1600,"userId":"C0A80A6E","enbid":"509146"},//
	// {"timestamp":1476147836900,"id":216099,"gpp":"0_2_5","rsrp":-800,"userId":"C0A80A6E","enbid":"509146"},{"timestamp":1476147836900,"id":216100,"gpp":"0_2_6","rsrp":-1360,"userId":"C0A80A6E","enbid":"509146"},{"timestamp":1476147836900,"id":216101,"gpp":"0_2_7","rsrp":-1100,"userId":"C0A80A6E","enbid":"509146"}]}
	PointF f;
	private void getCurrentPrruWithRsrp() {
		Map<String, String> params = new HashMap<String, String>();
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.GET, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/getCurrentPrruWithRsrp?userId="
						+ load.getLocaIpOrMac(), new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.d("getCurrentPrruWithRsrp", jsonobj.toString());
						try {
							prruInfos.clear();
							JSONArray array = jsonobj.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								JSONObject o = (JSONObject) array.get(i);
								PrruInfo pi = new PrruInfo();
								pi.setGpp(o.getString("gpp"));
								pi.setRsrp(o.getString("rsrp"));
								prruInfos.add(pi);
							}
							prruInfos = removeToLengthIsTen(prruInfos);
							for (int i = 0; i < locationArray.size(); i++) {
								if(prruInfos.get(0).getGpp().equals(locationArray.get(i).getNe_code())){
									f = locationArray.get(i).getF();
									break;
								}
							}
							num = array.length();
							handler.sendEmptyMessage(1);
//							tv_prruNum.setText(getString(R.string.prruNum) + num);
//							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getCurrentPrruWithRsrpTask();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("getCurrentPrruWithRsrperror", error.toString());
						getCurrentPrruWithRsrpTask();
						Toast.makeText(PrruInfoAcitivity.this,
								getString(R.string.prruinfo_norespond),
								Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prru_info_acitivity, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isBack = true;
		handlers.removeCallbacks(mCompassViewUpdater);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		handlers.postDelayed(mCompassViewUpdater, 20);// 20毫秒后重新执行自己
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRequestQueue.cancelAll(this);
		isBack = true;
//		if (bitmap != null) {
//			bitmap.recycle();
//		}
		// bitmap.recycle();
	}

}
