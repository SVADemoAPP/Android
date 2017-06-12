package com.hck.imagemap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hck.imagemap.adapter.HighSetAdapter;
import com.hck.imagemap.config.GlobalConfig;

public class HighSetAcitivity extends Activity {

	private GridView gridView;
	private TextView tv_method_up;
	private RelativeLayout ping;
	// private RelativeLayout method_up;
	private SharedPreferences myPreferences;
	private Spinner spinner1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_high_set_acitivity);
		final Intent in = this.getIntent();
		final Bundle bn = in.getExtras();
		gridView = (GridView) findViewById(R.id.grid);
		myPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
		String[] names = { getString(R.string.set_jingdu),
				getString(R.string.set_prru), getString(R.string.set_guandao),
				getString(R.string.message), getString(R.string.delay),
				getString(R.string.wifipositioning),
				getString(R.string.fingerprinting),
				getString(R.string.setdistance) };
		int[] pictures = { R.drawable.hightset_accuracy_test,

		R.drawable.hightset_prru_messsage,
				R.drawable.hightset_arrow_inertial_navigation,
				R.drawable.hightset_push_msg, R.drawable.hightset_out_time,
				R.drawable.wifi, R.drawable.zhiwen, R.drawable.ic_launcher };
		gridView.setAdapter(new HighSetAdapter(this, names, pictures));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					Intent intent = new Intent(HighSetAcitivity.this,
							ThisAcitivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("path", bn.getString("path"));
					bundle.putDouble("xSport", bn.getDouble("xSport"));
					bundle.putDouble("ySport", bn.getDouble("ySport"));
					bundle.putDouble("scale", bn.getDouble("scale"));
					bundle.putString("place", bn.getString("place"));
					bundle.putInt("floorNo", bn.getInt("floorNo"));
					bundle.putInt("placeId", bn.getInt("placeId"));
					bundle.putString("floor", bn.getString("floor"));
					bundle.putSerializable("currFloor",
							bn.getSerializable("currFloor"));
					intent.putExtras(bundle);

					startActivity(intent);
					break;
				case 1:
					Intent intent1 = new Intent(HighSetAcitivity.this,
							PathAcitivity.class);
					Bundle bundle1 = new Bundle();
					bundle1.putString("path", bn.getString("path"));
					bundle1.putDouble("xSport", bn.getDouble("xSport"));
					bundle1.putDouble("ySport", bn.getDouble("ySport"));
					bundle1.putDouble("scale", bn.getDouble("scale"));
					bundle1.putString("place", bn.getString("place"));
					bundle1.putInt("floorNo", bn.getInt("floorNo"));
					bundle1.putInt("placeId", bn.getInt("placeId"));
					bundle1.putString("floor", bn.getString("floor"));
					bundle1.putSerializable("currFloor",
							bn.getSerializable("currFloor"));
					intent1.putExtras(bundle1);
					startActivity(intent1);
					break;
				case 2:
					Intent intent2 = new Intent(HighSetAcitivity.this,
							PrruInfoAcitivity.class);
					Bundle bundle2 = new Bundle();
					bundle2.putString("path", bn.getString("path"));
					bundle2.putDouble("xSport", bn.getDouble("xSport"));
					bundle2.putDouble("ySport", bn.getDouble("ySport"));
					bundle2.putDouble("scale", bn.getDouble("scale"));
					bundle2.putString("place", bn.getString("place"));
					bundle2.putInt("floorNo", bn.getInt("floorNo"));
					bundle2.putInt("placeId", bn.getInt("placeId"));
					bundle2.putString("floor", bn.getString("floor"));
					bundle2.putSerializable("currFloor",
							bn.getSerializable("currFloor"));
					intent2.putExtras(bundle2);
					startActivity(intent2);
					break;
				case 3:
					Intent intent3 = new Intent(HighSetAcitivity.this,
							ConfigActivity.class);
					startActivity(intent3);
					break;
				case 4:
					Intent intent4 = new Intent(HighSetAcitivity.this,
							PushMessageActivity.class);
					Bundle bundle4 = new Bundle();
					bundle4.putString("path", bn.getString("path"));
					bundle4.putDouble("xSport", bn.getDouble("xSport"));
					bundle4.putDouble("ySport", bn.getDouble("ySport"));
					bundle4.putDouble("scale", bn.getDouble("scale"));
					bundle4.putString("place", bn.getString("place"));
					bundle4.putInt("floorNo", bn.getInt("floorNo"));
					bundle4.putInt("placeId", bn.getInt("placeId"));
					bundle4.putString("floor", bn.getString("floor"));
					bundle4.putSerializable("currFloor",
							bn.getSerializable("currFloor"));
					intent4.putExtras(bundle4);
					startActivity(intent4);
					break;
				case 5:
					Intent intent5 = new Intent(HighSetAcitivity.this,
							LocationDelayActivity.class);
					Bundle bundle5 = new Bundle();
					bundle5.putString("path", bn.getString("path"));
					bundle5.putDouble("xSport", bn.getDouble("xSport"));
					bundle5.putDouble("ySport", bn.getDouble("ySport"));
					bundle5.putDouble("scale", bn.getDouble("scale"));
					bundle5.putString("place", bn.getString("place"));
					bundle5.putInt("floorNo", bn.getInt("floorNo"));
					bundle5.putInt("placeId", bn.getInt("placeId"));
					bundle5.putString("floor", bn.getString("floor"));
					bundle5.putSerializable("currFloor",
							bn.getSerializable("currFloor"));
					intent5.putExtras(bundle5);
					startActivity(intent5);
					break;
				case 6:
					Intent intent6 = new Intent(HighSetAcitivity.this,
							WifiStrActivity.class);
					startActivity(intent6);
					break;
				case 7:
					Intent intent7 = new Intent(HighSetAcitivity.this,
							FingerprintingActivity.class);
					Bundle bundle7 = new Bundle();
					bundle7.putString("path", bn.getString("path"));
					bundle7.putDouble("xSport", bn.getDouble("xSport"));
					bundle7.putDouble("ySport", bn.getDouble("ySport"));
					bundle7.putDouble("scale", bn.getDouble("scale"));
					bundle7.putString("place", bn.getString("place"));
					bundle7.putInt("floorNo", bn.getInt("floorNo"));
					bundle7.putInt("placeId", bn.getInt("placeId"));
					bundle7.putString("floor", bn.getString("floor"));
					bundle7.putSerializable("currFloor",
							bn.getSerializable("currFloor"));
					intent7.putExtras(bundle7);
					startActivity(intent7);
					break;
				case 8:
					Intent intent8 = new Intent(HighSetAcitivity.this,
							OffsetSettingsActivity.class);
					startActivity(intent8);
					break;
				default:
					break;
				}
			}
		});
		ping = (RelativeLayout) findViewById(R.id.ping);
		// method_up = (RelativeLayout) findViewById(R.id.method_up);
		tv_method_up = (TextView) findViewById(R.id.tv_method_up);
		ping.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HighSetAcitivity.this,
						PingActivity.class);
				startActivity(intent);
			}
		});
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				tv_method_up.setText(spinner1.getSelectedItem().toString());
				SharedPreferences preferences = getSharedPreferences("setting",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				GlobalConfig.setIdentification(tv_method_up.getText()
						.toString());
				editor.putString("upload", tv_method_up.getText().toString());
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		String upload = myPreferences.getString("upload", "IP");
		if (upload.equals("IP")) {
			spinner1.setSelection(0);
			tv_method_up.setText(spinner1.getSelectedItem().toString());
		} else {
			spinner1.setSelection(1);
			tv_method_up.setText(spinner1.getSelectedItem().toString());
		}
	}

	public void backClick(View v) {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.high_set_acitivity, menu);
		return true;
	}

}
