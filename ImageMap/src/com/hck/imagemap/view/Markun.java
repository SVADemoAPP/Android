package com.hck.imagemap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class Markun extends TextView
{
    public Markun(Context context)
    {
        super(context);
    }

    public Markun(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public Markun(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused()
    {
        return true;
    }
}
