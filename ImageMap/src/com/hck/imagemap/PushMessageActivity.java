package com.hck.imagemap;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.TouchImageView1.OnSingleClickListener;
import net.yoojia.imagemap.core.CircleShape;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.hck.imagemap.entity.MessageMode;
import com.hck.imagemap.utils.Loction;
import com.hck.imagemap.utils.UpLoad;
import com.hck.imagemap.view.CustomDialog;

public class PushMessageActivity extends Activity {
	private RequestQueue mRequestQueue;
	private UpLoad load;
	private ImageMap1 map;
	private RelativeLayout layoutTemp;
	private Bundle bn;
	private boolean isBack = false;
//	private Bitmap bitmap;
	private RelativeLayout layout;
	private JSONArray messageList;
	private Floor currFloor;
//	private int postionNum = 0;
	private boolean isRequestLocation = false;// 用于判断是否请求位置
	private Button pushClick;
	private Loction loction;
	private Button cancleClick;
	private boolean touchTag = true;
	private PointF pointF;
	private int testTag = 0;//
	private int Countdown;
	private EditText touchPointX, touchPointY, duration;
	private NumberFormat numberFormat;
	private float x, y;// 终点像素坐标
	private int correctTime = 0;// 正确推送次数
	private int noMessageTime = 0;// 未推送消息次数
	private int wrongTime = 0;// 推送错误次数
	private boolean isInArea = false;// 是否在推送范围
	private TextView tv_timer;
	private TextView tv_showResult;
	private Map<String, String> messageMap = new HashMap<String, String>();
	private List<MessageMode> list = new LinkedList<MessageMode>();
	private List<MessageMode> allMessageList = new LinkedList<MessageMode>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tv_showResult.setText(getString(R.string.correctTime)
						+ correctTime + "\n" + getString(R.string.nopushTime)
						+ noMessageTime + "\n" + getString(R.string.wrongTime)
						+ wrongTime + "\n" + getString(R.string.booleanRange)
						+ isInArea);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.push_message_test);
		initView();
		getAllPushMessage();
		requestLocationTask();
	}

	private void initView() {
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
		load = new UpLoad(this);
		loction = new Loction();
		layout = (RelativeLayout) findViewById(R.id.rl_pushMessage_map);
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		tv_showResult = (TextView) findViewById(R.id.tv_showResult);
		pushClick = (Button) findViewById(R.id.pushClick);
		cancleClick = (Button) findViewById(R.id.cancleClick);
		cancleClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Cancle();
			}

		});
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutTemp = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.image_map_item, null);
		map = (ImageMap1) layoutTemp.findViewById(R.id.imagemap);
		layoutTemp.setLayoutParams(lp);
		layout.addView(layoutTemp);
		mRequestQueue = Volley.newRequestQueue(this);
		map.setMapBitmap(MapConstant.bitmap);
		currFloor = (Floor) bn.getSerializable("currFloor");
		map.setOnSingleClickListener(new OnSingleClickListener() {

			@Override
			public void onSingle(PointF p) {
				pointF = p;
				if (touchTag) {
					CircleShape cs = new CircleShape("touchPoint", Color.BLUE);
					cs.setValues(String.format("%.5f:%.5f:20", pointF.x,
							pointF.y));
					map.addShape(cs, false);
					testTag = 1;
				}
			}

		});
	}

	public void PushClick(View view) {
		if (getString(R.string.realPoint)
				.equals(pushClick.getText().toString())) {
			if (testTag == 0) {
				Toast.makeText(this, getString(R.string.choseend), Toast.LENGTH_SHORT).show();
				return;
			}
			cancleClick.setVisibility(View.VISIBLE);
			View views = View.inflate(PushMessageActivity.this,
					R.layout.push_message_config, null);
			touchPointX = (EditText) views.findViewById(R.id.xxPoint);
			touchPointY = (EditText) views.findViewById(R.id.yyPoint);
			duration = (EditText) views.findViewById(R.id.duration);
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
					PushMessageActivity.this);
			String lockPosition = getString(R.string.lockPosition).toString();
			String yes = getString(R.string.yes).toString();
			String no = getString(R.string.no).toString();
			builder.setTitle(lockPosition);
			builder.setPositiveButton(yes,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							touchTag = false;
							map.removeShape("touchPoint");
							Countdown = Integer.parseInt(duration.getText()
									.toString());
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
									Color.BLUE);
							cs.setValues(String.format("%.5f:%.5f:20", x, y));
							map.addShape(cs, false);

							pushClick.setText(getString(R.string.dynicStart));
							caculateNearMessage();
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(no,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setContentView(views);
			builder.create().show();
		} else if (getString(R.string.dynicStart).equals(
				pushClick.getText().toString())) {
//			messageNum = 0;
			handler.postDelayed(time, 1000);
			isRequestLocation = true;
			pushClick.setText(getString(R.string.endTest));
			tv_timer.setText(Countdown + "s");
			tv_timer.setVisibility(View.VISIBLE);
			tv_showResult.setText(getString(R.string.correctTime) + correctTime
					+ "\n" + getString(R.string.nopushTime) + noMessageTime
					+ "\n" + getString(R.string.wrongTime) + wrongTime + "\n"
					+ getString(R.string.booleanRange) + isInArea);
			tv_showResult.setVisibility(View.VISIBLE);
			/*
			 * double durationTime = Double.parseDouble(duration.getText()
			 * .toString()); handler.postDelayed(runnable, (long) (durationTime
			 * * 60 * 1000));
			 */
		}
	}

	private double distance = 0;
	private double radius;
	private int ID;
	private List<Integer> nearList = new LinkedList<Integer>();
	private boolean find = false;

	private double lastTime;
	private void requestLocation() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip",  load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getData", new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						try {
							System.out.println(">>>" + jsonobj.toString());
							final JSONObject o = jsonobj.getJSONObject("data");
							if (currFloor.getFloorNo() != o.getInt("z")) {
								requestLocationTask();
								return;
							}
							
                            if (lastTime == o.getDouble("timestamp")) {
                                requestLocationTask();
                                return;
                            }
                            lastTime = o.getDouble("timestamp");
							if (jsonobj.has("message")) {
								messageList = jsonobj.getJSONArray("message");
								if (messageList.length() > 0) {
									for (int i = 0; i < messageList.length(); i++) {
										JSONObject t = (JSONObject) messageList
												.get(i);

										if (t.has("id")) {
											ID = i;
										} else {
											ID = i;
										}
										find = false;
										for (int j = 0; j < nearList.size(); j++) {
											if (nearList.get(j) == ID) {
												correctTime++;
												find = true;
												break;
											}
										}
										if (!find) {
											wrongTime++;
										}

										String newMessage = t
												.getString("message");
										String exitMessage = messageMap
												.get(newMessage);
										if (exitMessage != null) {
											continue;
										}
										messageMap.put(newMessage, "1");
										double xx = loction.location(
												t.getDouble("xSpot") * 10,
												t.getDouble("ySpot") * 10,
												currFloor)[0];
										double yy = loction.location(
												t.getDouble("xSpot") * 10,
												t.getDouble("ySpot") * 10,
												currFloor)[1];
										distance = Math.sqrt(Math.abs(x - xx)
												* Math.abs(x - xx)
												+ Math.abs(y - yy)
												* Math.abs(y - yy))
												/ currFloor.getScale();
										if (t.has("rangeSpot")) {
											radius = Double.parseDouble(t
													.getString("rangeSpot"));
										} else {
											radius = 5;
										}

										MessageMode messageMode = new MessageMode();
										messageMode.setPointF(new PointF(
												(float) t.getDouble("xSpot"),
												(float) t.getDouble("ySpot")));
										messageMode.setRadius(radius);
										messageMode.setDistance(distance);
										messageMode.setID(ID);
										list.add(messageMode);
									}
								} else {
									noMessageTime++;
								}
							}
							// requestLocationTask();
							/*
							 * loc.setX(oriLocation[0]);
							 * loc.setY(oriLocation[1]);
							 * 
							 * addPostion(loc);
							 */
						} catch (Exception e) {
							Log.e("error22", e + "");
						}
						handler.sendEmptyMessage(1);
						requestLocationTask();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("ThisAcitivity", "requestLocation:", error);
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

	public void getAllPushMessage() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", load.getLocaIpOrMac());
		params.put("isPush", "2");
		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
						+ "/sva/api/getAllMessageData", new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						try {
							System.out.println(">>>" + jsonobj.toString());
							messageList = jsonobj.getJSONArray("data");
							if (messageList.length() > 0) {
								for (int i = 0; i < messageList.length(); i++) {
									JSONObject t = (JSONObject) messageList
											.get(i);
									double xx = loction.location(
											t.getDouble("xSpot") * 10,
											t.getDouble("ySpot") * 10,
											currFloor)[0];
									double yy = loction.location(
											t.getDouble("xSpot") * 10,
											t.getDouble("ySpot") * 10,
											currFloor)[1];

									radius = Double.parseDouble(t
											.getString("rangeSpot"))
											* currFloor.getScale();
									ID = t.getInt("id");
									MessageMode messageMode = new MessageMode();
									messageMode.setPointF(new PointF((long) xx,
											(long) yy));
									messageMode.setRadius(radius);
									messageMode.setID(ID);
									allMessageList.add(messageMode);
								}
							}

						} catch (Exception e) {
							Log.e("error22", e + "");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("ThisAcitivity", "requestLocation:", error);
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

	/**
	 * 计算用户实际位置在消息推送范围的点 没在推送范围时找最近的推送点
	 */
	private double distanse1 = 9999, distanse2 = 9999;
	private int NEARTAG = 0;

	public void caculateNearMessage() {
		if (allMessageList.size() > 0) {
			for (int i = 0; i < allMessageList.size(); i++) {
				double x1 = allMessageList.get(i).getPointF().x;
				double y1 = allMessageList.get(i).getPointF().y;
				if (Math.sqrt(Math.abs(x - x1) * Math.abs(x - x1)
						+ Math.abs(y - y1) * Math.abs(y - y1)) < allMessageList
						.get(i).getRadius()) {
					nearList.add(allMessageList.get(i).getID());
					isInArea = true;
				}
			}

			if (nearList.size() == 0) {
				isInArea = false;
				for (int i = 0; i < allMessageList.size(); i++) {
					double x1 = allMessageList.get(i).getPointF().x;
					double y1 = allMessageList.get(i).getPointF().y;
					distanse1 = Math.sqrt(Math.abs(x - x1) * Math.abs(x - x1)
							+ Math.abs(y - y1) * Math.abs(y - y1));
					if (distanse1 < distanse2) {
						distanse2 = distanse1;
						NEARTAG = i;
					}

				}
				nearList.add(allMessageList.get(NEARTAG).getID());
			}

		}
	}

//	/**
//	 * 根据定位结果打点
//	 * 
//	 * @param loc
//	 */
//	private void addPostion(Location loc) {
//		Loction loction = new Loction();
//		double xx = loction.location(loc.getX(), loc.getY(), currFloor)[0];
//		double yy = loction.location(loc.getX(), loc.getY(), currFloor)[1];
//		CircleShape postion = new CircleShape("NO" + postionNum, Color.RED);
//		postion.setValues(String.format("%.5f:%.5f:10", xx, yy));
//		map.addShape(postion, false);
//		postionNum++;
//		requestLocationTask();
//	}

	private void Cancle() {
		isRequestLocation = false;
		handler.removeCallbacks(time);
		tv_timer.setVisibility(View.GONE);
		tv_showResult.setVisibility(View.GONE);
		list.clear();
		noMessageTime = 0;
		wrongTime = 0;
		messageMap.clear();
		isInArea = false;
		correctTime = 0;
		cancleClick.setVisibility(View.GONE);
		pushClick.setVisibility(View.VISIBLE);
		pushClick.setText(getString(R.string.realPoint));
		touchTag = true;
		testTag = 0;
		map.removeShape("touchPoint");
		map.removeShape("endPoint");
		// handler.postDelayed(run, 1200);
	}

	private void requestLocationTask() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isBack) {
					if (isRequestLocation) {
						requestLocation();
					} else {
						requestLocationTask();
					}
				}
			}
		}, 1500);
	}

	private Runnable time = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			tv_timer.setText((Countdown - 1) + "s");
			if (Countdown - 1 == 0) {
				handler.removeCallbacks(time);
				ShowResult();
			} else {
				Countdown--;
				handler.postDelayed(time, 1000);
			}
		}
	};
