package com.hck.imagemap;

import com.hck.imagemap.config.GlobalConfig;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WifiStrActivity extends Activity {

	private EditText send_time;
    private EditText content;
    private Button btnSave;
    private SharedPreferences myPreferences;
    private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_str);
		initView();
		String send_time1 = myPreferences.getString("send_time", "25");//静止门限
        String content1 = myPreferences.getString("content", "send"); //滤波窗长
        send_time.setText(send_time1);
        content.setText(content1);
	}

	private void initView() {
        myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = myPreferences.edit();
		send_time = (EditText) findViewById(R.id.send_time);
		content = (EditText) findViewById(R.id.content);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                editor.putString("send_time", send_time.getText().toString());
                editor.putString("content", content.getText().toString());
                editor.commit();
                GlobalConfig.setSend_time(Integer.parseInt(send_time.getText().toString()));
                GlobalConfig.setContent(content.getText().toString());
                finish();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi_str, menu);
		return true;
	}

}
