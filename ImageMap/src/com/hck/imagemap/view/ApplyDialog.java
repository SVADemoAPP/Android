package com.hck.imagemap.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hck.imagemap.R;

/**
 * 自定义免责申明dialog Created by zhoubin on 2015/12/21.
 */
public class ApplyDialog extends Dialog
{
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_ok;
    private TextView tv_cancel;
    private View mContentView;
    private CheckBox cb_ok;
    private View.OnClickListener mOkListener;
    private String mContent;
    private String mTitle;
    private Context mContext;
    private View.OnClickListener mCancleListener;

    public ApplyDialog(Context context)
    {
        this(context, 0);
        this.mContext = context;
    }

    public ApplyDialog(Context context, int themeResId)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContentView = View.inflate(getContext(), R.layout.dialog_apply, null);
        setContentView(mContentView);
        initView();
        initListener();
    }

    private void initView()
    {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        cb_ok = (CheckBox) findViewById(R.id.cb_ok);

        if (!TextUtils.isEmpty(mTitle))
        {
            tv_title.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mContent))
        {
            tv_content.setText(mContent);
        }
    }

    private void initListener()
    {
        cb_ok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                tv_ok.setEnabled(isChecked);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mCancleListener != null)
                {
                    mCancleListener.onClick(v);
                }
                dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOkListener != null)
                {
                    mOkListener.onClick(v);
                }
            }
        });
    }

    public void setOkListener(View.OnClickListener listener)
    {
        this.mOkListener = listener;
    }

    public void setCancelListener(View.OnClickListener listener)
    {
        this.mCancleListener = listener;
    }

    public void setContent(String content)
    {
        this.mContent = content;
        if (tv_content != null)
        {
            tv_content.setText(content);
        }
    }

    public void setContent(int content)
    {
        this.mContent = (String) mContext.getText(content);
        if (tv_content != null)
        {
            tv_content.setText(content);
        }
    }

    public void setTitle(String title)
    {
        this.mTitle = title;
        if (tv_title != null)
        {
            tv_title.setText(title);
        }
    }

    public void setTitle(int title)
    {
        this.mTitle = (String) mContext.getText(title);
        if (tv_title != null)
        {
            tv_title.setText(title);
        }
    }
}
