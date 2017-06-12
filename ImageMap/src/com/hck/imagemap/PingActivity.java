package com.hck.imagemap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hck.imagemap.adapter.PingsvaAdapter;
import com.hck.imagemap.config.GlobalConfig;
import com.hck.imagemap.entity.Pingsva;
import com.hck.imagemap.view.CustomDialog;

public class PingActivity extends Activity
{

    private Button pingServer, pingSva;
    private ProgressDialog progressDialog;
    private StringBuffer stringBuffer;
    private Process p;
    private String ip;
    private StringBuilder builder;
    private TextView tv_ping;
    private ImageView imageView;
    private ImageView iv_phone;
    private RequestQueue mRequestQueue;
    private ArrayList<Pingsva> svalist;
    private PopupWindow pw;
    private View pingChoose;
    private ListView pingsvaList;
    private List<String> listSva = new ArrayList<String>();
    private PingsvaAdapter pingsvaAdapter;
    private View pingView;
    private EditText etpingtime;
    private EditText etpingsize;
    private EditText etpingnumber;
    private String pingnum;
    private String pingsize;
    private String pingtime;
    private String replaceAll;


    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
            case 1:
                tv_ping.setText("");
                iv_phone.setImageResource(R.drawable.phone);
                imageView.setImageResource(R.drawable.normal);
                tv_ping.setText(stringBuffer);
                break;
            case 2:
                tv_ping.setText("");
                iv_phone.setImageResource(R.drawable.phone);
                imageView.setImageResource(R.drawable.error);
                break;
            case 3:
                tv_ping.setText("");
                iv_phone.setImageResource(R.drawable.server);
                imageView.setImageResource(R.drawable.normal);
                tv_ping.setText(replaceAll);
                break;
            case 4:
                tv_ping.setText("");
                iv_phone.setImageResource(R.drawable.server);
                imageView.setImageResource(R.drawable.error);
                break;
            default:
                break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ping);
        SharedPreferences MyPreferences = getSharedPreferences("setting",
                Activity.MODE_PRIVATE);
        ip = MyPreferences.getString("serverIp", "223.202.102.66");
        mRequestQueue = Volley.newRequestQueue(this);
        imageView = (ImageView) findViewById(R.id.inter);
        tv_ping = (TextView) findViewById(R.id.tvping);
        iv_phone = (ImageView) findViewById(R.id.ivphone);
        initView();
        getPingsvaData();
        btnListenner();
    }

    /**
     * 获取pingsva商场数据
     */
    private void getPingsvaData()
    {
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.GET, "http://" + GlobalConfig.server_ip
                        + "/sva/svalist/api/getTableData", null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.i("getPingsvaData",
                                "pingsvalist:" + jsonobj.toString());
                        JSONArray array = null;
                        Pingsva pingsva;
                        try
                        {
                            array = jsonobj.getJSONArray("data");
                            svalist = new ArrayList<Pingsva>();
                            svalist.clear();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < array.length(); i++)
                        {
                            pingsva = null;
                            pingsva = new Pingsva();
                            try
                            {
                                JSONObject o = array.getJSONObject(i);
                                pingsva.setIp(o.getString("ip"));
                                pingsva.setName(o.getString("name"));
                                svalist.add(pingsva);
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        if (svalist != null && svalist.size() > 0)
                        {
                            showSvalist();
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i("pingsva", "pingsvalist_error:" + error);

                    }
                });
        mRequestQueue.add(newMissRequest);
    }

    /**
     * listview上显示商场名称
     */
    protected void showSvalist()
    {
        String beforeSva = null;
        String afterSva = null;
        String name = null;
        listSva.clear();
        for (int i = 0; i < svalist.size(); i++)
        {
            beforeSva = svalist.get(i).getName();
            if (beforeSva.equals(afterSva))
            {
                continue;
            }
            afterSva = beforeSva;
            listSva.add(svalist.get(i).getName());
            name = svalist.get(i).getName();
        }
        pingsvaAdapter = new PingsvaAdapter(this, listSva);
        pingsvaList.setAdapter(pingsvaAdapter);
        for (int i = 0; i < listSva.size(); i++)
        {
            if (name.equals(listSva.get(i).toString()))
            {
                pingsvaAdapter.setSelectedPosition(i);
                pingsvaAdapter.notifyDataSetChanged();
                break;
            }
        }

    }

    /**
     * pingsva的每个商场
     * 
     * @param postion
     */
    public void pingSva(int postion)
    {
        JsonObjectRequest newMissRequest = new JsonObjectRequest(
                Request.Method.POST, "http://" + GlobalConfig.server_ip
                        + "/sva/api/pingSVA?ip=" + svalist.get(postion).getIp()
                        + "&pingnumber=" + pingnum + "&packtsize=" + pingsize
                        + "&timeout=" + pingtime, null,

                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject jsonobj)
                    {
                        Log.i("pingSva()", "pingnum=" + pingnum);
                        Log.i("pingSva()", "pingsize=" + pingsize);
                        Log.i("pingSva()", "pingtime=" + pingtime);
                        Log.i("pingsva", "pingsva:::" + jsonobj.toString());

                        try
                        {
                            JSONArray array = new JSONArray();
                            array = jsonobj.getJSONArray("data");
                            String isError = jsonobj.getString("error");
                            builder = new StringBuilder();
                            builder.append(array.toString());
                            int i=builder.length();
                            builder.replace(0, 2, "");
                            builder.replace(i-6, i-1, "");
                            //ilder转成string，替换所以得","","-->\n
                            replaceAll = builder.toString().replaceAll("\",\"\",\"", "\n");

                            // builder.replace(0, 8, "\n");
                            // builder.replace(36, 42, "\n\n");
                            // builder.replace(81, 93, "\n\n");
                            // builder.replace(110, 119, "\n\n");
                            // builder.replace(148, 155, "\n\n");
                            // builder.replace(168, 176, "\n\n");
                            // builder.replace(201, 206, "\n");
                            if (isError.equals("false"))
                            {
                                handler.sendEmptyMessage(3);
                            } else
                            {
                                handler.sendEmptyMessage(4);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e)
                        {
                            Log.e("error", e + "");
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(PingActivity.this,
                                getString(R.string.noResponse),
                                Toast.LENGTH_LONG).show();
                        Log.i("error", "++" + error.toString());
                        handler.sendEmptyMessage(4);
                        progressDialog.dismiss();
                    }
                });
        mRequestQueue.add(newMissRequest);
    }

    private void btnListenner()
    {
        pingServer.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                imageView.setImageResource(R.drawable.success);
                progressDialog = ProgressDialog.show(PingActivity.this,
                        "Waiting", getString(R.string.pingServer), true, false);
                String[] ipp = ip.split(":");
                ping(ipp[0]);
            }
        });

        pingSva.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 点击pingava按钮弹出listview的pop
                pw = new PopupWindow(pingChoose, LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, true);
                pw.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.market_item_bg));
                pw.showAsDropDown(v, 0, 3);

            }
        });
        // pingsva listView的点击监听事件
        pingsvaList.setOnItemClickListener(new OnItemClickListener()
        {
            private String beforeSva;
            private String allSva;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    final int position, long id)
            {
                // 选择listview通知变化
                allSva = listSva.get(position).toString();
                if (!allSva.equals(beforeSva))
                {
                    beforeSva = allSva;
                    pingsvaAdapter.setSelectedPosition(position);
                    pingsvaAdapter.notifyDataSetChanged();
                    pw.dismiss();
                }
                pw.dismiss();
                // 选择弹出对话框
                String pingsva = getString(R.string.pingSva).toString();
                String yes = getString(R.string.yes).toString();
                String no = getString(R.string.no).toString();
                CustomDialog.Builder builder = new CustomDialog.Builder(
                        PingActivity.this);
                builder.setTitle(pingsva);
                pingView = View.inflate(PingActivity.this, R.layout.pingsva_view, null);
                etpingnumber = (EditText) pingView.findViewById(R.id.etpingnumber);
                etpingsize = (EditText) pingView.findViewById(R.id.etpingsize);
                etpingtime = (EditText) pingView.findViewById(R.id.etpingtime);
                builder.setContentView(pingView);
                builder.setPositiveButton(yes,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                Log.i("zz", "准备ping()方法");
                                pingnum = etpingnumber.getText().toString();
                                pingsize = etpingsize.getText().toString();
                                pingtime = etpingtime.getText().toString();
                                Log.i("ping", "pingnum=" + pingnum);
                                Log.i("ping", "pingsize=" + pingsize);
                                Log.i("ping", "pingtime=" + pingtime);
                                if (TextUtils.isEmpty(pingnum)
                                        || TextUtils.isEmpty(pingsize)
                                        || TextUtils.isEmpty(pingtime)
                                        || pingnum.equals("0")
                                        || pingsize.equals("0")
                                        || pingtime.equals("0"))
                                {
                                    Log.i("zz", "输入信息为空.....");
                                    Toast.makeText(PingActivity.this,
                                            getString(R.string.pingmessage),
                                            Toast.LENGTH_LONG).show();
                                } else
                                {
                                    imageView
                                            .setImageResource(R.drawable.success);
                                    progressDialog = ProgressDialog.show(
                                            PingActivity.this, "Waiting",
                                            getString(R.string.pingSVA), true,
                                            false);
                                    Log.i("zz", "有信息，开始ping....");
                                    pingSva(position);
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.setNegativeButton(no,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }

        });
    }

    private void initView()
    {
        pingServer = (Button) findViewById(R.id.pingServer);
        pingSva = (Button) findViewById(R.id.pingSva);
        pingChoose = View.inflate(getApplicationContext(),
                R.layout.pingsva_choose, null);
        pingsvaList = (ListView) pingChoose.findViewById(R.id.pingSva_list);
    }

    private void ping(final String ip)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    p = Runtime.getRuntime().exec("ping -c 4 -w 10 " + ip);// ping3次
                    // 读取ping的内容，可不加。
                    int count = p.waitFor();
                    if (count == 0)
                    {
                        InputStream input = p.getInputStream();

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(input));

                        stringBuffer = new StringBuffer();

                        String content = "";

                        stringBuffer.append("\n");
                        while ((content = in.readLine()) != null)
                        {
                            stringBuffer.append(content + "\n");
                        }
                        handler.sendEmptyMessage(1);
                    } else
                    {
                        handler.sendEmptyMessage(2);
                    }

                } catch (Exception e)
                {
                    Log.e("error", e + "");
                } finally
                {
                    progressDialog.dismiss();
                    p.destroy();
                }
            }
        }).start();

    }

    public void backClick(View v)
    {
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
