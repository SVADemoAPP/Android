package com.hck.imagemap.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hck.imagemap.config.GlobalConfig;

/**
 * 获取ip订阅地图类
 * 
 * @author 
 * 
 */
public class UpLoad
{

    private String lastIP;
    private WifiManager wifiManager;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo info;
    private String TAG;
    private RequestQueue mRequestQueue;
    private int placeId = 0;

    public UpLoad(Context context)
    {
        mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        mRequestQueue = Volley.newRequestQueue(context);
        TAG = context.getPackageName();
    }

    /**
     * 根据标识获取IP or MAC
     * 
     * @return
     */
    public String getLocaIpOrMac()
    {
        // return "10.175.85.18";
        if ("MAC".equals(GlobalConfig.identification))
        {
            return getLocaMAC();
        } else
        {
            String ip = getLocaIp();
            if (lastIP == null)
            {
                lastIP = ip;
            }
            if (ip != null && !(ip.equals(lastIP)))// 当ip变化的时候
            {
                toSubscription(ip);
            }
            lastIP = ip;
            return ip;
        }

    }
    
    /**
     * 给ip前两部分加密
     */
    public String setIpPassword()
    {
        String ip = getLocaIp();
//        String replaceIp = ip.replace(".",",");
        String[] spliteIp = ip.split("\\.");
        String newIp;
        String firstIP = "" ;
        if (spliteIp.length>=4)
        {
            for (int i = 0; i < spliteIp[0].length(); i++)
            {
                firstIP = firstIP+"*";
            }
            String secondIP = "" ;
            for (int i = 0; i < spliteIp[1].length(); i++)
            {
                secondIP = secondIP+"*";
            }
            newIp = firstIP+"."+secondIP+"."+spliteIp[2]+"."+spliteIp[3];
            return newIp;
        }
        else {
            return null;
        }
    }

    public void setPlaceId(int placeId)
    {
        this.placeId = placeId;
    }

    /**
     * 重新订阅地图
     * 
     * @param ip
     */
    /*
     * private void getMapData(String ip) { Map<String, String> params = new
     * HashMap<String, String>(); params.put("ip", ip); params.put("isPush",
     * "2"); JsonObjectRequest newMissRequest = new JsonObjectRequest(
     * Request.Method.POST, "http://" + GlobalConfig.server_ip +
     * "/sva/api/getMapDataByIp", new JSONObject(params), new
     * Response.Listener<JSONObject>() {
     * 
     * @Override public void onResponse(JSONObject jsonobj) { } }, new
     * Response.ErrorListener() {
     * 
     * @Override public void onErrorResponse(VolleyError error) { Log.e(TAG,
     * "jsonobj:", error); } });
     * 
     * mRequestQueue.add(newMissRequest);
     * 
     * }
     */

    /**
     * 重新订阅地图
     * 
     * @param ip
     */
    private void toSubscription(String ip)
    {
        Log.i("placeId", placeId + "");
        Map<String, String> params = new HashMap<String, String>();
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/subscription?storeId=" + placeId + "&ip="
                        + ip, new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.i("subscription", jsonobj.toString());
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "jsonobj:", error);
                    }
                });

        mRequestQueue.add(newMissRequest);

    }

    /**
     * 获取本地MAC
     * 
     * @return
     */
    private String getLocaMAC()
    {
        info = mConnectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable())
        {
            if (ConnectivityManager.TYPE_MOBILE == info.getType())
             {
                return getLocalMacAddressFromIp();
            } else if (ConnectivityManager.TYPE_WIFI == info.getType())
            {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                return wifiInfo.getMacAddress();
            }
        }

        return "0.0.0.1";
    }

    // 根据IP获取本地Mac
    public String getLocalMacAddressFromIp()
    {
        String mac_s = "";
        try
        {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress
                    .getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return mac_s.toUpperCase();
    }

    public static String byte2hex(byte[] b)
    {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
            {
                if (n == 0)
                {
                    hs = hs.append("0").append(stmp);
                } else
                {
                    hs = hs.append("-").append("0").append(stmp);
                }

            } else
            {
                if (n == 0)
                {
                    hs = hs.append(stmp);
                } else
                {
                    hs = hs.append("-").append(stmp);
                }
            }
        }
        return String.valueOf(hs);
    }

    private String getLocaIp()
    {
        // return "10.175.85.18";
        info = mConnectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable())
        {
            if (ConnectivityManager.TYPE_MOBILE == info.getType())
            {
                return getLocalIpAddress();
            } else if (ConnectivityManager.TYPE_WIFI == info.getType())
            {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ip = intToIp(ipAddress);
                return ip;
            }
        }
        return "0.0.0.1";
    }

    private String intToIp(int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
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
            Log.e(TAG, ex.toString());
        }
        return null;
    }
}
