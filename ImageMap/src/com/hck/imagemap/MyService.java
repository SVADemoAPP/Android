package com.hck.imagemap;

//Download by http://www.codefans.net
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hck.imagemap.Constant.MapConstant;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.utils.PhoneInfo;
import com.hck.imagemap.utils.UpLoad;

public class MyService extends Service
{

    private UpLoad load;
    private SharedPreferences myPreferences;
    private SharedPreferences.Editor editor;
    
    int n = 1;
    @Override
    public IBinder onBind(Intent arg0)
    {
        Log.d("Service", "onBind");
        Toast.makeText(getBaseContext(), "onBind", 0).show();
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        Log.d("Service", "onCreate");
        n = 1;
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = myPreferences.edit();
        load = new UpLoad(getBaseContext());
        mRequestQueue = Volley.newRequestQueue(getBaseContext());
        timer = new Timer();
        timer.schedule(new Work(),0, 4000);
//        new BroadCastUdp().start();
        super.onCreate();
    }

    class Work extends TimerTask{

        @Override
        public void run() {
            try
            {
                    Message message = new Message();
                    message.what=1;
                    handler.sendMessage(message);
            } catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
            }
        } 
     }
     
     Handler handler = new Handler()
     {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==1)
            {
                savePhone();
            }
        }
         
     };
     private RequestQueue mRequestQueue;
     private void savePhone()
    {
         Map<String, String> params = new HashMap<String, String>();
         params.put("ip", load.getLocaIpOrMac());
         params.put("phoneNumber", getPhoneNumber());
         JsonObjectRequest newMissRequest = new JsonObjectRequest(
                 Request.Method.POST, "http://" + GlobalConfig.server_ip
                         + "/sva/api/savePhone", new JSONObject(params),
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject jsonobj) {
                         Log.d("savePhone", "savePhone:" + jsonobj.toString());
                         System.out.println("savePhone:" + jsonobj.toString());
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                     }
                 });
         mRequestQueue.add(newMissRequest);
    }
     
    public String getPhoneNumber(){
        String string = null;
        PhoneInfo siminfo = new PhoneInfo(getBaseContext());
        string = siminfo.getNativePhoneNumber();
        return string;
     }
    @Override
    public void onDestroy()
    {
        Log.d("Service", "onDestroy");
//        Toast.makeText(getBaseContext(), "onDestroy", 0).show();
        timer.cancel();
        super.onDestroy();
    }
    Timer timer;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO Auto-generated method stub
//        Toast.makeText(getBaseContext(), "onStartCommand", 0)
//        .show();
//        show();
        
        return super.onStartCommand(intent, flags, startId);
    }
    public static final int DEFAULT_PORT = 43708;
	private static final int MAX_DATA_PACKET_LENGTH = 40;
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];
	
    public class BroadCastUdp extends Thread {
		private DatagramSocket udpSocket;

//		public BroadCastUdp(String dataString) {
//			this.dataString = dataString;
//		}

		public void run() {
			DatagramPacket dataPacket = null;

			try {
				udpSocket = new DatagramSocket();

			} catch (Exception e) {
				Log.e("Service", e.toString());
			}
			while (MapConstant.start) {
				dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
				try {
					byte[] data = GlobalConfig.getContent().getBytes();
					dataPacket.setData(data);
					dataPacket.setLength(data.length);
					dataPacket.setPort(DEFAULT_PORT);
					InetAddress broadcastAddr;
					broadcastAddr = InetAddress.getByName("223.202.102.66");
					dataPacket.setAddress(broadcastAddr); 
					if (MapConstant.wifiSwitch) {
						udpSocket.send(dataPacket);
					}
					sleep(GlobalConfig.getSend_time());
				} catch (Exception e) {
					Log.e("Service", e.toString());
				}
			}
			udpSocket.close();
		}
	}
}
