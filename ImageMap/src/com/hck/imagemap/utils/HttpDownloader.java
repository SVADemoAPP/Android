package com.hck.imagemap.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader
{

    private URL url = null;

    /**
     * 根据URL下载文件,前提是这个文件当中的内容是文本,函数的返回值就是文本当中的内容 1.创建一个URL对象
     * 2.通过URL对象,创建一个HttpURLConnection对象 3.得到InputStream 4.从InputStream当中读取数据
     * 
     * @param urlStr
     * @return
     */
    public String download(String urlStr)
    {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try
        {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream()));
            while ((line = buffer.readLine()) != null)
            {
                sb.append(line);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                buffer.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 
     * @param urlStr
     * @param path
     * @param fileName
     * @return -1:文件下载出错 0:文件下载成功 1:文件已经存在
     */
    public int downFile(String urlStr, String path, String fileName)
    {
        InputStream inputStream = null;
        try
        {
            FileUtils fileUtils = new FileUtils();

            // fileUtils.isFileExistAndDelete(path + fileName);

            inputStream = getInputStreamFromURL(urlStr);
            File resultFile = fileUtils.write2SDFromInput(path, fileName,
                    inputStream);
            if (resultFile == null)
            {
                return 0;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     * 
     * @param urlStr
     * @return
     * @throws IOException
     */
    public InputStream getInputStreamFromURL(String urlStr) throws IOException
    {
        HttpURLConnection urlConn = null;
        InputStream inputStream = null;
        try
        {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5000);
            inputStream = urlConn.getInputStream();

        } catch (IOException e)
        {
            throw e;
        }

        return inputStream;
    }
}