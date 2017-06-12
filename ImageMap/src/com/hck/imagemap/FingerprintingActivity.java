package com.hck.imagemap;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.core.Bubble.RenderDelegate;
import net.yoojia.imagemap.core.CollectPointShape;
import net.yoojia.imagemap.core.MoniPointShape;
import net.yoojia.imagemap.core.PushMessageShape;
import net.yoojia.imagemap.core.Shape;
import net.yoojia.imagemap.core.ShapeExtension.OnShapeActionListener;
import net.yoojia.imagemap.core.SpecialShape;
import net.yoojia.imagemap.support.MResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hck.imagemap.Constant.MapConstant;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.entity.Floor;
import com.hck.imagemap.utils.Loction;
import com.hck.imagemap.utils.UpLoad;
import com.hck.imagemap.view.CommonProgressDialog;
import com.hck.imagemap.view.CommonProgressDialog.ClickListenerInterface;
import com.hck.imagemap.view.CustomDialog;

public class FingerprintingActivity extends Activity {

	private int default_pointNum = 15;
	private int default_time = 50;
	private int default_radius = 30000;

	private ImageMap1 map;
	private RelativeLayout layout;
	private RelativeLayout layoutTemp;
	private Bundle bn;
//	private Bitmap bitmap;
	private TextView tv_locationX, tv_locationY, tv_getpoint, tv_getmin;
	private Loction loction;
	private String tv_location_X;
	private String tv_location_Y;
	private NumberFormat numberFormat;
	private Button btn_confi;
	private String getX;
	private String getY;
	private UpLoad load;
	private RequestQueue mRequestQueue;
	RelativeLayout rl_allimg;
	Button bt_refush;
	View vi_collect;
	private PopupWindow pop;
	int count;
	TextView tv_tag_coll_point;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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

				tv_locationX.setText("X:" + tv_location_X + "m");
				tv_locationY.setText("Y:" + tv_location_Y + "m");
				tv_getpoint.setText("N:" + default_pointNum);
				tv_getmin.setText("T:" + default_time + "s");
				updataTask();
				break;
			case 2:
				btn_confi.setText(getString(R.string.uploading));
				showDialog();
				break;
			case 3:
				process = (int) Math.ceil(count / 1000.0);
				if (process < default_time) {
					mDialog.setProgress(process);
				}
				upLoadTask();
				break;
			case 4:
				mDialog.setProgress(100);
				isUpLoad = false;
				upLoadTask();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fingerprinting);
		load = new UpLoad(this);
		loction = new Loction();
		vi_collect = View
				.inflate(this, MResource.getIdByName(getApplication(),
						"layout", "vi_collect"), null);
		pop = new PopupWindow(vi_collect, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setAnimationStyle(MResource.getIdByName(getApplication(), "style",
				"AnimBottom"));
		mRequestQueue = Volley.newRequestQueue(this);
		layout = (RelativeLayout) findViewById(R.id.rl_fpt_map);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutTemp = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.image_map_item, null);
		bt_refush = (Button) findViewById(R.id.bt_refush);
		rl_allimg = (RelativeLayout) findViewById(R.id.rl_allimg);
		bt_refush.setOnClickListener(refushClickListener);
		map = (ImageMap1) layoutTemp.findViewById(R.id.imagemap);
		layoutTemp.setLayoutParams(lp);
		layout.addView(layoutTemp);
		Intent in = this.getIntent();
		bn = in.getExtras();
		// getPrruSubscribe();
//		rl_allimg.setVisibility(View.VISIBLE);
//		bitmap = BitmapFactory.decodeFile(Environment
//				.getExternalStorageDirectory() + bn.getString("path"));

		tv_locationX = (TextView) findViewById(R.id.tv_getlocX);
		tv_locationY = (TextView) findViewById(R.id.tv_getlocY);
		tv_getpoint = (TextView) findViewById(R.id.tv_getpoint);
		tv_getmin = (TextView) findViewById(R.id.tv_getmin);
		btn_confi = (Button) findViewById(R.id.btn_confi);
		map.setAllowTranslate(true);
		initClick();
