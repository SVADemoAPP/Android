package com.hck.imagemap.adapter;

import java.util.List;

import com.hck.imagemap.R;
import com.hck.imagemap.entity.MessageContent;
import com.hck.imagemap.entity.PrruInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PrruInfoAdapter extends BaseAdapter
{

    private Context context;
    private List<PrruInfo> list;

    public PrruInfoAdapter(Context context, List<PrruInfo> list)
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
            convertView = View.inflate(context, R.layout.prru_info_item, null);
            holder = new ViewHolder();
            holder.tv_gpp = (TextView) convertView.findViewById(R.id.tv_gpp);
            holder.tv_rsrp = (TextView) convertView.findViewById(R.id.tv_rsrp);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if(list.size()>0){
        	holder.tv_gpp.setText(list.get(position).getGpp() + "");
        	holder.tv_rsrp.setText(list.get(position).getRsrp() + "");
        }

        return convertView;
    }

    public static class ViewHolder
    {
        public TextView tv_gpp;
        public TextView tv_rsrp;
    }

}
