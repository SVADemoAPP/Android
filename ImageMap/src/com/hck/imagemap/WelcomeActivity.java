package com.hck.imagemap;

import java.util.Locale;

import com.hck.imagemap.Constant.MapConstant;
import com.hck.imagemap.view.ApplyDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class WelcomeActivity extends Activity
{
    private long splashDelay = 2000; // 5 seconds
    private SharedPreferences myPreferences;
    private Configuration cfg;
    private DisplayMetrics dm;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        cfg = getResources().getConfiguration();
        dm = getResources().getDisplayMetrics();
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        // 借助SharedPreferences对象从内部存储获得相关信息
        flag = myPreferences.getBoolean("isUsed", true);
        if (flag)
        {
            String language = myPreferences.getString("Language", "English");
            if (language.equals("简体中文"))
            {
                cfg.locale = Locale.SIMPLIFIED_CHINESE;
                getResources().updateConfiguration(cfg, dm);
            } else
            {
                cfg.locale = Locale.ENGLISH;
                getResources().updateConfiguration(cfg, dm);
            }
            SetApplyDialog();// 协议对话框
        } else
        {
            setLangue();
        }
    }

    /**
     * 安全免责申明对话框
     */
    private void SetApplyDialog()
    {
        final ApplyDialog dialog = new ApplyDialog(this);
        dialog.setTitle(getString(R.string.applyTitle));
        dialog.setContent(getString(R.string.applyContent));
        dialog.setOkListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Editor et = myPreferences.edit();
                et.putBoolean("isUsed", false);// 同意后设置为false
                et.commit();
                setLangue();
                dialog.dismiss();
            }
        });
        dialog.setCancelListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Editor et = myPreferences.edit();
                et.putBoolean("isUsed", true);// 不同意为true
                et.commit();
                System.exit(0);// 不同意直接退出s
            }
        });
        dialog.show();
    }

    /**
     * 语言设置
     */
    private void setLangue()
    {
        String language = myPreferences.getString("Language", "简体中文");
        if (language.equals("简体中文"))
        {
            cfg.locale = Locale.SIMPLIFIED_CHINESE;
            getResources().updateConfiguration(cfg, dm);
        } else
        {
            cfg.locale = Locale.ENGLISH;
            getResources().updateConfiguration(cfg, dm);
        }
        // 延迟2s跳入下一页
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity();
            }
        }, splashDelay);
    }

    @Override
    public void onBackPressed()
    {
        System.exit(0);
        super.onBackPressed();
    }

    private void startActivity()
    {

        if (flag)
        {
            // 第一次访问进入新手指导
            Log.i("zb", "第一次访问进入新手指导");
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, GuideActivity.class);
            startActivity(intent);
            Editor et = myPreferences.edit();
            et.putBoolean("isUsed", false);
            et.commit();
        } else
        {
            // 进入主页面
            /*Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);*/
            if (!MapConstant.startedApp) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        }
        finish();
    }
}
