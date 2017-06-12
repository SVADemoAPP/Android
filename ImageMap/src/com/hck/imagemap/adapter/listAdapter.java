package com.hck.imagemap.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hck.imagemap.R;

public class listAdapter extends BaseAdapter
{

    private Context context;
    private List<String> list;
    private int selectedPosition = -1;// 选中的位置

    public listAdapter(Context context, List<String> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void setSelectedPosition(int position)
    {
        selectedPosition = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView
                    .findViewById(R.id.textView1);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (selectedPosition == position)
        {
            holder.textView.setSelected(true);
            holder.textView.setPressed(true);
            holder.textView
                    .setBackgroundResource(R.drawable.floor_no_background);
            holder.textView.setTextColor(Color.WHITE);

        } else
        {
            holder.textView.setSelected(false);
            holder.textView.setPressed(false);
            holder.textView.setBackgroundColor(Color.TRANSPARENT);
            holder.textView.setTextColor(Color.BLACK);

        }
        holder.textView.setText(list.get(position));

        return convertView;
    }

    public static class ViewHolder
    {
        public TextView textView;
    }

}