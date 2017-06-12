package com.hck.imagemap.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.util.Log;

public class Utils
{
    private static String TAG = "Utils";

    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String getCurrentDateStr()
    {
        return formatter.format(new Date());
    }

    public static void saveBitmap(Bitmap bm, String picName)
    {
        Log.e(TAG, "保存图片");
        File d = new File("/sdcard/SVA/");
        if (!d.exists())
        {
            d.mkdirs();
        }
        File f = new File("/sdcard/SVA/", picName);

        try
        {
            if (f.exists())
            {
                f.delete();
            }

            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
