package com.hck.imagemap;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OffsetSettingsActivity extends Activity
{

    private EditText et_distance;
    private Button btn_confirm_distance;
    private SharedPreferences myPreferences;
    private Editor et;
    private String distance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offset_settings);

        et_distance = (EditText) findViewById(R.id.et_distance);
        btn_confirm_distance = (Button) findViewById(R.id.btn_confirm_distance);

        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        et = myPreferences.edit();
        et_distance.setText(myPreferences.getString("distance", "5"));

        btn_confirm_distance.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                distance = et_distance.getText().toString();

                if (TextUtils.isEmpty(distance))
                {
                    Toast.makeText(OffsetSettingsActivity.this,
                            getString(R.string.warmdistance), 0).show();
                } else
                {
                    et.putString("distance", distance);
                    et.commit();
                    Toast.makeText(OffsetSettingsActivity.this,
                            getString(R.string.saveInfo), 0).show();
                    finish();
                }
            }
        });
    }
    
    public void backClick(View view)
    {
    	finish();
    }

}
