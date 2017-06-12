package com.hck.imagemap;

import com.hck.imagemap.config.GlobalConfig;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelpActivity extends Activity
{

    private ProgressDialog progressDialog;
    private WebView wv_help;

    @SuppressLint("SetJavaScriptEnabled") @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);
        wv_help = (WebView) findViewById(R.id.wv_help);
        WebSettings s = wv_help.getSettings();     
        s.setBuiltInZoomControls(true);    
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);    
        s.setUseWideViewPort(true);   
        s.setLoadWithOverviewMode(true);     
        s.setSavePassword(true);    
        s.setSaveFormData(true);   
        s.setJavaScriptEnabled(true);  
        wv_help.loadUrl("http://" + GlobalConfig.server_ip
                + "/sva/helpcn/userApp.html");
        progressDialog = ProgressDialog.show(HelpActivity.this, "Loading",
                "正在加载......", true, false);
        wv_help.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                progressDialog.dismiss();
                // 结束
                super.onPageStarted(view, url, favicon);
            }

        });

    }

    /**
     * 返回按钮监听
     * 
     * @param view
     */
    public void helpClick(View view)
    {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

}
