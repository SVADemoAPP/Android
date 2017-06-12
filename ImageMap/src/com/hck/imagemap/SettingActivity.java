package com.hck.imagemap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.hck.imagemap.view.ClearEditText;
import com.hck.imagemap.view.CustomDialog;

public class SettingActivity extends Activity
{

    private ClearEditText serverIp;
    private Button button;
    private String ipNow;
    private SharedPreferences myPreferences;
    private Spinner spinner;
//    private Spinner sp_ins_api;
    private RelativeLayout highSet, generalSet;
    private TextView tv_lang;
//    private TextView tv_ins_api;
    private RelativeLayout rl_set;
    
//    private RelativeLayout rl_ins_api;
    private View command_view;
    private EditText userName, password;
    private Intent in;
    private Bundle bn;
    private ProgressDialog progressDialog;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Log.i("TAG", "进入setting");
        mRequestQueue = Volley.newRequestQueue(this);
        serverIp = (ClearEditText) findViewById(R.id.serverIp);
        serverIp.clearFocus();
        button = (Button) findViewById(R.id.button);
        generalSet = (RelativeLayout) findViewById(R.id.general_set);
        in = this.getIntent();
        bn = in.getExtras();
        rl_set = (RelativeLayout) findViewById(R.id.rl_set);
        tv_lang = (TextView) findViewById(R.id.tv_lang);
        highSet = (RelativeLayout) findViewById(R.id.highSet);
        highSet.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                commandOrder();
            }

        });

        generalSet.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingActivity.this,
                        GeneralSettingActivity.class);
                startActivity(intent);
            }
        });
        
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3)
            {
                tv_lang.setText(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });
        spinner.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                case MotionEvent.ACTION_DOWN:
                    rl_set.setBackgroundResource(R.drawable.ic_preference_pressed);
                    break;
                case MotionEvent.ACTION_UP:
                    rl_set.setBackgroundResource(R.drawable.ic_preference_normal);
                    break;
                case MotionEvent.ACTION_MOVE:
                    rl_set.setBackgroundResource(R.drawable.ic_preference_normal);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    rl_set.setBackgroundResource(R.drawable.ic_preference_normal);
                    break;
                }
                return false;
            }
        });
        
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        
        String language = myPreferences.getString("Language", "English");
        if (language.equals("简体中文"))
        {
            spinner.setSelection(0);
            tv_lang.setText(spinner.getSelectedItem().toString());
        } else
        {
            spinner.setSelection(1);
            tv_lang.setText(spinner.getSelectedItem().toString());
        }
        final String langrage = spinner.getSelectedItem().toString();
        final String ip = myPreferences.getString("serverIp",
                "223.202.102.66:8086");

        serverIp.setText(ip);
        ipNow = ip;

        button.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                SharedPreferences preferences = getSharedPreferences("setting",
                        Activity.MODE_PRIVATE);
                // 获得SharedPreferences.Editor
                SharedPreferences.Editor editor = preferences.edit();

                String nullIp = getString(R.string.nullIp).toString();
                /*
                 * String wrongIp = getString(R.string.wrongIp).toString();
                 * String nullPort = getString(R.string.nullPort).toString();
                 * String wrongPort = getString(R.string.wrongPort).toString();
                 */
                String ip = serverIp.getText().toString();

                String changeLangrage = spinner.getSelectedItem().toString();

                if (ip == null || ip.equals(""))
                {
                    Toast.makeText(SettingActivity.this, nullIp,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                /* if (isIpv4(ip)) { */
                if (ip.contains(":"))
                {

                    if (":".equals(ip.substring(ip.length() - 1, ip.length())))
                    {
                        Toast.makeText(SettingActivity.this,
                                getString(R.string.wrongAddress),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String[] split = ip.split(":");
                    if (split.length == 2)
                    {
                        if (!isIpv4(split[0]))
                        {
                            Toast.makeText(SettingActivity.this,
                                    getString(R.string.wrongIp),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!isPort(split[1]))
                        {
                            Toast.makeText(SettingActivity.this,
                                    getString(R.string.wrongPort),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else
                    {
                        Toast.makeText(SettingActivity.this,
                                getString(R.string.wrongAddress),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                } else
                {
                    Toast.makeText(SettingActivity.this,
                            getString(R.string.wrongAddress), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                GlobalConfig.setServerIp(ip);
                editor.putString("serverIp", ip);
                editor.commit();
                // editor.commit();
                /*
                 * } else { Toast.makeText(SettingActivity.this, wrongIp,
                 * Toast.LENGTH_LONG).show(); return; }
                 */

                /*
                 * if (port3 == null || port3.equals("")) {
                 * Toast.makeText(SettingActivity.this, nullPort,
                 * Toast.LENGTH_LONG).show(); return; }
                 */
                /*
                 * if (isPort(port3)) { GlobalConfig.setServerIp(port3);
                 * editor.putString("port", port3); editor.commit();
                 */

                if (!changeLangrage.equals(langrage))
                {
                    Locale curLocale = getResources().getConfiguration().locale;
                    if (curLocale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        setLang(Locale.ENGLISH);
                        editor.putString("Language", "English");
                        editor.commit();
                    } else
                    {
                        setLang(Locale.SIMPLIFIED_CHINESE);
                        editor.putString("Language", "简体中文");
                        editor.commit();
                    }

                }

                if (ipNow.equals(ip) && changeLangrage.equals(langrage))
                {
                    bn.putString("server", "noChange");
                    // Log.i(">>>>>>>>>>", "nochange");
                } else
                {
                    bn.putString("server", "change");
                    // Log.i(">>>>>>>>>>", "change");
                }
                in.putExtras(bn);
                SettingActivity.this.setResult(RESULT_OK, in);
                finish();

                /*
                 * } else { Toast.makeText(SettingActivity.this, wrongPort,
                 * Toast.LENGTH_LONG).show(); }
                 */

            }
        });

    }

    /**
     * 口令对话框
     */
    protected void commandOrder()
    {
        String kouling = getString(R.string.kouling).toString();
        String yes = getString(R.string.yes).toString();
        String no = getString(R.string.no).toString();
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(kouling);
        command_view = View.inflate(this, R.layout.command_view, null);
        userName = (EditText) command_view.findViewById(R.id.etcomuser);
        password = (EditText) command_view.findViewById(R.id.etcompwd);
        userName.setText("admin");
        password.setText("admin");
        builder.setContentView(command_view);
        builder.setPositiveButton(yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.i("zb", "order方法以wei执行。。。。。");
                order();
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
        builder.create().show();

    }

    /**
     * 口令验证
     */
    protected void order()
    {
    
    	String user = userName.getText().toString();
        String pawd = password.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pawd))
        {
            Log.i("zb", "用户名密码为空。。。。。。");
            Toast.makeText(SettingActivity.this,
                    getString(R.string.nullKouling), Toast.LENGTH_SHORT).show();
        } else
        {
            progressDialog = ProgressDialog
                    .show(SettingActivity.this, "Waiting",
                            getString(R.string.koulingYanzheng), true, false);
            String name = userName.getText().toString();
            String pwd = password.getText().toString();
            Log.i("zb", "name=" + name);
            Log.i("zb", "pwd=" + pwd);
            Map<String, String> param = new HashMap<String, String>();
            Log.i("zb", "param=" + param);
            JsonObjectRequest subRequest = new JsonObjectRequest(
                    Request.Method.POST, "http://" + GlobalConfig.server_ip
                            + "/sva/api/checkCode?name=" + name + "&password="
                            + pwd, new JSONObject(param),
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject jsonobj)
                        {
                            Log.d("TAG", "jsonobj1:" + jsonobj.toString());
                            progressDialog.dismiss();
                            try
                            {
                                Log.i("zb", "setting,,口令验证成功");
                                if (jsonobj.getBoolean("data"))
                                {
                                    // 验证正确跳转
                                    Intent intent = new Intent(
                                            SettingActivity.this,
                                            HighSetAcitivity.class);
                                    Bundle bundle = new Bundle();
                                    if ("".equals(bn.getString("path")))
                                    {
                                        Toast.makeText(SettingActivity.this,
                                                getString(R.string.reload),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    bundle.putString("path",
                                            bn.getString("path", ""));
                                    bundle.putDouble("xSport",
                                            bn.getDouble("xSport", 0));
                                    bundle.putDouble("ySport",
                                            bn.getDouble("ySport", 0));
                                    bundle.putDouble("scale",
                                            bn.getDouble("scale", 0));
                                    bundle.putString("place",
                                            bn.getString("place", ""));
                                    bundle.putInt("floorNo",
                                            bn.getInt("floorNo", 0));
                                    bundle.putInt("placeId",
                                            bn.getInt("placeId", 0));
                                    bundle.putString("floor",
                                            bn.getString("floor", ""));
                                    bundle.putSerializable("currFloor",
                                            bn.getSerializable("currFloor"));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else
                                {
                                    Toast.makeText(SettingActivity.this,
                                            getString(R.string.nameWrong),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e)
                            {
                                Log.e("error", e + "");
                            }
                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.e("TAG", "jsonobj2:", error);
                            progressDialog.dismiss();
                            Toast.makeText(SettingActivity.this,
                                    getString(R.string.noResponse),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            mRequestQueue.add(subRequest);
        }
    }

    public void helpClick(View view)
    {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    protected void setLang(Locale l)
    {
        MapConstant.local=l;
        Configuration cfg = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        cfg.locale = l;
        getResources().updateConfiguration(cfg, dm);
    }

    public void setting_back(View v)
    {
        finish();
    }

    public boolean isIpv4(String ipAddress)
    {

        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();

    }

    public boolean isPort(String p)
    {
        String port = "/^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|"
                + "(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)/";

        Pattern pattern = Pattern.compile(port);
        Matcher matcher = pattern.matcher(p);
        return matcher.matches();
    }

}
