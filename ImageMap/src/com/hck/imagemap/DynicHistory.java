package com.hck.imagemap;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hck.imagemap.Dao.Dynic;
import com.hck.imagemap.Dao.Dynic_DB;
import com.hck.imagemap.adapter.DynicAdapter;

public class DynicHistory extends Activity
{

    private Dynic_DB dynic_db;
    private List<Dynic> dynic;
    private DynicAdapter dynicAdapter;
    private View popView;
    private ListView dynicListView;
    private PopupWindow pw;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dynic_history);
        dynic_db = new Dynic_DB(this);
        dynicListView = (ListView) findViewById(R.id.lvDynicHistory);
        dynic = dynic_db.queryAll();
        dynicAdapter = new DynicAdapter(this, dynic);
        dynicListView.setAdapter(dynicAdapter);
        popView = View.inflate(this, R.layout.pop_up_view, null);
        textView = (TextView) popView.findViewById(R.id.popup);
        dynicListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3)
            {
                Dynic content = dynic.get(position);
                Intent intent = new Intent(DynicHistory.this,
                        ResultAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wucha", "" + content.getWucha());
                System.out.println(" content.getWucha()=" + content.getWucha());
                bundle.putString("num", "" + content.getNum());
                bundle.putString("t", Double.parseDouble(content.getT()) + "");
                bundle.putString("tof", Double.parseDouble(content.getTOF())
                        + "");
                bundle.putString("fot", Double.parseDouble(content.getFOT())
                        + "");
                bundle.putString("ten", Double.parseDouble(content.getTen())
                        + "");
                bundle.putString("yesNo", "no");
                bundle.putString("ygpc", 0 + "");
                intent.putExtras(bundle);
                startActivity(intent);
                bundle.clear();
            }
        });

        dynicListView.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                    final int arg2, long arg3)
            {
                pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, true);
                pw.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.chatto_bg_pressed));
                pw.showAsDropDown(view, 0, 0);
                textView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dynicDelete(arg2);
                        pw.dismiss();
                    }
                });
                return true;

            }
        });
    }

    private void dynicDelete(int position)
    {
        Dynic content = (Dynic) dynicListView.getItemAtPosition(position);
        String time = content.getTime();
        dynic_db.delete(time); // 删除了 数据库里面的记录
        dynic.remove(content);// 删除当前listview里面的数据.
        dynicAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_acitivity, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        dynic_db.close();
        super.onDestroy();
    }

    public void dh_back(View v)
    {
        finish();
    }

}
