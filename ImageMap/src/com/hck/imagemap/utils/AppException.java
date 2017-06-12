package com.hck.imagemap.utils;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.os.Looper;

public class AppException extends Exception implements UncaughtExceptionHandler
{

    private static final long serialVersionUID = -6262909398048670705L;

    private String message;

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException()
    {
        super();
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public AppException(String message, Exception excp)
    {
        super(message, excp);
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * 获取APP异常崩溃处理对象
     * 
     * @param context
     * @return
     */
    public static AppException getAppExceptionHandler()
    {
        return new AppException();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {

        if (!handleException(ex) && mDefaultHandler != null)
        {
            mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    /**
     * 自定义异常处理
     * 
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }

        final Activity activity = AppManager.getAppManager().currentActivity();

        if (activity == null)
        {
            return false;
        }

        new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                // new AlertDialog.Builder(activity).setTitle("提示")
                // .setCancelable(false)
                // .setMessage("亲，App开小差去了,待我出去找找...")
                // .setNeutralButton("去吧", new OnClickListener()
                // {
                // @Override
                // public void onClick(DialogInterface dialog,
                // int which)
                // {
                // AppManager.getAppManager().exitApp(activity);
                // }
                // }).create().show();
                FileUtils utils = new FileUtils();
                String filePath = "/sdcard/SVA";
                String fileName = "/ERROR.log";
                utils.writeTxtToFile(ex.getStackTrace().toString(), filePath, fileName);
//                Toast.makeText(activity, "存在异常数据", 0).show();
                Looper.loop();
            }
        }.start();

        return true;
    }

}