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

import com.hck.imagemap.Dao.MessageDao;
import com.hck.imagemap.adapter.HistoryAdapter;
import com.hck.imagemap.entity.MessageContent;

public class HistoryActivity extends Activity
{

    private MessageDao dao;
    private List<MessageContent> contents;
    private HistoryAdapter adapter;
    private View popView;
    private ListView listView;
    private PopupWindow pw;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_acitivity);
        dao = new MessageDao(this);
        listView = (ListView) findViewById(R.id.lvhistory);
        contents = dao.findAll();
        adapter = new HistoryAdapter(this, contents);
        listView.setAdapter(adapter);
        popView = View.inflate(this, R.layout.pop_up_view, null);
        textView = (TextView) popView.findViewById(R.id.popup);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                MessageContent content = contents.get(position);
                Intent intent = new Intent(HistoryActivity.this,
                        ResultAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wucha", "" + content.get_wucha());
                bundle.putString("num", "" + content.get_num());
                bundle.putString("t", Double.parseDouble(content.get_T()) + "");
                bundle.putString("tof", Double.parseDouble(content.get_TOF())
                        + "");
                bundle.putString("fot", Double.parseDouble(content.get_FOT())
                        + "");
                bundle.putString("ten", Double.parseDouble(content.get_Ten())
                        + "");
                bundle.putString("yesNo", "no");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    final int position, long id)
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
                        delete(position);
                        pw.dismiss();
                    }
                });
                return true;
            }
        });
    }

    private void delete(int position)
    {
        MessageContent content = (MessageContent) listView
                .getItemAtPosition(position);
        String time = content.get_time();
        dao.delete(time); // 删除了 数据库里面的记录
        contents.remove(content);// 删除当前listview里面的数据.
        adapter.notifyDataSetChanged();
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
        dao.helper.close();
        super.onDestroy();
    }

    public void setting_back(View v)
    {
        finish();
    }
}
