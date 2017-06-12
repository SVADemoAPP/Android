package com.hck.imagemap.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hck.imagemap.AddressData;
import com.hck.imagemap.R;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Adapter for countries
 */
public class CountryAdapter extends AbstractWheelTextAdapter
{
    // Countries names
    private String countries[] = AddressData.PROVINCES;

    /**
     * Constructor
     */
    public CountryAdapter(Context context, String countries[])
    {
        super(context, R.layout.country_layout, NO_RESOURCE);
        this.countries = countries;
        setItemTextResource(R.id.countryname);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent)
    {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount()
    {
        return countries.length;
    }

    @Override
    public CharSequence getItemText(int index)
    {
        return countries[index];
    }
}