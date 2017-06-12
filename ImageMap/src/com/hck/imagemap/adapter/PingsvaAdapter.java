package com.hck.imagemap.adapter;

import java.util.List;

import com.hck.imagemap.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PingsvaAdapter extends BaseAdapter
{

    private Context context;
    private List<String> list;
    private int selectedPosition = -1;// 选中的位置

    public PingsvaAdapter(Context context, List<String> list)
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
            convertView = View
                    .inflate(context, R.layout.pingsvalist_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_svaName);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (selectedPosition == position)
        {
            holder.tvName.setSelected(true);
            holder.tvName.setPressed(true);
            holder.tvName.setBackgroundColor(Color.parseColor("#83c4f4"));
            holder.tvName.setTextColor(Color.WHITE);

        } else
        {
            holder.tvName.setSelected(false);
            holder.tvName.setPressed(false);
            holder.tvName.setBackgroundColor(Color.TRANSPARENT);
            holder.tvName.setTextColor(Color.BLACK);

        }
        holder.tvName.setText(list.get(position));

        return convertView;
    }

    public static class ViewHolder
    {
        public TextView tvName;
    }

}
