package com.hck.imagemap.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;
import android.os.Environment;
import android.util.Log;

public class FileUtils
{
    private String SDPATH;

    private int FILESIZE = 4 * 1024;

    public String getSDPATH()
    {
        return SDPATH;
    }

    public FileUtils()
    {
        // 得到当前外部存储设备的目录( /SDCARD )
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 在SD卡上创建文件
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException
    {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }
    
    public static List<PointF> Calculation(PointF start, PointF end)
    {
        List<PointF> result = new ArrayList<PointF>();
        double _x = Math.abs(start.x - end.x);
        double _y = Math.abs(start.y - end.y);
        double distance = Math.sqrt(_x*_x+_y*_y)/10;
        int num = (new Double(Math.ceil(distance)).intValue());
        if(num > 1)
        {
            _x = _x / distance;
            _y = _y / distance;
            double startX =  start.x;
            double startY =  start.y;
            //xFlag 为-1为减去
            int xFlag = 1; 
            int yFlag = 1; 
            if(start.x > end.x)
            {
                xFlag = -1;
            }
            if(start.y > end.y)
            {
                yFlag = -1;
            }
            for(int i =0; i < num-1 ; i++)
            {
                PointF f = new PointF();
                startX = startX + xFlag * _x;
                startY = startY + yFlag * _y;
                f.x = (new Double(startX)).floatValue();
                f.y = (new Double(startY)).floatValue();
                result.add(f);
            }
        }
        return result;
    }

    /**
     * 在SD卡上创建目录
     * 
     * @param dirName
     * @return
     */
    public File createSDDir(String dirName)
    {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     * 
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName)
    {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上的文件夹是否存在
     * 
     * @param fileName
     * @return
     */
    public boolean isFileExistAndDelete(String fileName)
    {
        File file = new File(SDPATH + fileName);
        if (file.exists())
        {
            return file.delete();
        }
        return true;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     * 
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public File write2SDFromInput(String path, String fileName,
            InputStream input)
    {
        File file = null;
        OutputStream output = null;
        try
        {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];

            /*
             * 真机测试，这段可能有问题，请采用下面网友提供的 while((input.read(buffer)) != -1){
             * output.write(buffer); }
             */

            /* 网友提供 begin */
            int length;
            while ((length = (input.read(buffer))) > 0)
            {
                output.write(buffer, 0, length);
            }
            /* 网友提供 end */

            output.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                output.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return file;
    }

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath,
            String fileName)
    {
        // 生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try
        {
            File file = new File(strFilePath);
            if (!file.exists())
            {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e)
        {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    public void deleteFile(String filePath, String fileName)
    {
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        File file = new File(strFilePath);
        if (file.exists())
        {
            file.delete();
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName)
    {
        File file = null;
        makeRootDirectory(filePath);
        try
        {
            file = new File(filePath + fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath)
    {
        File file = null;
        try
        {
            file = new File(filePath);
            if (!file.exists())
            {
                file.mkdir();
            }
        } catch (Exception e)
        {
            Log.i("error:", e + "");
        }
    }

}