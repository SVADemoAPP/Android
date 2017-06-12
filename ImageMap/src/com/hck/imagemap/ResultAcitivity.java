package com.hck.imagemap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hck.imagemap.view.CakeSurfaceView;
import com.hck.imagemap.view.CakeSurfaceView.RankType;
import com.hck.imagemap.view.CustomDialog;

public class ResultAcitivity extends Activity
{

    private CakeSurfaceView cakeSurfaceView;
    private TextView textView;
    private String wucha, num, T, TOF, FOT, Ten, yesNo, ygpc, aver;
    private Button saveBtn, throw_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.jingdu_result);
        textView = (TextView) findViewById(R.id.tvresult);
        cakeSurfaceView = (CakeSurfaceView) findViewById(R.id.cake);
        Bundle bundle = getIntent().getExtras();
        ygpc = bundle.getString("ygpc", "0");
        wucha = bundle.getString("wucha", "0");
        num = bundle.getString("num", "0");
        T = bundle.getString("t", "0");
        TOF = bundle.getString("tof", "0");
        FOT = bundle.getString("fot", "0");
        Ten = bundle.getString("ten", "0");
        yesNo = bundle.getString("yesNo", "0");
        aver = bundle.getString("aver", "0");
        System.out.println(ygpc + " " + wucha + " " + num + " " + aver);
        String results = getString(R.string.results).toString();
//        saveBtn = (Button) findViewById(R.id.btnsave);
//        throw_btn = (Button) findViewById(R.id.btnthrow);
        if (!"yes".equals(yesNo))
        {
            saveBtn.setVisibility(View.INVISIBLE);
            throw_btn.setVisibility(View.INVISIBLE);
        }
        String error = getString(R.string.error).toString();
        String nums = getString(R.string.num).toString();
        String meters = getString(R.string.mi).toString();
        String totalNum = getString(R.string.totalNum).toString();
        Locale l = getResources().getConfiguration().locale;
        String metersAway = getString(R.string.mi).toString();
        if (!"yes".equals(yesNo))
        {
            saveBtn.setVisibility(View.INVISIBLE);
            throw_btn.setVisibility(View.INVISIBLE);
            textView.setText(error + ":  " + wucha + " "
                    + getString(R.string.mi) + "\n\n" + nums + ":  " + num);
        } else
        {
            if ("0".equals(bundle.getString("type")))
            {
                textView.setText(results + ":  " + aver + " "
                        + getString(R.string.mi) + "\n\n" + error + ":  "
                        + wucha + " " + getString(R.string.mi) + "\n\n"
                        + getString(R.string.ygpc) + ":  " + ygpc + " "
                        + getString(R.string.mi) + "\n\n" + nums + ":  " + num);
            } else
            {
                textView.setText(error + ":  " + wucha + " "
                        + getString(R.string.mi) + "\n\n"
                        + getString(R.string.ygpc) + ":  " + ygpc + " "
                        + getString(R.string.mi) + "\n\n" + nums + ":  " + num);
            }
        }
        if (l.equals(Locale.SIMPLIFIED_CHINESE))
        {
            List<CakeSurfaceView.CakeValue> cakeValues2 = new ArrayList<CakeSurfaceView.CakeValue>();
            cakeValues2.add(new CakeSurfaceView.CakeValue("0-3" + meters, Float
                    .parseFloat(T), "0-3"
                    + totalNum
                    + ": "
                    + new BigDecimal((Float.parseFloat(T) / 100.0 * Integer
                            .parseInt(num))).setScale(0,
                            BigDecimal.ROUND_HALF_UP)));
            cakeValues2.add(new CakeSurfaceView.CakeValue("3-5" + meters, Float
                    .parseFloat(TOF), "3-5"
                    + totalNum
                    + ": "
                    + new BigDecimal((Float.parseFloat(TOF) / 100.0 * Integer
                            .parseInt(num))).setScale(0,
                            BigDecimal.ROUND_HALF_UP)));
            cakeValues2.add(new CakeSurfaceView.CakeValue("5-10" + meters,
                    Float.parseFloat(FOT), "5-10"
                            + totalNum
                            + ": "
                            + new BigDecimal(
                                    (Float.parseFloat(FOT) / 100.0 * Integer
                                            .parseInt(num))).setScale(0,
                                    BigDecimal.ROUND_HALF_UP)));
            cakeValues2.add(new CakeSurfaceView.CakeValue("10" + metersAway,
                    Float.parseFloat(Ten), "10"
                            + totalNum
                            + ": "
                            + new BigDecimal(
                                    (Float.parseFloat(Ten) / 100.0 * Integer
                                            .parseInt(num))).setScale(0,
                                    BigDecimal.ROUND_HALF_UP)));
            cakeSurfaceView.setData(cakeValues2);
        } else
        {
            List<CakeSurfaceView.CakeValue> cakeValues2 = new ArrayList<CakeSurfaceView.CakeValue>();
            cakeValues2.add(new CakeSurfaceView.CakeValue("3" + meters, Float
                    .parseFloat(T), totalNum
                    + " 3 "
                    + "meters: "
                    + new BigDecimal((Float.parseFloat(T) / 100.0 * Integer
                            .parseInt(num))).setScale(0,
                            BigDecimal.ROUND_HALF_UP)));
            cakeValues2.add(new CakeSurfaceView.CakeValue("3-5" + meters, Float
                    .parseFloat(TOF), totalNum
                    + " 3-5 "
                    + "meters: "
                    + new BigDecimal((Float.parseFloat(TOF) / 100.0 * Integer
                            .parseInt(num))).setScale(0,
                            BigDecimal.ROUND_HALF_UP)));
            cakeValues2.add(new CakeSurfaceView.CakeValue("5-10" + meters,
                    Float.parseFloat(FOT), totalNum
                            + " 5-10 "
                            + "meters: "
                            + new BigDecimal(
                                    (Float.parseFloat(FOT) / 100.0 * Integer
                                            .parseInt(num))).setScale(0,
                                    BigDecimal.ROUND_HALF_UP)));
            cakeValues2.add(new CakeSurfaceView.CakeValue("10" + metersAway,
                    Float.parseFloat(Ten), totalNum
                            + " 10 "
                            + "meters: "
                            + new BigDecimal(
                                    (Float.parseFloat(Ten) / 100.0 * Integer
                                            .parseInt(num))).setScale(0,
                                    BigDecimal.ROUND_HALF_UP)));
            cakeSurfaceView.setData(cakeValues2);
        }
        cakeSurfaceView.setDetailTopSpacing(15);
        cakeSurfaceView.setDetailLeftSpacing(6);
        cakeSurfaceView.setRankType(RankType.RANK_BY_ROW);
    }

    public void knowClick(View view)
    {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.makesure));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                ResultAcitivity.this.setResult(2);
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no),
                new android.content.DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ResultAcitivity.this.setResult(3);
                        finish();
                    }
                });
        builder.create().show();
       
    }

    public void throwClick(View view)
    {
        ResultAcitivity.this.setResult(4);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        ResultAcitivity.this.setResult(4);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
         * if (id == R.id.action_settings) { return true; }
         */
        return super.onOptionsItemSelected(item);
    }

    public void setting_back(View v)
    {
        finish();
    }
}