//		if (bitmap == null) {
//			finish();
//			return;
//		}
		map.setMapBitmap(MapConstant.bitmap);
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setGroupingUsed(false);
		updataTask();
		upLoadTask();
		getAllFeaturePoint();
	}

	private View bubble;

	private void initClick() {
		map.setOnShapeClickListener(new OnShapeActionListener() {
			@Override
			public void onPushMessageShapeClick(PushMessageShape shape,
					float xOnImage, float yOnImage) {
			}

			@Override
			public void onSpecialShapeClick(SpecialShape shape, float xOnImage,
					float yOnImage) {
			}

			@Override
			public void onCollectShapeClick(CollectPointShape shape,
					float xOnImage, float yOnImage) {
				if (bubble == null) {
					bubble = getLayoutInflater().inflate(
							MResource.getIdByName(getApplication(), "layout",
									"collect_pop_tip"), null);
					setMsgBubbleview(bubble);
				}
			}

			@Override
			public void outShapeClick(float xOnImage, float yOnImage) {
				// bubble = null;
				if (pop != null) {
					pop.dismiss();

				}
			}

			@Override
			public void onMoniShapeClick(MoniPointShape shape, float xOnImage,
					float yOnImage) {
				// TODO Auto-generated method stub
				if (bubble == null) {
					bubble = getLayoutInflater().inflate(
							MResource.getIdByName(getApplication(), "layout",
									"collect_pop_tip"), null);
					setMsgBubbleview(bubble);
				}

			}
		});
	}

	private void setMsgBubbleview(final View bubble) {
		map.setBubbleView(bubble, new RenderDelegate() {
			@Override
			public void onDisplay(final Shape shape, View bubbleView) {
				Button btn_col_del = (Button) vi_collect
						.findViewById(R.id.btn_col_del);
				tv_tag_coll_point = (TextView) vi_collect
						.findViewById(R.id.tv_tag_coll_point);
				tv_tag_coll_point.setText(shape.tag + "");
				pop.showAtLocation(vi_collect, Gravity.BOTTOM, 0, 0);
				btn_col_del.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (bubble != null) {
							bubble.setVisibility(View.GONE);
						}
						String id = shape.tag.toString().split("S")[1];
						removeFeatureValue(id, shape);
						pop.dismiss();

					}
				});
			}
		});
	}

	// https://{ip}:{port}/sva/api/removeFeatureValue HTTP/HTTPS（RESTful） JSON
	// Body:
	// {
	// “floorNo”:“10001”,
	// “x”:50,
	// “y”:50
	// }

	private void removeFeatureValue(String id, final Shape shape) {
		progressDialog = ProgressDialog.show(this, "Loading",
				"waiting for remove", true, false);

		Map<String, String> params = new HashMap<String, String>();
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/removeFeatureValue?id=" + id,
				new JSONObject(params), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						// “error”:null, “data”:”start collecting”
						System.out.println("removeFeatureValue:"
								+ jsonobj.toString());
						map.removeShape(shape.tag);
						progressDialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						Toast.makeText(FingerprintingActivity.this,
								getString(R.string.removeF), Toast.LENGTH_LONG)
								.show();
					}
				});
		mRequestQueue.add(newMissRequest);
	}

	/**
	 * 通知服务器开始收集
	 */
	private void startCollect() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", load.getLocaIpOrMac());
		params.put("radius", default_radius + "");
		params.put("x", getX + "");
		params.put("y", getY + "");
		params.put("timeInSeconds", default_time + "");
		params.put("length", default_pointNum + "");
		params.put("floorNo", bn.getInt("floorNo") + "");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/getStartCollectPrru", new JSONObject(
						params), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						// “error”:null, “data”:”start collecting”
						System.out.println("getStartCollectPrru:"
								+ jsonobj.toString());
						mHandler.sendEmptyMessage(2);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						mHandler.sendEmptyMessage(2);
					}
				});
		mRequestQueue.add(newMissRequest);
	}

	ProgressDialog progressDialog;

	CommonProgressDialog mDialog;

	private void showDialog() {
		mDialog = new CommonProgressDialog(this);
		mDialog.setOnKeyListener(keylistener);
		mDialog.setMessage(getString(R.string.collecting));
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// cancel(true);
			}
		});
		mDialog.show();
		mDialog.setMax(default_time);
		mDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void dismissDialog() {
				endUpLoad();
			}
		});
		isUpLoad = true;
	}

	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}

	};
	private View.OnClickListener refushClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			// getPrruSubscribe();
		}
	};

	/**
	 * 更新坐标线程循环
	 */
	private void updataTask() { // 延迟1s运行
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
			}
		}, 1000);
	}

	boolean isUpLoad;
	int timeState = 3000;
	int process;

	/**
	 * 更新收集进度循环
	 */
	private void upLoadTask() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isUpLoad) {
					if (count / 1000 >= default_time + 15) {
						isUpLoad = false;
						process = 0;
						count = 0;
						mDialog.dismiss();
						btn_confi.setText(getString(R.string.upload));
						Toast.makeText(FingerprintingActivity.this,
								"Time Out And Exit", Toast.LENGTH_SHORT).show();
						upLoadTask();
						return;
					}
					upLoading();
					// mHandler.sendEmptyMessage(3);
				} else {
					upLoadTask();
				}
			}
		}, timeState);
	}

	/**
	 * 查询采集是否成功
	 */
	private void upLoading() {
		// https://{ip}:{port}/sva/api/getCollectResult
		// {
		// “floorNo”:“10001”,
		// “x”:50,
		// “y”:50
		// “error”:“null”,
		// “data”:true//成功-true，失败-false
		// }
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", load.getLocaIpOrMac());
		params.put("x", getX + "");
		params.put("y", getY + "");
		params.put("floorNo", bn.getInt("floorNo") + "");
		params.put("radius", default_radius + "");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/getCollectResult", new JSONObject(
						params), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						// “error”:null, “data”:”start collecting”
						System.out.println("getCollectResult:"
								+ jsonobj.toString());
						count += timeState;
						try {
							if ("waiting".equals(jsonobj.getString("data"))) {
								mHandler.sendEmptyMessage(3);
							} else if ("success".equals(jsonobj
									.getString("data"))) {
								mHandler.sendEmptyMessage(4);
								map.clearShapes();
								process = 0;
								count = 0;
								mDialog.dismiss();
								btn_confi.setText(getString(R.string.upload));
								Toast.makeText(FingerprintingActivity.this,
										"success", Toast.LENGTH_SHORT).show();
								getAllFeaturePoint();
							} else {
								// map.clearShapes();
								process = 0;
								count = 0;
								mDialog.dismiss();
								btn_confi.setText(getString(R.string.upload));
								isUpLoad = false;
								upLoadTask();
								Toast.makeText(FingerprintingActivity.this,
										"faild", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						upLoadTask();
					}
				});
		newMissRequest.setRetryPolicy(new DefaultRetryPolicy(300,1,1.0f));
		mRequestQueue.add(newMissRequest);
	}

	// https://{ip}:{port}/sva/api/stopCollectPrru HTTP/HTTPS（RESTful） JSON
	// Body:
	// {
	// “ip”:“0.0.0.0”,
	// “floorNo”:”10001”,
	// “x”:50,
	// “y”:50
	// }
	/**
	 * 终止采集过程
	 */
	private void endUpLoad() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", load.getLocaIpOrMac());
		params.put("x", getX + "");
		params.put("y", getY + "");
		params.put("floorNo", bn.getInt("floorNo") + "");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/stopCollectPrru",
				new JSONObject(params), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						// “error”:null, “data”:”start collecting”
						System.out.println("stopCollectPrru:"
								+ jsonobj.toString());
						isUpLoad = false;
						process = 0;
						btn_confi.setText(getString(R.string.upload));
						mDialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						isUpLoad = false;
						btn_confi.setText(getString(R.string.upload));
						process = 0;
						mDialog.dismiss();
					}
				});
		mRequestQueue.add(newMissRequest);
	}

	/**
	 * 
	 * 获取所有完成的特征点
	 */
	// https://{ip}:{port}/sva/api/getAllFeaturePoint
	private void getAllFeaturePoint() {
		Map<String, String> params = new HashMap<String, String>();
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/app/getAllFeaturePoint?floorNo="
						+ bn.getInt("floorNo"), new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						try {
							JSONArray array = jsonobj.getJSONArray("data");
							System.out.println("getAllFeaturePoint:"
									+ jsonobj.toString());
							for (int i = 0; i < array.length(); i++) {
								int id = array.getJSONObject(i).getInt("id");
								double x = array.getJSONObject(i)
										.getDouble("x");
								double y = array.getJSONObject(i)
										.getDouble("y");
								if (!"simulate".equals(array.getJSONObject(i)
										.getString("userId"))) {
									CollectPointShape collectPointShape = new CollectPointShape(
											"CPS" + id, Color.RED,
											FingerprintingActivity.this, "dwf");
									collectPointShape.setValues(String.format(
											"%.5f:%.5f:10",
											loction.location(
													x * 10,
													y * 10,
													(Floor) bn
															.getSerializable("currFloor"))[0],
											loction.location(
													x * 10,
													y * 10,
													(Floor) bn
															.getSerializable("currFloor"))[1]));
									map.addShape(collectPointShape, false); // 加到地图上
								} else {
									MoniPointShape collectPointShape1 = new MoniPointShape(
											"CPS" + id, Color.RED,
											FingerprintingActivity.this, "dwf1");
									collectPointShape1.setValues(String.format(
											"%.5f:%.5f:10",
											loction.location(
													x * 10,
													y * 10,
													(Floor) bn
															.getSerializable("currFloor"))[0],
											loction.location(
													x * 10,
													y * 10,
													(Floor) bn
															.getSerializable("currFloor"))[1]));
									map.addShape(collectPointShape1, false); // 加到地图上
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("getAllFeaturePoint:"
								+ jsonobj.toString());

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		mRequestQueue.add(newMissRequest);
	}

	public void setting_back(View view) {
		finish();
	}

	public void congi_Click(View view) {
		if (getString(R.string.upload).equals(btn_confi.getText())) {
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			View vv = View.inflate(this, R.layout.collection_pop, null);
			final EditText et_pointx = (EditText) vv
					.findViewById(R.id.et_pointx);
			final EditText et_pointy = (EditText) vv
					.findViewById(R.id.et_pointy);
			final EditText et_num = (EditText) vv.findViewById(R.id.et_num);
			final EditText et_time = (EditText) vv.findViewById(R.id.et_time);
			final EditText et_radius = (EditText) vv
					.findViewById(R.id.et_radius);
			et_pointx.setText(tv_location_X);
			et_pointy.setText(tv_location_Y);
			et_num.setText(default_pointNum + "");
			et_time.setText(default_time + "");
			et_radius.setText(default_radius + "");
			builder.setTitle(getString(R.string.samples));
			builder.setContentView(vv);
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							getX = et_pointx.getText().toString().trim();
							getY = et_pointy.getText().toString().trim();
							default_pointNum = Integer.parseInt(et_num
									.getText().toString().trim());
							default_time = Integer.parseInt(et_time.getText()
									.toString().trim());
							default_radius = Integer.parseInt(et_radius
									.getText().toString().trim());

							if ("".equals(getX) || "".equals(getY)) {
								mHandler.sendEmptyMessage(0);
							} else if (Float.parseFloat(getX)
									* bn.getDouble("scale")
									+ bn.getDouble("xSport")
									* bn.getDouble("scale") > MapConstant.bitmap.getWidth()
									|| Float.parseFloat(getY)
											* bn.getDouble("scale")
											+ bn.getDouble("ySport")
											* bn.getDouble("scale") > MapConstant.bitmap
												.getHeight()
									|| Float.parseFloat(getY)
											* bn.getDouble("scale")
											+ bn.getDouble("ySport")
											* bn.getDouble("scale") < 0
									|| Float.parseFloat(getX)
											* bn.getDouble("scale")
											+ bn.getDouble("xSport")
											* bn.getDouble("scale") < 0) {
								Toast.makeText(FingerprintingActivity.this,
										getString(R.string.notAt),
										Toast.LENGTH_SHORT).show();
							} else {
								startCollect();
							}
							arg0.dismiss();
						}
					});
			builder.setNegativeButton(getString(R.string.no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
						}
					});
			builder.create().show();
		}
		// else if(getString(R.string.upload).equals(btn_confi.getText())){
		//
		// }
	}
}
