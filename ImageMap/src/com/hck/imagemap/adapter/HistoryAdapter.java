package com.hck.imagemap.adapter;

import java.util.List;

import com.hck.imagemap.R;
import com.hck.imagemap.entity.MessageContent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter
{

    private Context context;
    private List<MessageContent> list;

    public HistoryAdapter(Context context, List<MessageContent> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.history_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tvtime);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).get_time() + "");

        return convertView;
    }

    public static class ViewHolder
    {
        public TextView textView;
    }

}
