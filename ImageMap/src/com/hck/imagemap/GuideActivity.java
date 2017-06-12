package com.hck.imagemap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hck.imagemap.adapter.MYViewPagerAdapter;

public class GuideActivity extends Activity
{

    public static int width;
    public static int height;
    private ArrayList<View> views = new ArrayList<View>();

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        initViewPager();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    private void initViewPager()
    {
        ViewPager viewPager = (ViewPager) findViewById(R.id.guidePagerId);
        View view1 = LayoutInflater.from(this).inflate(R.layout.guide01_item,
                null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.guide02_item,
                null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.guide03_item,
                null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        MYViewPagerAdapter adapter = new MYViewPagerAdapter();
        adapter.setViews(views);
        viewPager.setAdapter(adapter);
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
