package com.hck.imagemap.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hck.imagemap.R;
import com.hck.imagemap.entity.Floor;

public class FloorAdapter extends BaseAdapter
{
    private Context context;
    private List<Floor> list = new ArrayList<Floor>(0);

    private ListView listView;

    public ListView getListView()
    {
        return listView;
    }

    public void setListView(ListView listView)
    {
        this.listView = listView;
    }

    public FloorAdapter(Context context, List<Floor> list)
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
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        Floor f = list.get(position);
        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(
                    R.layout.horizontal_list_item, parent, false);
            Holder holder = new Holder();
            holder.floor_name = (TextView) view.findViewById(R.id.textlistitem);
            view.setTag(holder);

        }
        Holder holder = (Holder) view.getTag();
        holder.floor_name.setText(f.getFloor());

        return view;
    }

    public void updateView(int position)
    {
        Floor f = list.get(position);
        int visiblePosition = listView.getFirstVisiblePosition();
        View view = listView.getChildAt(position - visiblePosition);
        Holder holder = (Holder) view.getTag();
        holder.floor_name.setText(f.getFloor());
    }

    class Holder
    {
        public TextView floor_name;
    }
}
