package com.hck.imagemap.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hck.imagemap.R;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.utils.UpLoad;

public class ColletDialog extends Dialog {

	private Context context;
	private ClickListenerInterface clickListenerInterface;
	HorizontalProgressBar hpb_progress;
	private RequestQueue mRequestQueue;
	private UpLoad load;
	float x;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(int floorNo) {
		this.floorNo = floorNo;
	}

	float y;
	int time;
	int length;
	int floorNo;

	int timeTask = 1000;
	int process;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				hpb_progress.setProgress(10);
				process = hpb_progress.getProgress();
				break;
			}
		};
	};

	public interface ClickListenerInterface {
		public void dismissDialog();
	}

	public ColletDialog(Context context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequestQueue = Volley.newRequestQueue(context);
		load = new UpLoad(context);
		init();
	}


	private void upLoading(){
//		https://{ip}:{port}/sva/api/getCollectResult
//		{
//		“floorNo”:“10001”,
//		“x”:50,
//		“y”:50
//		“error”:“null”,
//		“data”:true//成功-true，失败-false
//		}
        Map<String, String> params = new HashMap<String, String>();
		params.put("x", getX()+"");
		params.put("y", getY()+"");
		params.put("floorNo", getFloorNo()+"");
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://" + GlobalConfig.server_ip
                        + "/sva/api/getCollectResult",
                new JSONObject(params), new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                    	//“error”:null, “data”:”start collecting”
                        System.out.println("getStartCollectPrru:"
                                + jsonobj.toString());
                        try {
							if(!jsonobj.getBoolean("data")){
								handler.sendEmptyMessage(1);
							}else {
								hpb_progress.setProgress(100);
								isUpLoading = false;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    	
                    }
                });
        mRequestQueue.add(newMissRequest);
	}

	boolean isUpLoading;

	private void upLoadTask() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isUpLoading) {
					upLoading();
//					handler.sendEmptyMessage(1);
//					hpb_progress.setProgress(10);
				} else {
					upLoadTask();
				}
			}
		}, timeTask);
	}

	public void init() {
		hpb_progress = (HorizontalProgressBar) findViewById(R.id.hpb_progress);
//		hpb_progress.setProgress(10);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.collect_dialog, null);
		TextView textView = (TextView) view.findViewById(R.id.tv_endload);
		textView.setOnClickListener(new clickListener());
		setContentView(view);
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 锟斤拷取锟斤拷幕锟斤拷锟斤拷锟斤�?
		lp.width = (int) (d.widthPixels); // 锟竭讹拷锟斤拷锟斤拷为锟斤拷幕锟斤拷0.6
		lp.height = (int) (d.heightPixels - 10);
		dialogWindow.setAttributes(lp);
		isUpLoading = true;
		upLoadTask();
	}

	@Override
	public void onBackPressed() {
		return;
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.tv_endload:
				isUpLoading = false;
				clickListenerInterface.dismissDialog();
				break;
			}
		}

	};

}
