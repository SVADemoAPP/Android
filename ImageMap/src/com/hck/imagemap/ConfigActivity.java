package com.hck.imagemap;

import com.hck.imagemap.config.GlobalConfig;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends Activity
{

    private EditText threshold;
    private EditText readius;
    private EditText weights;
    private EditText crossWeights;
    private EditText maxDeviate;
    private EditText moreMaxDeviate;
    private EditText direction;
    private EditText errorAngle;
    private EditText positioningError;
    private EditText filterNumber;
    private EditText gaussVariance;
    private Button btnSave;
    private SharedPreferences myPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_config);
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = myPreferences.edit();
        initView();
        initData();
    }

    private void initData()
    {
        String threshold1 = myPreferences.getString("threshold", "0.2");//静止门限
        String readius1 = myPreferences.getString("readius", "10"); //滤波窗长
        String directionWeights1 = myPreferences.getString("weights", "0.04"); //横向权重
        String crossWeights1 = myPreferences.getString("crossWeights", "0.02"); //纵向权重
        String maxDeviate1 = myPreferences.getString("maxDeviate", "8");
        String moreMaxDeviate1 = myPreferences.getString("moreMaxDeviate", "5");
        String direction1 = myPreferences.getString("direction", "0");
        String errorAngle1=myPreferences.getString("errorAngle", "1");// 静止次数
        String positioningError1=myPreferences.getString("positioningError", "0.6");//过滤波峰误差
        String filterNumber1=myPreferences.getString("filterNumber", "15");//波峰阈值
        String gaussVariance1=myPreferences.getString("gaussVariance", "0.6");//步长
        threshold.setText(threshold1);
        readius.setText(readius1);
        weights.setText(directionWeights1);
        crossWeights.setText(crossWeights1);
        maxDeviate.setText(maxDeviate1);
        moreMaxDeviate.setText(moreMaxDeviate1);
        direction.setText(direction1);
        errorAngle.setText(errorAngle1);
        positioningError.setText(positioningError1);
        filterNumber.setText(filterNumber1);
        gaussVariance.setText(gaussVariance1);
    }

    private void initView()
    {
        threshold = (EditText) findViewById(R.id.threshold);
        readius = (EditText) findViewById(R.id.readius);
        weights = (EditText) findViewById(R.id.weights);
        crossWeights = (EditText) findViewById(R.id.crossWeights);
        maxDeviate = (EditText) findViewById(R.id.maxDeviate);
        moreMaxDeviate = (EditText) findViewById(R.id.moreMaxDeviate);
        direction = (EditText) findViewById(R.id.direction);
        errorAngle = (EditText) findViewById(R.id.errorAngle);
        positioningError = (EditText) findViewById(R.id.positioningError);
        filterNumber = (EditText) findViewById(R.id.filterNumber);
        gaussVariance = (EditText) findViewById(R.id.gaussVariance);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                 if (TextUtils.isEmpty(threshold.getText().toString()))
                {
                    Toast.makeText(ConfigActivity.this,
                            getString(R.string.thresholdEmpty), Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(weights.getText().toString()))
                {
                    Toast.makeText(ConfigActivity.this,
                            getString(R.string.weightsEmpty), Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(maxDeviate.getText().toString()))
                {
                    Toast.makeText(ConfigActivity.this,
                            getString(R.string.maxDeviateEmpty), Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(moreMaxDeviate.getText()
                        .toString()))
                {
                    Toast.makeText(ConfigActivity.this,
                            getString(R.string.moreMaxDeviateEmpty), Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(direction.getText().toString()))
                {
                    Toast.makeText(ConfigActivity.this,
                            getString(R.string.directionEmpty), Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("threshold", threshold.getText().toString());
                editor.putString("readius", readius.getText().toString());
                editor.putString("weights", weights.getText().toString());
                editor.putString("crossWeights", crossWeights.getText().toString());
                editor.putString("maxDeviate", maxDeviate.getText().toString());
                editor.putString("moreMaxDeviate", moreMaxDeviate.getText()
                        .toString());
                editor.putString("direction", direction.getText().toString());
                editor.putString("errorAngle", errorAngle.getText().toString());
                editor.putString("positioningError", positioningError.getText().toString());
                editor.putString("filterNumber", filterNumber.getText().toString());
                editor.putString("gaussVariance", gaussVariance.getText().toString());
                editor.commit();
                GlobalConfig.setThreshold(threshold.getText().toString());
                GlobalConfig.setSpr(readius.getText().toString());
                GlobalConfig.setWeights(weights.getText().toString());
                GlobalConfig.setCrossWeights(crossWeights.getText().toString());
                GlobalConfig.setMaxDeviate(maxDeviate.getText().toString());
                GlobalConfig.setMoreMaxDeviate(moreMaxDeviate.getText()
                        .toString());
                GlobalConfig.setErrorAngle(Double.parseDouble(errorAngle.getText().toString()));
                GlobalConfig.setPositioningError(Double.parseDouble(positioningError.getText().toString()));
                GlobalConfig.setFilterNumber(Double.parseDouble(filterNumber.getText().toString()));
                GlobalConfig.setGaussVariance(Double.parseDouble(gaussVariance.getText().toString()));
                GlobalConfig.setDirection(direction.getText().toString());
                GlobalConfig.setGaussVariance(Double.parseDouble(gaussVariance.getText().toString()));
                finish();
            }
        });
    }

    public void config_backClick(View view)
    {
        this.finish();
    }

}