//	/**
//	 * 消除消息和实际位置点
//	 */
//	private Runnable run = new Runnable() {
//		public void run() {
//			for (int i = 0; i < messageNum; i++) {
//				map.removeShape("messageB" + i);
//				map.removeShape("messageA" + i);
//			}
//			messageNum = 0;
//		}
//	};

	/**
	 * xianshi 结果
	 */

	private void ShowResult() {
		isRequestLocation = false;
		StringBuilder builde = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			MessageMode mode = list.get(i);
			builde.append("\n\n" + "ID:" + mode.getID());
			builde.append("\n" + getString(R.string.messageCenter)
					+ mode.getPointF().toString());
			builde.append("\n" + getString(R.string.radius) + mode.getRadius());
			builde.append("\n" + getString(R.string.distance)
					+ numberFormat.format(mode.getDistance()));
		}
		View view = View.inflate(PushMessageActivity.this,
				R.layout.show_result, null);
		TextView textView = (TextView) view.findViewById(R.id.text_showResult);
		textView.setText(getString(R.string.correctTime) + correctTime + "\n"
				+ getString(R.string.nopushTime) + noMessageTime + "\n"
				+ getString(R.string.wrongTime) + wrongTime + "\n"
				+ getString(R.string.booleanRange) + isInArea + "\n" + builde);
		CustomDialog.Builder builder = new CustomDialog.Builder(
				PushMessageActivity.this);
		builder.setTitle(getString(R.string.test_results));
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						saveTestData();
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(getString(R.string.no),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Cancle();
						dialog.dismiss();
					}
				});
		builder.setContentView(view);
		builder.create().show();
	}

	private void saveTestData() {
		StringBuilder builde = new StringBuilder();
		StringBuilder builde1 = new StringBuilder();
		if (builde.length() > 0) {
			builde.delete(0, builde.length() - 1);
			builde1.delete(0, builde1.length() - 1);
		}
		for (int i = 0; i < list.size(); i++) {
			MessageMode mode = list.get(i);
			builde.append("(" + mode.getPointF().x + "," + mode.getPointF().y
					+ "," + mode.getRadius() + ")");
			builde1.append(numberFormat.format(mode.getDistance()) + ",");
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("placeId", String.valueOf(currFloor.getPlaceId()));
		param.put("floorNo", String.valueOf(currFloor.getFloorNo()));
		param.put("floor", currFloor.getFloor());
		param.put("place", currFloor.getPlace());
		param.put("pushRight", String.valueOf(correctTime));
		param.put("pushWrong", String.valueOf(wrongTime));
		param.put("notPush", String.valueOf(noMessageTime));
		param.put("centerRadius", builde.toString());
		param.put("centerReality", builde1.toString());
		if (isInArea) {
			param.put("isRigth", String.valueOf(1));
		} else {
			param.put("isRigth", String.valueOf(0));
		}

		JsonObjectRequest newMissRequest = new JsonObjectRequest(
				Request.Method.POST, "http://" + GlobalConfig.server_ip
				+ "/sva/api/savaMessageData", new JSONObject(param),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonobj) {
						Log.d("locationDelay",
								"saveTestData:" + jsonobj.toString());
						Cancle();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("locationDelay", "saveTestData:", error);
						Cancle();
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
	public void onBackPressed() {
		super.onBackPressed();
//		bitmap.recycle();
		finish();
	}

	public void setting_back(View v) {
//		bitmap.recycle();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRequestQueue.cancelAll(this);
		allMessageList.clear();
		nearList.clear();
		isBack = true;
//		if (bitmap != null) {
//			bitmap.recycle();
//		}
		// bitmap.recycle();
	}

}
