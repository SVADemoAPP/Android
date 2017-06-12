package com.hck.imagemap.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.imagemap.R;

public class HighSetAdapter extends BaseAdapter
{

    // 布局填充器
//    private LayoutInflater inflater;
    // 用于替换”手机防盗“的新标题
//    private String newname;
    // 将九个item的每一个标题都存入该数组中
    private String[] names;
    private int[] pictures;
    private Context context;

    public HighSetAdapter(Context context, String[] names, int[] pictures)
    {
        this.names = names;
        this.pictures = pictures;
        this.context = context;
    }

    /**
     * 返回gridview有多少个item
     */
    public int getCount()
    {
        return names.length;
    }

    /**
     * 获取每个item对象，如果我们不对这个返回的item对象做相应的操作的话， 我们可以返回一个null， 这里我们简单处理一下，返回position
     */
    public Object getItem(int position)
    {
        return position;
    }

    /**
     * 返回当前item的id
     */
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * 返回每一个gridview中条目中的view对象
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.highset_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item);
            holder.iv_pictures = (ImageView) convertView
                    .findViewById(R.id.iv_item);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(names[position]);
        holder.iv_pictures.setImageResource(pictures[position]);
        // iv_icon.setImageResource(icons[0]);
        // 第一个Item，也即“手机防盗”对应的Item
        // if (position == 0) {
        // //判断sp中取出的newname是否为空，如果不为空的话，将“手机防盗”对应的标题修改为sp中修改后的标题
        // if (!TextUtils.isEmpty(newname)) {
        // holder.tv_name.setText(newname);
        // }
        // }
        return convertView;
    }

    public class ViewHolder
    {
        public TextView tv_name;
        public ImageView iv_pictures;
    }
}
